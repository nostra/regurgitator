package no.api.regurgitator.storage;

import com.thoughtworks.xstream.XStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Currently a rather feeble class which just stores everything internally based on the keys.
 *
 * IMPORTANT: This implementation does not have support for storing more than one response per URI.
 */
public class FeebleInMemoryStorage implements ServerResponseStore {

    private final Map<ServerResponseKey, String> data = new HashMap<>();

    private static final XStream xstream = new XStream();

    private int size = 0;

    public FeebleInMemoryStorage(String ignoredPath) {
        // We do not use given path here since this class stores the data in memory.
    }

    @Override
    public Optional<ServerResponseKey> store(ServerResponse serverResponse) {
        ServerResponseKey key = ServerResponseKey.fromServerResponse(serverResponse);
        if (!store(key, xstream.toXML(serverResponse))) {
            return Optional.empty();
        }
        return Optional.ofNullable(key);
    }

    @Override
    public Optional<ServerResponse> read(ServerResponseKey key, int responseStatus) {
        String payload = data.get(key);
        if (payload == null) {
            return Optional.empty();
        }
        return Optional.ofNullable((ServerResponse) xstream.fromXML(payload));
    }

    @Override
    public List<ServerResponseKey> getKeys() {
        List<ServerResponseKey> keys = new ArrayList<>(data.keySet());
        Collections.sort(keys);
        return keys;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public double getSizeAsKb() {
        return size / 1024L;
    }

    private boolean store(ServerResponseKey key, String content) {
        if (content == null || key == null) {
            return false;
        }
        String old = data.put(key, content);
        if (old != null) {
            size -= old.length();
        }
        size += content.length();
        return true;
    }
}
