package no.api.regurgitator.wiremock;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.UrlMatchingStrategy;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import no.api.regurgitator.storage.ServerResponse;
import no.api.regurgitator.storage.ServerResponseMeta;
import no.api.regurgitator.storage.header.Headers;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Wrap a regurgitator mock result as a wiremock template
 */
public class RegurgitatorWireMock {

    /**
     * To be invoked similarly to this:
     * <code>WireMock.stubFor(RegurgitatorWireMock.regurgitatorStub(mock));</code>
     *
     * This alleviates the need for mocking the content as such in wiremock, and
     * you can concentrate on getting the headers right.
     */
    public static MappingBuilder regurgitatorStub(ServerResponse mock) {
        RequestMethod requestMethod = resolveMethodFrom( mock.getMeta() );

        UrlMatchingStrategy urlMatchingStrategy = null;
        try {
            urlMatchingStrategy = WireMock.urlEqualTo(new URI(mock.getMeta().getUri()).getPath());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Not expecting previously saved mock to contain error in URI. " +
                    "But it did... Contents:"+mock.getMeta().getUri());
        }

        final ResponseDefinitionBuilder aResponse = WireMock.aResponse();

        final Headers headers = mock.getMeta().getHeaders();
        headers.getHeaderNames()
                .stream()
                .forEach(k -> aResponse.withHeader(k, headers.getHeaderValue(k)));

        return new MappingBuilder(requestMethod, urlMatchingStrategy )
                .willReturn(aResponse
                    .withStatus(mock.getMeta().getStatus())
                        .withBody(mock.getContent()));
    }

    private static RequestMethod resolveMethodFrom(ServerResponseMeta meta) {
        switch ( meta.getMethod()) {
            case GET:
                return RequestMethod.GET;
            case OPTIONS:
                return RequestMethod.OPTIONS;
            case HEAD:
                return RequestMethod.HEAD;
            case POST:
                return RequestMethod.POST;
            case PUT:
                return RequestMethod.PATCH;
            case DELETE:
                return RequestMethod.DELETE;
            case TRACE:
                return RequestMethod.TRACE;
            default:
                throw new RuntimeException("Unexpected: "+meta.toString());
        }

    }
}
