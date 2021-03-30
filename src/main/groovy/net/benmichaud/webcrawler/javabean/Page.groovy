package net.benmichaud.webcrawler.javabean


import groovy.transform.TupleConstructor

/**
 * A class that represents a web page, all the target links it contains and all the source pages that refer to it.
 */
@TupleConstructor
class Page {
    String href
    String title
    List<Link> links = []
    List<Page> sources = []
    List<Page> targets = []

    @Override
    boolean equals(Object other) {
        return other && href == other.href
    }

    @Override
    String toString() {
        return '[href: ' + href + ', title: ' + title + ']'
    }

    String toJsonString(List<Page> pages) {
        int linkIndex = 0;
        int targetIndex = 0;
        def linkJson = ''
        links.each { linkJson += (linkIndex++ ? ', ' : '') + it.toJsonString() }
        def pageJson = ''
        targets.each {
            if (!pages.contains(it) && pages.add(it)) {
                pageJson += (targetIndex++ ? ', ' : '') + it.toJsonString(pages)
            }
        }
        return '{"href": ' + (href ? '"' + href + '"' : 'null') + ', "title": ' + (title ? '"' + title + '"' : 'null') +
                ', "links": [' + linkJson + ']' +
                ', "targets": [' + pageJson + ']' + '}'
    }

}
