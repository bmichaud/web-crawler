package net.benmichaud.webcrawler.utility


import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import net.benmichaud.webcrawler.javabean.Link
import net.benmichaud.webcrawler.javabean.Page
import net.benmichaud.webcrawler.javabean.Site
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * This class will crawl a website, but only follow links within the same domain as the original request.
 *
 * Call the {@link #crawl} method to get a JSON representation of the result.
 */
class WebCrawler {
    Logger log = LoggerFactory.getLogger(this.getClass())
    boolean isDebug = log.isDebugEnabled()

    WebCrawler() {
    }

    /**
     * This method initiates a web crawl from the given URL.
     *
     * After calling this, call {@link #getJson(Site)} or {@link #getPrettyJson(Site)} to obtain output.
     *
     * @param urlString the URL string to start crawling
     * @return the resulting {@link Site} object
     */
    Site crawl(String urlString) {
        assert (urlString != null)
        Site site = new Site()
        site.rootLink = new Link(href: urlString, name: urlString)
        getPage(site, site.rootLink.href, null)
        return site
    }

    /**
     * Builds a JSON string for thesite site and returns it. If the {@link #crawl(String)} method was not called first,
     * this will return null.
     *
     * @param the {@link Site} object to process
     * @return the JSON string
     */
    String getJson(Site site) {
        return site ? site.toJsonString() : null
    }

    /**
     * Builds a Pretty JSON string for thesite site and returns it. If the {@link #crawl(String)} method was not called first,
     * this will return null.
     *
     * @param the {@link Site} object to process
     * @return the pretty JSON string
     */
    String getPrettyJson(Site site) {
        def json = getJson(site)
        if (json) {
            def slurper = new JsonSlurper()
            def builder = new JsonBuilder()
            builder slurper.parseText(json)
            json = builder.toPrettyString()
        }
        return json
    }

    /**
     * Gets the page at the given HREF, referenced by the given source page in the given site map.
     *
     * @param href the href to retrieve
     * @param source the source page that links to it
     * @param site the site map
     * @return the retrieved page
     */
    private Page getPage(Site site, String href, Page source) {
        def page = new Page()
        page.href = href
        if (site.pages.contains(page)) {
            if (isDebug) log.debug("Page: [{}] already in site map.", page.href)
            page = site.pages.get(site.pages.indexOf(page))
            if (source) {
                page.sources.add(source)
            }
        } else {
            if (isDebug) log.debug("Page: [{}] is not yet in site map.", page.href)
            if (!site.rootPage) site.rootPage = page
            site.pages.add(page)
            def prefix = ''
            def path = ''
            if (source) {
                page.sources.add(source)
                source.targets.add(page)
                URL sourceUrl = new URL(source.href)
                prefix = sourceUrl.protocol + '://' + sourceUrl.host + (sourceUrl.port && sourceUrl.port > 0 && !((sourceUrl.port == 80) || (sourceUrl.port == 443)) ? ':' + sourceUrl.port : '')
                path = sourceUrl.path
                while (path.endsWith('/.')) {
                    path = path.substring(0, path.size() - 2)
                }
                while (path.endsWith('/')) {
                    path = path.substring(0, path.size() - 1)
                }
            }
            Document doc = Jsoup.connect(href).get()
            if (doc) {
                def titles = doc.select("title")
                if (isDebug) log.debug("Title(s) found: '{}'", titles)
                page.title = titles ? escapeJsonValue(titles[0].text()) : href
                def anchors = doc.select("a")
                if (isDebug) log.debug("Anchors found: [{}]", anchors)
                if (anchors) {
                    anchors.each { anchor ->
                        def aHref = anchor.attributes.get('href')
                        boolean proceed = true
                        if (isDebug) log.debug("Processing anchor href: [{}]...", aHref)
                        if (aHref.startsWith('//')) {
                            if (isDebug) log.debug("HREF with no protocol?")
                            prefix = new URL(site.rootLink.href).protocol + ':' + aHref
                        } else if (aHref =~ /^\/[^\/]?/) {
                            if (isDebug) log.debug("Only path in href, no host")
                            URL rootUrl = new URL(site.rootLink.href)
                            prefix = rootUrl.protocol + '://' + rootUrl.host + (rootUrl.port && rootUrl.port > 0 && !((rootUrl.port == 80) || (rootUrl.port == 443)) ? ':' + rootUrl.port : '')
                            aHref = prefix + aHref
                        } else if (aHref.startsWith('#')) {
                            if (isDebug) log.debug("intra-page (internal) anchor link")
                            proceed = false
                        } else if (aHref =~ /^\.\//) {
                            if (isDebug) log.debug("Current relative path")
                            if (source) {
                                aHref = prefix + path + aHref.substring(1)
                            } else {
                                proceed = false
                            }
                        } else if (aHref =~ /^\.\.\//) {
                            if (isDebug) log.debug("Upward relative path")
                            if (source) {
                                def snippedPath = (path.startsWith('/') ? path.substring(1) : path)
                                def pathParts = snippedPath.split(/\/+/)
                                int endIndex = pathParts.size() - 1
                                if (isDebug) log.debug("There are {} path elements in source path: [{}]", endIndex + 1, path)
                                while (aHref.startsWith('../')) {
                                    if (aHref.size() > 3) {
                                        aHref = aHref.substring(3)
                                        endIndex--
                                    } else {
                                        aHref = ''
                                    }
                                }
                                def temp = prefix
                                for (int i = 0; i <= endIndex; i++) {
                                    temp += '/' + pathParts[i]
                                }
                                aHref = temp + '/' + aHref
                                if (isDebug) log.debug("Relative path corrected: [{}]", aHref)
                            } else {
                                proceed = false
                            }
                        } else if (!aHref.startsWith('http')) {
                            if (isDebug) log.debug("Relative path?")
                            if (source) {
                                aHref = prefix + path + '/' + aHref
                            } else {
                                proceed = false
                            }
                        }
                        if (proceed) {
                            def text = escapeJsonValue(anchor.text())
                            if (isDebug) log.debug("Found Link: href: [{}], name: [{}]", aHref, text)
                            def link = new Link(href: aHref, name: text)
                            page.links.add(link)
                        } else {
                            if (isDebug) log.debug("Ignoring link with HREF: [{}]", aHref)
                        }
                    }
                }
            } else {
                log.info("Page not found at this location: [{}].", href)
            }
            if (isDebug) log.debug("Processing {} links...", page.links?.size())
            page.links.each { link ->
                if (linkHasSiteDomain(link, site)) {
                    if (isDebug) log.debug("Link: [{}] has the same domain as site link: [{}]", link, site.rootLink)
                    try {
                        Page nextPage = getPage(site, link.href, page)
                        if (nextPage) {
                            page.targets.add(nextPage)
                        }
                    } catch (Exception e) {
                        if (isDebug) log.debug("Error while retrieving page at link: [{}]", link, e)
                    }
                }
            }
        }
        return page
    }

    /**
     * After comparing the incoming link href to the top-level site link href, if the two domains are the same, then return true.
     *
     * @param link the link to compare
     * @param site the top-level site map class
     * @return <code>   true</code> if the two domains are the same, otherwise <code>false</code>
     */
    private boolean linkHasSiteDomain(Link link, Site site) {
        return new URL(site.rootLink.href)?.host == new URL(link.href).host
    }

    String escapeJsonValue(String value) {
        return value?.replaceAll('"', '\\\\"')
    }
}
