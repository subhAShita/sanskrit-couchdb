package subhAShita

import net.liftweb.json.{NoTypeHints, Serialization}
import org.slf4j.LoggerFactory

object dbMaker {
  val log = LoggerFactory.getLogger(getClass.getName)
  def main(args: Array[String]): Unit = {
    implicit val formats = Serialization.formats(NoTypeHints)
    while (vishvasaPriyaSamskritaPadyani.hasNext()) {
      log debug Serialization.writePretty(vishvasaPriyaSamskritaPadyani.next())
    }
  }
}
