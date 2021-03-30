package net.benmichaud.webcrawler.utility

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import net.benmichaud.webcrawler.javabean.Link
import net.benmichaud.webcrawler.javabean.Page
import net.benmichaud.webcrawler.javabean.Site
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.ccil.cowan.tagsoup.Parser

/**
 * This class will crawl a website, but only follow links within the same domain as the original request.
 *
 * Call the {@link #crawl} method to get a JSON representation of the result.
 */
class WebCrawler {
    Logger log = LoggerFactory.getLogger(this.getClass())
    def ENCODING = 'UTF-8'

    WebCrawler() {
    }

    /**
     * This method initiates a web crawl from the given URL.
     *
     * @param urlString
     * @return
     */
    String crawl(String urlString) {
        assert (urlString != null)
        Site map = new Site()
        map.rootLink = new Link(href: urlString, name: urlString)
        populateSite(map)
        return toJsonString(map)
    }

    /**
     * Initial step to populating the entire site map.
     *
     * @param site the site map object to populate
     */
    void populateSite(Site site) {
        site.rootPage = getPage(site, site.rootLink.href, null)
    }

    /**
     * Gets the page at the given HREF, referenced by the given source page in the given site map.
     *
     * @param href the href to retrieve
     * @param source the source page that links to it
     * @param site the site map
     * @return the retrieved page
     */
    Page getPage(Site site, String href, Page source) {
        def page = new Page()
        page.href = href
        if (site.pages.contains(page)) {
            log.debug("Page: [{}] already in site map.", page.href)
            page = site.pages.get(site.pages.indexOf(page))
            if (source) {
                page.sources.add(source)
            }
        } else {
            log.debug("Page: [{}] is not yet in site map.", page.href)
            site.pages.add(page)
            if (source) {
                page.sources.add(source)
                source.targets.add(page)
            }
            def parser = new XmlSlurper(new Parser())
            new URL(href).withReader(ENCODING) { reader ->
                def document = parser.parse(reader)
                if (document) {
                    page.title = document?.head?.title
                    document?.'**'.find { it['@id'] == 'results' }?.'**'.findAll { tag -> tag.name() == 'a' }.each { anchor ->
                        log.debug("Found Link: href: [{}], name: [{}]", anchor.href, anchor.text)
                        def link = new Link(href: anchor.href, name: anchor.text)
                        page.links.add(link)
                    }
                    log.debug("Page after parsing:\n{}", page)
                } else {
                    log.info("Page not found at this location: [{}].", href)
                }
            }
            log.debug("Processing {} links...", page.links?.size())
            page.links.each { link ->
                if (linkHasSiteDomain(link, site)) {
                    log.debug("Link: [{}] has the same domain as site link: [{}]", link, site.rootLink)
                    try {
                        Page nextPage = getPage(site, link.href, page)
                        if (nextPage) {
                            page.targets.add(nextPage)
                        }
                    } catch (Exception e) {
                        log.debug("Error while retrieving page at link: [{}]", link, e)
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
     * @return <code>  true</code> if the two domains are the same, otherwise <code>false</code>
     */
    boolean linkHasSiteDomain(Link link, Site site) {
        return new URL(site.rootLink.href)?.host == new URL(link.href).host
    }

    /**
     * converts a JavaBean object into a JSON string.
     *
     * @param object the object to convert
     * @return the resulting string
     */
    String toJsonString(Object object) {
        String jsonData = object.toString();
        try {
            ObjectMapper mapper = new ObjectMapper();
            jsonData = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert given object to JSON format.", e);
        }
        return jsonData;
    }

}
