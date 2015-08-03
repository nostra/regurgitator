package no.api.regurgitator.storage;

import no.api.regurgitator.storage.header.ServerRequestMethod;
import org.junit.Assert;
import org.junit.Test;

import static no.api.regurgitator.storage.header.ServerRequestMethod.GET;

public class MD5DiskStorageTest {

    @Test
    public void testPath() {
        MD5DiskStorage diskStorage = new MD5DiskStorage("foo");
        String mockuri = "http://relax.v3.api.no/relax-1.6/polldata/41/7441831";
        Assert.assertEquals("GET/b2903c2f095d81402603b915991722fd",
                            diskStorage.filePathCreator().createFilePath(ServerRequestMethod.GET, mockuri));
    }

    @Test
    public void testCreateKeyFromAdvancedURI() {
        MD5DiskStorage diskStorage = new MD5DiskStorage("foo");
        String mockuri = "http://bed.api.no/api/acpcomposer/v0.1/resolve/http://www.oa.no/Den_siste_l_a_i_Dokkadeltaet_synker__N__skal_den_reddes-5-35-36062.html#reloaded";
        ServerResponseKey key = new ServerResponseKey(GET, mockuri);
        Assert.assertEquals("GET/3ecd8ad691d355826c82a98288511c07",
                            diskStorage.filePathCreator().createFilePath(ServerRequestMethod.GET, mockuri));
    }

    @Test
    public void testStrangeCharacters() {
        MD5DiskStorage diskStorage = new MD5DiskStorage("foo");
        String mockuri = "http://www.ba.no/?a=%20&&amp;=+";
        ServerResponseKey key = new ServerResponseKey(GET, mockuri);
        Assert.assertEquals("GET/0600ce2b4e0c3500f149c7aca9241de5",
                            diskStorage.filePathCreator().createFilePath(key.getRequestMethod(), key.getRequestURI()));
        Assert.assertEquals(GET, key.getRequestMethod());
    }

}