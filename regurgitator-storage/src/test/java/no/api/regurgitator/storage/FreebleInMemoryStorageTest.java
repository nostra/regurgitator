package no.api.regurgitator.storage;

import no.api.regurgitator.storage.header.Headers;
import no.api.regurgitator.storage.header.ServerRequestMethod;
import no.api.regurgitator.storage.template.ServerResponseStoreTestCase;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class FreebleInMemoryStorageTest extends ServerResponseStoreTestCase {

    @AfterClass
    public static void cleanUp() throws IOException {
        FileUtils.deleteDirectory(new File("mocktemp"));
    }

    @Override
    public String getStorageManager() {
        return FeebleInMemoryStorage.class.getName();
    }

    @Override
    public String getArchivedFolder() {
        return null;
    }

    @Override
    public boolean getSkipLongTest() {
        return false;
    }

    @Test
    public void getKeysTest() {
        ServerResponseStore store = ServerResponseStoreLoader.load(getStorageManager(), null);
        ServerResponseMeta meta1 = new ServerResponseMeta(200, ServerRequestMethod.GET, "http://www.oa.no",
                                                          new Headers(new HashMap<>()));
        ServerResponse s1 = new ServerResponse("oa", meta1);
        store.store(s1);
        ServerResponseMeta meta2 = new ServerResponseMeta(200, ServerRequestMethod.GET, "http://www.ba.no",
                                                          new Headers(new HashMap<>()));
        ServerResponse s2 = new ServerResponse("ba", meta2);
        store.store(s2);
        List<ServerResponseKey> keys = store.getKeys();
        Assert.assertNotNull(keys);
        Assert.assertEquals(2, keys.size());
        Assert.assertEquals("http://www.ba.no", keys.get(0).getRequestURI()); // Prove that the list is sorted
        Assert.assertEquals(522, store.getSize());
        Assert.assertEquals(0.002, store.getSizeAsKb(), 0.01);
    }
}