package no.api.regurgitator.filters;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import no.api.regurgitator.exception.RegurgitatorException;
import no.api.regurgitator.storage.ServerResponse;
import no.api.regurgitator.storage.ServerResponseKey;
import no.api.regurgitator.storage.ServerResponseStore;
import no.api.regurgitator.storage.header.NettyHttpHeadersUtil;
import no.api.regurgitator.storage.header.ServerRequestMethod;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ProxyRegurgitatorFilter extends HttpFiltersAdapter {

    private static final Logger log = LoggerFactory.getLogger(ProxyRegurgitatorFilter.class);

    private final ServerResponseStore storage;

    public ProxyRegurgitatorFilter(ServerResponseStore storage, HttpRequest originalRequest) {
        super(originalRequest);
        this.storage = storage;
    }

    @Override
    public HttpResponse requestPre(HttpObject httpObject) {
        DefaultFullHttpResponse response =
                new DefaultFullHttpResponse(originalRequest.getProtocolVersion(), HttpResponseStatus.OK);
        ByteBuf content = response.content();
        ServerResponseKey key = createServerResponseKeyFromRequest();
        final Optional<ServerResponse> serverResponse = storage.read(key, response.getStatus().code());
        if (!serverResponse.isPresent()) {
            // TODO Need to create something with better headers
            throw new RegurgitatorException("Ops - have not recorded: " + originalRequest.getUri());
        }
        final ServerResponse replacement = serverResponse.get();
        boolean locationHeaderSeen = false;
        if (replacement.getMeta().getHeaders() != null) {
            NettyHttpHeadersUtil.appendHeadersToResponse(replacement.getMeta().getHeaders(), response);
            // If we have a location header, we don't want to serve the body.
            if (replacement.getMeta().getHeaders().hasHeader(HttpHeaders.Names.LOCATION)) {
                locationHeaderSeen = true;
            }
        }

        byte[] asBytes = new byte[0];
        if (!locationHeaderSeen) {
            // Not adding content if we have a location redirect
            asBytes = replacement.getContent().getBytes(); // TODO Add encoding
        }
        if (replacement.getMeta().getStatus() > 0) {
            response.setStatus(HttpResponseStatus.valueOf(replacement.getMeta().getStatus()));
        }
        content.capacity(asBytes.length);
        content.clear();
        content.writeBytes(asBytes);

        log.debug("Serving replacement for " + originalRequest.getUri());
        return response;
    }

    private ServerResponseKey createServerResponseKeyFromRequest() {
        return new ServerResponseKey(
                ServerRequestMethod.findRequestMethodFromString(originalRequest.getMethod().toString()),
                     originalRequest.getUri());
    }

}
