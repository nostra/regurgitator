package no.api.regurgitator.storage;

import no.api.regurgitator.storage.template.ServerResponseStoreTestCase;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;

import java.io.File;
import java.io.IOException;

public class FreebleInMemoryStorageTest extends ServerResponseStoreTestCase {

    @AfterClass
    public static void cleanUp() throws IOException {
        FileUtils.deleteDirectory(new File("mocktemp"));
    }

    @Override
    public String getStorageManager() {
        return "no.api.regurgitator.storage.FeebleInMemoryStorage";
    }

    @Override
    public String getArchivedFolder() {
        return null;
    }

    @Override
    public boolean getSkipLongTest() {
        return false;
    }
}
