package no.api.regurgitator.resources;

import io.netty.handler.codec.http.HttpRequest;
import no.api.regurgitator.filters.ProxyEaterFilter;
import no.api.regurgitator.filters.ProxyRegurgitatorFilter;
import no.api.regurgitator.storage.ServerResponseStore;
import no.api.regurgitator.views.IndexView;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.TEXT_HTML )
public class IndexResource {
    private static Logger log = LoggerFactory.getLogger(IndexResource.class);
    private HttpProxyServer server = null;
    private Boolean toRecord = Boolean.TRUE;
    private ServerResponseStore storage;

    public IndexResource(ServerResponseStore storage, Boolean toRecord) {
        this.storage = storage;
        this.toRecord = toRecord;
    }

    @GET
    public IndexView getIndex(@Context HttpServletRequest req) {

        return new IndexView(toRecord, storage);
    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    public IndexView toggle() {
        toRecord = !toRecord;

        log.debug("Toggled... Now storing the data? Answer: "+toRecord);

        return new IndexView(toRecord, storage);
    }

    public Object startProxy(final int proxyPort) {
        server =
            DefaultHttpProxyServer.bootstrap()
                .withPort(proxyPort)
                .withFiltersSource(new HttpFiltersSourceAdapter() {
                    @Override
                    public HttpFilters filterRequest(HttpRequest originalRequest) {
                        if (toRecord) {
                            return new ProxyEaterFilter(storage, originalRequest);
                        } else {
                            return new ProxyRegurgitatorFilter(storage, originalRequest);
                        }
                    }

                    //*
                    // Notice that the limit might give some trouble. Consider increasing significantly,
                    // _or_ handle chunks
                    @Override
                    public int getMaximumRequestBufferSizeInBytes() {
                        return 10 * 1024 * 1024;
                    }

                    @Override
                    public int getMaximumResponseBufferSizeInBytes() {
                        return 10 * 1024 * 1024;
                    }
                    //*/
                })
                .start();
        return this;
    }
}
