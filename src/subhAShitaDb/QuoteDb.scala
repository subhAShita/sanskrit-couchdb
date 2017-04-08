package subhAShitaDb

import java.io.File

import com.couchbase.lite.Document
import com.couchbase.lite.UnsavedRevision
import scala.collection.JavaConverters._
import com.couchbase.lite.{Database, Document, JavaContext, Manager}
import net.liftweb.json.{Extraction, Serialization, ShortTypeHints}
import org.slf4j.LoggerFactory
import sanskritnlp.quote.{TopicAnnotation, _}
import subhAShitaDb.dbMakerSanskrit.{log, quoteDb}

// This version of the database uses Java (rather than Android) API.
class QuoteDb(language: Language) {
  val log = LoggerFactory.getLogger(getClass.getName)
  var quoteDb: Database = null
  var annotationDb: Database = null
  var dbManager: Manager = null
  def openDatabase() = {
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

  def updateDocument(document: Document, jsonMap: Map[String,Object]) = {
    document.update(new Document.DocumentUpdater() {
      override def update(newRevision: UnsavedRevision): Boolean = {
        val properties = newRevision.getUserProperties
        properties.putAll(jsonMap.asJava)
        newRevision.setUserProperties(properties)
        true
      }
    })
  }

  def addQuote(quoteText: QuoteText) : Boolean = {
    val jsonMap = getJsonMap(quoteText)
    // log debug(jsonMap.toString())
    val document = quoteDb.getDocument(quoteText.key)
    updateDocument(document, jsonMap)
    return true
  }

  def addAnnotation(annotation: Annotation): Boolean = {
    val jsonMap = getJsonMap(annotation)
    log debug(annotation.getKey())
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
    recordsModified += quoteWithInfo.originAnnotations.map(addAnnotation(_).compare(false)).sum
    recordsModified += quoteWithInfo.ratingAnnotations.map(addAnnotation(_).compare(false)).sum
    return recordsModified
  }


}


object dbMakerSanskrit {
  val log = LoggerFactory.getLogger(getClass.getName)
  val quoteDb = new QuoteDb(language = Language("sa"))

  def main(args: Array[String]): Unit = {
    quoteDb.openDatabase()
    log info s"Updated records ${vishvasaPriyaSamskritaPadyani.map(quoteDb.addQuoteWithInfo(_)).sum} from vishvasaPriyaSamskritaPadyani"
  }
}
