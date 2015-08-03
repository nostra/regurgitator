package no.api.regurgitator.storage;

import no.api.regurgitator.storage.header.ServerRequestMethod;
import no.api.regurgitator.storage.template.ServerResponseStoreTestCase;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static no.api.regurgitator.storage.header.ServerRequestMethod.GET;

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

    @Test
    public void testPath() {
        DiskStorage diskStorage = new DiskStorage("foo");
        String mockuri = "http://relax.v3.api.no/relax-1.6/polldata/41/7441831";
        Assert.assertEquals("GET/http/relax/v3/api/no/relax/1/6/polldata/41/7441831",
                            diskStorage.filePathCreator().createFilePath(ServerRequestMethod.GET, mockuri));
    }

    @Test
    public void testCreateKeyFromAdvancedURI() {
        DiskStorage diskStorage = new DiskStorage("foo");
        String mockuri = "http://bed.api.no/api/acpcomposer/v0.1/resolve/http://www.oa.no/Den_siste_l_a_i_Dokkadeltaet_synker__N__skal_den_reddes-5-35-36062.html#reloaded";
        ServerResponseKey key = new ServerResponseKey(GET, mockuri);
        Assert.assertEquals("GET/http/bed/api/no/api/acpcomposer/v0/1/resolve/http/www/oa/no/Den/siste/l/a/i/Dokkadeltaet/synker/N/skal/den/reddes/5/35/36062/html/reloaded",
                            diskStorage.filePathCreator().createFilePath(ServerRequestMethod.GET, mockuri));
    }

    @Test
    public void testStrangeCharacters() {
        DiskStorage diskStorage = new DiskStorage("foo");
        String mockuri = "http://www.ba.no/?a=%20&&amp;=+";
        ServerResponseKey key = new ServerResponseKey(GET, mockuri);
        Assert.assertEquals("GET/http/www/ba/no/a/20/amp",
                            diskStorage.filePathCreator().createFilePath(key.getRequestMethod(), key.getRequestURI()));
        Assert.assertEquals(GET, key.getRequestMethod());
    }

}
