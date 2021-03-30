package net.benmichaud.webcrawler


import net.benmichaud.webcrawler.utility.WebCrawler

/**
 * This is the main class for the web crawler application.
 */
class App {

    /**
     * Call this maim method from the command-line with a single argument giving a starting point resource for the crawl.
     *
     * @param args the arguments sent on the command-line
     */
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
