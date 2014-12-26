package no.api.regurgitator;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import no.api.regurgitator.health.AlwaysGood;
import no.api.regurgitator.resources.IndexResource;
import no.api.regurgitator.resources.ReadResource;
import no.api.regurgitator.storage.ServerResponseStore;
import no.api.regurgitator.storage.ServerResponseStoreLoader;

public class RegurgitatorApplication extends Application<RegurgitatorConfiguration> {

    public static void main(String[] args) throws Exception { // NOSONAR From framework
        new RegurgitatorApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<RegurgitatorConfiguration> bootstrap) {
        bootstrap.addBundle(new ViewBundle());
        bootstrap.addBundle(new AssetsBundle("/asset/", "/asset/"));
    }

    @Override
    public void run(RegurgitatorConfiguration configuration, Environment environment) {
        // TODO storage might be null. Should probably add a null check on this.
        final ServerResponseStore storage =
                ServerResponseStoreLoader.load(configuration.getStorageManager(), configuration.getArchivedFolder());
        environment.healthChecks().register("dummy", new AlwaysGood());
        environment.jersey().register(
                new IndexResource(storage, configuration.getRecordOnStart()).startProxy(configuration.getProxyPort()));
        environment.jersey().register(new ReadResource(storage));
    }
}
