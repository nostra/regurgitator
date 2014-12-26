package no.api.regurgitator.mock;

import io.netty.handler.codec.http.HttpMethod;
import no.api.regurgitator.storage.ServerResponse;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RegurgitatorMockLoaderTest {

    private static String BASE_MOCK_PATH = "src/test/resources/mock/";

    private RegurgitatorMockLoader mockLoader;

    public RegurgitatorMockLoaderTest() {
        mockLoader = new RegurgitatorMockLoader(locateMockPath());
    }

    @Test
    public void testValidResponse() {
        ServerResponse response = mockLoader.getMockFor(HttpMethod.GET, "http://www.rb.no");
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getContent().contains("apiHeaderMultifix"));
    }

    @Test
    public void testNonExistingResponse() {
        Assert.assertNull(mockLoader.getMockFor(HttpMethod.GET, "http://www.oa.no"));
    }

    /*
         * Make sure tests can be executed from both Regurgitator root and from inside regurgitator-mock.
         */
    private String locateMockPath() {
        String expectedPath = "./regurgitator-mock/" + BASE_MOCK_PATH;
        Path path = Paths.get(expectedPath);
        if (Files.isDirectory(path)) {
            return expectedPath;
        } else {
            return "./" + BASE_MOCK_PATH;
        }
    }
}