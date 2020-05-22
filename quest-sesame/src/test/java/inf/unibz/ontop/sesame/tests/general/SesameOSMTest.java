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

import sesameWrapper.SesameVirtualRepo;

//import org.openrdf.query.resultio.sparqlkml.stSPARQLResultsKMLWriter;

public class SesameOSMTest extends TestCase {

	
	
	public void test() throws Exception
	{
		
		
		//create a sesame repository
		RepositoryConnection con = null;
		Repository repo = null;
		
		try {
			
			//String owlfile = "/home/constant/Vista/urban_atlas_melod.owl";
			//String owlfile = "src/test/resources/general/dummy.owl";
			String owlfile = "/home/constant/gisat/CityDistricts.owl";
			//String obdafile = "src/test/resources/general/lgd-bremen.obda";
			String obdafile = "/home/constant/mappings-ontop/osmfr2.obda";
			//String owlfile = 	"/home/timi/ontologies/helloworld/helloworld.owl";
			repo = new SesameVirtualRepo("my_name", owlfile, obdafile, false, "TreeWitness");
	
			repo.initialize();
			
			con = repo.getConnection();
			
			String prefixes = "prefix ex: <http://meraka/moss/exampleBooks.owl#> \n "
					+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
					+ "PREFIX cd: <http://melodiesproject.eu/CityDistricts/ontology#> \n"
					+ "PREFIX geo: <http://www.opengis.net/ont/geosparql#> \n"
					+ "PREFIX f: <http://melodiesproject.eu/field/ontology#> \n "
					+ "Prefix lgd: <http://www.app-lab.eu/resource/> \n"
					+ "Prefix lgdo: <http://www.app-lab.eu/ontology/> \n"
					+ "Prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n";
; 
			
			///query repo
			 try {
				 
				 String buildings = prefixes + " select distinct ?cat where { \n"
				 		+ "?s rdf:type lgdo:Building . \n "
				 		+ "?s lgdo:buildingCategory ?cat \n"
				 		+ "} limit 2";
				 

				 
				 String parks = prefixes +  "select distinct ?x ?geo where {" +
	 						"?x lgdo:hasLandUse lgdo:park . " +
	 						 "?x geo:asWKT ?geo " +
	 						"} limit 1";
				 
				 
				 String stations = prefixes +  "select distinct ?c where {" +
	 						 "?x rdf:type lgdo:TransportStation . \n" +
	 						 "?x lgdo:stationType ?c .  \n" +
	 						 "?x geo:asWKT ?geo   \n" +
	 						"}";
				 
				 String roads = prefixes +  "select distinct ?x ?geo where {" +
 						 "?x rdf:type lgdo:Road . \n" +
 						 "?x lgdo:roadType lgdo:motorway . "
 						 + "?x lgdo:maxspeed ?speed . \n" +
 						 "?x geo:asWKT ?geo   \n" +
 						"} limit 5";
			 
				 String parkings = prefixes +  "select distinct ?x where {" +
 						 "?x rdf:type lgdo:TrafficRelatedPlace . \n" +
 						 "?x lgdo:trafficType lgdo:parking . " +
 						 "?x geo:asWKT ?geo   \n" +
 						"}";
			 		 
				 
				 String spatialJoin = prefixes +  "select distinct ?geo where {" +
	 						"?x lgd:landUse lgd:port . "
	 						+ "?x geo:asWKT ?geo ." +
	 						 "?x1 geo:asWKT ?geo1 ."
	 						+ "FILTER(<http://www.opengis.net/def/function/geosparql/sfIntersects>(?geo,?geo1))" +
	 						"}";
				 			  
	
			      TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, roads);
			      FileOutputStream f = new FileOutputStream("/home/constant/ontop-kml/Vista.kml");
				  TupleQueryResultHandler handler = new SPARQLResultsTSVWriter(System.out);
				  //TupleQueryResultWriterFactory kml = new stSPARQLResultsKMLWriterFactory();

				  //TupleQueryResultHandler spatialHandler  = new stSPARQLResultsKMLWriter(f);
			       tupleQuery.evaluate(handler);
			   
			      
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

