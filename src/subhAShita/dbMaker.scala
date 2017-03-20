package subhAShita

import org.slf4j.LoggerFactory

object dbMaker {
  val log = LoggerFactory.getLogger(getClass.getName)
  def SanskritSubhashitaTest = {
    val subhashita = new SanskritSubhashita("दण्डः शास्ति प्रजाः सर्वाः दण्ड एवाभिरक्षति। दण्डः सुप्तेषु जागर्ति दण्डं धर्मं विदुर्बुधाः।। \tदण्डः\t")
    log info subhashita.key
    assert(subhashita.key == "")
  }
  def main(args: Array[String]): Unit = {
    SanskritSubhashitaTest
  }
}
