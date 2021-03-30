package net.benmichaud.webcrawler


import net.benmichaud.webcrawler.utility.WebCrawler

/**
 * This is the main class for the web crawler application.
 */
class App {

    static final String DEFAULT_FILE = "web-crawler-output.json"

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
                def json = wc.crawl(urlString)
                def fileName = (args.length > 1 ? args[1] : DEFAULT_FILE)
                File file = new File(fileName)
                file.write(json)
                println "Output is in the file: [${fileName}]"
            }
        } else {
            def msg = "Please pass in the URL of the web site to crawl."
            println msg
            throw new IllegalArgumentException(msg)
        }
    }
}
