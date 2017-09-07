
import scala.collection.mutable.ArrayBuffer
import io.Source
import util.control.Breaks._

    var buff = ArrayBuffer[String]()
    val list = Source.fromFile("/Users/xan0/Documents/to_fix.txt").getLines.map(_.trim).toVector

    var i = 0

list.foreach(println)
val saved = list(i).last
print(saved)

    while(i < list.size){
      breakable {
        val saved = list(i).last
       // val nextSaved = list( i + 1 ).last
        // val followingSaved = list( i + 2 ).last

        if (saved == ',') {
          buff += list( i )
          i = i + 1
          break
          }
        else {
          val droppedHead = list(i + 1).drop(1)
          val replaced = list( i ).replaceAll( "\" +", droppedHead )
          buff += replaced
          i = i + 2
        } // if/else
      }
    } // END while

buff.size
buff.foreach(println)
list.foreach(println)
