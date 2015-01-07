package no.api.regurgitator.storage.header;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Simple data holder for HTTP headers. For real testing on the headers it would be a good idea to add them to
 * to a good header implementation. Netty.io, Jersey, Apache HTTPComponents and Spring all have good tools for dealing
 * with headers. Choose the one that suits your need.
 */
public final class Headers {

    private final Map<String, List<String>> headers;

    public Headers(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    /**
     * Check if a header is present.
     *
     * @param name The name of the header.
     * @return <code>true</code> if found, or <code>false</code> if not found.
     */
    public boolean hasHeader(String name) {
        return headers.containsKey(name);
    }
    /**
     * Get all values for a given header.
     *
     * @param name The name of the header.
     * @return A list of the header values if found, else an empty list.
     */
    public List<String> getHeaderValues(String name) {
        return headers.getOrDefault(name, new ArrayList<>());
    }

    /**
     * Get the first value for a header if any.
     *
     * @param name The name of the header.
     * @return The value as string, or <code>null</code> if not found.
     */
    public String getHeaderValue(String name) {
        return hasHeader(name) ? headers.get(name).get(0) : null;
    }

    /**
     * Return a set of all header names.
     *
     * @return A set of all the header names.
     */
    public Set<String> getHeaderNames() {
        return headers.keySet();
    }

}
