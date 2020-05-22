package SPARQLSon;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;

public class CreateID {

	public static void main(String[] args) throws IOException {

		String TDBdirectory = "/Users/<user-name>/Desktop/demoberlin";
		// String queryString = "PREFIX ex: <http://example.org/> SELECT ?x ?l WHERE {?x ex:is_located_in ex:Uruguay . ?x ex:label ?l } ORDER BY ?x ?l";
		String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
						   + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						   + "PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/> "
						   + "SELECT ?o WHERE {?x rdfs:label ?l . "
						   + "?x rdf:type <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductType1> . "
						   + "?o bsbm:product ?x}";
		
		queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				   + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				   + "PREFIX bsbm: <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/vocabulary/> "
				   + "SELECT ?r WHERE {?x rdfs:label ?l . "
				   + "?x rdf:type <http://www4.wiwiss.fu-berlin.de/bizer/bsbm/v01/instances/ProductType1> . "
				   + "?r bsbm:reviewFor ?x}";
		
		
		
		long start = System.nanoTime();
		
		DatabaseWrapper dbw = new DatabaseWrapper(TDBdirectory);
		ResultSet rs = dbw.execQuery(queryString);
		
		Writer writer = new BufferedWriter(new OutputStreamWriter(
	              new FileOutputStream("/Users/<user-name>/Desktop/reviewid.ttl"), "utf-8"));
	  
	   
		for ( ; rs.hasNext() ; ) {
			QuerySolution rb = rs.nextSolution() ;
			String element = rb.get("r").toString();
			String id = (element.split("/")[element.split("/").length - 1]).replaceAll("Review", "");
			writer.write("<" + element + "> <http://example.org/id> " + id + " . \n");
		}
		
		
		writer.close();
		dbw.qexec.close();
		dbw.dataset.close();
		
		long elapsedTime = System.nanoTime() - start;
		
		System.out.println(elapsedTime / 1000000000.0);
		
	}

}
