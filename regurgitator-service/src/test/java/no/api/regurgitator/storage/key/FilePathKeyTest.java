package no.api.regurgitator.storage.key;

import org.junit.Test;

import org.junit.Assert;

/**
 *
 */
public class FilePathKeyTest {

    @Test
    public void testCreateKey() {
        String mockuri = "GET_http://relax.v3.api.no/relax-1.6/polldata/41/7441831";
        FilePathKey key = new FilePathKey(mockuri);
        Assert.assertEquals("GET/http/relax/v3/api/no/relax/1/6/polldata/41/7441831/save.xml", key.getFullPath());
    }

    @Test
    public void testStrangeCharacters() {

        String mockuri = "GET_http://www.ba.no/?a=%20&&amp;=+";
        FilePathKey key = new FilePathKey(mockuri);
        Assert.assertEquals("GET/http/www/ba/no/a/20/amp/save.xml", key.getFullPath());
    }
}
