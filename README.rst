The regurgitator
================

Set up a proxy recorder in order to later spool of data
during integration tests.

This app is based on 

* http://www.dropwizard.io/
* https://github.com/adamfisk/LittleProxy

For desired changes, please make a **pull request**.

    
Running the proxy
^^^^^^^^^^^^^^^^^

You need Java-8 and Maven 3.x. Check out, compile with maven and run::

   mvn clean install
   rm -f rm *-service/target/original* 
   java -jar *-service/target/*-service-*-SNAPSHOT.jar server *-app/src/main/resources/etc/*.yml

The strange path to the dropwizard.yml file is due to amedia deployment defaults.
The configuration is a standard dropwizard configuration.

Configuration which is specific for the regurgitator is:

============== ======================================================================
storageManager  Class name of something which implements ServerResponseStore
                Defaults to feeble inmemory storage.
proxyPort       Where the proxy listens
============== ======================================================================

Testing the proxy
^^^^^^^^^^^^^^^^^

Get the index page from somewhere using the proxy::
    
    GET -p http://localhost:9077 http://www.rb.no/
    http --proxy=http:http://localhost:9077 http://www.rb.no/ 
    
You should be able to open http://localhost:9076/
and find the request there.
    
The tools:

* http://search.cpan.org/dist/libwww-perl/
* http://httpie.org/ 

Using java-setup
^^^^^^^^^^^^^^^^

You need to add the following to your java command:: 

    -Dhttp.proxyHost=localhost -Dhttp.proxyPort=9077

This can also be added to your MAVEN_OPTS, if you are running maven::

   export MAVEN_OPTS="-Dhttp.proxyHost=localhost -Dhttp.proxyPort=9077 $MAVEN_OPTS"

