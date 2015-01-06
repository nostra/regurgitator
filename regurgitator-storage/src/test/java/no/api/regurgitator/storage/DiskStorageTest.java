package no.api.regurgitator.storage;

import no.api.regurgitator.storage.template.ServerResponseStoreTestCase;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Ignore
public class DiskStorageTest extends ServerResponseStoreTestCase {

    @Override
    public String getStorageManager() {
        return DiskStorage.class.getName();
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

    @Test
    public void testGetKeys() {
        // Make sure this test will work from both Regurgitator root folder and from inside the regurgitator-storage
        // module.
        File dir = new File(".");
        String folder = "src/test/resources/mock/";
        if (!dir.getName().equals("regurgitator-storage")) {
            folder = "regurgitator-storage/" + folder;
        }
        ServerResponseStore load = ServerResponseStoreLoader.load(getStorageManager(), folder);
        List<ServerResponseKey> keys = load.getKeys();
        Assert.assertNotNull(keys);
        Assert.assertEquals(2, keys.size());
    }
}
