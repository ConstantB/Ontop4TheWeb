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

public class SesameVirtualRequLGDTest extends TestCase {

	
	public void test() throws Exception
	{
		
		
		//create a sesame repository
		RepositoryConnection con = null;
		Repository repo = null;
		
		try {
			
			//String owlfile = "/home/constant/Vista/urban_atlas_melod.owl";
			//String owlfile = "/home/constant/books.owl";
			String owlfile = "/home/constant/rasters/geosparql.owl";
			
			//for opendap its cop.obda
			String obdafile = "/home/constant/mappings-ontop/requelgd.obda";
			//String owlfile = 	"/home/timi/ontologies/helloworld/helloworld.owl";
			repo = new SesameVirtualRepo("my_name", owlfile, obdafile, false, "TreeWitness");
	
			repo.initialize();
			
			con = repo.getConnection();
			
			 String prefixes = "prefix ex: <http://meraka/moss/exampleBooks.owl#> \n "
						+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
						+ "PREFIX f: <http://melodiesproject.eu/floods/> \n"
						+ "PREFIX geo: <http://www.opengis.net/ont/geosparql#> \n"
						+ "PREFIX gadm: <http://melodiesproject.eu/gadm/> \n"
						+ "PREFIX gag: <http://geo.linkedopendata.gr/gag/ontology/> \n"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
						+ "PREFIX osm: <http://melodiesproject.eu/osm/> \n"
						+ "PREFIX clc: <http://geo.linkedopendata.gr/corine/ontology#> \n"
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
				 
				 String spatialQuery = prefixes + "select distinct ?x1 ?lu  \n where \n{" +
	 						"?x1 gag:asWKT ?g1 . \n" +
	 						" ?x2 clc:asWKT ?g2 . "
	 						+ "?x2 clc:hasLandUse ?lu .\n" +
	 						"FILTER(<http://www.opengis.net/def/function/geosparql/sfIntersects>(?g1,?g2)) \n" +
	 						"} limit 10";
				 
			      TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, spatialQuery );
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

