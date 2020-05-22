package SPARQLSon;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.json.JSONException;

public class ExampleOpenWeather {

	public static void main(String[] args) throws JSONException, Exception {

		// Database loading, change it for your folder
		String TDBdirectory = "/home/constant/spapi-repo";
		DatabaseWrapper dbw = new DatabaseWrapper(TDBdirectory);

		// Examples of query using the BIND_API function


		String test_use_case = "PREFIX ex: <http://example.org/> "
				+ "SELECT * WHERE {?x ex:label ?label . "
				+ "SERVICE <http://api.openweathermap.org/data/2.5/weather?q={label}&appid=be84c20688b078837610d2010e2cd564>{ "
				+ "  ($.[\"main\"][\"temp\"], $.[\"main\"][\"temp\"]) AS (?t, ?d) "
				+ "} "
				+ "}";
		
		String twitter =  "SELECT ?tw WHERE { \n"
				+ " ?x <http://example.org/twitter/label17> ?l . "
			//	+ "SERVICE <http://api.openweathermap.org/data/2.5/weather?q={label}&appid=be84c20688b078837610d2010e2cd564>{ "
				+ "SERVICE <https://api.twitter.com/1.1/search/tweets.json?q=iswc2017&count=100> {"
				+ "($.statuses[0:10].id) AS (?tw) "
				+ "} "
				+ "} ";
		
		String twittermix =  "SELECT ?t ?tw WHERE { \n"
				+ " ?x <http://example.org/twitter/label17> ?l . "
			//	+ "SERVICE <http://api.openweathermap.org/data/2.5/weather?q={label}&appid=be84c20688b078837610d2010e2cd564>{ "
				+ "SERVICE <https://api.twitter.com/1.1/search/tweets.json?q={l}&count=100> {"
				+ "($.statuses[0:10].id, $.statuses[0:10].user.name) AS (?t, ?tw) "
				+ "} "
				+ "} ";
		
		String users =  "SELECT ?u ?n  WHERE { \n"
				+ " ?x <http://example.org/twitter/label17> ?l . "
			//	+ "SERVICE <http://api.openweathermap.org/data/2.5/weather?q={label}&appid=be84c20688b078837610d2010e2cd564>{ "
				+ "SERVICE <https://api.twitter.com/1.1/search/tweets.json?q={l}&count=100> {"
				+ "($.statuses[0:10].user.screen_name, $.statuses[0:10].user.name) AS (?u, ?n) "
				+ "} "
				+ "} ";
		
		String all = "select ?s ?o where {"
				+ "?s <http://example.org/twitter/label17> ?o"
				+ "}";

	
		 
		GetJSONStrategy strategy_oauth = new OAuthStrategy();
		GetJSONStrategy strategy_oauth2 = new OAuthStrategy();


		// Storage of strategies and parameters to call the API(s)

		ArrayList<GetJSONStrategy> strategy = new ArrayList<>();
		ArrayList<HashMap<String, String>> params = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> params3 = new HashMap<String, String>();

		strategy.add(strategy_oauth);

		params3.put("replace_string", "_");
		params3.put("cache", "false");
		params3.put("distinct", "false");
		params3.put("min_api_call", "false");
		
		// This params are key for an OAuth authenticaton, maybe are my keys for Twitter or Yelp
		
		params3.put("consumerKey", "5vQVQ4B8bUcNGG3WOKr80gPdQ");
		params3.put("consumerSecret", "jkQw1PPQrKcKddBjg6AqYNH3n7cAogXhNTwf4m13urR37zKUdG");
		params3.put("token", "747542150561341440-RyK8r6AA0iCr3w5cbuNKmcxCDRfdJ42");
		params3.put("tokenSecret", "v5PfDnaLCIRu8KyLmfzXDOrykUtK96mmIwkTQNoUHG7mW");
		 
		strategy.add(strategy_oauth);
		strategy.add(strategy_oauth2);

		//params.add(params1);
		//params.add(params2);
		params.add(params3);

		// Execution of the query
		String selected_query = test_use_case;

		System.out.println("QUERYING: \n" + selected_query);
		long start = System.nanoTime();
		ResultSet rs = dbw.evaluateSPARQLSon(selected_query, strategy, params, false);
		// MappingSet ms = new MappingSet(rs);
		// System.out.println(ms.serializeAsValues());
		// rs = dbw.evaluateSPARQLSon("SELECT * WHERE {" + ms.serializeAsValues() + "}", strategy, params, false);
		ResultSetFormatter.outputAsTSV(rs);
		long elapsedTime = System.nanoTime() - start;

		System.out.println("Total Time: " + elapsedTime / 1000000000.0);
		System.out.println("API Time: " + dbw.apiOptimizer.timeApi / 1000000000.0);
		dbw.qexec.close();
		dbw.dataset.close();
		printStatistics();

	}

	public static void printStatistics() {
		System.out.println("API Calls: " + Experiments.API_CALLS);
		System.out.println("Cached Calls: " + Experiments.CACHED_CALLS);
		int count = 0;
		for(int[] stats: Experiments.REPEATED_CALLS) {
			System.out.println("SERVICE " + count + ": " + stats[0] + " - " + stats[1]);
			count += 1;
		}
	}

}
