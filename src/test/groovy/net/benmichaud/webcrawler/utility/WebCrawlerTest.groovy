package net.benmichaud.webcrawler.utility

import groovy.json.JsonSlurper
import org.codehaus.groovy.runtime.powerassert.PowerAssertionError
import spock.lang.Specification

/**
 * a test class for the {@link WebCrawler} class
 */
class WebCrawlerTest extends Specification {
    def "Web crawler responds with JSON when live page URL is given"() {
        given:
        def crawler = new WebCrawler()
        def slurper = new JsonSlurper()
        def urlString = "http://wiprodigital.com/"

        when:
        println "Testing web crawler with non-null argument..."
        def responseJson = crawler.crawl(urlString)
        def responseMap = slurper.parseText(responseJson)
        println "response:\n" + responseJson

        then:
        responseMap.rootLink.href == urlString
        responseMap.rootPage.links
    }

    def "Web crawler responds with JSON when defunct URL is given"() {
        given:
        def crawler = new WebCrawler()
        def slurper = new JsonSlurper()
        def urlString = "http://test.com/"

        when:
        println "Testing web crawler with non-null argument..."
        def responseJson = crawler.crawl(urlString)
        def responseMap = slurper.parseText(responseJson)
        println "response:\n" + responseJson

        then:
        responseMap.rootLink.href == urlString
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
