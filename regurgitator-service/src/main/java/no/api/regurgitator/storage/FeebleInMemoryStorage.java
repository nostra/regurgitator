package no.api.regurgitator.storage;

import com.thoughtworks.xstream.XStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Currently a rather feeble class which just stores everything internally
 * based on the keys.
 */
public class FeebleInMemoryStorage implements ServerResponseStore {
    // Just for testing....
    private final Map<String, String> data = new HashMap<>();
    private static final XStream xstream = new XStream();

    private int size = 0;


    @Override
    public void store(String key, ServerResponse page) {
        store( key, xstream.toXML(page));
    }

    private void store(String key, String content) {
        if ( content == null || key == null ) {
            return;
        }
        String old = data.put(key, content);
        if ( old != null ) {
            size -= old.length();
        }
        size += content.length();
    }

    @Override
    public ServerResponse read(String key) {
        String payload = data.get(key);
        if ( payload == null ) {
            return null;
        }

        return (ServerResponse)xstream.fromXML(payload);
    }

    @Override
    public List<String> getKeys() {
        List<String> keys = new ArrayList<>(data.keySet());
        Collections.sort(keys);
        return keys;
    }

    /**
     * Just an indication of space
     */
    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getSizeAsKb() {
        return size / 1024;
    }
}
