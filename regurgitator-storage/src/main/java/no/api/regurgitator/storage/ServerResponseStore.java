package no.api.regurgitator.storage;

import java.util.List;
import java.util.Optional;

/**
 * Notice that the constructor of the implementing class needs to take a String as a parameter. Pointing to a storage folder.
 */
public interface ServerResponseStore {

    /**
     * Store the server response somewhere
     *
     * @param serverResponse The response to be stored.
     *
     * @return An Optional with a unique key for the response if the response was stored, else <code>empty</code>.
     */
    Optional<ServerResponseKey> store(ServerResponse serverResponse);

    /**
     * @return Stored server response, or empty
     */
    Optional<ServerResponse> read(ServerResponseKey key);

    /**
     * @return List of keys for the benefit of a GUI
     */
    List<ServerResponseKey> getKeys();

    /**
     * @return Size of stored elements in bytes. The implementation do not support this if it returns -1.
     */
    long getSize();

    /**
     * @return Size of elements in KBs. For the benefit of a GUI. The implementation do not support this if it returns -1.
     */
    double getSizeAsKb();
}
