package no.api.proxyrecorder.storage;

import java.util.List;

/**
 *
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
    int getSize();

    /**
     * @return Size of elements in KBs. For the benefit of a GUI
     */
    int getSizeAsKb();
}
