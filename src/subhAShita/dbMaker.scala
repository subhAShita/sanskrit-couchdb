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
    quoteDb = dbManager.getDatabase(s"quoteDb__${language.code}")
    annotationDb = dbManager.getDatabase(s"annotationDb__${language.code}")
  }
}

object dbSanskritMaker extends dbMaker(language = Language("sa")){

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
    while (vishvasaPriyaSamskritaPadyani.hasNext()) {
      val jsonPretty = Serialization.writePretty(vishvasaPriyaSamskritaPadyani.next())
    }
  }
}
