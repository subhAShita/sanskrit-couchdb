package subhAShita

import org.slf4j.LoggerFactory
import sanskritnlp.transliteration.transliterator

class Subhashita(val text: String){
  val key: String = text
  val startLetter: String = text.toList.head.toString
}

class SanskritSubhashita(override val text: String) extends Subhashita(text) {
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
