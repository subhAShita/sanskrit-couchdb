package subhAShita

import java.io.FileReader
import java.util

import net.liftweb.json.Serialization
import org.apache.commons.csv.{CSVFormat, CSVRecord, QuoteMode}
import org.slf4j.LoggerFactory
import sanskritnlp.quote.{TopicAnnotation, _}
import sanskritnlp.transliteration.transliterator

class QuoteList(val fileName: String, val id: String) {
  var csvReader = new FileReader(fileName)
  var records: util.Iterator[CSVRecord] =
    CSVFormat.TDF.withFirstRecordAsHeader.withIgnoreEmptyLines.parse(csvReader).iterator()
  val source: Source = null
}

object vishvasaPriyaSamskritaPadyani
  extends QuoteList(
    fileName = "/home/vvasuki/subhAShita-db-sanskrit/mUlAni/vishvAsa-priya-padyAni/प्रिय-काव्यांशाः priya-kAvyAmshAH subhAShitAni सुभाषितानि - पद्यानि.tsv",
    id = "विश्वास-प्रिय-पद्यानि") {
  val log = LoggerFactory.getLogger(getClass.getName)
  override val source = sourceHelper.getSanskritDevanaagariiSource("विश्वास-प्रिय-संस्कृत-पद्यानि", "विश्वासः"::Nil)
  def hasNext(): Boolean = records.hasNext

  def next(): QuoteWithInfo = {
    val record = records.next()
    log debug record.toMap.toString
    if (record.get("भाषा") == "" || record.get("भाषा") == "संस्कृतम्") {
      val quoteText = QuoteText(
        ScriptRendering(text = record.get("सुभाषितम्"), scheme = transliterator.scriptDevanAgarI)::Nil,
        language = Language("sa"))
      var descriptionAnnotations = List[DescriptionAnnotation]()
      // Don't add empty strings as descriptions.
      if (record.get("विवरणम्").nonEmpty) {
        descriptionAnnotations = DescriptionAnnotation(
          textKey=quoteText.key, source,
          QuoteWithInfo(new QuoteText(text = record.get("विवरणम्"))))::Nil
      }

      var originAnnotations  = List[OriginAnnotation]()
      val author = record.get("वक्ता")
      if (author.nonEmpty) {
        originAnnotations = OriginAnnotation(textKey=quoteText.key, source=source,
          origin = sourceHelper.fromAuthor(author = author)) :: Nil
      }
      val subhashita = QuoteWithInfo(quoteText,
        descriptionAnnotations=descriptionAnnotations,
        ratingAnnotations = RatingAnnotation(textKey=quoteText.key, source=source, overall = Rating(5))::Nil,
        topicAnnotations = record.get("विषयः").split(",").map(_.trim).map(
          x => TopicAnnotation(textKey=quoteText.key, source=source,
            topic = Topic(ScriptRendering(text = x, scheme = transliterator.scriptDevanAgarI),
              language = Language("sa")))).toList,
        originAnnotations = originAnnotations
      )
      return subhashita
    } else {
      log error("Unsupported language: " + record.toString)
      sys.exit()
    }
  }
}
