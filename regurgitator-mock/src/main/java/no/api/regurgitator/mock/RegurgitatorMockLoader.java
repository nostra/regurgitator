package no.api.regurgitator.mock;

import io.netty.handler.codec.http.HttpMethod;
import no.api.regurgitator.storage.SaveInHardDiskStorage;
import no.api.regurgitator.storage.ServerResponse;
import no.api.regurgitator.storage.key.FilePathKey;

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
 *     ServerResponse response = mockLoader.getMockFor(HttpMethod.GET, "http://www.vg.no/index.php");
 * </pre>
 */
public class RegurgitatorMockLoader {

    private final SaveInHardDiskStorage storage;

    /**
     * Mock loader constructor.
     *
     * @param folder
     *         The full path to the directory where the Regurgitator files are located. Remember trailing slash.
     */
    public RegurgitatorMockLoader(String folder) {
        this.storage = new SaveInHardDiskStorage(folder);
    }

    /**
     * Get a Regurgitator response from the mock folder.
     *
     * @param method
     *         The expected HTTP Method. (Regurgitator will store the same URL in different files depending on the used
     *         HTTP Method.
     * @param url
     *         The requested url as String.
     */
    public ServerResponse getMockFor(HttpMethod method, String url) {
        FilePathKey shortKey = new FilePathKey(method.toString().toUpperCase() + "_" + url);
        return storage.read(shortKey.getFullPath());
    }
}
