package no.api.proxyrecorder.resources;

import no.api.proxyrecorder.storage.ServerResponse;
import no.api.proxyrecorder.storage.ServerResponseStore;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 *
 */
@Produces(MediaType.TEXT_PLAIN)
@Path("/read/")
public class ReadResource {

    private ServerResponseStore storage;

    public ReadResource(ServerResponseStore storage) {
        this.storage = storage;
    }

    @GET
    @Path("{key}")
    public String read(@Context HttpServletRequest req, @PathParam("key") String key) {
        ServerResponse page = storage.read(key);
        if ( page == null ) {
            return "Error: Could not find element with key "+key;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> header : page.getHeaders()) {
            sb.append(header.getKey()).append(" ").append(header.getValue()).append("\n");
        }
        sb.append("Http status ").append(page.getStatus()).append("\n");
        sb.append("------------------------------------------\n");
        sb.append(page.getContent());
        return sb.toString();
    }
}
