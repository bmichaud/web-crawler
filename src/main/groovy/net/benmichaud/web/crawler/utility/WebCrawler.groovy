package net.benmichaud.web.crawler.utility

import groovy.json.JsonBuilder

/**
 * This class will crawl a website, but only follow links within the same domain as the original request.
 *
 * Call the {@link #crawl} method to get a JSON representation of the result.
 */
class WebCrawler {
    WebCrawler() {
    }


    String crawl(String urlString) {
        assert(urlString != null)
        def builder = new JsonBuilder()
        builder {
            url urlString
        }
        return builder.toString();
    }
}
