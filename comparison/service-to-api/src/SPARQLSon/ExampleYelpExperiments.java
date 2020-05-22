package SPARQLSon;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.json.JSONException;

public class ExampleYelpExperiments {

	public static void main(String[] args) throws JSONException, Exception {

		// Database loading, change it for your folder
		String TDBdirectory = "/home/constant/spapi-repo-yelp";
		DatabaseWrapper dbw = new DatabaseWrapper(TDBdirectory);

		// Examples of query using the BIND_API function

		String yelp1b = "SELECT distinct  ?i   WHERE {"
				//	+ "?x <http://yago-knowledge.org/resource/isLocatedIn> <http://yago-knowledge.org/resource/Chile> . "
				//	+ "?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://yago-knowledge.org/resource/wikicat_Communes_of_Chile> . "
				+ " ?x <http://www.w3.org/2000/01/rdf-schema#label> ?l . "
					+ "SERVICE <https://api.yelp.com/v3/businesses/{l}>{"
					+ "  ( $.[\"id\"]) AS (?i)"
					+ "} "
					+ "} ";
		
		String loc = " SELECT distinct  ?i ?name  WHERE {"
				+ " ?x <http://www.w3.org/2000/01/rdf-schema#label> ?l ."
				+ "bindd"
            + " SERVICE <https://api.yelp.com/v3/businesses/{l}>{"
             + " ($.[\"businesses\"][0:20][\"name\"], $.[\"businesses\\\"][0:20][\"rating\"]) AS (?b, ?r)"
             + "   }"
             + " }";
	
	
		String yelp2b = "SELECT distinct  ?i ?name  WHERE {"
				//	+ "?x <http://yago-knowledge.org/resource/isLocatedIn> <http://yago-knowledge.org/resource/Chile> . "
				//	+ "?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://yago-knowledge.org/resource/wikicat_Communes_of_Chile> . "
				+ " ?x <http://www.w3.org/2000/01/rdf-schema#label> ?l . "
					+ "SERVICE <https://api.yelp.com/v3/businesses/{l}>{"
					+ "  ( $.[\"id\"], $.[\"name\"]) AS (?i, ?name)"
					+ "} "
					+ "} ";
		
		String yelp3b = "SELECT distinct  ?i ?name ?rating   WHERE {"
				//	+ "?x <http://yago-knowledge.org/resource/isLocatedIn> <http://yago-knowledge.org/resource/Chile> . "
				//	+ "?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://yago-knowledge.org/resource/wikicat_Communes_of_Chile> . "
				+ " ?x <http://www.w3.org/2000/01/rdf-schema#label> ?l . "
					+ "SERVICE <https://api.yelp.com/v3/businesses/{l}>{"
					+ "  ( $.[\"id\"], $.[\"name\"], $.[\"rating\"]) AS (?i, ?name, ?rating)"
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
		params3.put("cache", "false");
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
		String selected_query = loc;

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
