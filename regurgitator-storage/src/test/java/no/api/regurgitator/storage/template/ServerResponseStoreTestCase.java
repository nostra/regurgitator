package no.api.regurgitator.storage.template;

import com.thoughtworks.xstream.XStream;
import no.api.regurgitator.storage.ServerResponse;
import no.api.regurgitator.storage.ServerResponseKey;
import no.api.regurgitator.storage.ServerResponseMeta;
import no.api.regurgitator.storage.ServerResponseStore;
import no.api.regurgitator.storage.ServerResponseStoreLoader;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

import static no.api.regurgitator.storage.header.ServerRequestMethod.GET;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * This class is a basic template for test ServerResponseStore Class
 */
abstract public class ServerResponseStoreTestCase {

    private static final Logger log = LoggerFactory.getLogger(ServerResponseStoreTestCase.class);

    private static ServerResponseKey mockShortKey;

    private static ServerResponseKey mockLongKey;

    private static ServerResponseKey mockStrangeKey;

    private static ServerResponseKey mockEmptyKey;

    private static ServerResponse mockShortKeyResponse;

    private static ServerResponse mockLongKeyResponse;

    private static ServerResponse mockStrangeKeyResponse;

    private ServerResponseStore storage;

    private static XStream xstream = new XStream();

    protected static String getFileResource(String file) {
        return ServerResponseStoreTestCase.class.getResource(file).getPath();
    }

    @BeforeClass
    public static void initMock() throws IOException {
        mockShortKey = buildMockShortKey();
        mockLongKey = buildMockLongKey();
        mockStrangeKey = buildMockStrangeKey();
        mockEmptyKey = buildMockEmptyKey();
        mockShortKeyResponse = buildShortKeyResponse();
        mockLongKeyResponse = buildLongKeyResponse();
        mockStrangeKeyResponse = buildStrangeKeyResponse();
    }

    private static ServerResponse buildResponse() throws IOException {
        ServerResponseMeta meta = (ServerResponseMeta) xstream.fromXML(IOUtils.toString(new FileInputStream(
                new File(getFileResource("/meta.xml")))));
        return new ServerResponse(IOUtils.toString(new FileInputStream(new File(getFileResource("/content.txt")))), meta);
    }

    private static ServerResponse buildShortKeyResponse() throws IOException {
        ServerResponse r = buildResponse();
        ServerResponseMeta meta = new ServerResponseMeta(
                r.getMeta().getStatus(),
                r.getMeta().getMethod(),
                buildMockShortKey().getRequestURI(),
                r.getMeta().getHeaders());
        return new ServerResponse(r.getContent(), meta);
    }

    private static ServerResponseKey buildMockShortKey() {
        return new ServerResponseKey(GET, "http://relax.v3.api.no/relax-1.6/polldata/41/7441831");
    }

    private static ServerResponse buildLongKeyResponse() throws IOException {
        ServerResponse r = buildResponse();
        ServerResponseMeta meta = new ServerResponseMeta(
                r.getMeta().getStatus(),
                r.getMeta().getMethod(),
                buildMockLongKey().getRequestURI(),
                r.getMeta().getHeaders());
        return new ServerResponse(r.getContent(), meta);
    }


    private static ServerResponseKey buildMockLongKey() {
        StringBuilder s = new StringBuilder();
        s.append("http://relax.v3.api.no/relax-1.6/polldata/41/7441831?if=long");

        for (int i = 0; s.length() < 1024; i++) {
            s.append("&if").append(i).append("=log");
        }
        return new ServerResponseKey(GET, s.toString());
    }

    private static ServerResponseKey buildMockEmptyKey() {
        return new ServerResponseKey(GET, "http://relax.v3.api.no/relax-1.6/polldata/41/555555");
    }

    private static ServerResponse buildStrangeKeyResponse() throws IOException {
        ServerResponse r = buildResponse();
        ServerResponseMeta meta = new ServerResponseMeta(
                r.getMeta().getStatus(),
                r.getMeta().getMethod(),
                buildMockStrangeKey().getRequestURI(),
                r.getMeta().getHeaders());
        return new ServerResponse(r.getContent(), meta);
    }

    private static ServerResponseKey buildMockStrangeKey() {
        return new ServerResponseKey(GET, "http://www.ba.no/?a=%20&&amp;=+");
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
        assertNotNull("Can't create store", storage);
    }

    @Test
    public void testShortURLSave() {
        storage.store(mockShortKeyResponse);
        Optional<ServerResponse> read = storage.read(mockShortKey, 200);
        Assert.assertTrue(read.isPresent());
        Assert.assertEquals(mockShortKeyResponse.getContent(), read.get().getContent());

        //TODO: create more mockResponse such as image or response from same uri with different post
    }

    @Test
    public void testLongURLSave() {
        if (!getSkipLongTest()) {
            storage.store(mockLongKeyResponse);
            Optional<ServerResponse> read = storage.read(mockLongKey, 200);
            Assert.assertTrue(read.isPresent());
            Assert.assertEquals(mockLongKeyResponse.getContent(), read.get().getContent());
        }
    }

    @Test
    public void testStrangeURLSave() {
        storage.store(mockStrangeKeyResponse);
        Optional<ServerResponse> read = storage.read(mockStrangeKey, 200);
        Assert.assertTrue(read.isPresent());
        Assert.assertEquals(mockStrangeKeyResponse.getContent(), read.get().getContent());
    }

    @Test
    public void testEmptyPageRequest() {
        // test not saved page
        assertFalse(storage.read(mockEmptyKey, 200).isPresent());
    }


}
