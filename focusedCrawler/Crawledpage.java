package focusedCrawler;

import java.util.List;

import org.jsoup.select.Elements;

public class Crawledpage {
	String srcurl;
	Elements links;
	List<String> relatedlinks;
	int incoming;
	int outgoing;
	public Crawledpage(String url,Elements links) {
		srcurl=url;
		this.links=links;
	}
}
