package extractor;

import java.util.Set;

public class Extractor {
	public Set<String> getSeeds(String topic) {
		//GetAccessToken
//		GetAccessToken token = new GetAccessToken();
//		token.get(args);	
		//SearchTwitter
		SearchTwitter search = new SearchTwitter();
		search.search(topic);
		Set<String> seeds = search.getSeeds();
		System.out.println("seeds ready");
		return seeds;
	}

}
