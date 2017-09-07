from AnonBrowser import *
# from bs4 import BeautifulSoup
# from urllib.request import urlopen
from bs4 import BeautifulSoup
from sys import argv

def get_links():
    pages = []
    for number in range(1, argv(1)):
        browser = AnonBrowser()
        browser.anonimize()
        urlUpdate = "http://www.processlibrary.com/en/directory/a/" + str(number)
        page = browser.open(urlUpdate)
        html = page.read()
        soup = BeautifulSoup(html, "lxml")
        for link in soup.findAll("a", href=True):
            pages.append(link['href'])
    return pages

def scrape_pages(links):
    the_text = []
    urls = links
    i = 0
    for link in urls:
        try:
            url = "http://www.processlibrary.com/" + link
            browser = AnonBrowser()
            browser.anonimize()
            page = browser.open(url)
            html = page.read()
            soup = BeautifulSoup(html, "lxml")
            result = soup.findAll("div", {"class": "six columns"})
        except Exception:
            pass
        if result:
            check = str(result[0].get_text)
            the_text.append(check)
        i = i + 1
    return the_text

def main():
    pages = get_links()
    unique = set(pages)
    sorted_list = sorted(unique)
    # convert_list = list(sorted_list)
    sliced = sorted_list[argv(2):argv(3)]
    scraped = scrape_pages(sliced)

    for found in scraped:
        print found

if __name__ == '__main__':
    main()
