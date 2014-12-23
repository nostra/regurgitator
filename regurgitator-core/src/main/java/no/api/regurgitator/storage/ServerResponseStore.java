package no.api.regurgitator.storage;

import java.util.List;

/**
 * Notice that the constructor of the implementing class needs to
 * take RegurgitatorConfiguration as a parameter
 */
public interface ServerResponseStore {

    /**
     * Store the server response somewhere
     */
    void store(String key, ServerResponse page);

    /**
     * @return Stored server response, or null
     */
    ServerResponse read(String key);

    /**
     *  @return List of keys for the benefit of a GUI
     */
    List<String> getKeys();

    /**
     * @return Size of stored elements in bytes.
     */
    long getSize();

    /**
     * @return Size of elements in KBs. For the benefit of a GUI
     */
    long getSizeAsKb();
}
