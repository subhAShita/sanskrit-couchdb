package subhAShita

import org.slf4j.LoggerFactory

object dbMaker {
  val log = LoggerFactory.getLogger(getClass.getName)
  def main(args: Array[String]): Unit = {
    while (vishvasaPadyani.hasNext()) {
      log debug vishvasaPadyani.next().toString
    }
  }
}
