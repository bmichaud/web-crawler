package net.benmichaud.webcrawler.javabean


import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor

/**
 * This class encapsulates an entire site map.
 */
@EqualsAndHashCode
@TupleConstructor
class Site {
    Link rootLink
    Page rootPage
    List<Page> pages = []

    @Override
    String toString() {
        return '[rootLink: ' + rootLink + ', rootPage: ' + rootPage + ']'
    }

    String toJsonString() {
        return '{"rootLink": ' + (rootLink ? rootLink.toJsonString() : 'null') + ', "rootPage": ' + (rootPage ? rootPage.toJsonString([]) : 'null') + '}'
    }

}
