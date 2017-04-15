package subhAShitaDb

import java.io.File

import scala.collection.JavaConversions.mapAsScalaMap
import com.couchbase.lite.Document
import com.couchbase.lite.UnsavedRevision

import scala.collection.JavaConverters._
import com.couchbase.lite.{Database, Document, JavaContext, Manager}
import net.liftweb.json.{Extraction, Serialization, ShortTypeHints}
import org.slf4j.LoggerFactory
import sanskritnlp.quote.{TopicAnnotation, _}
import com.couchbase.lite.Query
import com.couchbase.lite.QueryEnumerator
import com.couchbase.lite.QueryRow
import com.couchbase.lite.util.Log
import com.fasterxml.jackson.databind
import com.fasterxml.jackson.databind.ObjectMapper

// This version of the database uses Java (rather than Android) API.
class QuoteInfoDb(language: Language) {
  val log = LoggerFactory.getLogger(getClass.getName)
  var quoteDb: Database = null
  var annotationDb: Database = null
  var dbManager: Manager = null
  def openDatabases() = {
    dbManager =  new Manager(new JavaContext("data") {
      override def getRootDirectory: File = {
        val rootDirectoryPath = "/home/vvasuki/subhAShita-db-sanskrit"
        new File(rootDirectoryPath)
      }
    }, Manager.DEFAULT_OPTIONS)
    dbManager.setStorageType("ForestDB")
    quoteDb = dbManager.getDatabase(s"quote_db__${language.code}")
    annotationDb = dbManager.getDatabase(s"annotation_db__${language.code}")
  }

  def closeDatabases = {
    quoteDb.close()
    annotationDb.close()
  }

  def getJsonMap(caseObj: Any): Map[String,Object] = {
    // implicit val formats = Serialization.formats(NoTypeHints)
    implicit val formats = Serialization.formats(ShortTypeHints(
      List(
        classOf[QuoteText],
        classOf[OriginAnnotation],
        classOf[DescriptionAnnotation],
        classOf[TopicAnnotation],
        classOf[RatingAnnotation]
      )))
    val jobj = Extraction.decompose(caseObj)
    return jobj.values.asInstanceOf[Map[String,Object]]
  }

  def toJava(x: Any): Any = {
    import scala.collection.JavaConverters._
    x match {
      case y: scala.collection.MapLike[_, _, _] =>
        y.map { case (d, v) => toJava(d) -> toJava(v) } asJava
      case y: scala.collection.SetLike[_,_] =>
        y map { item: Any => toJava(item) } asJava
      case y: Iterable[_] =>
        y.map { item: Any => toJava(item) } asJava
      case y: Iterator[_] =>
        toJava(y.toIterable)
      case _ =>
        x
    }
  }

  def updateDocument(document: Document, jsonMap: Map[String,Object]) = {
    document.update(new Document.DocumentUpdater() {
      override def update(newRevision: UnsavedRevision): Boolean = {
        val properties = newRevision.getUserProperties
        val jsonMapJava = toJava(jsonMap).asInstanceOf[java.util.Map[String, Object]]
//        log debug jsonMapJava.getClass.toString
        properties.putAll(jsonMapJava)
        newRevision.setUserProperties(properties)
        true
      }
    })
  }

  def addQuote(quoteText: QuoteText) : Boolean = {
    val jsonMap = getJsonMap(quoteText)
     log debug(jsonMap.toString())
//    sys.exit()
    val document = quoteDb.getDocument(quoteText.key)
    updateDocument(document, jsonMap)
    return true
  }

  def addAnnotation(annotation: Annotation): Boolean = {
    val jsonMap = getJsonMap(annotation)
//    log debug(annotation.getKey())
    log debug(jsonMap.toString())
    val document = annotationDb.getDocument(annotation.getKey())
    updateDocument(document, jsonMap)
    return true
  }

