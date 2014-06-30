package no.api.regurgitator.storage;

import no.api.regurgitator.RegurgitatorConfiguration;

/**
 *
 */
public class SaveInHardDiskStorageFactory {

    public static SaveInHardDiskStorage createStorage(RegurgitatorConfiguration conf) {
        SaveInHardDiskStorage storage = new SaveInHardDiskStorage();
        String archivedFolder = conf.getArchivedFolder();
        if (!archivedFolder.isEmpty() && archivedFolder.endsWith("/")) {
            storage.setSaveDir(archivedFolder);
        } else {
            storage.setSaveDir("Save/");
        }
        storage.updateSize();
        return storage;
    }

}
