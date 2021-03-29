package net.benmichaud.web.crawler.utility

import groovy.json.JsonSlurper
import org.codehaus.groovy.runtime.powerassert.PowerAssertionError
import spock.lang.Specification

/**
 * a test class for the {@link WebCrawler} class
 */
class WebCrawlerTest extends Specification {
    def "Web crawler responds with JSON when URL is given"() {
        given:
        def crawler = new WebCrawler()
        def slurper = new JsonSlurper()
        def urlString = "http://test.com/"

        when:
        println "Testing web crawler with non-null argument..."
        def response = slurper.parseText(crawler.crawl(urlString))

        then:
        response['url'] == urlString
    }

    def "Web crawler throws an exception when no argument is given."() {
        given:
        def crawler = new WebCrawler()

        when:
        println "Testing web crawler with null argument..."
        crawler.crawl(null)

        then:
        PowerAssertionError e = thrown()
    }
}
