package SPARQLSon;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.json.JSONException;

public class ExampleYelp {

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
		
		String foursquare =  "SELECT ?cat WHERE { \n"
				+ " ?x <http://example.org/twitter/label17> ?l . "
			//	+ "SERVICE <http://api.openweathermap.org/data/2.5/weather?q={label}&appid=be84c20688b078837610d2010e2cd564>{ "
				+ "SERVICE <https://api.foursquare.com/v2/venues/search?ll=40.7,-74&client_id=GQMVTAZZPTYOEV5IM4VVTFK5QKSIPWU4WPSREPJKTRK4TLSX&client_secret=HUBLQY2MDPNGG1OK2S02M3YBX0FPB42J0PEEGBQNWC21ZYPZ&v=20181010> {"
				+ "($.venues.id) AS (?cat) "
				+ "} "
				+ "} ";
		
		String yelp1 = "SELECT ?b WHERE {"
				//	+ "?x <http://yago-knowledge.org/resource/isLocatedIn> <http://yago-knowledge.org/resource/Chile> . "
				//	+ "?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://yago-knowledge.org/resource/wikicat_Communes_of_Chile> . "
				+ " ?x <http://example.org/label> ?l . "
					+ "SERVICE <https://api.yelp.com/v3/businesses/search?term=Burgers&location={l}&sort=2>{"
					+ "  ($.[\"businesses\"][0:20][\"name\"]) AS (?b)"
					+ "} "
					+ "} ";
		
		 
		String yelp2 = "SELECT distinct  ?b ?r WHERE {"
				//	+ "?x <http://yago-knowledge.org/resource/isLocatedIn> <http://yago-knowledge.org/resource/Chile> . "
				//	+ "?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://yago-knowledge.org/resource/wikicat_Communes_of_Chile> . "
				+ " ?x <http://example.org/label> ?l . "
					+ "SERVICE <https://api.yelp.com/v3/businesses/search?term=Burgers&location={l}>{"
					+ "  ($.[\"businesses\"][0:20][\"name\"], $.[\"businesses\"][0:20][\"rating\"]) AS (?b, ?r)"
					+ "} "
					+ "} ";
		
		String yelp3 = "SELECT distinct  ?b ?r ?i WHERE {"
				//	+ "?x <http://yago-knowledge.org/resource/isLocatedIn> <http://yago-knowledge.org/resource/Chile> . "
				//	+ "?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://yago-knowledge.org/resource/wikicat_Communes_of_Chile> . "
				+ " ?x <http://example.org/label> ?l . "
					+ "SERVICE <https://api.yelp.com/v3/businesses/search?term=Burgers&location={l}>{"
					+ "  ($.[\"businesses\"][0:20][\"name\"], $.[\"businesses\"][0:20][\"rating\"], $.[\"businesses\"][0:20][\"id\"]) AS (?b, ?r, ?i)"
					+ "} "
					+ "} ";
	
		String yelp2b = "SELECT distinct  ?b ?i ?l WHERE {"
				//	+ "?x <http://yago-knowledge.org/resource/isLocatedIn> <http://yago-knowledge.org/resource/Chile> . "
				//	+ "?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://yago-knowledge.org/resource/wikicat_Communes_of_Chile> . "
				+ " ?x <http://example.org/label> ?l . "
					+ "SERVICE <https://api.yelp.com/v3/businesses/search?term=Burgers&location={l}>{"
					+ "  ($.[\"businesses\"][0:20][\"name\"], $.[\"businesses\"][0:20][\"id\"]) AS (?b, ?i)"
					+ "} "
					+ "} ";
		

