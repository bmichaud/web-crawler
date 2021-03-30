package net.benmichaud.webcrawler.javabean

import groovy.transform.Canonical

/**
 * A class that represents a web page, all the target links it contains and all the source pages that refer to it.
 */
@Canonical
class Page {
    String href
    String title
    List<Link> links = []
    List<Page> sources = []
    List<Page> targets = []

    @Override
    public boolean equals(Object other) {
        return other && href == other.href
    }
}
