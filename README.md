# SexyScraper
I'm going to store any web-scraping scripts I write here. 

If you want to see the results of my web-scraping, check out all the Proc classes in AnalyzeVolatity.

SuperScraper.py

SuperScraper.py uses Python 2.7 and has mechanize, BeautifulSoup4 and urllib2 dependencies. It is a web-scraper that I'm using to create a giant list of known DLLs, processes, and descriptions of the programs that use them. Because the web-site is huge, the program creates an anonymous browser that erases cookies, changes useragent, and changes IP address. Before using the anonymous browser, the web-site blocked my IP address. 

Arguments: 
(1) Letter that the process or DLL begins with.
(2) The amount of pages that the section of the web-site contains. 

Example Usage:
$ python SuperScraper.py a 113

SexyScraper.py

SexyScraper.py uses Python 3.6 and is a variation of a script I wrote to scrape information from a web-site containing descriptions of known processes. If you want to use the program, I suggest using the logic multiple jupyter notebook. First you need to sort the list of hyperlinks and then figure out how to split up the work. Based on my estimates, the program will take over 6 hours to run if it isn't split up.

SuperScraper.scala

Program used to clean data retrieved from SuperScraper.py
Example Usage:
~$ scalac SuperScraper.scala
~$ scala SuperScraper c_processes.txt
