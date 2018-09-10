package focusedCrawler;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class Crawler {

	Downloader dn;
	Storage st;
	Scheduler sc;
	int maxLinks;
	int maxDepth;
	String topic;
	public Crawler(int links,int depth,Set<String> seeds,String topic) {
		maxLinks=links;
		maxDepth=depth;
		sc=new Scheduler(maxDepth,maxLinks,seeds,topic);
		st=new Storage(sc);
		dn=new Downloader(st);	
		sc.setDownloader(dn);
		this.topic=topic;
	}
	public void run() throws IOException {
		sc.schedule();
	}
	public List<Crawledpage> getPages(){
		return st.getCrawledpages();
	}
	public List<String> getLinks(){
		return sc.getLinks();
	}
}
