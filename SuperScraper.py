from AnonBrowser import *
from bs4 import BeautifulSoup
from sys import argv

if len(argv) == 4:
    script, begin_slice, end_slice, length = argv
    pages = []
    for number in range(1, int(length)):
        try:
            browser = AnonBrowser()
            browser.anonymize()
            urlUpdate = "http://www.processlibrary.com/en/directory/a/" + str(number)
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
    convert_list = list(unique)
    sorted_list = sorted(convert_list)
    urls = sorted_list[int(begin_slice):int(end_slice)]

    for link in urls:
        try:
            url = "http://www.processlibrary.com/" + link
            new_browser = AnonBrowser()
            new_browser.anonymize()
            page_found = new_browser.open(url)
            html = page_found.read()
            soup = BeautifulSoup(html, "lxml")
            result = soup.findAll("div", {"class": "six columns"})

        except Exception:
            continue

        if result != '':
            check = str(result[0].get_text)
            the_text.append(check)

        i = i + 1

    for found in the_text:
        print found
else:
    print "Fuck! You didn't enter the correct number of arguments"
