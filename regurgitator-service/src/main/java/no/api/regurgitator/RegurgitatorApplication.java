package no.api.regurgitator;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import no.api.regurgitator.health.AlwaysGood;
import no.api.regurgitator.resources.IndexResource;
import no.api.regurgitator.resources.ReadResource;
import no.api.regurgitator.storage.SaveInHardDiskStorageFactory;
import no.api.regurgitator.storage.ServerResponseStore;

import java.lang.reflect.InvocationTargetException;

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
            String storageManagerClassName = configuration.getStorageManager();
            if ( storageManagerClassName.equals("no.api.regurgitator.storage.SaveInHardDiskStorage") ) {
                storage = SaveInHardDiskStorageFactory.createStorage(configuration);
            } else {
                storage = (ServerResponseStore) Class.forName(storageManagerClassName)
                        .getConstructor(RegurgitatorConfiguration.class)
                        .newInstance(configuration);
            }
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException
                | ClassNotFoundException e) {
            throw new RuntimeException("Could not find configuration of storage manager or declaration of it is wrong.",
                    e);
        }
        environment.healthChecks().register("dummy", new AlwaysGood());
        environment.jersey().register(new IndexResource(storage).startProxy(configuration.getProxyPort()));
        environment.jersey().register(new ReadResource(storage));
    }
}
