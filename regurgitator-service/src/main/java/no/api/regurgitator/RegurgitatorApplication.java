package no.api.regurgitator;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import no.api.regurgitator.health.AlwaysGood;
import no.api.regurgitator.resources.IndexResource;
import no.api.regurgitator.resources.ReadResource;
import no.api.regurgitator.storage.ServerResponseStore;

/**
 *
 */
public class RegurgitatorApplication extends Application<RegurgitatorConfiguration> {
    //private static Logger log = LoggerFactory.getLogger(PhoebeApplication.class);

    public static void main(String[] args) throws Exception { // NOSONAR From framework
        new RegurgitatorApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<RegurgitatorConfiguration> bootstrap) {
        bootstrap.addBundle(new ViewBundle());
    }

    @Override
    public void run(RegurgitatorConfiguration configuration, Environment environment) {
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
