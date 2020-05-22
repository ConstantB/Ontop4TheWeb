package SPARQLSon;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainQuery {

	public static void main(String[] args) throws Exception {

		// Database loading
		String TDBdirectory = "/Users/<user-name>/Desktop/demo";
		DatabaseWrapper dbw = new DatabaseWrapper(TDBdirectory);

		// Examples of query using the BIND_API function


		String test_use_case = "PREFIX ex: <http://example.org/> "
				+ "SELECT ?l ?weather ?tw WHERE {?x ex:is_located_in ex:Chile . ?x ex:label ?l "
				+ "SERVICE <http://api.openweathermap.org/data/2.5/forecast?q={l},Chile&appid=99ac6530dcbd78fa4c02d08ec5297a52>{"
				+ "  ($.list[0].weather[0].description) AS (?weather)"
				+ "} "
				+ "SERVICE <https://api.twitter.com/1.1/search/tweets.json?q={l},Chile&result_type=recent>{"
				+ "  ($.statuses[0:3].text) AS (?tw)"
				+ "}"
				+ "}";

		test_use_case = "PREFIX ex: <http://example.org/> "
				+ "SELECT * WHERE {?x ex:is_located_in ex:Uruguay .  "
				+ "SERVICE <http://localhost:3000/api>{"
				+ "  ($.value_2[*]) AS (?v)"
				+ "} "
				+ "}";

		// Definition of parameters
		HashMap<String, String> params1 = new HashMap<String, String>();
		HashMap<String, String> params2 = new HashMap<String, String>();
		HashMap<String, String> params3 = new HashMap<String, String>();


		/*
		 *  Twitter app "SPARQLSon"
		 */
		// Authentication parameters
		params1.put("consumerKey", "rUz46qaGhBluwPP0eW9cYbLwX");
		params1.put("consumerSecret", "5IwORmXuijykDcg3gGvzTTpuX6dLeWXyK1mBiVrIFj8d8VDtM7");
		params1.put("token", "272959523-U7E3vIDq0Z17PPcVe5Spd0CxhcADdmvHL9wEd09K");
		params1.put("tokenSecret", "yuShdbXSH1Vucn7LYyflf0IpDTncdfBJ9eLBy9IO8pvLq");
		// Optimization parameters
		params1.put("replace_string", "_");
		params1.put("cache", "false");
		params1.put("min_api_call", "false");
		// params1.put("pipeline", "true");

		/*
		 *  Openweather
		 */
		// Authentication parameters
		params2.put("consumerKey", "");
		params2.put("consumerSecret", "");
		params2.put("token", "");
		params2.put("tokenSecret", "");
		params2.put("replace_string", "_");
		params2.put("cache", "false");
		// Optimization parameters
		params2.put("replace_string", "_");
		params2.put("cache", "false");
		params2.put("min_api_call", "false");
		// params2.put("pipeline", "true");

		params3.put("consumerKey", "fqBmHXJ6DeeTHQsyBvPWFw");
		params3.put("consumerSecret", "h7OS1xMz1Nf7HPKlJQkCdH6zszw");
		params3.put("token", "HT_BrNwG6PGWgKGjxSYy34-H1HlVug_O");
		params3.put("tokenSecret", "DRvZvXShinHhcK93LV2ZnNd1JK0");
		params3.put("replace_string", "_");


		// Definition of strategies to call the API(s)

		GetJSONStrategy strategy_oauth = new OAuthStrategy();
		GetJSONStrategy strategy_basic = new BasicStrategy();

		// Storage of strategies and parameters to call the API(s)

		ArrayList<GetJSONStrategy> strategy = new ArrayList<>();
		ArrayList<HashMap<String, String>> params = new ArrayList<HashMap<String,String>>();

		strategy.add(strategy_basic);
		strategy.add(strategy_oauth);

		params.add(params2);
		params.add(params1);


		// Execution of the query
		String selected_query = test_use_case;

		// System.out.println("QUERYING: \n" + selected_query);
		long start = System.nanoTime();
		ResultSet rs = dbw.evaluateSPARQLSon(selected_query, strategy, params);
		MappingSet ms = new MappingSet(rs);
		System.out.println(ms.serializeAsValues());
		rs = dbw.evaluateSPARQLSon("SELECT * WHERE {" + ms.serializeAsValues() + "}", strategy, params, false);
		ResultSetFormatter.outputAsTSV(rs);
		long elapsedTime = System.nanoTime() - start;
		dbw.qexec.close();
		dbw.dataset.close();

		// System.out.println("Mappings: " + dbw.mappingCount);
		// System.out.println("API Calls: " + dbw.apiOptimizer.apiCalls);
		// System.out.println("Total: " + elapsedTime / 1000000000.0);
		// System.out.println("API: " + dbw.apiOptimizer.timeApi / 1000000000.0);
		elapsedTime = elapsedTime - dbw.apiOptimizer.timeApi;
		// System.out.println("DB: " + elapsedTime / 1000000000.0);



	}

}
