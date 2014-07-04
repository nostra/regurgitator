package no.api.regurgitator.storage;

import io.dropwizard.testing.junit.DropwizardAppRule;
import no.api.regurgitator.RegurgitatorApplication;
import no.api.regurgitator.RegurgitatorConfiguration;
import no.api.regurgitator.storage.template.ServerResponseStoreTestCase;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.ClassRule;

import java.io.File;
import java.io.IOException;

/**
 *
 */
public class SaveInHardDiskTest extends ServerResponseStoreTestCase {

    @ClassRule
    public static final DropwizardAppRule<RegurgitatorConfiguration> RULE =
            new DropwizardAppRule<RegurgitatorConfiguration>(RegurgitatorApplication.class,
                    getFileResource("/saveInHardDisk-dropwizard.yml"));

    @Override
    public RegurgitatorConfiguration getRegurgitatorConfiguration() {
        return RULE.getConfiguration();
    }

    @Override
    public boolean getSkipLongTest() {
        return false;
    }

    @After
    public void cleanUp() throws IOException {
        FileUtils.deleteDirectory(new File("mocktemp"));
    }
}
