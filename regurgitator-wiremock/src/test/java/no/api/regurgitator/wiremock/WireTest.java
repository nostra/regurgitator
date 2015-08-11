package no.api.regurgitator.wiremock;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import no.api.regurgitator.mock.RegurgitatorMockLoader;
import no.api.regurgitator.storage.ServerResponse;
import no.api.regurgitator.storage.header.ServerRequestMethod;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.notMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

public class WireTest {

    private static final int WIREMOCK_PORT = 8089;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(WIREMOCK_PORT); // No-args constructor defaults to port 8080

    /**
     * This test is taken from the wiremock doc, and is just retained as an
     * example.
     */
    @Test
    @Ignore
    public void exampleTest() throws InterruptedException {
        stubFor(get(urlEqualTo("/my/resource"))
                //.withHeader("Accept", equalTo("text/xml"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>Some content</response>")));

        Thread.sleep(15 * 1000L);
        //Result result = myHttpServiceCallingObject.doSomething();
        //assertTrue(result.wasSuccessFul());

        verify(postRequestedFor(urlMatching("/my/resource/[a-z0-9]+"))
                .withRequestBody(matching(".*<message>1234</message>.*"))
                .withHeader("Content-Type", notMatching("application/json")));
    }

    @Test
    public void fromExample() {
        RegurgitatorMockLoader mockLoader = new RegurgitatorMockLoader("src/test/resources/mock");
        Optional<ServerResponse> mock = mockLoader.getMockFor(ServerRequestMethod.GET, "http://localhost:8501/hello/", 200);
        Assert.assertTrue(mock.isPresent());
        Assert.assertNotNull(mock.get().getContent());
    }

    @Test
    public void useRegurgitatorWithWiremock() throws IOException, URISyntaxException, InterruptedException {
        RegurgitatorMockLoader mockLoader = new RegurgitatorMockLoader("src/test/resources/mock");
        ServerResponse mock = mockLoader.getMockFor(ServerRequestMethod.GET, "http://localhost:8501/hello/",
                200).orElse(null);

        stubFor(RegurgitatorWireMock.regurgitatorStub(mock));

        String str = urlContents(new URL("http://localhost:" + WIREMOCK_PORT + "/hello/"));
        Assert.assertTrue(str.contains("hello"));

        //Thread.sleep(10000L);
        // Just verifying that the expected method was called:
        verify(getRequestedFor(urlEqualTo("/hello/")));
    }

    private String urlContents(URL url ) throws IOException {
        try ( BufferedReader br =
                      new BufferedReader( new InputStreamReader( url.openStream())) ) {

            return br.lines().reduce("", String::concat);
        }
    }

}
