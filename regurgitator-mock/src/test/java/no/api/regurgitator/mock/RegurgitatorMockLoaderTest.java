package no.api.regurgitator.mock;

import no.api.regurgitator.storage.ServerResponse;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static no.api.regurgitator.storage.header.ServerRequestMethod.GET;

public class RegurgitatorMockLoaderTest {

    private static String BASE_MOCK_PATH = "src/test/resources/mock/";

    private RegurgitatorMockLoader mockLoader;

    public RegurgitatorMockLoaderTest() {
        mockLoader = new RegurgitatorMockLoader(locateMockPath());
    }

    @Test
    public void testValidResponse() {
        Optional<ServerResponse> response = mockLoader.getMockFor(GET,
                                                                  "http://www.rb.no", 300);
        Assert.assertTrue(response.isPresent());
        Assert.assertTrue(response.get().getContent().contains("apiHeaderMultifix"));
    }

    @Test
    public void testNonExistingResponse() {
        Optional<ServerResponse> response = mockLoader.getMockFor(GET, "http://www.oa.no", 200);
        Assert.assertFalse(response.isPresent());
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