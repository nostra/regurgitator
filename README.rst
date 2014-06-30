The regurgitator
================

Set up a proxy recorder in order to later spool off data. Useful for
integration tests, and looking into your communication. 

This application has been made in order to be able to create a static 
data source for setting up tests with gemini: 
https://github.com/bem/gemini

The source code is based on 

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
                Defaults to feeble in-memory storage.
proxyPort       Where the proxy listens
============== ======================================================================

Notice that you at the time of writing has a storage choice of file storage and
in-memory storage.

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

You can also change your proxy setting in your favorite browser to
use the regurgitator as proxy.

Using java-setup
^^^^^^^^^^^^^^^^

You need to add the following to your java command:: 

    -Dhttp.proxyHost=localhost  -Dhttp.nonProxyHosts= -Dhttp.proxyPort=9077

This can also be added to your MAVEN_OPTS, if you are running maven::

   export MAVEN_OPTS="-Dhttp.proxyHost=localhost -Dhttp.proxyPort=9077 -Dhttp.nonProxyHosts= $MAVEN_OPTS"

