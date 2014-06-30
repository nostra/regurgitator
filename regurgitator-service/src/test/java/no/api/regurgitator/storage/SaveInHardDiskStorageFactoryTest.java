package no.api.regurgitator.storage;

import io.dropwizard.testing.junit.DropwizardAppRule;
import no.api.regurgitator.RegurgitatorApplication;
import no.api.regurgitator.RegurgitatorConfiguration;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 *
 */
public class SaveInHardDiskStorageFactoryTest {

    @ClassRule
    public static final DropwizardAppRule<RegurgitatorConfiguration> RULE =
            new DropwizardAppRule<RegurgitatorConfiguration>(RegurgitatorApplication.class,
                    getFileResource("/test-dropwizard.yml"));

    private static String getFileResource(String file) {
        return SaveInHardDiskStorageFactoryTest.class.getResource(file).getPath();
    }

    @Test
    public void testResourceFile() {
        Assert.assertNotNull("Test file missing", getClass().getResource("/test-dropwizard.yml"));
    }

    @Test
    public void testCreateStorage(){
        SaveInHardDiskStorage storage = SaveInHardDiskStorageFactory.createStorage(RULE.getConfiguration());
        Assert.assertNotNull(storage);
        Assert.assertEquals("mocktemp/",storage.getSaveDir());
    }

}
