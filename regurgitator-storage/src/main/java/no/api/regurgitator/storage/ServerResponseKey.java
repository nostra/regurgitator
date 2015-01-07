package no.api.regurgitator.storage;

import no.api.regurgitator.storage.header.ServerRequestMethod;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A key object representing a HTTP response, where the request method and uri makes a unique key.
 *
 * Intended to be used by {@link no.api.regurgitator.storage.ServerResponseStore} implementations.
 */
public final class ServerResponseKey implements Comparable<ServerResponseKey> {

    private final ServerRequestMethod requestMethod;

    private final String requestURI;

    private final String keyPath;

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
        this.keyPath = createFilePath(requestMethod, requestURI);
    }

    public ServerRequestMethod getRequestMethod() {
        return requestMethod;
    }

    public String getRequestURI() {
        return requestURI;
    }

    /**
     * Return the key converted to a path for use by {@link no.api.regurgitator.storage.ServerResponseStore}
     * implementations.
     */
    public String getPath() {
        return this.keyPath;
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
                ", keyPath='" + keyPath + '\'' +
                '}';
    }

    private String createFilePath(ServerRequestMethod requestMethod, String requestURI) {
        List<String> pathList = new ArrayList<>();
        pathList.add(requestMethod.toString());
        for (String eachPaths : StringUtils.split(this.customReplaceString(requestURI, '_'), "_")) {
            pathList.add(convertLinuxName(eachPaths));
        }
        return makePathFile(pathList);
    }

    private String customReplaceString(String target, char replace) {
        StringBuilder tempString = new StringBuilder();
        for (char singleChar : target.toCharArray()) {
            if (Character.isAlphabetic(singleChar) || Character.isDigit(singleChar)) {
                tempString.append(singleChar);
            } else {
                tempString.append(replace);
            }
        }
        return tempString.toString();
    }

    private String convertLinuxName(String s) {
        return StringUtils.isEmpty(s) ? "null" : s;
    }

    private String makePathFile(List<String> pathFile) {
        return StringUtils.join(pathFile, "/");
    }

    public static ServerResponseKey fromServerResponse(ServerResponse response) {
        return new ServerResponseKey(response.getMeta().getMethod(), response.getMeta().getUri());
    }


}
