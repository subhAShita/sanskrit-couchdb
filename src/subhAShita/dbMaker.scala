package subhAShita

import com.couchbase.lite.{Database, JavaContext, Manager}
import net.liftweb.json.{Serialization, ShortTypeHints}
import sanskritnlp.quote.{TopicAnnotation, _}
import org.slf4j.LoggerFactory

class dbMaker(language: Language) {
  val log = LoggerFactory.getLogger(getClass.getName)
  var quoteDb: Database = null
  var annotationDb: Database = null
  var dbManager: Manager = null
  def openDatabase() = {
    dbManager =  new Manager(new JavaContext("data"), Manager.DEFAULT_OPTIONS)
    quoteDb = dbManager.getDatabase(s"quote_db__${language.code}")
    // Does not work. quoteDb.setStorageType("ForestDB")
    annotationDb = dbManager.getDatabase(s"annotation_db__${language.code}")
  }
}

object dbMakerSanskrit extends dbMaker(language = Language("sa")){

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
    openDatabase()

    while (vishvasaPriyaSamskritaPadyani.hasNext()) {
      val quoteWithInfo = vishvasaPriyaSamskritaPadyani.next()
      val quoteTextMap = Serialization.write(quoteWithInfo.quoteText)
      val jsonPretty = Serialization.writePretty()
    }
  }
}
