import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.util.control.Breaks.{break, breakable}
import sys.process._

/**
  * Created by xan0 on 9/6/17.
  */
class SuperScraper {

  def main( args: Array[String] ): Unit = {


    val soup = makeSoup()
    soup.foreach(println) 

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
      .map(x => x.trim)

    return splitUp

  } // END run()

  def makeSoup(): ArrayBuffer[String] = {
    val soupWork: String = Some( "python SuperScraper.py 113 8000 12000".!!.trim ).getOrElse("")
    val scrapedStuff = run(soupWork)

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

}
