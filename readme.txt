Twitter Focused Crawler is a single-thread focused crawler in java, using links extracted from twitter as seeds, filtering links with anchor text and link content, to crawl the web in breath-first order, which support customized query topic, crawler depths and overall extracted links and is able to extract 20000 links in 10 minutes.

How to use the code:
javac -cp twitter4j-core-4.0.4.jar extractor\*.java 
jar cvf extractor.jar extractor\*.class 
javac -cp extractor.jar;jsoup-1.11.3.jar focusedCrawler\*.java 
java -cp .\*;. focusedCrawler.Pipeline 
Input parameter as required, please choose a hot topic so that the focused crawler can get enough results. 
