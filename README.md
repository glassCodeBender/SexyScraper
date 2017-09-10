# SexyScraper
I'm going to store any web-scraping scripts I write here. 

SuperScraper.py

SuperScraper.py uses Python 2.7 and has mechanize and urllib2 dependencies. It is a web-scraper that I'm using to create a giant list of known DLLs, processes, and descriptions of the programs that use them. 

Arguments: 
(1) Letter that the program begins with.
(2) The amount of pages that the web-site contains. 

Example Usage:
$ python SuperScraper.py a 113

SexyScraper.py
SexyScraper.py is a variation of a script I wrote to scrape information from a web-site about known processes. If you want to use the program, I suggest using the logic multiple jupyter notebook. First you need to sort the list of hyperlinks and then figure out how to split up the work. Based on my estimates, the program will take over 6 hours to run if it isn't split up.

SuperScraper.scala

Program used to parse data retrieved from SuperScraper.scala
