package no.api.regurgitator.storage;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SaveInHardDiskStorage implements ServerResponseStore {

    private static final Logger log = LoggerFactory.getLogger(SaveInHardDiskStorage.class);

    private static final XStream xstream = new XStream();

    private String saveDir = "Save/";

    private int size = -1;

    public SaveInHardDiskStorage(String archivedFolder) {
        if (!archivedFolder.isEmpty() && archivedFolder.endsWith("/")) {
            saveDir = archivedFolder;
        }
    }

    public String getSaveDir() {
        return saveDir;
    }

    public void updateSize() {
        Queue<File> dirs = new LinkedList<>();
        dirs.add(new File(saveDir));
        size = 0;
        while (!dirs.isEmpty()) {
            File[] listFiles = dirs.poll().listFiles();
            subLoopUpdateSize(dirs, listFiles);
        }
    }

    private void subLoopUpdateSize(Queue<File> dirs, File[] allFiles) {
        if (allFiles == null) {
            return;
        }
        for (File f : allFiles) {
            if (f.isDirectory()) {
                dirs.add(f);
            } else if (f.isFile()) {
                size += f.length();
            }
        }
    }

    @Override
    public void store(String key, ServerResponse page) {
        store(key, xstream.toXML(page));
    }

    private void store(String key, String content) {
        if (key == null || content == null) {
            log.debug("reject key :" + key);
            return;
        }

        // try to load old content
        String loadOldContent = loadContentWithKey(key);

        if (saveContentWithKey(key, content)) {

            //Update size
            if (!StringUtils.isEmpty(loadOldContent)) {
                size -= loadOldContent.length();
            }
            size += content.length();
        }
    }

    private boolean saveContentWithKey(String key, String content) {
        try {
            File file = new File(saveDir + key);
            log.debug("save: " + saveDir + key);
            FileUtils.writeStringToFile(file, content);
            return true;
        } catch (IOException e) {
            log.error("Save fail [" + key + "] ", e);
            return false;
        }
    }

    private String loadContentWithKey(String key) {
        try {
            log.debug("load data :" + saveDir + key);
            return IOUtils.toString(new FileInputStream(new File(saveDir + key)));
        } catch (IOException e) {
            log.error("Cannot read [" + key + "]", e);
            return null;
        }
    }

    public List<String> listAllTargetFile() {
        List<String> allKeys = new ArrayList<>();
        Queue<File> dirs = new LinkedList<>();
        dirs.add(new File(saveDir));

        while (!dirs.isEmpty()) {
            File[] listFiles = dirs.poll().listFiles();

            subLoopListAllTargetFile(dirs, allKeys, listFiles);
        }

        return allKeys;
    }

    private void subLoopListAllTargetFile(Queue<File> dirs, List<String> allKeys, File[] allFiles) {
        if (allFiles == null) {
            return;
        }
        for (File f : allFiles) {
            if (f.isDirectory()) {
                dirs.add(f);
            } else if (f.isFile()) {
                String key = f.getPath().replace(saveDir, "");
                allKeys.add(key);
            }
        }
    }

    @Override
    public ServerResponse read(String key) {
        String content = loadContentWithKey(key);
        if (content != null) {
            return (ServerResponse) xstream.fromXML(content);
        } else {
            return null;
        }
    }


    @Override
    public List<String> getKeys() {
        return listAllTargetFile();
    }

    @Override
    public long getSize() {
        if (size < 0) {
            updateSize();
        }
        return size;
    }

    @Override
    public long getSizeAsKb() {
        return getSize() / 1024L;
    }

}
