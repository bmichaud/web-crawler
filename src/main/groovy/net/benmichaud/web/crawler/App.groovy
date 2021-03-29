package net.benmichaud.web.crawler

import net.benmichaud.web.crawler.utility.WebCrawler

class App {

    static void main(String[] args) {
        if (args) {
            def urlString = args[0]
            if (urlString) {
                def wc = new WebCrawler()
                println wc.crawl(urlString)
            }
        } else {
            def msg = "Please pass in the URL of the web site to crawl."
            println msg
            throw new IllegalArgumentException(msg)
        }
    }
}
