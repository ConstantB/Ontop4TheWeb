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

public class SesameVirtualGeoExampleTest extends TestCase {

	
	public void test() throws Exception
	{
		
		
		//create a sesame repository
		RepositoryConnection con = null;
		Repository repo = null;
		
		try {
			
			//String owlfile = "/home/constant/Vista/urban_atlas_melod.owl";
			//String owlfile = "/home/constant/books.owl";
			String owlfile = "/home/constant/geo-example/tbox.owl";
			String obdafile = "/home/constant/geo-example/tbox.obda";
			//String owlfile = 	"/home/timi/ontologies/helloworld/helloworld.owl";
			repo = new SesameVirtualRepo("my_name", owlfile, obdafile, false, "TreeWitness");
	
			repo.initialize();
			
			con = repo.getConnection();
			
			 String prefixes = "prefix ex: <http://meraka/moss/exampleBooks.owl#> \n "
						+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
						+ "PREFIX f: <http://melodiesproject.eu/floods/> \n"
						+ "PREFIX geo: <http://www.opengis.net/ont/geosparql#> \n"
						+ "PREFIX gadm: <http://melodiesproject.eu/gadm/> \n"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
						+ "PREFIX osm: <http://melodiesproject.eu/osm/> \n"
						+ "PREFIX clc: <http://melodiesproject.eu/clc/> \n" ;
			
			///query repo
			 try {
				 String preds = prefixes +  "select distinct ?s ?p ?g1  where {" +
				 						"?s ?p ?g1 . " +
				 						"}";
				 
				 String geoexample =  "PREFIX geo: <http://www.opengis.net/ont/geosparql#> "+
						 						 "PREFIX geof: <http://www.opengis.net/def/function/geosparql/> "+
 						 "PREFIX : <http://example.com#> \n"+
 						 "select ?p ?gxa  \n"+
 						 "where {   \n"+
 						 "?p :vive_in_comune ?c.  \n " +
 						  "?x :comuneName ?c .  \n"+
 						 "?x geo:hasGeometry ?gx.   \n "+
 						 "?gx geo:asWKT ?gxa .  \n"+
 						 "FILTER(<http://www.opengis.net/def/function/geosparql/sfIntersects>(\"POLYGON((11.771850585937 41.737060546875, 12.376098632812 42.60498046875, 13.793334960937 41.94580078125, 13.727416992187 40.902099609375, 13.408813476562 41.033935546875, 11.771850585937 41.737060546875))\",?gxa))"+
 						  " \n }\n";
 	
				 
				 
			      TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, geoexample  );
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

