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
import java.util.List;

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

public class SesameVirtualMadisTest extends TestCase {

	
	public void test() throws Exception
	{
		
		
		//create a sesame repository
		RepositoryConnection con = null;
		Repository repo = null;
		
		try {
			
			//String owlfile = "/home/constant/Vista/urban_atlas_melod.owl";
			//String owlfile = "/home/constant/books.owl";
			String owlfile = "/home/constant/vista/vista.owl";
			
			//for opendap its cop.obda
			String obdafile = "/home/constant/mappings-ontop/vitodap.obda";
			//String owlfile = 	"/home/timi/ontologies/helloworld/helloworld.owl";
			repo = new SesameVirtualRepo("my_name", owlfile, obdafile, false, "TreeWitness");
			
			String fk_keyfile;
			//ImplicitDBConstraintsReader userConstraints = new ImplicitDBConstraintsReader(new File(fk_keyfile));
			// factory.setImplicitDBConstraints(userConstraints);
			// this.reasoner = factory.createReasoner(ontology, new SimpleConfiguration());
	
			
			repo.initialize();
			
			con = repo.getConnection();
			
			 String prefixes = "prefix ex: <http://meraka/moss/exampleBooks.owl#> \n "
						+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
						+ "PREFIX f: <http://melodiesproject.eu/floods/> \n"
						+ "PREFIX geo: <http://www.opengis.net/ont/geosparql#> \n"
						+ "PREFIX gadm: <http://melodiesproject.eu/gadm/> \n"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
						+ "PREFIX osm: <http://melodiesproject.eu/osm/> \n"
						+ "PREFIX clc: <http://melodiesproject.eu/clc/> \n"
						+ "PREFIX : <http://meraka/moss/exampleBooks.owl#>\n"
						+ "PREFIX lai: <http://geo.linkedopendata.gr/lai/ontology#>";
			
			///query repo
			 try {
				 String preds = prefixes +  "select distinct  ?s ?name ?d17 ?d18   where {" +
				 						"?s <http://meraka/moss/exampleBooks.owl#debt2017> ?d17 . \n "
				 						+ "?s <http://meraka/moss/exampleBooks.owl#debt2018> ?d18 . \n"
				 						+ "?s <http://meraka/moss/exampleBooks.owl#name> ?name . \n" +
				 						"} limit 10";
				 
				 String airt = prefixes +  "select distinct  ?s ?g1   where {" +
	 						"?s geo:asWKT ?g1 . " +
	 						"}";
	 
				 
				 String query = prefixes +  "select distinct ?s1  ?g  \n"
				 		+ "where {" 
					 		+ "?s1 rdf:type f:rasterC .  \n"
					 		//+ "?s1 f:value ?v . "
					 		+ "?s1 f:vgeom ?g .\n"
	 						+ "} "
	 						+ "limit 1 "  ;		
				 
				 String madis = prefixes + "select distinct  ?s1  where { \n"
				 		+ "?s1 lai:hasLai ?airt1 . \n"
				 		+ "?s1 geo:asWKT ?g1 . "
				 		+ "?s2 lai:hasLai ?airt2 ."
				 		+ "?s2 geo:asWKT ?g2 .\n"
				 		+ "filter(<http://www.opengis.net/def/function/geosparql/sfIntersects>(?g1, ?g2))"
				 		+ "} limit 2" ;
				 
				 
				 String gadapm = prefixes + "select distinct  ?s1 ?lai  where { \n"
				 		+ "?s1 lai:hasLai ?lai . \n"
				 		+ "?s1 geo:asWKT ?g1 . "
				 	//	+ "?s1 lai:hasAdm ?adm2 ."
				 	//	+ "?s2 geo:asWKT ?g2 .\n"
				 	//	+ "filter(<http://www.opengis.net/def/function/geosparql/sfIntersects>(?g1, ?g2))"
				 		+ "} limit 200" ;
				 
				 String gadm = prefixes + "select distinct  ?s2  where { \n"
					 		+ "?s2 lai:hasAdm ?adm2 ."
					 		+ "?adm2 geo:asWKT ?g2 .\n"
					 		+ "} limit 2" ;
				 
				 String star = prefixes + "select distinct   ?s   \n"
					 		+ "where {\n"
					 		+ "?s ?p ?o"
					 		+ "}";
				 
			      TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, madis );
			      FileOutputStream f = new FileOutputStream("/home/constant/ontop-kml/Vista.kml");
				  TupleQueryResultHandler handler = new SPARQLResultsTSVWriter(new FileOutputStream("/home/constant/results-lai.tsv"));
				  //TupleQueryResultWriterFactory kml = new stSPARQLResultsKMLWriterFactory();

				  //TupleQueryResultHandler spatialHandler  = new stSPARQLResultsKMLWriter(f);
				  long start = System.nanoTime();
			       tupleQuery.evaluate(handler);
			      long end = System.nanoTime();
			      
			     /* queryString =  "CONSTRUCT {?s ?p ?o} WHERE {?s ?p ?o}";
			      GraphQuery graphQuery = con.prepareGraphQuery(QueryLanguage.SPARQL, queryString);
			      GraphQueryResult gresult = graphQuery.evaluate();
			      while(gresult.hasNext())
			      {
			    	  Statement s = gresult.next();
			    	  System.out.println(s.toString());
			      }
			      */
			      // TupleQuery tupleQuery2 = con.prepareTupleQuery(QueryLanguage.SPARQL, madis );
			       //tupleQuery2.evaluate(handler);
			      System.out.println("Query Execution time: " + (end-start)/1000000 + " milliseconds");
				  System.out.println("Closing...");
				 
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

