package no.api.regurgitator.storage;

import no.api.regurgitator.storage.header.ServerRequestMethod;

/**
 * A key object representing a HTTP response, where the request method and uri makes a unique key.
 *
 * Intended to be used by {@link no.api.regurgitator.storage.ServerResponseStore} implementations.
 */
public final class ServerResponseKey implements Comparable<ServerResponseKey> {

    private final ServerRequestMethod requestMethod;

    private final String requestURI;

    /**
     * Constructor
     *
     * @param requestMethod
     *         The method used for retrieving the response.
     * @param requestURI
     *         The uri used for retrieving the response.
     */
    public ServerResponseKey(ServerRequestMethod requestMethod, String requestURI) {
        assert requestMethod != null;
        assert requestURI != null;
        this.requestMethod = requestMethod;
        this.requestURI = requestURI;
    }

    public ServerRequestMethod getRequestMethod() {
        return requestMethod;
    }

    public String getRequestURI() {
        return requestURI;
    }

    @Override
    public int compareTo(ServerResponseKey obj) {
        return this.toString().compareTo(obj.toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ServerResponseKey)) {
            return false;
        }

        final ServerResponseKey that = (ServerResponseKey) obj;

        if (requestMethod != that.requestMethod) {
            return false;
        }
        if (!requestURI.equals(that.requestURI)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = requestMethod.hashCode();
        result = 31 * result + requestURI.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ResponseKey{" +
                "requestMethod='" + requestMethod + '\'' +
                ", requestURI='" + requestURI + '\'' +
                '}';
    }


    public static ServerResponseKey fromServerResponse(ServerResponse response) {
        return new ServerResponseKey(response.getMeta().getMethod(), response.getMeta().getUri());
    }


}
