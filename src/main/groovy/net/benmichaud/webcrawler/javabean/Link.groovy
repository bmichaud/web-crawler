package net.benmichaud.webcrawler.javabean

import groovy.transform.Canonical

/**
 * This class encapsulates a hyperlink.
 */
@Canonical
class Link {
    String href
    String name
}