		String test_use_case_1 = "SELECT * WHERE{?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/City> . " + 
				" ?y <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/City> .  \n" + 
				"?x <http://www.w3.org/2003/01/geo/wgs84_pos#long> ?lo .  \n" + 
				"?y <http://www.w3.org/2003/01/geo/wgs84_pos#long> ?lo2 .  \n" + 
				"?x <http://www.w3.org/2003/01/geo/wgs84_pos#lat> ?la .  \n" + 
				"?y <http://www.w3.org/2003/01/geo/wgs84_pos#lat> ?la2 .  \n" + 
				"?x <http://www.w3.org/2000/01/rdf-schema#label> ?x2 .  \n" + 
				"?y <http://www.w3.org/2000/01/rdf-schema#label> ?y2  \n" + 
				"FILTER((?la <= -?la2 + 1) && (?la >= -?la2 - 1) && " + 
				"(?lo <= -((?lo2/abs(?lo2))*(180 - abs(?lo2))) + 1) && (?lo >= -((?lo2/abs(?lo2))*(180 - abs(?lo2))) - 1))  "
				+ "SERVICE <http://api.openweathermap.org/data/2.5/weather?q={x2}&appid=be84c20688b078837610d2010e2cd564>{"
				+ "  ($.[\"main\"][\"temp\"]) AS (?t) "
				+ "} "
				+ "SERVICE <http://api.openweathermap.org/data/2.5/weather?q={y2}&appid=be84c20688b078837610d2010e2cd564>{"
				+ "  ($.[\"main\"][\"temp\"]) AS (?t2)"
				+ "} "
				+ "}";
		
		String yelp2Union = "SELECT   ?b ?i  WHERE {"
				//	+ "?x <http://yago-knowledge.org/resource/isLocatedIn> <http://yago-knowledge.org/resource/Chile> . "
				//	+ "?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://yago-knowledge.org/resource/wikicat_Communes_of_Chile> . "
				+ " ?x <http://example.org/label> ?l . "
					+ "SERVICE <https://api.yelp.com/v3/businesses/search?term=Burgers&location={l}>{"
					+ "  ($.[\"businesses\"][0:20][\"name\"]) AS (?b) ."
					+ "} "
					+ "SERVICE <https://api.yelp.com/v3/businesses/search?term=Burgers&location={l}>{"
					+ "  ( $.[\"businesses\"][0:20][\"id\"]) AS (?i)"
					+ "} "
					+ "} ";
	
		
		String all = "select distinct ?p ?o where {"
				+ "?s ?p ?o"
				+ "}";

		
		GetJSONStrategy strategy_oauth = new BasicStrategy();
		GetJSONStrategy strategy_oauth2 = new OAuthStrategy();


		// Storage of strategies and parameters to call the API(s)

		ArrayList<GetJSONStrategy> strategy = new ArrayList<>();
		ArrayList<HashMap<String, String>> params = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> params3 = new HashMap<String, String>();

		strategy.add(strategy_oauth2);

		params3.put("replace_string", "_");
		params3.put("cache", "true");
		params3.put("distinct", "false");
		params3.put("min_api_call", "false");
		
		// This params are key for an OAuth authenticaton, maybe are my keys for Twitter or Yelp
		
		//params3.put("consumerKey", "GQMVTAZZPTYOEV5IM4VVTFK5QKSIPWU4WPSREPJKTRK4TLSX");
		params3.put("consumerKey", "zY7lVGH-dms35GHp6GvntBsyo26swi9lCbNZpAL4_LVC40N6f_Qs6vYF2ydKJGD4rJOan3iz9l8AmfGK5BFzU1WA66Cp0lHHeY6W60IkQoKfLkGWdgafHgjPKFm_W3Yx");
		params3.put("consumerSecret", "HUBLQY2MDPNGG1OK2S02M3YBX0FPB42J0PEEGBQNWC21ZYPZ");
		params3.put("token", "Bearer zY7lVGH-dms35GHp6GvntBsyo26swi9lCbNZpAL4_LVC40N6f_Qs6vYF2ydKJGD4rJOan3iz9l8AmfGK5BFzU1WA66Cp0lHHeY6W60IkQoKfLkGWdgafHgjPKFm_W3Yx");
		params3.put("tokenSecret", "v5PfDnaLCIRu8KyLmfzXDOrykUtK96mmIwkTQNoUHG7mW");
		
		strategy.add(strategy_oauth);
		//strategy.add(strategy_oauth2);

		//params.add(params1);
		//params.add(params2);
		params.add(params3);

		// Execution of the query
		String selected_query = yelp1;

		System.out.println("QUERYING: \n" + selected_query);
		long start = System.nanoTime();
		ResultSet rs = dbw.evaluateSPARQLSon(selected_query, strategy, params, false);
		int results = 0; 
				
		/*while(rs.next() != null){
			results++;
		}*/
		
		System.out.println("results: " + rs.getRowNumber());
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
