package no.api.example;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    // java -jar target/sparktest-0.0.1-SNAPSHOT.jar  > sparkoutput.txt 2>&1
    public static void main(String[] args) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        // print logback's internal status
        StatusPrinter.print(lc);

        new Main().doMojo();
    }

    private void doMojo( ) {
        staticFileLocation("/assets");
        // port(config.getPort());

        // matches "GET /hello/foo" and "GET /hello/bar"
        // request.params(":name") is 'foo' or 'bar'
        get("/hello/:name", (request, response) -> {
            return "Hello: " + request.params(":name");
        });

        get("/hello",(request, response) -> {
            response.redirect("/hello/");
            return null;
        });

        get("/hello/", (request, response) -> {
            return "Hello World! Path: " + request.pathInfo();
        });


        get("/index.html", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Hello World!");

            // The hello.ftl file is located in directory:
            // src/test/resources/spark/template/freemarker
            return new ModelAndView(attributes, "hello.ftl");
        }, new FreeMarkerEngine());

    }
}
