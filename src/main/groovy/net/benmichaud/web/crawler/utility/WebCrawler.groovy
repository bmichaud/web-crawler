package net.benmichaud.web.crawler.utility

import groovy.json.JsonBuilder

class WebCrawler {
    WebCrawler() {
    }


    String crawl(String urlString) {
        def builder = new JsonBuilder()
        builder {
            url urlString
        }
        return builder.toString();
    }
}
