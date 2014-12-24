package no.api.regurgitator.mock;

import no.api.regurgitator.storage.SaveInHardDiskStorage;
import no.api.regurgitator.storage.ServerResponse;

/**
 * Helper class for loading Regurgitator mock files from disk and into ServerResponse objects.
 * <p/>
 * This class is intended to be used in Unit and Integration tests, so it is possible to test on cache files generated
 * by Regurgitator.
 * <p/>
 * Typical usage:
 * <pre>
 *     RegurgitatorMockLoader mockLoader = new RegurgitatorMockLoader("Path to mock files");
 *     ServerResponse response = mockLoader.getMockFor("GET_http://www.vg.no/index.php");
 * </pre>
 */
public class RegurgitatorMockLoader {

    private final SaveInHardDiskStorage storage;

    /**
     * Mock loader constructor.
     *
     * @param folder
     *         The full path to the directory where the Regurgitator files are located.
     */
    public RegurgitatorMockLoader(String folder) {
        this.storage = new SaveInHardDiskStorage(folder);
    }

    /**
     * Get a stored Regurgitator response.
     *
     * @param key The Regurgiator key. // TODO Explain or point to doc.
     */
    public ServerResponse getMockFor(String key) {
        return storage.read(key);
    }
}
