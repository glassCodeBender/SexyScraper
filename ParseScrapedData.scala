
import io.Source
import collection.mutable.ArrayBuffer
import util.control.Breaks._

class ParseScrapedData {

  def method( ): Unit = {

    val fileNames = Vector[String]( "/Users/user_name/PycharmProjects/untitled1/process1000.txt"
    )

    val result = fixProcesses(fileNames(0))
    result.foreach(println)

  } // END main()

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
    val read1 = readFile(fileName)

    var buffer = ArrayBuffer[String]()
    var boundCount: Int = 0
    var info = ""

    for (value <- read1){
      breakable{
        if (value.contains("bound method Tag.get")){
          boundCount = boundCount + 1
          if (boundCount == 2 && value.contains("bound method")) {
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

    return buffer
  } // END fixProcesses()

  def readFile(fileName: String): Vector[String] = {
    Source.fromFile(fileName)
      .getLines
      .filterNot(x => x.contains("FREE registry scan"))
      .map(_.trim)
      .toVector
  }

} // END ParseScrapedData class
