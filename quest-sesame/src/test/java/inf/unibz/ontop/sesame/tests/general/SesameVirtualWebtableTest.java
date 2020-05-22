package inf.unibz.ontop.sesame.tests.general;

/*
 * #%L
 * ontop-quest-sesame
 * %%
 * Copyright (C) 2009 - 2013 Free University of Bozen-Bolzano
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Binding;

import junit.framework.TestCase;

import org.openrdf.model.Statement;
import org.openrdf.query.BindingSet;
import org.openrdf.query.GraphQuery;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.TupleQueryResultHandler;
import org.openrdf.query.resultio.TupleQueryResultWriterFactory;
//import org.openrdf.query.resultio.sparqlkml.stSPARQLResultsKMLWriter;
//import org.openrdf.query.resultio.sparqlkml.stSPARQLResultsKMLWriterFactory;
import org.openrdf.query.resultio.sparqlxml.SPARQLResultsXMLWriter;
import org.openrdf.query.resultio.text.tsv.SPARQLResultsTSVWriter;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;

import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLFactory;
import it.unibz.krdb.sql.ImplicitDBConstraintsReader;
import sesameWrapper.SesameVirtualRepo;

//import org.openrdf.query.resultio.sparqlkml.stSPARQLResultsKMLWriter;

public class SesameVirtualWebtableTest extends TestCase {
	


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
			String obdafile = "/home/constant/mappings-ontop/webtable10.obda";
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
						+ "PREFIX geo: <http://www.opengis.net/ont/geosparql#> \n"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
						+ "PREFIX wiki: <http://en.wikipedia.org/movies/ontology#> \n"
						+ "PREFIX rot:  <http://www.rottentomatoes.com/top/bestofrt/>\n";
			
			///query repo
			 try {
				 String preds = prefixes +  "select distinct  ?p  where {" +
				 						"?s ?p ?d17 . \n " +
				 						"} limit 10";
				 
				 String airt = prefixes +  "select distinct  ?s ?g1   where {" +
	 						"?s geo:asWKT ?g1 . " +
	 						"}";
				 
				 String one = prefixes +  "select *   \n"
					 		+ "where {" 
						 		+ "?s1 :date ?d .  \n"
						 		+ "?s1 :lead ?l."
		 						+ "}";

				 String two = prefixes +  "select distinct ?s1  ?d   \n"
					 		+ "where {" 
						 		+ "?s1 :date ?d .  \n"
						 		+ "?s1 :lead \"1.5\"^^<http://www.w3.org/2001/XMLSchema#float> ."
						 	//	+ "filter (?s1 >\"<http://meraka/moss/exampleBooks.owl#12>\")\n"
		 						+ "} limit 2";
				 
				 String three = prefixes +  "select distinct ?p ?l ?d \n"
					 		+ "where {" 
						 		+ "?s1 :date ?d .  \n"
						 		+ "?s1 :lead ?l .\n"
						 		+ "?s1 :pollingFirm ?p .\n"
		 						+ "} limit 5";
				 
				 String four = prefixes +  "select distinct *  \n"
				 		+ "where {" 
					 		+ "?s1 :date ?d .  \n"
					 		+ "?s1 :lead ?l .\n"
					 		+ "?s1 :pollingFirm ?poll .\n"
					 		+ "?s1 :estimPD ?pd"
	 						+ "}";
				 
				 String wiki = prefixes + "select ?s ?title \n"
				 		+ "where {\n"
				 		+ "?s wiki:title ?title"
				 		+ "}";
				 
				 String rotten = prefixes + "select ?s ?title \n"
					 		+ "where {\n"
					 		+ "?s rot:title ?title"
					 		+ "}";
				
		
				 String films2 = prefixes + "select distinct ?title  \n"
					 		+ "where {\n"
					 		+ "?s rot:title ?title . \n"
					 		+ "?s2 wiki:title ?title . \n"
					 		+ "} \n";
				 
				 String films3 = prefixes + "select distinct ?title ?rating  \n"
					 		+ "where {\n"
					 		+ "?s rot:title ?title . \n"
					 		+ "?s rot:rating ?rating ."
					 		+ "?s2 wiki:title ?title . \n"
					 		+ "} \n";
				 
				 String films4 = prefixes + "select distinct ?title ?rating  ?reviews \n"
					 		+ "where {\n"
					 		+ "?s rot:title ?title . \n"
					 		+ "?s rot:rating ?rating ."
					 		+ "?s rot:reviews ?reviews . \n"
					 		+ "?s2 wiki:title ?title . \n"
					 		+ "} \n";
				 
				 String films5 = prefixes + "select distinct ?title ?rating ?rank7 ?reviews \n"
					 		+ "where {\n"
					 		+ "?s rot:title ?title . \n"
					 		+ "?s rot:rating ?rating ."
					 		+ "?s rot:reviews ?reviews . \n"
					 		+ "?s2 wiki:title ?title . \n"
					 		+ "?s2 wiki:rank07 ?rank7 . \n"
					 		+ "} \n";	
				 
				 String star = prefixes + "select *  \n"
					 		+ "where {\n"
					 		+ "?s ?p ?o"
					 		+ "}";
				
				 		
				  queries.add(one);
				  queries.add(two);
				  queries.add(three);
				  queries.add(four);
				  
			      TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, one );
			      FileOutputStream f = new FileOutputStream("/home/constant/ontop-kml/Vista.kml");
				  TupleQueryResultHandler handler = new SPARQLResultsTSVWriter(System.out);
				  long startTime = System.nanoTime();   
				  tupleQuery.evaluate(handler);
				  long endTime = System.nanoTime();  
				  int results = 0;
				 /* while(rs.hasNext()){
				results++;
				  }*/
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

