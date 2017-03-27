package subhAShita

import java.io.FileReader
import java.util

import org.apache.commons.csv.{CSVFormat, CSVRecord, QuoteMode}
import org.slf4j.LoggerFactory
import sanskritnlp.subhAShita.{TopicAnnotation, _}
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
  override val source = Source(ScriptRendering(text = "विश्वास-प्रिय-संस्कृत-पद्यानि", scheme = transliterator.scriptDevanAgarI),
    List(ScriptRendering(text = "विश्वासः", scheme = transliterator.scriptDevanAgarI)))
  def hasNext(): Boolean = records.hasNext

  def next(): QuoteInfo = {
    val record = records.next()
    log debug record.toMap.toString
    if (record.get("भाषा") == "" || record.get("भाषा") == "संस्कृतम्") {
//      val originAnnotations = OriginAnnotation(source=source,
//        origin = Source(ScriptRendering(text = record.get("स्रोतांसि"), scheme = transliterator.scriptDevanAgarI),
//          List(ScriptRendering(text = record.get("वक्ता"), scheme = transliterator.scriptDevanAgarI))))::Nil
      val quoteText = QuoteText(
        ScriptRendering(text = record.get("सुभाषितम्"), scheme = transliterator.scriptDevanAgarI)::Nil,
        language = Language("sa"))
      var descriptionAnnotations = List[DescriptionAnnotation]()
      if (record.get("विवरणम्").nonEmpty) {
        descriptionAnnotations = DescriptionAnnotation(key=quoteText.key, source, ScriptRendering(text = record.get("विवरणम्")))::Nil
      }
      val subhashita = QuoteInfo(quoteText,
        descriptionAnnotations=descriptionAnnotations,
        ratingAnnotations = RatingAnnotation(key=quoteText.key, source=source, overall = Rating(5))::Nil,
        topicAnnotations = record.get("विषयः").split(",").map(_.trim).map(
          x => TopicAnnotation(key=quoteText.key, source=source,
            topic = Topic(ScriptRendering(text = x, scheme = transliterator.scriptDevanAgarI),
              language = Language("sa")))).toList
      )
      return subhashita
    } else {
      log error("Unsupported language: " + record.toString)
      sys.exit()
    }
  }
}
