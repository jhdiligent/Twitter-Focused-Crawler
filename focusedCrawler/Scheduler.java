package focusedCrawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scheduler {
	int max;
	int counter;
	List<String> extractedlinks;
	Set<String> extractedurls;
	Queue<String> linkstocrawl;
	int depth;
	Downloader dn;
	boolean on;
	String topic;
	public Scheduler(int depth,int maxlinks,Set<String> seeds,String topic) {
		max=maxlinks;
		counter=0;
		this.depth=depth;
		extractedlinks=new LinkedList<>();
		extractedurls=new HashSet<>();
		linkstocrawl=new LinkedList<>();
		linkstocrawl.addAll(seeds);		
		on=true;
		this.topic=topic;
	}
	public void setDownloader(Downloader dn) {
		this.dn=dn;
	}
	/**
	 * add links from storage to scheduler. Choose topic relatic links and add it to queue linkstocrawl 
	 * @param links links from storage
	 * @param page crawledpage
	 * @return false if reach required number of links
	 */
	public boolean addURL(Elements links,Crawledpage page) {
		if(!on) {
			return false;
		}
		List<String> relatedlinks = new LinkedList<String>();
		for(Element link : links) {
			if(counter>=max) {
				on=false;
				page.relatedlinks=relatedlinks;
				return false;
			}	
			//links with anchor text or anchor image
			String url = link.toString();	
			String terms[]=topic.split(" ");
			/**
			 * roughly choose topic related links
			 */
			Boolean related =false;
			for(String term : terms) {
				if(url.toLowerCase().contains(term)) {
					related=true;
					break;
				}
			}			
			if(!related)continue;
			String realurl=link.attr("href");
			if(!realurl.startsWith("http:")&&!realurl.startsWith("https:"))continue;
			extractedlinks.add(realurl);
			counter++;
			System.out.println(counter+":"+realurl);
			relatedlinks.add(realurl);
			if(extractedurls.contains(realurl))continue;
			extractedurls.add(realurl);
			linkstocrawl.add(realurl);
		}
		page.relatedlinks=relatedlinks;
		return true;
	}
	/**
	 * Send links at linkstocrawl to Downloader in breath-first order. Check if depth reach off-line limit.
	 * @throws IOException
	 */
	public void schedule() throws IOException {
		int curd=0;
		while(on&&curd<depth) {
			int size = linkstocrawl.size();
			for(int i=0;i<size;i++) {
				String tocrawl=linkstocrawl.poll();
				if(!dn.download(tocrawl))
					break;
			}
			curd++;
			System.out.println("current depth: "+curd+", maxDepth: "+depth);
		}
	}
	public List<String> getLinks(){
		return extractedlinks;
	}
}
