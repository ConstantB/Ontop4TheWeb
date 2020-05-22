package inf.unibz.ontop.sesame.tests.experiments;

import java.lang.*;
import java.util.ArrayList;
import java.util.List;


public class SparqlUpExperimentQueries {
	
	
	String  prefixes =  "prefix ex: <http://meraka/moss/exampleBooks.owl#> \n "
			+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
			+ "PREFIX f: <http://melodiesproject.eu/floods/> \n"
			+ "PREFIX geo: <http://www.opengis.net/ont/geosparql#> \n"
			+ "PREFIX gadm: <http://melodiesproject.eu/gadm/> \n"
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
			+ "PREFIX osm: <http://melodiesproject.eu/osm/> \n"
			+ "PREFIX clc: <http://melodiesproject.eu/clc/> \n"
			+ "PREFIX : <http://meraka/moss/exampleBooks.owl#>\n"
			+ "PREFIX lai: <http://geo.linkedopendata.gr/lai/ontology#> \n"
			+ "PREFIX twitter: <http://twitter.com/> \n"
			+ "PREFIX four: <http://foursquare.com/> \n"
			+ "PREFIX wiki: <http://en.wikipedia.org/movies/ontology#> \n"
			+ "PREFIX rot:  <http://www.rottentomatoes.com/top/bestofrt/>\n";

	 
	String webtable1 = prefixes +  "select distinct  ?s1 ?d   where {" +
															"?s1 :date ?d .  \n" +
																"}";
	
	String webtable2 = prefixes +  "select distinct ?s1 ?d ?l   \n"
	 															+ "where {" 
	 															+ "?s1 :date ?d .  \n"
	 															+ "?s1 :lead ?l .\n"
	 															+ "}";
	String webtable2sel = prefixes +  "select distinct ?s1 ?d    \n"
																+ "where {" 
																+ "?s1 :date ?d .  \n"
														 		+ "?s1 :lead \"1.5\"^^<http://www.w3.org/2001/XMLSchema#float> . \n"
																+ "}";
	
	String webtable3 = prefixes +  "select distinct ?s1  ?d ?l ?poll  \n"
	 															+ "where {" 
	 															+ "?s1 :date ?d .  \n"
	 															+ "?s1 :lead ?l .\n"
	 															+ "?s1 :pollingFirm ?poll .\n"
	 															+ "}";
	
	String webtable4 = prefixes +  "select distinct ?s1  ?d ?pd ?poll  \n"
	 															+ "where {" 
	 															+ "?s1 :date ?d .  \n"
	 															+ "?s1 :lead ?l .\n"
	 															+ "?s1 :pollingFirm ?poll .\n"
	 															+ "?s1 :estimPD ?pd"
	 															+ "}";
	
	String twitter1 = prefixes + "select distinct ?s  where { \n"
	 												+ "?s twitter:tweetsAbout <http://iswc2018.semanticweb.org/> . "
	 												+ "} " ; 
	
	String twitter2 = prefixes + "select distinct ?s ?f  where { \n"
													+ "?s twitter:tweetsAbout <http://iswc2018.semanticweb.org/> . "
													+ "?s twitter:sentiment ?f . "
													+ "} " ; 
	
	String twitter3 = prefixes + "select distinct ?s ?f  where { \n"
													+ "?s twitter:tweetsAbout <http://iswc2018.semanticweb.org/> . "
													+ "?s twitter:sentiment ?f . "
													+ "?s twitter:author ?sn \n"
													+ "} " ; 
	
	String foursquare1 = prefixes + "select distinct ?s ?name  where { \n"
	 												+ "?s four:name ?name . \n"
	 
	 												+ "} ";
	 
	String foursquare2 = prefixes + "select distinct ?s ?name ?now  where { \n"
													+ "?s four:name ?name . \n"
													+ "?s four:hereNow ?now . \n"
													+ "} ";
	
	String foursquare3 = prefixes + "select distinct ?s ?name ?now ?category where { \n"
				+ "?s four:name ?name . \n"
				+ "?s four:hereNow ?now . \n"
				+ "?s four:cat ?category . \n"
				+ "} ";
	
	 String films2 = prefixes + "select distinct ?title   \n"
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
		 
	public ArrayList<String> getQueries(String operator){
		ArrayList<String> l = new ArrayList<String>();
		
		switch(operator){
		case "webtable": 
			l.add(webtable1);
			l.add(webtable2);
			l.add(webtable3);
			l.add(webtable4);
			break;
		case "foursquare":
			l.add(foursquare1);
			l.add(foursquare2);
			l.add(foursquare3);
			break;
		case "twitter":
			l.add(twitter1);
			l.add(twitter2);
			l.add(twitter3);
			break;
		case "films":
			l.add(films2);
			l.add(films3);
			l.add(films5);
			//l.add(films4);
			break;
		default: //scalability experiments
			l.add(webtable2);
			l.add(webtable2sel);
			break;
		}
		
		return l;
		
	}
	
	//public  enum WebtableQueries {webtable1, webtable2, webtable3, webtable4};
	
	
	
	


}
