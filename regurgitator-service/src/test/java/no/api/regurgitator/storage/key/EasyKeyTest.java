package no.api.regurgitator.storage.key;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class EasyKeyTest {

    @Test
    public void testCreateKey(){
        String mockuri = "GET_http://relax.v3.api.no/relax-1.6/polldata/41/7441831";
        Key key = new EasyKey(mockuri);
        Assert.assertEquals(key.getFullPath(),"GET/http/relax/v3/api/no/relax-1/6/polldata/41/7441831/save.xml");
    }
}
