from urllib.request import urlopen
from bs4 import BeautifulSoup
import string
import time

"""
This is a program I wrote to scrape a web-site for information about Windows processes.
Since I don't like python anymore, I'm going to parse the results of the web-scraping from 
a txt file into a TreeMap with scala.

This program runs EXTREMELY slow. My recommendation is to take the logic, break up the list 
returned by remove_extras(), and run the program with a bunch of different programs. This assumes
you don't already know how to write programs with concurrency.

NOTE: I can't remember how to write full python programs anymore so I doubt the program will
run as is. If you want to use the program, use the program's logic in a jupyter notebook. It'd
probably just be easier to wait until ProcessTree.scala is finished if you are looking for a 
big list of processes.
"""
class SexyScraper:

    def __init__(self):
        pass

    def main(self):
        links = get_links()
        cleaned = clean_list(links)
        unique = remove_extras(cleaned)
        scraped = scrape_pages(unique)
        write_file(scraped)

    def get_links(self):
        all_letters = string.ascii_lowercase
        pages = []
        for letter in all_letters:
            urlUpdate = "http://www.liutilities.com/products/wintaskspro/processlibrary/other/" + letter
            html = urlopen( urlUpdate )
            soup = BeautifulSoup(html, "lxml")
            # regex = re.compile("^http://www.liutilities.com/products/wintaskspro/processlibrary/other/")
            for link in soup.findAll("a", href=True):
                pages.append(link['href'])
        return pages

    """ Remove links with blank spaces in them. They don't work. """
    def clean_list(self, links):
        unique = set(links)
        new_list = []
        for item in unique:
            if " " in item:
                new_list.append(item)
        for item in new_list:
            unique.remove(item)
        return unique

    """ Remove pages that don't have the info we want. """
    def remove_extras(self, unique):
        all_letters = string.ascii_lowercase
        for letter in all_letters:
            url = "/products/wintaskspro/processlibrary/other/" + letter
            if url in unique:
                unique.remove(url)
        return unique

    """ Get the info we want from each page we are looking through."""
    def scrape_pages(self, links):
        the_text = []
        urls = links
        i = 0
        for link in urls:
            try:
                if i % 10 == 0:
                    time.sleep(1)
                if i % 2 == 0:
                    time.sleep(.1)
                url = "http://www.liutilities.com" + link
                html = urlopen(url).read()
                soup = BeautifulSoup(html, "lxml")
                result = soup.findAll("div", {"class": "right"})
            except Exception:
                continue
            if result:
                check = str(result[0].get_text)
                the_text.append(check)
            i = i + 1
        return the_text

    """ Write results to a txt file. """
    def write_file(self, pages):
        the_file = open("scraped_results.txt", "w")
        for line in pages:
            the_file.write(line)
        the_file.close()

sexy = SexyScraper()
sexy.main()
