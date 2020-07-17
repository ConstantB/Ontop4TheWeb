[![Build Status](https://travis-ci.org/ontop/ontop.png?branch=develop)](https://travis-ci.org/ontop/ontop)
[![Maven Central](https://img.shields.io/maven-central/v/it.unibz.inf.ontop/ontop.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22it.unibz.inf.ontop%22)

Ontop4theWeb is a framework that extends the OBDA paradigm with the ability to query Web APIs (Foursquare, Twitter, Yelp, etc)  and Web tables (HTML)  using SPARQL 
on-the-fly, saving time and resources for developers and data scientists/engineers as data don't have to be downloaded and converted into RDF before querying. 
With Ontop4TheWeb, you can create a virtual OBDA repository and pose SPARQL queries to the Web APIs of your interest. The data will be transparently downloaded after
posing the queries, thus retrieving the most up-to-date snapshots of data. For this reason, Ontop4TheWeb is suitable for querying On-the-fly data of high velocity, i.e., 
that get updated frequently. 

Under the hood 
------------------------------------

To achieve this magic SPARQL-to-* translation, Ontop4TheWeb employs ontologies and mappings (in R2RML/OBDA format). Mappings map the web data into 
virtual RDF terms. Ontop and Ontop-spatial use relational databases as back-end. Ontop4TheWeb employs virtual databases. Virtual tables are tables that are not 
aterialised and do not even exist before a SPARQL query is posed to the system. In tranditional OBDA, mappings contain connection to existing databases and materialised
tables that are already available once an OBDA repository is created. In this case, a virtual OBDA repo is created with mappings that contain a virtual table operator
instead of a virtual table. So the mappings include a function that is invoked when a SPARQL query is posed and involves the respective mapping. 
The SPARQL query gets translated into the SQL-extended query containing the virtual table operator declared in the respective mapping.
The function  then creates a virtual table operator and populates it with fresh data fetched from the respective Web API. Ontop4TheWeb then translates the relational
results into virtual RDF terms and the results are returned to the user, in the same way as if the data was materliased and stored in an RDF store.

Mind the Cache 
Ontop4TheWeb is suitable for web sources that get updated frequently. But web sources get updated on a different rate. There are sources that get updated
every few seconds, minutes, days or months, or even get partially updated. Ontop4TheWeb implements a caching mechanism that can be configured with the 
update rate of the source of interest, by passing the configuration parameter as an argument of the virtual table operator that is included in the mappings. 
So, it is possible that the same virtual table operator is used for multiple sources or multiple insances of the same source (with different caching parameter). 
Caching stores the results of a virtual table operator to the disk for a configurable time window w, so that if the same query is posed within this time window, 
the cached results will be returned, without connecting to the Web API, but only after the time window expires. 

Installation
------------------------------
*Prerequisities
Ontop4TheWeb uses the system MadIS as back-end. It is an APSW wrapper for SQLite, enabling users to write their own SQL operators (e.g., virtual tables) in a few lines
of Python code. This is used in replacement to the DBMSs that serve as back-end systems for traditional OBDA systems like Ontop. 
We have extended MadIS with additional virtual table operators (for the Web APIs and the caching mechanism), so please first download our fork of MadIS here: https://github.com/ConstantB/madis 
and follow the installation instractions. 
* A web server, e.g., Apache Tomcat. The easiest way to use Ontop4TheWeb is by setting up an endpoint. For this, download a webserver such as Apache Tomcat. 

After madis is installed successfully follow the steps described below: 
1. Go to the precompiled directory of this repository 
2. Copy the warfiles included in this directory to the webapps folder of Apache Tomcat7 and start the webserver (if it is not started already). 
3. Use an onbda file with the mappings and an ontology in order to set up a repository. Example mappings are provided in the mappings directory. Please note that the ConnectionUrl should be updated with the directory of the MadIS source path. 
4. Using your browser, visit the openrdf-workbench application that has been deployed on Tomcat. Create a repository and use your newly created OBDA mappings file and an ontology file. 
5. After your repository has been set up, you are ready to execute some SPARQL queries!  
 

==================
Ontop4theWeb is an extension of Ontop-spatial
Ontop-spatial is a fork  of Ontop extended with geospatial 
support. Ontop is a framework for ontology based data access (OBDA). 

Check also issues.txt for known issues and todo.txt for future 
enchancements

Licensing terms 
--------------------
Ontop4TheWeb extends Ontop-spatial, which is a fork of -ontop. Thus, it is available under the same license. 
The -ontop- framework is available under the Apache License, Version 2.0

All documentation is licensed under the 
[Creative Commons](http://creativecommons.org/licenses/by/4.0/)
(attribute)  license.

References
--------------------
For more information about approach behind Ontop4TheWeb, as well as use cases, please read the following technical report: http://arxiv.org/abs/2005.11264 




