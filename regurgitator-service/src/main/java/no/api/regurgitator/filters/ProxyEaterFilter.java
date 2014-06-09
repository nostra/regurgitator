package no.api.regurgitator.filters;

import io.netty.buffer.ByteBufHolder;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import no.api.regurgitator.storage.ServerResponse;
import no.api.regurgitator.storage.ServerResponseStore;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.impl.ProxyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 *
 */
public class ProxyEaterFilter extends HttpFiltersAdapter {
    private static final Logger log = LoggerFactory.getLogger(ProxyEaterFilter.class);
    private final StringBuilder buffer = new StringBuilder();

    private final ServerResponseStore storage;

    public ProxyEaterFilter(ServerResponseStore storage, HttpRequest originalRequest) {
        super(originalRequest);
        this.storage = storage;
    }

    @Override
    public HttpObject responsePost(HttpObject httpObject) {
        if (httpObject instanceof ByteBufHolder) {
            log.trace("Recording content on path {}",originalRequest.getUri());
            String content = ((ByteBufHolder) httpObject).content().toString(Charset.forName("UTF-8"));
            buffer.append(content);

        }
        if (ProxyUtils.isLastChunk(httpObject) && httpObject instanceof ByteBufHolder  ) {
            String key = createKey(originalRequest);

            ServerResponse page = new ServerResponse();
            page.setContent(buffer.toString());

            if ( httpObject instanceof HttpResponse) {
                page.setStatus(((HttpResponse) httpObject).getStatus().code());
            }

            if ( httpObject instanceof  HttpMessage ) {
                HttpHeaders headers = ((HttpMessage)httpObject).headers();
                page.setHeaders(headers);
            }
            log.debug("Storing on key "+key);
            storage.store(key, page);
        }
        return httpObject;
    }

    /**
     * Creating storage key
     */
    public static String createKey(HttpRequest source) {
        return source.getMethod()+"_"+ source.getUri();
    }

}
