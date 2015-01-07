package no.api.regurgitator.filters;

import io.netty.buffer.ByteBufHolder;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import no.api.regurgitator.storage.ServerResponse;
import no.api.regurgitator.storage.ServerResponseMeta;
import no.api.regurgitator.storage.ServerResponseStore;
import no.api.regurgitator.storage.header.Headers;
import no.api.regurgitator.storage.header.NettyHttpHeadersUtil;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.impl.ProxyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.HashMap;

import static no.api.regurgitator.storage.header.ServerRequestMethod.findRequestMethodFromString;

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
            log.trace("Recording content on path {}", originalRequest.getUri());
            String content = ((ByteBufHolder) httpObject).content().toString(Charset.forName("UTF-8"));
            buffer.append(content);
        }

        if (ProxyUtils.isLastChunk(httpObject) && httpObject instanceof ByteBufHolder) {

            Headers headers = new Headers(new HashMap<>());
            int status = -1;
            if (httpObject instanceof HttpMessage) {
                if (httpObject instanceof HttpResponse) {
                    status = ((HttpResponse) httpObject).getStatus().code();
                }
                headers = NettyHttpHeadersUtil.convert(((HttpMessage) httpObject).headers());
            }
            storage.store(new ServerResponse(
                    buffer.toString(),
                    new ServerResponseMeta(
                            status,
                            findRequestMethodFromString(originalRequest.getMethod().toString()),
                            originalRequest.getUri(),
                            headers)));
        }
        return httpObject;
    }

}
