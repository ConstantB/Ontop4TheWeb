package SPARQLSon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;

public class EasyQuery {

	public static void main(String[] args) throws IOException {

		String TDBdirectory = "/Users/<user-name>/Desktop/miniYago";
		// String queryString = "PREFIX ex: <http://example.org/> SELECT ?x ?l WHERE {?x ex:is_located_in ex:Uruguay . ?x ex:label ?l } ORDER BY ?x ?l";
		String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				   + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				   + "PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/> "
				   + "PREFIX ex: <http://example.org/> "
				   + "PREFIX rev: <http://purl.org/stuff/rev#> "
				   + "PREFIX dc: <http://purl.org/dc/elements/1.1/> "
				   + "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
				   + "SELECT * WHERE {?x rdfs:label ?l . "
				   + "?x rdf:type <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductType1> . "
				   + "?r bsbm:reviewFor ?x . ?r ex:id ?id . "
				   + "?r rev:reviewer ?reviewer . ?reviewer foaf:name ?revName . "
				   + "?r dc:title ?revTitle . ?r rev:text ?revText}";
		
		/* queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				   + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				   + "PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/> "
				   + "PREFIX ex: <http://example.org/> "
				   + "PREFIX rev: <http://purl.org/stuff/rev#> "
				   + "PREFIX dc: <http://purl.org/dc/elements/1.1/> "
				   + "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
				   + "SELECT * WHERE {?x rdfs:label ?l . "
				   + "?x rdf:type <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductType1> . "
				   + "?o bsbm:product ?x . ?o ex:id ?id . ?o bsbm:price ?price . ?o bsbm:vendor ?vendor . "
				   + "?vendor rdfs:label ?vendorTitle . ?vendor bsbm:country ?country} ";
		
		*/
		queryString = "PREFIX ex: <http://example.org/> "
				+ "PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/> "
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "SELECT * WHERE {?product rdfs:label ?label . "
				+ "?product rdf:type <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductType1> . "
				+ "<http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer7/Product286> rdfs:label ?label2 "
				+ "FILTER (<http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer7/Product286> != ?product) "
				+ "?product bsbm:productFeature ?f . <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/dataFromProducer7/Product286> bsbm:productFeature ?f2}";
		
		queryString = "PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/PropertyTextual1> "
		         + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
		         + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
		         + "SELECT * WHERE {?x rdf:type <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductType1> . "
		         +      "?x rdfs:label ?l . "
		         +                 "?x <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/productPropertyNumeric1> ?p1 . "
		         +                 "?x <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/productPropertyNumeric2> ?p2 . "
		         +                 "?x <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/productPropertyNumeric3> ?p3 . "
		         + 				   "OPTIONAL {"
		         +                 "?x <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/productPropertyNumeric4> ?p4 . "
		         +                 "?x <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/productPropertyNumeric5> ?p5 }}";
		
		queryString = "SELECT ?x ?y ?t ?t2 WHERE{?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/City> . " + 
			   "?y <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/City> . " + 
			   "?x <http://www.w3.org/2003/01/geo/wgs84_pos#long> ?lo . " + 
			   "?y <http://www.w3.org/2003/01/geo/wgs84_pos#long> ?lo2 . " + 
			   "?x <http://www.w3.org/2003/01/geo/wgs84_pos#lat> ?la . " + 
			   "?y <http://www.w3.org/2003/01/geo/wgs84_pos#lat> ?la2 . " + 
			   "?x <http://www.w3.org/2000/01/rdf-schema#label> ?x2 . " + 
			   "?y <http://www.w3.org/2000/01/rdf-schema#label> ?y2 " + 
			   "FILTER((?la <= -?la2 + 0.25) && (?la >= -?la2 - 0.25) && " + 
			   "(?lo <= -((?lo2/abs(?lo2))*(180 - abs(?lo2))) + 0.25) && (?lo >= -((?lo2/abs(?lo2))*(180 - abs(?lo2))) - 0.25)) }";
			   
		queryString = "SELECT * WHERE {?x ?y <http://dbpedia.org/ontology/Museum> . ?x <http://dbpedia.org/ontology/location> <http://dbpedia.org/resource/London> . ?x <http://www.w3.org/2000/01/rdf-schema#label> ?l }";
		
		queryString = "SELECT * WHERE {?x <http://www.wikidata.org/prop/direct/P31> <http://www.wikidata.org/entity/Q6672512> . ?x <http://www.w3.org/2000/01/rdf-schema#label> ?l . ?x <http://www.wikidata.org/prop/direct/P17> <http://www.wikidata.org/entity/Q145> }";
		
		queryString = "SELECT * WHERE {?x ?y ?z}";
		
		long start = System.nanoTime();
		
		DatabaseWrapper dbw = new DatabaseWrapper(TDBdirectory);
		ResultSet rs = dbw.execQuery(queryString);
		
		/*File file = new File("/Users/<user-name>/Desktop/reviews.json");
		OutputStream fos = new FileOutputStream(file);
		ResultSetFormatter.outputAsJSON(fos, rs);
		
		fos.close();*/

		int mappingCount = 0;
		for ( ; rs.hasNext() ; ) {
			QuerySolution rb = rs.nextSolution() ;
			mappingCount++;
		}
		
		System.out.println("Triples: " + mappingCount);
		
		dbw.qexec.close();
		dbw.dataset.close();
		
		long elapsedTime = System.nanoTime() - start;
		
		System.out.println(elapsedTime / 1000000000.0);
		
	}

}
