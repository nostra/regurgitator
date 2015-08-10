package no.api.regurgitator.wiremock;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import javax.xml.transform.Result;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WireTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089); // No-args constructor defaults to port 8080

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

}
