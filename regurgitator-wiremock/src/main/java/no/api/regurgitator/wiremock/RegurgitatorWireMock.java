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

    public static MappingBuilder buildFromRegurgitator(ServerResponse mock) throws URISyntaxException {
        RequestMethod requestMethod = resolveMethodFrom( mock.getMeta() );

        UrlMatchingStrategy urlMatchingStrategy;
        urlMatchingStrategy = WireMock.urlEqualTo(new URI( mock.getMeta().getUri()).getPath());

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
