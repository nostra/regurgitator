package no.api.proxyrecorder.filters;

import com.thoughtworks.xstream.XStream;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import no.api.proxyrecorder.storage.ServerResponse;
import no.api.proxyrecorder.storage.ServerResponseStore;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class ProxyRegurgitatorFilter extends HttpFiltersAdapter {
    private static final Logger log = LoggerFactory.getLogger(ProxyRegurgitatorFilter.class);
    private static XStream xstream = new XStream();

    private final ServerResponseStore storage;

    public ProxyRegurgitatorFilter(ServerResponseStore storage, HttpRequest originalRequest) {
        super(originalRequest);
        this.storage = storage;
    }

    @Override
    public HttpResponse requestPre(HttpObject httpObject) {
        //log.debug("-----"+new String( dfh.content().array()) );
        // TODO Would probably be able to cache different response status'. But - 200 OK is the most important
        DefaultFullHttpResponse response =
                new DefaultFullHttpResponse(originalRequest.getProtocolVersion(), HttpResponseStatus.OK);
        ByteBuf content = response.content();
        final String key = ProxyEaterFilter.createKey(originalRequest);
        final ServerResponse replacement = storage.read(key);
        if ( replacement == null ) {
            // TODO Need to create something with better headers
            throw new RuntimeException("Ops - have not recorded: "+originalRequest.getUri());
        }
        boolean locationHeaderSeen = false;
        if ( replacement.getHeaders() != null ) {
            response.headers().add(replacement.getHeaders());
            // If we have a location header, we don't want to serve the body.
            if ( replacement.getHeaders().contains("Location") ) {
                locationHeaderSeen = true;
            }
        }

        byte[] asBytes = new byte[0];
        if ( ! locationHeaderSeen ) {
            // Not adding content if we have a location redirect
            asBytes = replacement.getContent().getBytes(); // TODO Add encoding
        }
        if ( replacement.getStatus() > 0 ) {
            response.setStatus(HttpResponseStatus.valueOf(replacement.getStatus()));
        }
        content.capacity(asBytes.length);
        content.clear();
        content.writeBytes(asBytes);

        log.debug("Serving replacement for "+originalRequest.getUri());
        return response;
    }

}
