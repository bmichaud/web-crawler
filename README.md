# web-crawler
A simple web crawler that will crawl a given web resource and map out all non-redundant links at that same domain, and record, but not follow, all links to external resources.

## Output
The output is JSON, generated and saved to an output file whose default name is `web-crawler-output.json` in the current directory.

The top-level of the site has a `rootLink` and `rootPage` as follows:
```json
{
    "rootLink": {
        "href": "http://google.com/",
        "name": "http://google.com/"
    },
    "rootPage": {
        "href": "http://google.com/",
        "title": "Google"
    }
}
```

The root page is the parent of them all, all other pages are beneath it. Each page contains the following fields:
* `href` - the URL of the page
* `title` - the value of the <title> tag in the <head> if present or the same as `href`
* `links` - a list of all links on the page
* `targets` - a list of all pages in the same domain that have not already been cataloged elsewhere in the structure
* `sources` (not in JSON, but tracked in memory) - a list of all source pages in the same domain that refer to the page

Here is a page example:
```json
{
    "href": "https://google.com/search/howsearchworks/?fg=1",
    "title": "Google Search - Discover How Google Search Works",
    "links": [
        {
            "href": "http://google.com/search/howsearchworks/",
            "name": null
        },
        {
            "href": "http://google.com/search/howsearchworks/",
            "name": "Search"
        },
        {
            "href": "http://google.com/search/howsearchworks/",
            "name": "How Search works"
        },
        {
            "href": "http://google.com/search/howsearchworks/",
            "name": "Overview"
        },
        {
            "href": "http://google.com/search/howsearchworks/crawling-indexing/",
            "name": "Organizing information"
        },
        {
            "href": "http://google.com/search/howsearchworks/algorithms/",
            "name": "Search algorithms"
        },
        {
            "href": "http://google.com/search/howsearchworks/responses/",
            "name": "Useful responses"
        },
        {
            "href": "http://google.com/search/howsearchworks/mission/",
            "name": "Our mission"
        },
        {
            "href": "http://google.com/search/howsearchworks/mission/",
            "name": "Overview"
        },
        {
            "href": "http://google.com/search/howsearchworks/mission/users/",
            "name": "Rigorous testing"
        },
        {
            "href": "http://google.com/search/howsearchworks/mission/creators/",
            "name": "Help creators"
        },
        {
            "href": "http://google.com/search/howsearchworks/mission/open-web/",
            "name": "Maximize access"
        },
        {
            "href": "http://google.com/search/howsearchworks/",
            "name": "How Search works"
        },
        {
            "href": "http://google.com/search/howsearchworks/",
            "name": "Overview"
        },
        {
            "href": "http://google.com/search/howsearchworks/crawling-indexing/",
            "name": "Organizing information"
        },
        {
            "href": "http://google.com/search/howsearchworks/algorithms/",
            "name": "Search algorithms"
        },
        {
            "href": "http://google.com/search/howsearchworks/responses/",
            "name": "Useful responses"
        },
        {
            "href": "http://google.com/search/howsearchworks/mission/",
            "name": "Our mission"
        },
        {
            "href": "http://google.com/search/howsearchworks/mission/",
            "name": "Overview"
        },
        {
            "href": "http://google.com/search/howsearchworks/mission/users/",
            "name": "Rigorous testing"
        },
        {
            "href": "http://google.com/search/howsearchworks/mission/creators/",
            "name": "Help creators"
        },
        {
            "href": "http://google.com/search/howsearchworks/mission/open-web/",
            "name": "Maximize access"
        },
        {
            "href": "http://google.com/search/howsearchworks/crawling-indexing/",
            "name": "Learn more about crawling and indexing"
        },
        {
            "href": "http://google.com/search/howsearchworks/algorithms/",
            "name": "Learn more about Search algorithms"
        },
        {
            "href": "http://google.com/search/howsearchworks/responses/",
            "name": "Learn more about useful responses"
        },
        {
            "href": "https://howwemakemoney.withgoogle.com/",
            "name": "Learn more about how we make money with advertising"
        },
        {
            "href": "http://google.com/search/howsearchworks/mission/users/",
            "name": "Learn more about our rigorous testing"
        },
        {
            "href": "https://www.google.com/",
            "name": null
        },
        {
            "href": "https://www.google.com/policies/privacy/",
            "name": "Privacy"
        },
        {
            "href": "https://www.google.com/policies/terms/",
            "name": "Terms"
        },
        {
            "href": "https://www.google.com/about/",
            "name": "About Google"
        },
        {
            "href": "https://www.google.com/about/products/",
            "name": "Google Products"
        },
        {
            "href": "https://support.google.com/",
            "name": "Help"
        }
    ],
    "targets": []
}
```

## Required tools:
* JDK 11+

## How to build:
`./gradlew jar`

## How to run:
`./gradlew run --args="<website_url> [<output-json-file>]"`

## How to test:
`./gradlew test`

## Future considerations

Given more time, the following enhancements can be made to this application:
* Wrap the program in a web service.
* Create a client Web application to interact with the web service. This would display "names" that are actually images as images rather than text. 
* Create a mobile app to use the web service.
* Put the web service in a Docker container, pusblish it to ECR and deploy it to ECS or EKS.
* Enhance the configurability of the web scraper to map other types of links beyond just anchor links.
