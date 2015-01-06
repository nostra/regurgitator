package no.api.regurgitator.mock;

import no.api.regurgitator.storage.DiskStorage;
import no.api.regurgitator.storage.ServerResponse;
import no.api.regurgitator.storage.ServerResponseKey;
import no.api.regurgitator.storage.ServerResponseStore;
import no.api.regurgitator.storage.header.ServerRequestMethod;

import java.util.Optional;

/**
 * Helper class for loading Regurgitator mock files from disk and into ServerResponse objects.
 *
 * This class is intended to be used in Unit and Integration tests, so it is possible to test on cache files generated
 * by Regurgitator.
 *
 * Typical usage:
 * <pre>
 *     // Remember trailing slash in path
 *     RegurgitatorMockLoader mockLoader = new RegurgitatorMockLoader("./src/test/resources/path/to/mock/dir/");
 *     Optional&lt;ServerResponse&gt; response = mockLoader.getMockFor(ServerResponseKey.RequestMethod.GET, "http://www.vg.no/index.php");
 * </pre>
 */
public class RegurgitatorMockLoader {

    private final ServerResponseStore storage;

    /**
     * Mock loader constructor.
     *
     * @param folder
     *         The full path to the directory where the Regurgitator files are located. Remember trailing slash.
     */
    public RegurgitatorMockLoader(String folder) {
        this.storage = new DiskStorage(folder);
    }

    /**
     * Get a Regurgitator response from the mock folder.
     *
     * @param requestMethod
     *         The HTTP request method for the response.
     * @param requestURI
     *         The URI for the response.
     */
    public Optional<ServerResponse> getMockFor(ServerRequestMethod requestMethod, String requestURI) {
        return storage.read(new ServerResponseKey(requestMethod, requestURI));
    }

}
