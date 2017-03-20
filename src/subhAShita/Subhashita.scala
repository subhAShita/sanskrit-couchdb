package subhAShita

import org.slf4j.LoggerFactory
import sanskritnlp.transliteration.transliterator

import scala.collection.mutable

class Subhashita(val text: String){
  val key: String = text
  val start_letter: String = text.toList.head.toString
  var topics: List[String] = List[String]()
  var sources: List[String] = List[String]()
  val language: String = null
  var source_to_ratings = mutable.HashMap[String, Int]()
  var description: String = null
  var memorable_bits: List[String] = List[String]()
}

class SanskritSubhashita(override val text: String) extends Subhashita(text) {
  override var language: String = 'संस्कृतम्'
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
