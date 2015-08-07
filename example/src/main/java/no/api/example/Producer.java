package no.api.example;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;
import static spark.SparkBase.port;

public class Producer {

    private static final Logger log = LoggerFactory.getLogger(Consumer.class);

    public static void main(String[] args) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        // print logback's internal status
        StatusPrinter.print(lc);

        new Producer().doMojo();
    }

    private void doMojo( ) {
        port(8501);

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

    }
}
