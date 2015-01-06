package no.api.regurgitator.storage;

import org.junit.Assert;
import org.junit.Test;

public class ServerResponseStoreLoaderTest {

    @Test
    public void testLoadValid() throws Exception {
        ServerResponseStore load = ServerResponseStoreLoader.load(FeebleInMemoryStorage.class.getName(), null);
        Assert.assertNotNull(load);

    }

    @Test
    public void testLoadInvalid() throws Exception {
        ServerResponseStore load = ServerResponseStoreLoader.load("foo", null);
        Assert.assertNull(load);
    }
}