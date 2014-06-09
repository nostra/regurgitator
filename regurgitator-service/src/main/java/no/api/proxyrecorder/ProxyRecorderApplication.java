package no.api.proxyrecorder;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import no.api.proxyrecorder.health.AlwaysGood;
import no.api.proxyrecorder.resources.IndexResource;
import no.api.proxyrecorder.resources.ReadResource;
import no.api.proxyrecorder.storage.ServerResponseStore;

/**
 *
 */
public class ProxyRecorderApplication extends Application<ProxyRecorderConfiguration> {
    //private static Logger log = LoggerFactory.getLogger(PhoebeApplication.class);

    public static void main(String[] args) throws Exception { // NOSONAR From framework
        new ProxyRecorderApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ProxyRecorderConfiguration> bootstrap) {
        bootstrap.addBundle(new ViewBundle());
    }

    @Override
    public void run(ProxyRecorderConfiguration configuration, Environment environment) {
        ServerResponseStore storage = null;
        try {
            storage = (ServerResponseStore) Class.forName(configuration.getStorageManager()).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException("Could not find configuration of storage manager.",e);
        }
        environment.healthChecks().register("dummy", new AlwaysGood());
        environment.jersey().register(new IndexResource(storage).startProxy( configuration.getProxyPort() ));
        environment.jersey().register(new ReadResource(storage));
    }
}
