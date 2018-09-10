package focusedCrawler;

import org.jsoup.nodes.Document;

public class WebPage {
	String url;
	Document doc;
	public WebPage(String url,Document doc) {
		this.url=url;
		this.doc=doc;
	}
	
}
