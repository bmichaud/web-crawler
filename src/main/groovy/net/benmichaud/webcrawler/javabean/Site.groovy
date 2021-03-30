package net.benmichaud.webcrawler.javabean

import groovy.transform.Canonical

/**
 * This class encapsulates an entire site map.
 */
@Canonical
class Site {
    Link rootLink
    Page rootPage
    List<Page> pages = []
}
