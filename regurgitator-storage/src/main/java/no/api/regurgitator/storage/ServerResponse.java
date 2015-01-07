package no.api.regurgitator.storage;

public final class ServerResponse {

    private final String content;

    private final ServerResponseMeta meta;

    public ServerResponse(final String content, final ServerResponseMeta meta) {
        this.content = content;
        this.meta = meta;
    }

    public String getContent() {
        return content;
    }

    public ServerResponseMeta getMeta() {
        return meta;
    }

    @Override
    public String toString() {
        return "ServerResponse{" +
                "content='" + content + '\'' +
                ", meta=" + meta +
                '}';
    }
}
