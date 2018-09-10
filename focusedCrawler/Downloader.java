package focusedCrawler;

import java.io.IOException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Downloader {
	Storage st;
	public Downloader(Storage st) {
		this.st=st;
	}
	public boolean download(String url){
		Connection con;
		Document doc;
		try {
			con = Jsoup.connect(url);
			doc=con.get();
		} catch (IOException e) {
			return true;
		} 	
		WebPage page = new WebPage(url,doc);
		return st.parsePage(page);
	}	
}
