package inf.unibz.ontop.sesame.tests.experiments;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.TupleQueryResultHandler;
import org.openrdf.query.resultio.text.tsv.SPARQLResultsTSVWriter;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;

import sesameWrapper.SesameVirtualRepo;

public class SparqlUPWebtableExperiment {
	
	final String prefixes = "prefix : <http://meraka/moss/exampleBooks.owl#> \n "
			+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
			+ "PREFIX f: <http://melodiesproject.eu/floods/> \n"
			+ "PREFIX geo: <http://www.opengis.net/ont/geosparql#> \n"
			+ "PREFIX gadm: <http://melodiesproject.eu/gadm/> \n"
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
			+ "PREFIX osm: <http://melodiesproject.eu/osm/> \n"
			+ "PREFIX clc: <http://melodiesproject.eu/clc/> \n"
			+ "PREFIX lai: <http://geo.linkedopendata.gr/lai/ontology#>";


	String scalabilityQuery = prefixes +  "select distinct ?s1  ?d ?l  \n"
	 		+ "where {" 
	 		+ "?s1 :date ?d .  \n"
	 		+ "?s1 :lead ?l .\n"
				+ "}";
	
	final String owlfile = "/home/constant/vista/vista.owl";
	final String base = "/home/constant/mappings-ontop/webtable";
	final String resultsPath = "/home/constant/infobox/rml/webtables/statistics";
	
	public void executeQueryWithTime(String obdafile, Boolean warm){
		RepositoryConnection con = null;
		Repository repo = null;
		//String obdafile = base + "10"+ ".obda";	
		
		try {
			repo = new SesameVirtualRepo("my_name", owlfile, obdafile , false, "TreeWitness");
			repo.initialize();
			con = repo.getConnection();
			TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, scalabilityQuery );
		     FileOutputStream f = new FileOutputStream("/home/constant/ontop-kml/Vista.kml");
			 TupleQueryResultHandler handler = new SPARQLResultsTSVWriter(System.out);
			 
			 if(warm){
				 tupleQuery.evaluate();
			 }
			 
			 long startTime = System.nanoTime();   
			 TupleQueryResult rs = tupleQuery.evaluate();
			 long endTime = System.nanoTime();  
			 int results = 0;
			 while(rs.hasNext()){
				  results++;
			  }
			  long iterTime = System.nanoTime();
			  System.out.println("Time elapsed: " + ( endTime - startTime )/1000000 + " milliseconds. Iteration time: " + ( iterTime - endTime )/1000000);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void test() throws Exception
	{
		
		List<String> queries = new ArrayList<String>();
		//create a sesame repository
		RepositoryConnection con = null;
		Repository repo = null;
		
		try {
			
			//String owlfile = "/home/constant/Vista/urban_atlas_melod.owl";
			//String owlfile = "/home/constant/books.owl";
			String owlfile = "/home/constant/vista/vista.owl";
			
			//for opendap its cop.obda
			String obdafile = "/home/constant/mappings-ontop/webtable-10.obda";
			//String owlfile = 	"/home/timi/ontologies/helloworld/helloworld.owl";
			repo = new SesameVirtualRepo("my_name", owlfile, obdafile, false, "TreeWitness");
			
			String fk_keyfile;
			//ImplicitDBConstraintsReader userConstraints = new ImplicitDBConstraintsReader(new File(fk_keyfile));
			// factory.setImplicitDBConstraints(userConstraints);
			// this.reasoner = factory.createReasoner(ontology, new SimpleConfiguration());
	
			
			repo.initialize();
			
			con = repo.getConnection();
			
			 String prefixes = "prefix : <http://meraka/moss/exampleBooks.owl#> \n "
						+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
						+ "PREFIX f: <http://melodiesproject.eu/floods/> \n"
						+ "PREFIX geo: <http://www.opengis.net/ont/geosparql#> \n"
						+ "PREFIX gadm: <http://melodiesproject.eu/gadm/> \n"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
						+ "PREFIX osm: <http://melodiesproject.eu/osm/> \n"
						+ "PREFIX clc: <http://melodiesproject.eu/clc/> \n"
						+ "PREFIX lai: <http://geo.linkedopendata.gr/lai/ontology#>";
			
			///query repo
			 try {
				 String preds = prefixes +  "select distinct  ?p  where {" +
				 						"?s ?p ?d17 . \n " +
				 						"} limit 10";
				 
				 String airt = prefixes +  "select distinct  ?s ?g1   where {" +
	 						"?s geo:asWKT ?g1 . " +
	 						"}";
				 
				 String one = prefixes +  "select distinct ?s1    \n"
					 		+ "where {" 
						 		+ "?s1 :date ?d .  \n"
		 						+ "}";

				 String two = prefixes +  "select distinct ?s1  ?d ?l  \n"
					 		+ "where {" 
						 		+ "?s1 :date ?d .  \n"
						 		+ "?s1 :lead ?l .\n"
		 						+ "}";
				 
				 String three = prefixes +  "select distinct ?p ?l  \n"
					 		+ "where {" 
						 		//+ "?s1 :date ?d .  \n"
						 		+ "?s1 :lead ?l .\n"
						 		+ "?s1 :pollingFirm ?p .\n"
		 						+ "} limit 5";
				 
				 String four = prefixes +  "select distinct ?s1  ?d ?pd ?poll  \n"
				 		+ "where {" 
					 		+ "?s1 :date ?d .  \n"
					 		+ "?s1 :lead ?l .\n"
					 		+ "?s1 :pollingFirm ?poll .\n"
					 		+ "?s1 :estimPD ?pd"
	 						+ "}";
				 
		
				  queries.add(one);
				  queries.add(two);
				  queries.add(three);
				  queries.add(four);
				  
			      TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, two );
			      FileOutputStream f = new FileOutputStream("/home/constant/ontop-kml/Vista.kml");
				  TupleQueryResultHandler handler = new SPARQLResultsTSVWriter(System.out);
				  long startTime = System.nanoTime();   
				  TupleQueryResult rs = tupleQuery.evaluate();
				  long endTime = System.nanoTime();  
				  int results = 0;
				  while(rs.hasNext()){
					  results++;
				  }
				  long iterTime = System.nanoTime();
				  //TupleQueryResultWriterFactory kml = new stSPARQLResultsKMLWriterFactory();

				  //TupleQueryResultHandler spatialHandler  = new stSPARQLResultsKMLWriter(f);
			      // tupleQuery.evaluate(handler);
			   
			      
			     /* queryString =  "CONSTRUCT {?s ?p ?o} WHERE {?s ?p ?o}";
			      GraphQuery graphQuery = con.prepareGraphQuery(QueryLanguage.SPARQL, queryString);
			      GraphQueryResult gresult = graphQuery.evaluate();
			      while(gresult.hasNext())
			      {
			    	  Statement s = gresult.next();
			    	  System.out.println(s.toString());
			      }
			      */

				  System.out.println("Closing...");
				  System.out.println("Time elapsed: " + ( endTime - startTime )/1000000 + " milliseconds. Iteration time: " + ( iterTime - endTime )/1000000);
				 
			     con.close();
			    	  
			   }
			 catch(Exception e)
			 {
				 e.printStackTrace();
			 }
			
			
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
	
	System.out.println("Done.");	
	}


	

}
