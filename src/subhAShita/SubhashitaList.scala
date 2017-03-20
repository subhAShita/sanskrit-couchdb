package subhAShita

import java.io.FileReader
import java.util

import org.apache.commons.csv.{CSVFormat, CSVRecord, QuoteMode}

class SubhashitaList(val fileName: String, val id: String) {
  var csvReader = new FileReader(fileName)
  var records: util.Iterator[CSVRecord] = CSVFormat.TDF.withFirstRecordAsHeader.withIgnoreEmptyLines.parse(csvReader).iterator()
}

object vishvasaPadyani
  extends SubhashitaList(
    fileName = "/home/vvasuki/subhAShita-db-sanskrit/mUlAni/vishvAsa-priya-padyAni/प्रिय-काव्यांशाः priya-kAvyAmshAH subhAShitAni सुभाषितानि - पद्यानि.tsv",
    id = "विश्वास-प्रिय-पद्यानि") {
  def hasNext(): Boolean = records.hasNext

  def next(): Subhashita = {
    val record = records.next()
    if (record.get("भाषा") == "" || record.get("भाषा") == "संस्कृतम्") {
      val subhashita = new SanskritSubhashita(record.get("सुभाषितम्"))
      subhashita.description = record.get("विवरणम्")
      subhashita.source_to_ratings(id) = 5
      subhashita.authors(id) = record.get("वक्ता")
      subhashita.topics = record.get("विषयः").split(",").map(_.trim).toList
      return subhashita
    } else {
      val subhashita = new Subhashita(record.get("सुभाषितम्"), record.get("भाषा"))
      subhashita.description = record.get("विवरणम्")
      subhashita.source_to_ratings(id) = 5
      subhashita.authors(id) = record.get("वक्ता")
      subhashita.topics = record.get("विषयः").split(",").map(_.trim).toList
      return subhashita
    }
  }
}
