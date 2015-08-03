package no.api.regurgitator.storage;

import no.api.regurgitator.storage.header.ServerRequestMethod;
import org.junit.Assert;
import org.junit.Test;

import static no.api.regurgitator.storage.header.ServerRequestMethod.GET;


public class ServerResponseKeyTest {

    @Test
    public void testEquals() {
        String mockuri = "http://www.db.no";
        ServerResponseKey key = new ServerResponseKey(GET, mockuri);
        Assert.assertTrue(key.equals(key));
        ServerResponseKey key2 = new ServerResponseKey(GET, mockuri);
        Assert.assertTrue(key.equals(key2));
        Assert.assertTrue(key2.equals(key));
        ServerResponseKey key3 = new ServerResponseKey(ServerRequestMethod.POST, mockuri);
        Assert.assertFalse(key2.equals(key3));
        String mockuri2 = "http://www.db.no/";
        ServerResponseKey key4 = new ServerResponseKey(ServerRequestMethod.GET, mockuri2);
        Assert.assertFalse(key2.equals(key4));
        Assert.assertFalse(key.equals("foo"));
    }

    @Test
    public void testCompareTo() {
        String mockuri = "http://www.db.no";
        ServerResponseKey key = new ServerResponseKey(GET, mockuri);
        Assert.assertEquals(0, key.compareTo(key));
    }


}
