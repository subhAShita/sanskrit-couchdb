package subhAShita

import org.slf4j.LoggerFactory
import sanskritnlp.transliteration.transliterator

import scala.collection.immutable.ListMap
import scala.collection.mutable

class Subhashita(val text: String, val language: String){
  assert(text.nonEmpty)
  val key: String = text
  val start_letter: String = text.toList.head.toString
  var topics: List[String] = List[String]()
  var authors = mutable.HashMap[String, String]()
  var sources = mutable.HashMap[String, String]()
  var source_to_ratings = mutable.HashMap[String, Int]()
  var description: String = ""
  var memorable_bits: List[String] = List[String]()

  def getValueMap: ListMap[String, String] = {
    // Order below corresponds to the order in which columns should appear in a CSV.
    return ListMap(
      "text" -> text,
      "topics" -> topics.mkString(","),
      "language" -> language,
      "authors" -> authors.mkString(","),
      "sources" -> sources.mkString(","),
      "source_to_ratings" -> source_to_ratings.mkString(","),
      "description" -> description,
      "memorable_bits" -> description,
      "key" -> key)
  }
  override def toString: String = getValueMap.toString()
}

class SanskritSubhashita(override val text: String) extends Subhashita(text, "संस्कृतम्") {
  override val key: String =
    transliterator.transliterate(
      text.replaceAll("\\P{IsDevanagari}", "").replaceAll("[।॥०-९]+", "").replaceAll("\\s", ""), "dev", "optitrans")
      .replaceAll("[MNn]", "m")

}

object subhAShitaTest {
  val log = LoggerFactory.getLogger(getClass.getName)
  def SanskritSubhashitaTest = {
    val subhashita = new SanskritSubhashita("दण्डः शास्ति प्रजाः सर्वाः दण्ड एवाभिरक्षति। दण्डः सुप्तेषु जागर्ति दण्डं धर्मं विदुर्बुधाः।। \tदण्डः\t")
    log info subhashita.key
    assert(subhashita.key == "damDaHshaastiprajaaHsarvaaHdamDaevaabhiraxatidamDaHsupteShujaagartidamDamdharmamvidurbudhaaHdamDaH")

  }
  def main(args: Array[String]): Unit = {
    SanskritSubhashitaTest
  }
}
