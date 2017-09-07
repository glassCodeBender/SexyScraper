import io.Source
import scala.collection.mutable.ArrayBuffer

object Fixed {

  def main( args: Array[String] ): Unit = {

    val read = Source.fromFile( "/Users/xan0/Documents/scrapedprocesses.txt" )
      .getLines
      .map( _.trim )
      .toVector

    val makeIt = read.mkString(" ")

    // val regex = """(?=\w+\.(exe|EXE))\s""".r

    val splitIt = makeIt.split( "\\s(?=(\\w|\\d|\\[|\\])+\\.(exe|EXE))" )
    val result: Vector[String] = splitIt.distinct.toVector

    val buff = firstLoop(result)
    val nextLoop = theLoop(buff)
    val finalLoop = theLoop(nextLoop)
    val oneLoop = theLoop(finalLoop)
    val twoLoop = theLoop(oneLoop)

    twoLoop.foreach( println )
  } // END main()

  def firstLoop(result: Vector[String]): ArrayBuffer[String] = {
    val executable = """(\w|\d|\[|\])+\.(exe|EXE)""".r
    var i = 0
    var exe = ""
    var buff = ArrayBuffer[String]()

    while ( i < (result.size - 1) ) {

      val found = executable.findFirstIn( result(i) ).getOrElse( "" )
      val next = executable.findFirstIn( result(i + 1) ).getOrElse( "" )

      if ( !found.equalsIgnoreCase( next ) && !found.equals( "" ) ) {
        buff += result( i )
        i = i + 1
      } else {
        exe = result( i ) + " " + result( i + 1 )
        buff += exe
        i = i + 2
      } // END if/else
    } // END while loop
    return buff
  } // END
  def theLoop(result: ArrayBuffer[String]): ArrayBuffer[String] = {

    val executable = """(\w|\d|\[|\])+\.(exe|EXE)""".r
    var i = 0
    var exe = ""
    var buff = ArrayBuffer[String]()

    while ( i < (result.size - 1) ) {
      val found = executable.findFirstIn( result(i) ).getOrElse( "" )
      val next = executable.findFirstIn( result(i + 1) ).getOrElse( "" )

      if ( !found.equalsIgnoreCase( next ) && !found.equals("") ) {
        buff += result( i )
        i = i + 1
      } else {
        exe = result( i ) + " " + result( i + 1 )
        buff += exe
        i = i + 2
      } // END if/else
    } // END while loop
    return buff
  } // theLoop()

} // END Fixed class
