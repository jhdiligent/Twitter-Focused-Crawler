package extractor;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;
 

public class SearchTwitter{
	private Twitter twitter = new TwitterFactory().getInstance();
	private Set<String> seeds = new HashSet<String>();
	
	public void search(String topic) {
        try {
        	/**
        	 * Query content
        	 */
            Query query = new Query(topic);
            
            QueryResult result;
            for(int i=0;i<180;i++) {
	            result = twitter.search(query);
	            List<Status> tweets = result.getTweets();
	      
	            for (Status tweet : tweets) {	            	
	            	URLEntity[] arr = tweet.getURLEntities();
	                if(tweet.getURLEntities()!=null) {	                	
	                	for(URLEntity url: arr) {
	                		if(!seeds.contains(url.getExpandedURL())) {
	                			seeds.add(url.getExpandedURL());
	                			System.out.println(url.getExpandedURL());
	                		}
	                	}
	                }
	            }
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
	}
	
	public Set<String> getSeeds(){
		return seeds;
	}
}
