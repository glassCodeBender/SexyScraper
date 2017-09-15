import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.util.control.Breaks.{break, breakable}

/**
  * I'm pretty sure I ran the scraper with Python 3 on accident so I had to do some crazy coding to parse the data. 
  * Also, I should have used a stack instead of an ArrayBuffer since the data structure was pretty big.
  * 
  * Scraped data cleaner.
  * I didn't end up using this logic. I got tired of reposting code so I used the REPL and VIM for the extra cleaning.
  * This program only works with some of the scraped data. Some of the data will cause a buffer overflow.
  * I recommend removing some of the unecessary transformations.
  *
  * This program also doesn't remove duplicates. The logic for that is a secret. ;)
  */
object SuperScraper {

  def main( args: Array[String] ): Unit = {

    val fileName = "//Users//xan0//PycharmProjects//WebScraper//ScrapedProcesses//" + args(0)

    val firstClean = readResults(fileName)

    val secondClean = cleanUp(firstClean)

    val fullClean = fixProcesses(secondClean)

    val fixed1 = finalClean(fullClean)
    val fixed = lastFix(fixed1)
    val superClean: ArrayBuffer[String] = extraClean(fixed)
    val noHref = removeHref(superClean)
    val converted = convertIt(noHref)
    val fixConverted = fixFinalOutput(converted)
    val finalFix = fixList(fixConverted)

     finalFix.foreach(println)
  } // END main()

  def grabProcess(buff: ArrayBuffer[String]): ArrayBuffer[String] = {
    val regex = "^(\\S|\\s)+\\.(EXE|exe|dll|DLL)\\Z".r
    val process = buff.map(x => regex.findFirstIn(x).getOrElse(""))

    return process
  } // grabProcess()

  def fixList(buff: ArrayBuffer[String]): ArrayBuffer[String] = {
   val regex = "^\"\\S+\\.(EXE|exe|dll|DLL)\\sIS\\sA\\s".r
    val regex2 = "^\"(\\w|\\d|\\.|[~+])+\\.(EXE|exe|dll|DLL)".r

   val replaced =  buff.map(x => regex.replaceAllIn(x, "\"")).map(x => x.replaceAll("CALLED ", ""))

    return replaced
  }

  def fixFinalOutput(buff: ArrayBuffer[String]): ArrayBuffer[String] = {
    val regex = "^\"THE\\sFILE\\s".r
    val regex2 = "^\"THE\\sFILE\\sCALLED\\s".r
    val fixed = buff.map(x => regex.replaceAllIn(x, "\""))
    val fixed2 = fixed.map(x => regex2.replaceAllIn(x, "\""))

    return fixed2
  } // END fixFinalOutput()

  def convertIt(buff: ArrayBuffer[String]): ArrayBuffer[String] = {


    var mapped = mutable.Map[String, String]()

    val executable = "^(\\S|\\s)+\\.(EXE|exe|DLL|dll)\\s".r

    for(value <- buff){
      val check = executable.findFirstIn(value).getOrElse("")
      mapped += (check.trim.toUpperCase -> value)

    }

    // var newMapped = mutable.Map[String, String]()

    var newBuff = ArrayBuffer[String]()

    for((key, value) <- mapped){
     /* val regex = executable.findFirstIn(value).getOrElse("")
      val change = {
        "(?<=" + regex + "is\\s(a|an)\\s" + ")" + regex + "(?=\\sfrom)"
      }
      val changeReg = change.r
      val fixed = changeReg.replaceAllIn(value, "process")*/
      val description = value.replaceAll("\"", "")
      val newKey = key.replaceAll("\"", "")

      newBuff += "\"" + newKey.trim + "\"" + " -> " + "\"" +  description.trim + "\"" + ","

    } // END for loop

    val sortedBuff = newBuff.sorted

    return sortedBuff
  } // END convertIt()

  def removeHref(buff: ArrayBuffer[String]): ArrayBuffer[String] = {
    val regex =  "<a\\shref=http://www.uniblue.com/(\\S|\\s)+,$".r
    val result = buff.map( x => regex.replaceAllIn(x, "") )
    val regex2 = "<a href=http(\\S|\\s)+$".r
    val result2 = result.map(x => regex2.replaceAllIn(x, ""))
    val regex3 = "(//|\\s){2,}".r
    val result3 = result2.map(x => regex3.replaceAllIn(x, "//"))
    val regex4 = "<img(\\s|\\S)+".r
    val result4 = result3.map(x => regex4.replaceAllIn(x, ""))
    val regex5 = "(t|T|)he main process is <a(\\s|\\S)+$".r
    val result5 = result4.map(x => regex5.replaceAllIn(x, ""))
    val regex6 = "ATI\\sTECHNOLOGIES\\sUSE\\s([A-Z]|\\d)+\\sTO\\sOPERATE\\sTHEIR\\sGRAPHICS\\sDEVICES.\\sTHE\\sFILE\\s".r
    val oneMore = result5.map(x => regex6.replaceAllIn(x, ""))
    val finalResult = oneMore.map(x => x.replaceAll("&AMP", "and"))


   // "<a href=http://www.uniblue.com//"
    return finalResult.map(_.trim)
  }

  def finalClean(grabbed: ArrayBuffer[String]): ArrayBuffer[String] = {

    val splitUp = grabbed.flatMap(x => x.split("           "))
      .map(x => x.trim)
      .filterNot(x => x.contains("This process is still being reviewed"))
      .filterNot(x => x.contains("\"This program is a non-essential process, but should not be terminated unless suspected to be causing problems. \""))
      .filterNot(x => x.contains("<form action="))
      .filterNot(x => x.contains("TODO"))
      .filterNot(x => x.contains("N/A"))
      .map(_.trim)

    return splitUp

  } // END finalClean()

  def lastFix(buff: ArrayBuffer[String]): ArrayBuffer[String] = {
    buff.map(x => x.replaceAll("<div class=\"WordSection1\">", ""))
      .flatMap(x => x.split("          "))
      .flatMap(x => x.split("         "))
      .flatMap(x => x.split("      "))
      .flatMap(x => x.split("       "))
      .flatMap(x => x.split("     "))
      .map(x => x.trim)
  }


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
      .map(x => x.replaceAll("This program is a system service, and should not be terminated unless suspected to be causing problems.", ""))
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
    val reg4 = """////////////////""".r
    val check = reg.split(read)
    val extraCheck = check.flatMap(x => reg2.split(x)).toVector
    val superCheck = extraCheck.map(x => reg3.replaceAllIn(x, ""))
    val superDuperCheck = superCheck.map(x => reg4.replaceAllIn(x, "//"))

    return superDuperCheck
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
