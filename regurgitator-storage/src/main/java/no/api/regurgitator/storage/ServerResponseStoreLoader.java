package no.api.regurgitator.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * ServerResponseStoreLoader that loads from a given full class name.
 */
public class ServerResponseStoreLoader {

    private static final Logger log = LoggerFactory.getLogger(ServerResponseStoreLoader.class);

    private ServerResponseStoreLoader() {
        // Intentional
    }

    /**
     * Load ServerResponseStore class from class name.
     *
     * @param className
     *         The full class path to the store that should be instantiated.
     * @param archivedFolder
     *         Path to where server responses should be stored if the store supports storing to disk.
     *
     * @return New instance of the requested ServerResponseStore, <code>null</code> if it could not be instantiated.
     */
    public static ServerResponseStore load(String className, String archivedFolder) {
        try {
            return (ServerResponseStore) Class.forName(className)
                    .getConstructor(String.class)
                    .newInstance(archivedFolder);
        } catch (NoSuchMethodException
                | InvocationTargetException
                | InstantiationException
                | IllegalAccessException
                | ClassNotFoundException e) {
            log.warn("Could not load class {}", className, e);
            return null;
        }
    }
}
