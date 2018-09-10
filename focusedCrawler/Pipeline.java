package focusedCrawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import extractor.Extractor;

public class Pipeline {
	/**
	 * Test seeds
	 * @return
	 */
	public static Set<String> seeds() {
		String links[] = {
			"https://twitter.com/i/web/status/1027801780707422208",
			"https://wapo.st/2Olo8e3?tid=ss_tw&utm_term=.ec16c2f70017",
			"https://twitter.com/i/web/status/1027801779126394880",
			"https://twitter.com/i/web/status/1027801777280770048",
			"http://therightscoop.com/what-the-hell-is-going-on-at-the-fbi-lindsey-graham-makes-a-great-point-about-trump-youll-want-to-hear/",
			"https://www.motherjones.com/kevin-drum/2018/08/nunes-we-will-protect-trump-no-matter-what-hes-done/",
			"https://talkingpointsmemo.com/livewire/hannity-boasts-that-nobody-knows-nature-of-his-relationship-with-trump",
			"http://thehill.com/hilltv/rising/401185-the-handwritten-notes-exposing-what-fusion-gps-told-doj-about-trump",
			"https://twitter.com/i/web/status/1027801839826128897",
			"https://twitter.com/i/web/status/1027801837104029696",
			"https://www.20minutes.fr/monde/2319627-20180810-parents-melania-trump-naturalises-grace-migration-chaine",
			"https://twitter.com/i/web/status/1027801835799830533"
		};
		Set<String> seeds = new HashSet<>();
		for(String link:links) {
			seeds.add(link);
		}
		return seeds;
	}
	/**
	 * main function
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException {
		Scanner sc = new Scanner(System.in); 
		System.out.println("Enter topic");
		String topic = sc.nextLine();
		topic = topic.toLowerCase();
		System.out.println("Enter number of links");
		int numlinks = Integer.valueOf(sc.nextLine());
		System.out.println("Enter depth");
		int depth = Integer.valueOf(sc.nextLine());	
		sc.close();
		Extractor ex = new Extractor();
		Set<String> seeds = ex.getSeeds(topic);
//		Set<String> seeds = seeds();
		/**
		 * Crawler
		 */
		Crawler cr = new Crawler(numlinks, depth, seeds,topic);
		cr.run();
		/**
		 * Calculate statistical results
		 */
		//crawled pages
		List<Crawledpage> pages = cr.getPages();
		//extracted links
		List<String> links = cr.getLinks();
//		Calculator cal = new Calculator(links,pages,topic);
//		cal.output();
	}
}
