# Web Crawler - Find most used JS libraries

Web Crawler is a command line tool which counts top Javascript libraries used in web pages found on Google.

## What does it do?

0) Reads a string (search term) from standard input
1) Gets a Google result page for the search term
2) Extracts main result links from the page
3) Downloads the respective pages and extract the names of Javascript libraries used in them
4) Prints top 5 most used libraries to standard output

## Usage

1) Run ./gradlew jar in project root
2) java -jar build/libs/scalable-web-crawler.jar in project root
3) Enter a search term
4) Press ENTER key

## Tested With

Java 11

Gradle 5.6