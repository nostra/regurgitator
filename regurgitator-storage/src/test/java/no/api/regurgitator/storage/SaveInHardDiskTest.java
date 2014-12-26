package no.api.regurgitator.storage;

import no.api.regurgitator.storage.template.ServerResponseStoreTestCase;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Ignore;

import java.io.File;
import java.io.IOException;

@Ignore
public class SaveInHardDiskTest extends ServerResponseStoreTestCase {

    @Override
    public String getStorageManager() {
        return SaveInHardDiskStorage.class.getName();
    }

    @Override
    public String getArchivedFolder() {
        return "mocktemp/";
    }

    @Override
    public boolean getSkipLongTest() {
        return false;
    }

    @AfterClass
    public static void cleanUp() throws IOException {
        FileUtils.deleteDirectory(new File("mocktemp"));
    }
}
