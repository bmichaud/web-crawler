# web-crawler
A simple web crawler that will crawl a given web resource and map out all non-redundant links at that same domain, and record, but not follow, all links to external resources. 

## Required tools:
* JDK 11

## How to build:
`./gradlew jar`

## How to run:
`./gradlew run --args="<website_url>"`

## How to test:
`./gradlew test`
