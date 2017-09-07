package parseWebscrape

import scala.collection.mutable
import scala.io.Source
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Map

/**
  *
  */
object SuperFixed {
  def name( args: Array[String] ): Unit = {
    var buff = ArrayBuffer[String]()

    val readFile: Vector[String] = Source.fromFile("/Users/xan0/Documents/scraped.txt")
      .getLines
      .map(_.trim)
      .toVector

    val executable = """(\w|\d|\[|\])+\.(exe|EXE)""".r

    var mapped = mutable.Map[String, String]()

    var i = 0

    for(value <- readFile){
      val check = executable.findFirstIn(value).getOrElse("")
      mapped += (check.toUpperCase -> value)
      i = i + 1
    }

   // var newMapped = mutable.Map[String, String]()

    for((key, value) <- mapped){
      val regex = executable.findFirstIn(value).getOrElse("").r
      val change = {
        "(?<=" + regex + "is\\s(a|an)\\s" + ")" + "(\\w|\\d|\\[|\\])+\\.(exe|EXE)"  + "(?=\\sfrom)"
      }
      val changeReg = change.r
      val fixed = changeReg.replaceAllIn(value, "process")

      buff += "\"" + key + "\"" + " -> " + "\"" + fixed.trim + "\"" + ","
     //  newMapped += (key -> fixed)
    } // END for loop
    val sortedBuff = buff.sorted
    sortedBuff.foreach(println)

    // newMapped.size
/*
    for((key, value) <- newMapped){
      val name = executable.findFirstIn(value).getOrElse("")
      val regex = executable.findFirstIn(value).getOrElse("").r

      val withoutExe = "(\\w|\\d|\\[|\\])+(?=\\.(EXE|exe)\\sfrom)".r

      val processWithoutExe = withoutExe.findFirstIn(name).getOrElse("")

      val nextChange = {
        "(?<=" + regex + "\\sis\\s(a|an)\\s" + ")" + processWithoutExe + "(?=\\sfrom)"
      }

      val nextChangeFix = nextChange.r

      val finalFix = nextChangeFix.replaceAllIn(value, "process")

      buff += "\"" + key + "\"" + " -> " + "\"" + finalFix.trim + "\""
    } // END for loop

    buff.foreach(println)
*/
  } // END main()
} // END SuperFixed class
