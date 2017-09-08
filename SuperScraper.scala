import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.util.control.Breaks.{break, breakable}
import sys.process._

/**
  * Process Scraper. The python program uses Python 2.7 and has mechanize and urllib2 dependencies. 
  * I'm not sure if this program will work with the new scraper. Was test before with Python 3.5.
  */
object SuperScraper {

  def main( args: Array[String] ): Unit = {

    val letterMap = Map(
    "a" -> 113,
    "b" -> 37,
    "c" -> 131,
    "d" -> 85,
    "e" -> 51,
    "f" -> 48,
    "g" -> 33,
    "h" -> 48,
    "i" -> 73,
    "j" -> 16,
    "k" -> 22,
    "l" -> 74,
    "m" -> 115,
    "n" -> 57,
    "o" -> 30,
    "p" -> 94,
    "q" -> 13,
    "r" -> 53,
    "s" -> 153,
    "t" -> 57,
    "u" -> 26,
    "v" -> 46,
    "w" -> 66,
    "x" -> 18,
    "y" -> 5,
    "z" -> 8
    )

    for((letter, upperBound) <- letterMap){

      val soup = makeSoup(letter, upperBound)
      soup.foreach(println)
    }

  } // END main()

  def run(result: String): ArrayBuffer[String] = {

    val fileName = result

    val grabbed = fixProcesses(result)
    val splitUp = grabbed.flatMap(x => x.split("           "))
      .map(x => x.trim)
      .filterNot(x => x.contains("This process is still being reviewed"))
      .map(x => x.replaceAll("<div class=\"WordSection1\">", ""))
      .flatMap(x => x.split("          "))
      .flatMap(x => x.split("         "))
      .map(x => x.replaceAll("<bound method Tag.get_text of <div class=\"six columns\"> ", ""))
      .map(x => x.replaceAll("> </div> >", ""))
      .map(x => x.replaceAll("\\n", ""))
      .map(x => x.trim)

    return splitUp

  } // END run()

  def makeSoup(letter: String, length: Int): ArrayBuffer[String] = {
    val len = length.toString()
    val soupWork: Option[String] = Some( s"python SuperScraper.py $letter $len".!!.trim )
    val scrapedStuff = run(soupWork.getOrElse(""))

    return scrapedStuff
  } // END run()

  def makeBuffer(files: Vector[String]): ArrayBuffer[String] = {
    var buffer = ArrayBuffer[String]()
    var i = 0
    while(i < files.size){
      val fixed = fixProcesses( files(i) )
      buffer ++: fixed
      i = i + 1
    }

    return buffer
  } // END makeBuffer()

  def fixProcesses(fileName: String): ArrayBuffer[String] = {
    val read1 = readResults(fileName)

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

  def readResults(result: String): Vector[String] = {
    Source.fromFile(result)
      .getLines
      .filterNot(x => x.contains("Non-system processes like"))
      .map(_.trim)
      .map(x => x.replaceAll("<br/>", ""))
      .map(x => x.replaceAll("<p>", ""))
      .map(x => x.replaceAll("</p", ""))
      .toVector
  } // readFile()

} // END Object
