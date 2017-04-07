package subhAShitaDb

import java.io.File

import scala.collection.JavaConverters._
import com.couchbase.lite.{Database, JavaContext, Manager}
import net.liftweb.json.{Extraction, Serialization, ShortTypeHints}
import org.slf4j.LoggerFactory
import sanskritnlp.quote.{TopicAnnotation, _}

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
}


object dbMakerSanskrit {
  val log = LoggerFactory.getLogger(getClass.getName)
  val quoteDb = new QuoteDb(language = Language("sa"))

  def main(args: Array[String]): Unit = {
    // implicit val formats = Serialization.formats(NoTypeHints)
    implicit val formats = Serialization.formats(ShortTypeHints(
      List(
        classOf[QuoteText],
        classOf[OriginAnnotation],
        classOf[DescriptionAnnotation],
        classOf[TopicAnnotation],
        classOf[RatingAnnotation]
      )))
    quoteDb.openDatabase()

    while (vishvasaPriyaSamskritaPadyani.hasNext()) {
      val quoteWithInfo = vishvasaPriyaSamskritaPadyani.next()
      val quoteTextJobj = Extraction.decompose(quoteWithInfo.quoteText)
      val jsonMap = quoteTextJobj.values.asInstanceOf[Map[String,Object]]
      log debug(jsonMap.toString())
      val document = quoteDb.quoteDb.getDocument(quoteWithInfo.quoteText.key)
      if (document.getProperty("key") != null) {
        document.putProperties(jsonMap.asJava)
      } else {
        log warn(s"${quoteWithInfo.quoteText.key} already exists!")
      }

      val jsonPretty = Serialization.writePretty()

    }
  }
}
