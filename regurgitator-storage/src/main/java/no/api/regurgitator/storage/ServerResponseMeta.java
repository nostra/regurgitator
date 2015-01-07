package no.api.regurgitator.storage;

import no.api.regurgitator.storage.header.Headers;
import no.api.regurgitator.storage.header.ServerRequestMethod;

public final class ServerResponseMeta {

    private final int status;

    private final String uri;

    private final ServerRequestMethod method;

    private final Headers headers;

    public ServerResponseMeta(final int status, final ServerRequestMethod method, final String uri,
                              final Headers headers) {
        this.status = status;
        this.method = method;
        this.uri = uri;
        this.headers = headers;
    }

    public int getStatus() {
        return status;
    }

    public Headers getHeaders() {
        return headers;
    }

    public String getUri() {
        return uri;
    }

    public ServerRequestMethod getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return "ServerResponseMeta{" +
                "status=" + status +
                ", uri='" + uri + '\'' +
                ", method=" + method +
                ", headers=" + headers +
                '}';
    }
}
