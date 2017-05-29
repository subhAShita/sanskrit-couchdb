package subhAShitaDb

import java.io.FileReader
import java.util

import dbSchema._
import dbSchema.common.{Language, ScriptRendering, Source, sourceHelper, Text, textHelper}
import dbSchema.quote._
import org.apache.commons.csv.{CSVFormat, CSVRecord}
import org.slf4j.LoggerFactory
import sanskritnlp.transliteration.transliterator

abstract class QuoteList(val fileName: String, val id: String) extends Iterator[QuoteWithInfo] {
  var csvReader = new FileReader(fileName)
  // Assumes TSV file by default. Uses first line as header.
  var records: util.Iterator[CSVRecord] =
    CSVFormat.TDF.withFirstRecordAsHeader.withIgnoreEmptyLines.parse(csvReader).iterator()
  val source: Source = null

  override def hasNext: Boolean = records.hasNext
}

object mahAsubhAShitasangraha
  extends QuoteList(
    fileName = "/home/vvasuki/subhAShita-db-sanskrit/mUlAni/mahA-subhAShita-sangraha/mahA-subhAShita-sangraha_1_per_line_dev.txt",
    id = "महा-सुभाषित-सङ्ग्रहः") {
    val log = LoggerFactory.getLogger(getClass.getName)
    override val source = sourceHelper.getSanskritDevanaagariiSource("महा-सुभाषित-सङ्ग्रहः", "स्टेय्न्बाक्-लुड्विगः"::Nil)

    def next(): QuoteWithInfo = {
      val record = records.next()
      log debug record.toMap.toString
      val quoteText = QuoteText(text = textHelper.getSanskritDevangariiText(record.get("सुभाषितम्")))
      val quoteId = transliterator.transliterate(record.get("ID").replace("MSS_", "महा-सुभाषित-सङ्ग्रहः"), sourceScheme = "iast", destScheme = transliterator.scriptDevanAgarI)
      val referenceAnnotations = ReferenceAnnotation(
        textKey=quoteText.text.getKey, source = source,
        reference = textHelper.getSanskritDevangariiText(quoteId))::Nil
      val subhashita = QuoteWithInfo(quoteText,
        referenceAnnotations = referenceAnnotations
      )
      return subhashita
    }
  }


object vishvasaPriyaSamskritaPadyani
  extends QuoteList(
    fileName = "/home/vvasuki/subhAShita-db-sanskrit/mUlAni/vishvAsa-priya-padyAni/प्रिय-काव्यांशाः priya-kAvyAmshAH subhAShitAni सुभाषितानि - पद्यानि.tsv",
    id = "विश्वास-प्रिय-पद्यानि") {
  val log = LoggerFactory.getLogger(getClass.getName)
  override val source = sourceHelper.getSanskritDevanaagariiSource("विश्वास-प्रिय-संस्कृत-पद्यानि", "विश्वासः"::Nil)

  def next(): QuoteWithInfo = {
    val record = records.next()
//    log debug record.toMap.toString
    if (record.get("भाषा") == "" || record.get("भाषा") == "संस्कृतम्") {
      val quoteText = QuoteText(text = textHelper.getSanskritDevangariiText(record.get("सुभाषितम्")))
      var descriptionAnnotations = List[DescriptionAnnotation]()
      // Don't add empty strings as descriptions.
      if (record.get("विवरणम्").nonEmpty) {
        descriptionAnnotations = DescriptionAnnotation(
          textKey=quoteText.text.getKey, source = source,
          description = textHelper.fromOnlyText(text = record.get("विवरणम्")))::Nil
      }

      var originAnnotations  = List[OriginAnnotation]()
      val author = record.get("वक्ता")
      if (author.nonEmpty) {
        originAnnotations = OriginAnnotation(textKey=quoteText.text.getKey, source=source,
          origin = sourceHelper.fromAuthor(author = author)) :: Nil
      }
      val topics = record.get("विषयः").split(",").map(_.trim)
      var topicAnnotations = List[TopicAnnotation]()
      if (topics.nonEmpty) {
        topicAnnotations = TopicAnnotation(textKey=quoteText.text.getKey, source=source,
          topics = topics.toList.map(x =>
            new Topic(text = textHelper.getSanskritDevangariiText(x)))) :: Nil
      }
      val subhashita = QuoteWithInfo(quoteText,
        descriptionAnnotations=descriptionAnnotations,
        ratingAnnotations = RatingAnnotation(textKey=quoteText.text.getKey, source=source, overall = Rating(5))::Nil,
        topicAnnotations = topicAnnotations,
        originAnnotations = originAnnotations
      )
      return subhashita
    } else {
      log error("Unsupported language: " + record.toString)
      sys.exit()
    }
  }
}
