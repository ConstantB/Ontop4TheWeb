package inf.unibz.ontop.sesame.tests.experiments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.TupleQueryResultHandler;
import org.openrdf.query.resultio.text.tsv.SPARQLResultsTSVWriter;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import sesameWrapper.SesameVirtualRepo;

public class SparqlUPExperiments {
	
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
	
	final static String owlfile = "/home/constant/vista/vista.owl";
	final static String base = "/home/constant/mappings-ontop/";
	static String resultsPath = "/home/constant/infobox/webtables/statistics/";
	
	public enum usecases {twitter, foursquare, films};
	
	//public enum usecases { webtable10,webtable100, webtable1000, webtable10000, webtable100000};

	public static void main(String[] args)  {
		int runs = 1;                    //default values
		Boolean warm = false ; 
		String suffix = null;
		
		if(args.length > 1){
			runs = Integer.parseInt(args[0]);
			warm = Boolean.parseBoolean(args[1]);		
		}
		  FileWriter fileWriter =  null;
		  if(warm)
			  suffix = "warm.txt";
		  else
			  suffix = "cold.txt";
		 
		  String path = resultsPath+suffix;
		  System.out.println("FILE:" + path);
		  	File file = new File(path);
		  	if(file.exists()){
		  		System.out.println("File exists");
		  		try {
					fileWriter = new FileWriter(file, false);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  	}else{
		  		System.out.println("File does not exist");
		  		try {
		  			System.out.println(file.getAbsolutePath());
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  		try {
					fileWriter = new FileWriter(file, true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  	}
			  
		  	PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.printf("%s\t%s\t%s\t%s\t%s\n", "operator","eval", "iter","total",  "results");
			printWriter.close();
			try {
				fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		executeExperiments(runs, warm);
		System.out.println("ALL EXPERIMENTS EXECUTED");
		System.exit(0);
     
    }
	public static void executeExperiments(int runs, Boolean warm){
		for( usecases operator : usecases.values()){
			executeQuerySet(operator.toString(), runs, owlfile, warm);
		}
	}
	
	public static void executeQuerySet(String operator, int runs, String owlfile, Boolean warm){ //execute all queries for a specific operator
		SparqlUpExperimentQueries qr = new SparqlUpExperimentQueries();
		ArrayList<String> queries = qr.getQueries(operator);   //get the queries per use case: twitter, foursquare, webtables
		Iterator <String> iter = queries.iterator();
		int index =0;
		System.out.println("Number of queries for this operator: " + queries.size());
		
		while(iter.hasNext()){
			index++;
			executeQueryWithTime(operator, owlfile, iter.next(), index, runs, warm);
		}
		
	}
	
	public static void executeQueryWithTime(String operator, String owlfile, String query, int index, int runs, Boolean warm){ //repetitions of the same query using the same repo connection
		RepositoryConnection con = null;
		Repository repo = null;
		String suffix = "warm.txt";
		String cold ="";
		if (!warm){
			cold = "-cold";
			suffix = "-cold.txt";
		}
			
		String obdafile = base + operator + cold + ".obda";
		
		try {
			repo = new SesameVirtualRepo("my_name", owlfile, obdafile , false, "TreeWitness");
			repo.initialize();
			con = repo.getConnection();
			TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, query );
		     FileOutputStream f = new FileOutputStream("/home/constant/ontop-kml/Vista.kml");
			 TupleQueryResultHandler handler = new SPARQLResultsTSVWriter(System.out);
			 
			 if(warm){
				 tupleQuery.evaluate();
				 con.close();
				 con = repo.getConnection();
			 }
			 
			 long startTime = System.nanoTime();   
			 TupleQueryResult rs = tupleQuery.evaluate();
			 long endTime = System.nanoTime();  
			 int results = 0;
			 while(rs.hasNext()){
				  results++;
			  }
			  long iterTime = System.nanoTime();
			  long eval = (endTime - startTime )/1000000;
			  long iter = ( iterTime - endTime )/1000000;
			  
			  if(!warm){
				  Thread.sleep(60000); //sleep for a minute for cold cache
			  }
			  
			  con.close();
			  
			  
	
			  String path = resultsPath + suffix;
			  FileWriter fileWriter = new FileWriter(path, true);
			  PrintWriter printWriter = new PrintWriter(fileWriter);
			  printWriter.printf("%s\t%f\t%f\t%f\t%d\n",  operator + index, (double) eval, (double) iter, (double) eval+iter, results);
			  printWriter.close();
			  fileWriter.close();
			  System.out.printf("%s\t%f\t%f\t%f\t%d\n", operator+ index, (double) eval, (double) iter, (double) eval+iter, results);
			  System.out.println("Experiments finished");
			  return;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}


}
