package focusedCrawler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
	List<String> extractedlinks;
	List<Crawledpage> crawledpages;
	String topic;
	public Calculator(List<String> links,List<Crawledpage> pages,String topic) {
		extractedlinks=links;
		crawledpages = pages;
		this.topic=topic;
	}
	public int numUniqueLinks() {
		Set<String> uniquelinks = new HashSet<>();
		for(String link : extractedlinks) {
			if(!uniquelinks.contains(link)) {
				uniquelinks.add(link);
			}
		}
		return uniquelinks.size();
	}
	public Map<String,Integer> domainDistribution(){
		Map<String,Integer> domain2freq = new HashMap<>();
		
		for(String link : extractedlinks) {
			//identify domain name
			String domain = getDomainName(link);
			int freq = domain2freq.getOrDefault(domain, 0);
			freq++;
			domain2freq.put(domain, freq);
		}
		return domain2freq;
	}
	private String getDomainName(String link) {
        String regEx = "((http|https)://)(([a-zA-Z0-9._-]+)|([0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}))(([a-zA-Z]{2,6})|(:[0-9]{1,4})?)";
        Pattern pattern = Pattern.compile(regEx);
        Matcher m = pattern.matcher(link);
        if (m.find()) {
        	String arr[] = m.group().split("//");
        	return arr[1];
        }         
        return "";
	}
	public List<String>[] sortByType() {
		final int T=0, I=1, V=2;
		List<String> types[] = new List[3];
		for(int i=0;i<3;i++) {
			types[i]=new LinkedList<String>();
		}
		// text image video
		String text[]= {"","pdf","txt","html","htm","shtml","xhtml","rtf","inf","tex","ltx","doc","docx","ppt","pptx","wps","odf","aspx","php"};
		String image[]= {"bmp","gif","ico","jpg","jpeg","png","psd","tif","tiff","webp","gifv"};
		String video[] = {"avi","wmv","mpg","mpeg","mov","rm","ram","swf"};
		Set<String> textsu = new HashSet<>(Arrays.asList(text));
		Set<String> imagesu = new HashSet<>(Arrays.asList(image));
		Set<String> videosu = new HashSet<>(Arrays.asList(video));
		for(String link : extractedlinks) {
			String su = getSuffix(link);
			if(textsu.contains(su)) {
				types[T].add(link);
			}
			else if(imagesu.contains(su)) {
				types[I].add(link);
			}
			else if(videosu.contains(su)) {
				types[V].add(link);
			}
			else {
				types[T].add(link);
				System.out.println(link);
			}
		}
		return types;
	}
	private String getSuffix(String s) {
		if(s.charAt(s.length()-1)=='/')return "";
	    String su=s.substring(s.lastIndexOf('.')+1,s.length());
		if(su.length()>5)return "";
		return su;
	}
	public void pageAnalyzer(BufferedWriter writer) throws IOException {
		Map<String,Integer> url2incoming = new HashMap<>();
		Map<String,Crawledpage> url2page = new HashMap<>();
		for(Crawledpage page : crawledpages) {
			String url = page.srcurl;
			page.outgoing=page.relatedlinks.size();
			url2incoming.put(url, 0);
			url2page.put(url, page);
		}
		//calculate incoming links
		for(Crawledpage page : crawledpages) {
			for(String out : page.relatedlinks) {
				if(url2incoming.containsKey(out)) {
					int incoming = url2incoming.get(out);
					incoming++;
					url2incoming.put(out, incoming);
				}
			}			
		}
		//store incoming to page
		for(Map.Entry<String, Integer> entry : url2incoming.entrySet()) {
			String url = entry.getKey();
			int incoming=entry.getValue();
			Crawledpage page = url2page.get(url);
			page.incoming=incoming;		
		}
		//top 25
		Collections.sort(crawledpages,new Comparator<Crawledpage>() {
			@Override
			public int compare(Crawledpage p1, Crawledpage p2) {
				return p2.outgoing-p1.outgoing;
			}
		});
		writer.append("Pages with top 25 outgoing links\n");
		for(int i=0;i<25&&i<crawledpages.size();i++) {
			Crawledpage page = crawledpages.get(i);
			writer.append(page.srcurl);
			writer.append("  -  ");
			writer.append(page.outgoing+"\n");
		}
		Collections.sort(crawledpages,new Comparator<Crawledpage>() {
			@Override
			public int compare(Crawledpage p1, Crawledpage p2) {
				return p2.incoming-p1.incoming;
			}
		});	
		writer.append("Pages with top 25 incoming links\n");
		for(int i=0;i<25&&i<crawledpages.size();i++) {
			Crawledpage page = crawledpages.get(i);
			writer.append(page.srcurl);
			writer.append("  -  ");
			writer.append(page.incoming+"\n");
		}
	}
	public void output() throws IOException {
	    BufferedWriter writer = new BufferedWriter(new FileWriter("results", true));
	    //Topic
	    writer.append("Query:");
	    writer.append(topic+"\n");
	    writer.append("a. Number of unique links extracted:");
	    writer.append(numUniqueLinks()+"\n");
	    writer.append("b. Frequency distribution by domain \n");
	    writer.append("  Domain   -    Frequency\n");
	    Map<String,Integer> db=domainDistribution();
	    for(Map.Entry<String, Integer> entry : db.entrySet()) {
	    	String d2f = "   "+entry.getKey()+"  -  "+entry.getValue()+"\n";
	    	writer.append(d2f);
	    }
	    writer.append("c. Breakdown of links by type (e.g., text, image, video)\n");
	    List<String> types[] = sortByType();
	    String labels[] = {"Text","Image","Vedio"};
	    for(int i=0;i<3;i++) {
	    	writer.append(labels[i]+"  -  "+types[i].size()+"\n");
	    }
	    writer.append("d. For each crawled page, compute the number of incoming and outgoing links.Report the top-25 pages with the highest number of incoming and outgoing links.\n");
		pageAnalyzer(writer);
	    writer.close();
	}
}
