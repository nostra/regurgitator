package no.api.proxyrecorder.storage;

import io.netty.handler.codec.http.HttpHeaders;

/**
 *
 */
public class ServerResponse {

    public String getContent() {
        return content;
    }

    private String content;

    private int status;

    private HttpHeaders headers;

    public void setContent(String content) {
        this.content = content;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }
}
