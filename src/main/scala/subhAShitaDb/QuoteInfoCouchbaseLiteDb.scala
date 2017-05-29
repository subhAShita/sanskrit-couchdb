package subhAShitaDb

import java.io.File
import java.net.URL

import com.couchbase.lite.auth.BasicAuthenticator
import dbSchema._
import dbSchema.common.Language
import dbSchema.quote.{Annotation, QuoteText, QuoteWithInfo}
import dbUtils.{collectionUtils, jsonHelper}
import sanskrit_coders.db.couchbaseLite.CouchbaseLiteDb

import scala.collection.mutable
import scala.io.StdIn

//import com.couchbase.lite.{Database, Manager, JavaContext, Document, UnsavedRevision, Query, ManagerOptions}
import com.couchbase.lite.util.Log
import com.couchbase.lite.{Database, Manager, JavaContext, Document, UnsavedRevision, Query, ManagerOptions}
//import org.json4s.jackson.Serialization
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

// This version of the database uses Java (rather than Android) API.
class QuoteInfoCouchbaseLiteDb(language: Language) {
  implicit def databaseToCouchbaseLiteDb(s: Database) = new CouchbaseLiteDb(s)
  val log = LoggerFactory.getLogger(getClass.getName)
  var quoteDb: Database = null
  var annotationDb: Database = null
  var dbManager: Manager = null

  def openDatabasesLaptop() = {
    dbManager = new Manager(new JavaContext("data") {
      override def getRootDirectory: File = {
        val rootDirectoryPath = "/home/vvasuki/subhAShita-db-sanskrit"
        new File(rootDirectoryPath)
      }
    }, Manager.DEFAULT_OPTIONS)
    dbManager.setStorageType("ForestDB")
    quoteDb = dbManager.getDatabase(s"quote_db__${language.code}")
    annotationDb = dbManager.getDatabase(s"quote_annotation_db__${language.code}")
  }

  def replicateAll() = {
    quoteDb.replicate()
    annotationDb.replicate()
  }


  def closeDatabases = {
    quoteDb.close()
    annotationDb.close()
  }

  def purgeAll = {
    annotationDb.purgeDatabase()
    quoteDb.purgeDatabase()
  }

  def addQuote(quoteText: QuoteText): Boolean = {
    val jsonMap = jsonHelper.getJsonMap(quoteText)
    quoteDb.updateDocument(quoteText.text.getKey, jsonMap)
    return true
  }

  def addAnnotation(annotation: Annotation): Boolean = {
    val jsonMap = jsonHelper.getJsonMap(annotation)
    annotationDb.updateDocument(annotation.getKey, jsonMap)
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

  def ingestQuoteList(quoteList: QuoteList) = {
    log info s"Updated records ${quoteList.map(addQuoteWithInfo(_)).sum} from ${quoteList.getClass.getSimpleName}"
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

  def listAllCaseClassObjects = {
    //    listCaseClassObjects(quoteDb.createAllDocumentsQuery)
    annotationDb.listCaseClassObjects(annotationDb.createAllDocumentsQuery)
  }

  def testQuoteWrite() = {
    val jsonMap = Map("scriptRenderings" -> List(Map("text" -> "दण्डः शास्ति प्रजाः सर्वाः दण्ड एवाभिरक्षति। दण्डः सुप्तेषु जागर्ति दण्डं धर्मं विदुर्बुधाः।।", "scheme" -> "dev", "startLetter" -> "द")),
      "jsonClass" -> "QuoteText",
      "language" -> Map("code" -> "sa"),
      "key" -> "damDaHshaastiprajaaHsarvaaHdamDaevaabhiraxatidamDaHsupteShujaagartidamDamdharmamvidurbudhaaH"
    )
    quoteDb.updateDocument(jsonMap.get("key").get.toString, jsonMap)
  }

  def testQuoteRetrieval() = {
    val id = "damDaHshaastiprajaaHsarvaaHdamDaevaabhiraxatidamDaHsupteShujaagartidamDamdharmamvidurbudhaaH"
    val doc = quoteDb.getDocument(id)
    val jsonMap = doc.getUserProperties

    log debug jsonMap.toString
  }

}


object dbMakerSanskrit {
  val log = LoggerFactory.getLogger(getClass.getName)
  val quoteInfoDb = new QuoteInfoCouchbaseLiteDb(language = Language("sa"))

  def testQuoteReadWrite() = {
    quoteInfoDb.openDatabasesLaptop()
    quoteInfoDb.testQuoteWrite()
    quoteInfoDb.testQuoteRetrieval()
    quoteInfoDb.closeDatabases
    quoteInfoDb.openDatabasesLaptop()
    quoteInfoDb.testQuoteRetrieval()
    quoteInfoDb.testQuoteRetrieval()
  }

  def updateDb = {
    quoteInfoDb.ingestQuoteList(vishvasaPriyaSamskritaPadyani)
    quoteInfoDb.ingestQuoteList(mahAsubhAShitasangraha)
  }

  def main(args: Array[String]): Unit = {
    quoteInfoDb.openDatabasesLaptop()
    quoteInfoDb.replicateAll()
    // quoteInfoDb.checkConflicts
    updateDb
    //    quoteInfoDb.listAllCaseClassObjects
    //    quoteInfoDb.purgeAll
  }
}
