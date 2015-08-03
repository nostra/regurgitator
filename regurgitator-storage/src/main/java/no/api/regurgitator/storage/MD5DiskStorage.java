package no.api.regurgitator.storage;

import no.api.regurgitator.storage.filepath.FilePathCreator;
import no.api.regurgitator.storage.filepath.MockFriendlyFilePathCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MD5DiskStorage extends DiskStorage {

    private static final Logger log = LoggerFactory.getLogger(MD5DiskStorage.class);

    private FilePathCreator filePathCreator = new MockFriendlyFilePathCreator();

    public MD5DiskStorage(String archivedFolder) {
        super(archivedFolder);
    }

    @Override
    public FilePathCreator filePathCreator() {
        return filePathCreator;
    }

}
