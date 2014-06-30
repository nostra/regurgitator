package no.api.regurgitator.storage;

import com.thoughtworks.xstream.XStream;
import io.dropwizard.testing.junit.DropwizardAppRule;
import no.api.regurgitator.RegurgitatorApplication;
import no.api.regurgitator.RegurgitatorConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertNull;

/**
 *
 */
public class SaveInHardDiskStorageTest {

    @ClassRule
    public static final DropwizardAppRule<RegurgitatorConfiguration> RULE =
            new DropwizardAppRule<RegurgitatorConfiguration>(RegurgitatorApplication.class,
                    getFileResource("/test-dropwizard.yml"));

    private static final XStream xstream = new XStream();


    @Test
    public void testResourceFile() {
        Assert.assertNotNull("Test file missing", getClass().getResource("/test-dropwizard.yml"));
        Assert.assertNotNull("Test file missing", getClass().getResource("/mockresponse.xml"));
    }

    @After
    public void clearMockTemp() throws IOException {
        FileUtils.deleteDirectory(new File("mocktemp"));
    }

    private static String getFileResource(String file) {
        return SaveInHardDiskStorageTest.class.getResource(file).getPath();
    }

    @Test
    public void testSaveInHardDiskStorageTest() throws IOException {

        ServerResponseStore storage = SaveInHardDiskStorageFactory.createStorage(RULE.getConfiguration());
        String mockKey = "GET_http://relax.v3.api.no/relax-1.6/polldata/41/7441831";

        //mock test response
        ServerResponse response = (ServerResponse) xstream.fromXML(
                IOUtils.toString(new FileInputStream(
                        new File(getFileResource("/mockresponse.xml")))));

        Assert.assertNotNull("something wrong with mockresponse,xml", response);

        // test saved page
        storage.store(mockKey, response);
        Assert.assertEquals(response.getContent(), storage.read(mockKey).getContent());

        // test not saved page
        String mockEmptyKey = "GET_http://relax.v3.api.no/relax-1.6/polldata/41/555555";
        assertNull(storage.read(mockEmptyKey));
    }

}
