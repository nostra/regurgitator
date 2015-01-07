package no.api.regurgitator.resources;

import no.api.regurgitator.storage.ServerResponse;
import no.api.regurgitator.storage.ServerResponseKey;
import no.api.regurgitator.storage.ServerResponseStore;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

import static no.api.regurgitator.storage.header.ServerRequestMethod.findRequestMethodFromString;

@Produces(MediaType.TEXT_PLAIN)
@Path("/read/")
public class ReadResource {

    private ServerResponseStore storage;

    public ReadResource(ServerResponseStore storage) {
        this.storage = storage;
    }

    @GET
    @Path("{method}/{uri}")
    public String read(@Context HttpServletRequest req,
                       @PathParam("method") String method,
                       @PathParam("uri") String uri) {
        Optional<ServerResponse> page = storage.read(new ServerResponseKey(
                findRequestMethodFromString(method), uri));
        if (!page.isPresent()) {
            return "Error: Could not find element with method=" + method + " and uri=" + uri;
        }
        return createServerResponseReport(page.get());
    }

    private String createServerResponseReport(ServerResponse page) {
        StringBuilder sb = new StringBuilder();
        appendHeadersToStringBuilder(page, sb);
        appendHttpStatusToStringBuilder(page, sb);
        appendContentToStringBuilder(page, sb);
        return sb.toString();
    }

    private void appendContentToStringBuilder(ServerResponse page, StringBuilder sb) {
        sb.append("------------------------------------------\n");
        sb.append(page.getContent());
    }

    private void appendHttpStatusToStringBuilder(ServerResponse page, StringBuilder sb) {
        sb.append("Http status ").append(page.getMeta().getStatus()).append("\n");
    }

    private void appendHeadersToStringBuilder(ServerResponse page, StringBuilder sb) {
        for (String header : page.getMeta().getHeaders().getHeaderNames()) {
            for (String headerValue : page.getMeta().getHeaders().getHeaderValues(header)) {
                sb.append(header).append(" ").append(headerValue).append("\n");
            }
        }
    }
}
