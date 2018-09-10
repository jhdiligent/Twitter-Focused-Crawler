package focusedCrawler;

import java.util.LinkedList;
import java.util.List;
import org.jsoup.select.Elements;

public class Storage {
	List<Crawledpage> pages = new LinkedList<>();
	Scheduler sc;
	public Storage(Scheduler sc) {
		this.sc=sc;
	}
	/**
	 * extract links from web page
	 * @param webpage
	 * @return
	 */
	public boolean parsePage(WebPage webpage) {
		Elements links = webpage.doc.select("a[href]");
		Crawledpage page = new Crawledpage(webpage.url,links);
		pages.add(page);
		return sc.addURL(links,page);
	}
	public List<Crawledpage> getCrawledpages(){
		return pages;
	}
}
