package subhAShita

import com.couchbase.lite.{Database, JavaContext, Manager}
import net.liftweb.json.{Serialization, ShortTypeHints}
import sanskritnlp.quote.{TopicAnnotation, _}
import org.slf4j.LoggerFactory

object dbMaker {
  val log = LoggerFactory.getLogger(getClass.getName)
  var db: Database = null
  var dbManager: Manager = null
  def openDatabase() = {
    dbManager =  new Manager(new JavaContext("data"), Manager.DEFAULT_OPTIONS);
    db = dbManager.getDatabase("quoteDb");
  }

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
      log debug Serialization.writePretty(vishvasaPriyaSamskritaPadyani.next())
    }
  }
}
