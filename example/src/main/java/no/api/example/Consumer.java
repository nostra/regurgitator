package no.api.example;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;
import spark.utils.IOUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class Consumer {

    private static final Logger log = LoggerFactory.getLogger(Consumer.class);

    public static void main(String[] args) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        // print logback's internal status
        StatusPrinter.print(lc);

        new Consumer().doMojo();
    }

    private void doMojo( ) {
        staticFileLocation("/assets");
        port(8500);

        get("/index.html", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();

            List<String> list = new ArrayList<String>();

            list.add(read("hello"));
            list.add(read("hello/consumer"));

            attributes.put("content", list);

            return new ModelAndView(attributes, "hello.ftl");
        }, new FreeMarkerEngine());

    }

    private String read(String subPath) {
        try {
            return IOUtils.toString(new URI("http://localhost:8501/"+subPath).toURL().openStream());
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Got exception", e);
        }
    }
}
