package no.api.regurgitator.storage.template;

import com.thoughtworks.xstream.XStream;
import no.api.regurgitator.storage.ServerResponse;
import no.api.regurgitator.storage.ServerResponseStore;
import no.api.regurgitator.storage.ServerResponseStoreLoader;
import no.api.regurgitator.storage.key.FilePathKey;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * This class is a basic template for test ServerResponseStore Class
 */
abstract public class ServerResponseStoreTestCase {

    private static String mockShortKey;

    private static String mockLongKey;

    private static String mockStrangeKey;

    private static String mockEmptyKey;

    private static ServerResponse mockResponse;

    private ServerResponseStore storage;

    protected static String getFileResource(String file) {
        return ServerResponseStoreTestCase.class.getResource(file).getPath();
    }

    @BeforeClass
    public static void initMock() throws IOException {
        FilePathKey shortKey = new FilePathKey("GET_http://relax.v3.api.no/relax-1.6/polldata/41/7441831");
        mockShortKey = shortKey.getFullPath();
        StringBuilder s = new StringBuilder();
        s.append("GET_http://relax.v3.api.no/relax-1.6/polldata/41/7441831?if=long");

        for (int i = 0; s.length() < 1024; i++) {
            s.append("&if" + i + "=log");
        }
        FilePathKey longKey = new FilePathKey(s.toString());
        mockLongKey = longKey.getFullPath();
        FilePathKey strangKey = new FilePathKey("GET_http://www.ba.no/?a=%20&&amp;=+");
        mockStrangeKey = strangKey.getFullPath();
        XStream xstream = new XStream();
        mockResponse = (ServerResponse) xstream.fromXML(
                IOUtils.toString(new FileInputStream(
                        new File(getFileResource("/mockresponse.xml")))));
        mockEmptyKey = "GET_http://relax.v3.api.no/relax-1.6/polldata/41/555555";
    }

    /**
     * For skip long url test due to some implementation not support long key.
     */

    public abstract String getStorageManager();

    public abstract String getArchivedFolder();

    public abstract boolean getSkipLongTest();

    @Before
    public void testCreateServerResponseStore() {
        storage = ServerResponseStoreLoader.load(getStorageManager(), getArchivedFolder());
        assertNotNull("can't create store", storage);
    }

    @Test
    public void testShortURLSave() {
        storage.store(mockShortKey, mockResponse);
        Assert.assertEquals(mockResponse.getContent(), storage.read(mockShortKey).getContent());

        //TODO: create more mockResponse such as image or response from same uri with different post
    }

    @Test
    public void testLongURLSave() {
        if (!getSkipLongTest()) {
            storage.store(mockLongKey, mockResponse);
            Assert.assertEquals(mockResponse.getContent(), storage.read(mockLongKey).getContent());
        }
    }

    @Test
    public void testStrangeURLSave() {
        storage.store(mockStrangeKey, mockResponse);
        Assert.assertEquals(mockResponse.getContent(), storage.read(mockStrangeKey).getContent());
    }

    @Test
    public void testEmptyPageRequest() {
        // test not saved page
        assertNull(storage.read(mockEmptyKey));
    }


}
