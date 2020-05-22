package SPARQLSon;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.json.JSONException;

public class ExampleYelpExperimentsChicago {

	public static void main(String[] args) throws JSONException, Exception {

		// Database loading, change it for your folder
		String TDBdirectory = "/Users/Constant/spapi-repo-yelp-chicago";
		DatabaseWrapper dbw = new DatabaseWrapper(TDBdirectory);

		// Examples of query using the BIND_API function
		
		String loc = " SELECT distinct ?id ?b ?r " + 
				" WHERE {\n" + 
				" ?x <http://www.w3.org/2000/01/rdf-schema#label> ?l " + 
				"  SERVICE <https://api.yelp.com/v3/businesses/search?term=Burgers&location={l}&sort=2> {" + 
				"   ($.[\"businesses\"][0:20][\"name\"], $.[\"businesses\"][0:20][\"name\"],$.[\"businesses\"][0:20][\"rating\"] ) AS (?id, ?b, ?r)}" + 
				"}";
	
		String locbind = " SELECT distinct ?id ?b ?r " + 
				" WHERE {\n" + 
				" bind(\"Chicago\" as ?l) " + 
				"  SERVICE <https://api.yelp.com/v3/businesses/search?term=Burgers&location={l}&sort=2> {" + 
				"   ($.[\"businesses\"][0:20][\"name\"], $.[\"businesses\"][0:20][\"name\"],$.[\"businesses\"][0:20][\"rating\"] ) AS (?id, ?b, ?r)}" + 
				"}";

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
		String selected_query = locbind;

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
