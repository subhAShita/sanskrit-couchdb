package subhAShita

import java.io.FileReader
import java.util

import org.apache.commons.csv.{CSVFormat, CSVRecord}

class SubhashitaList(val fileName: String, val id: String) {
  var records: util.Iterator[CSVRecord] = null
  def InitializeTsv = {
    val in = new FileReader(fileName);
    records = CSVFormat.TDF.parse(in).iterator();
  }
  def nextSubhashita(): Subhashita = null
}

object VishvasaPadyani
  extends SubhashitaList(
    fileName = "/home/vvasuki/subhAShita-db-sanskrit/mUlAni/vishvAsa-priya-padyAni/प्रिय-काव्यांशाः priya-kAvyAmshAH subhAShitAni सुभाषितानि - पद्यानि.tsv",
    id = "विश्वास-प्रिय-पद्यानि") {
  def hasNext(): Boolean = records.hasNext

  def next(): Subhashita = {
    val record = records.next()
    val subhashita = new Subhashita(record.get("सुभाषितम्"))
    return subhashita
  }
}
