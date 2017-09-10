from AnonBrowser import *
from bs4 import BeautifulSoup
from sys import argv

"""
Program uses Python 2.7. Has mechanize and urllib2 dependencies. 
"""

script, letter, length = argv
pages = []

for number in range(1, int(length)):
    try:
        browser = AnonBrowser()
        browser.anonymize()
        urlUpdate = "http://www.processlibrary.com/en/directory/" + letter + "/" + str(number)
        page = browser.open(urlUpdate)
        html = page.read()
        soup = BeautifulSoup(html, "lxml")

        for link in soup.findAll("a", href=True):
            pages.append(link['href'])
    except Exception:
        continue

the_text = []
i = 0
unique = set(pages)

for link in unique:
    try:
        new_browser = AnonBrowser()
        new_browser.anonymize()
        url = "http://www.processlibrary.com/" + link
        page_found = new_browser.open(url)
        next_html = page_found.read()
        next_soup = BeautifulSoup(next_html, "lxml")
        result = next_soup.findAll("div", {"class": "six columns"})

    except Exception:
        continue

    if result != '':
        check = str(result[0].get_text)
        the_text.append(check)

    i = i + 1
"""
for found in the_text:
    print found

"""
try:
    file_name = letter + "_processes.txt"
    the_file = open(file_name, "w")
    for line in the_text:
        the_file.write(line)
    the_file.close()
except IOError:
    print "Something went wrong while printing to file."
