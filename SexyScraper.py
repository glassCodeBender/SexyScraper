from urllib.request import urlopen
from bs4 import BeautifulSoup
import string

"""
This is a program I wrote to scrape a web-site for information about Windows processes.
Since I don't like python anymore, I'm going to parse the results of the web-scraping into
a TreeMap with scala.
"""

class SexyScraper:

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

    def clean_list(self, links):
        unique = set(links)
        new_list = []
        for item in unique:
            if " " in item:
                new_list.append(item)
        for item in new_list:
            unique.remove(item)
        return unique

    def remove_extras(self, unique):
        all_letters = string.ascii_lowercase
        for letter in all_letters:
            url = "/products/wintaskspro/processlibrary/other/" + letter
            if url in unique:
                unique.remove(url)
        return unique

    def scrape_pages(links):
        the_text = []
        urls = links
        for link in urls:
            try:
                url = "http://www.liutilities.com" + link
                html = urlopen(url)
                soup = BeautifulSoup(html, "lxml")
                result = soup.findAll("div", {"id": "indexer"})
            except Exception:
                continue
            if result:
                the_text.append(result[0].get_text())
        return the_text

    def write_file(self, pages):
        the_file = open("/Documents/scraped_results.txt", "w")
        for line in pages:
            the_file.write(line)
        the_file.close()
