import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.util.control.Breaks.{break, breakable}

/**
  * Process Scraper. The python program uses Python 2.7 and has mechanize and urllib2 dependencies.
  * I'm not sure if this program will work with the new scraper. I tested program before with Python 3.6.
  */
object SuperScraper {

  def main( args: Array[String] ): Unit = {

    val fileName = "/Users/xan0/PycharmProjects/WebScraper/ScrapedProcesses/" + args(0)

    val firstClean = readResults(fileName)

    val secondClean = cleanUp(firstClean)

    val fullClean = fixProcesses(secondClean)

    val fixed = finalClean(fullClean)
    val superClean = extraClean(fixed)

    superClean.foreach(println)
  } // END main()

  def finalClean(grabbed: ArrayBuffer[String]): ArrayBuffer[String] = {

    val splitUp = grabbed.flatMap(x => x.split("           "))
      .map(x => x.trim)
      .filterNot(x => x.contains("This process is still being reviewed"))
        .filterNot(x => x.contains("\"This program is a non-essential process, but should not be terminated unless suspected to be causing problems. \""))
        .filterNot(x => x.contains("<form action="))
      .filterNot(x => x.contains("TODO"))
        .filterNot(x => x.contains("N/A"))
      .map(x => x.replaceAll("<div class=\"WordSection1\">", ""))
      .flatMap(x => x.split("          "))
      .flatMap(x => x.split("         "))
        .flatMap(x => x.split("      "))
        .flatMap(x => x.split("       "))
        .flatMap(x => x.split("     "))
      .map(x => x.trim)


    return splitUp

  } // END splitUp()

  def extraClean(buff: ArrayBuffer[String]): ArrayBuffer[String] = {
    buff.map(x => x.replaceAll("<bound method Tag.get_text of <div class=\"six columns\"> ", ""))
      .map(x => x.replaceAll("> </div> >", ""))
      .map(x => x.replaceAll("</div>", ""))
      .map(x => x.replaceAll("\\n", ""))
      .map(x => x.replaceAll(">", ""))
      .map(x => x.replaceAll("\"If you have any information with more details, please e-mail us with the details at PL@uniblue.net. \"", ""))
      .map(x => x.replaceAll("This program is a non-essential process, and is installed for ease of use and can be safely removed.", ""))
      .map(x => x.replaceAll(" This program is important for the stable and secure running of your computer and should not be terminated.", ""))
      .map(x => x.replaceAll(" Please see additional details regarding this process.", ""))
      .map(x => x.trim)
  }
/*
  @deprecated
  def makeSoup(letter: String, length: String): ArrayBuffer[String] = {
    val soupWork: Option[String] = Some( s"python SuperScraper.py $letter $length".!!.trim )
    val scrapedStuff = finalClean(soupWork.getOrElse(""))

    return scrapedStuff
  } // END run()

  @deprecated
  def makeBuffer(files: Vector[String]): ArrayBuffer[String] = {
    var buffer = ArrayBuffer[String]()
    var i = 0
    while(i < files.size){
      val fixed = fixProcesses( files )
      buffer ++: fixed
      i = i + 1
    }

    return buffer
  } // END makeBuffer()
*/
  def fixProcesses(read1: Vector[String]): ArrayBuffer[String] = {

    var buffer = ArrayBuffer[String]()
    var boundCount: Int = 0
    var info = ""

    for (value <- read1){
      breakable{
        if (value.contains("</div>>")){
          boundCount = boundCount + 1
          if (boundCount == 2 && value.contains("</div>>")) {
            buffer += info
            boundCount = 0
            info = ""
          } // END if
          break
        } // END outer if
        else{
          info = info + " " + value
        } // END if/else
      } // END breakable
    } // END for loop

    buffer.map(x => x.trim)

    return buffer
  } // END fixProcesses()

  def readResults(fileName: String): Vector[String] = {
    val read = Source.fromFile(fileName).mkString
    val reg = """\\r""".r
    val reg2 = """\\n""".r
    val reg3 = """\\t""".r
    val check = reg.split(read)
    val extraCheck = check.flatMap(x => reg2.split(x)).toVector
    val superCheck = extraCheck.map(x => reg3.replaceAllIn(x, ""))

    return superCheck
  } // readFile()

  def cleanUp(vec: Vector[String]): Vector[String] = {
    vec.map(x => x.replaceAll("<br/>", ""))
      .filterNot(x => x.contains("Non-system processes like"))
      .map(x => x.replaceAll("<p>", ""))
      .map(x => x.replaceAll("</p", ""))
      .map(x => x.replaceAll("\\t", ""))
      .map(_.trim)
  }

} // END Object