  // Returns the number of documents updated.
  def addQuoteWithInfo(quoteWithInfo: QuoteWithInfo): Int = {
    var recordsModified = 0
    recordsModified += addQuote(quoteWithInfo.quoteText).compare(false)
    recordsModified += quoteWithInfo.topicAnnotations.map(addAnnotation(_).compare(false)).sum
    recordsModified += quoteWithInfo.descriptionAnnotations.map(addAnnotation(_).compare(false)).sum
    recordsModified += quoteWithInfo.ratingAnnotations.map(addAnnotation(_).compare(false)).sum
    recordsModified += quoteWithInfo.originAnnotations.map(addAnnotation(_).compare(false)).sum
    recordsModified += quoteWithInfo.requestAnnotations.map(addAnnotation(_).compare(false)).sum
    recordsModified += quoteWithInfo.referenceAnnotations.map(addAnnotation(_).compare(false)).sum
    return recordsModified
  }

  def checkConflicts = {
    val query = quoteDb.createAllDocumentsQuery
    query.setAllDocsMode(Query.AllDocsMode.ONLY_CONFLICTS)
    val result = query.run
    result.iterator().asScala.foreach(row => {
      if (row.getConflictingRevisions.size > 0) {
        Log.w("checkConflicts", "Conflict in document: %s", row.getDocumentId)
      }
    })
  }

  def exportToTsv = {
    val query = quoteDb.createAllDocumentsQuery
    val result = query.run
    result.iterator().asScala.map(_.getDocumentId).foreach(id => {
      // val jsonMap = row.getDocument.getUserProperties.asScala("language").asInstanceOf[java.util.Map[String, Object]].asScala
      val jsonMap = quoteDb.getDocument(id).getUserProperties
      log debug jsonMap.toString
    })
  }

  def testQuoteWrite() = {
    val jsonMap =Map("scriptRenderings"-> List(Map("text"-> "दण्डः शास्ति प्रजाः सर्वाः दण्ड एवाभिरक्षति। दण्डः सुप्तेषु जागर्ति दण्डं धर्मं विदुर्बुधाः।।", "scheme" -> "dev", "startLetter" -> "द")),
      "jsonClass"->"QuoteText",
      "language"->Map("code" -> "sa"),
      "key"->"damDaHshaastiprajaaHsarvaaHdamDaevaabhiraxatidamDaHsupteShujaagartidamDamdharmamvidurbudhaaH"
    )
    val document = quoteDb.getDocument(jsonMap("key").toString)
    updateDocument(document, jsonMap)
  }

  def testQuoteRetrieval() = {
    val id = "damDaHshaastiprajaaHsarvaaHdamDaevaabhiraxatidamDaHsupteShujaagartidamDamdharmamvidurbudhaaH"
    val doc = quoteDb.getDocument(id)
    val jsonMap = doc.getUserProperties

    val json = new databind.ObjectMapper().writeValueAsString(jsonMap)
    log debug jsonMap.toString
  }

}


object dbMakerSanskrit {
  val log = LoggerFactory.getLogger(getClass.getName)
  val quoteInfoDb = new QuoteInfoDb(language = Language("sa"))

  def testQuoteReadWrite() = {
    quoteInfoDb.openDatabases()
    quoteInfoDb.testQuoteWrite()
    quoteInfoDb.testQuoteRetrieval()
    quoteInfoDb.closeDatabases
    quoteInfoDb.openDatabases()
    quoteInfoDb.testQuoteRetrieval()
    quoteInfoDb.testQuoteRetrieval()
  }

  def main(args: Array[String]): Unit = {
    quoteInfoDb.openDatabases()
    // quoteInfoDb.checkConflicts
//    quoteInfoDb.exportToTsv
//    log info s"Updated records ${vishvasaPriyaSamskritaPadyani.map(quoteInfoDb.addQuoteWithInfo(_)).sum} from vishvasaPriyaSamskritaPadyani"
    log info s"Updated records ${mahAsubhAShitasangraha.map(quoteInfoDb.addQuoteWithInfo(_)).sum} from mahAsubhAShitasangraha"
  }
}
