package no.api.regurgitator.storage;

import com.thoughtworks.xstream.XStream;
import no.api.regurgitator.storage.header.ServerRequestMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

/**
 * New default implementation of disk storage for Regurgitator.
 *
 * This implementation separates the storage of response data and headers into different readable files.
 */
public class DiskStorage implements ServerResponseStore {

    private static final Logger log = LoggerFactory.getLogger(DiskStorage.class);

    private static final XStream xstream = new XStream();

    public static final String META_XML = "meta.xml";
    public static final String CONTENT_TXT = "content.txt";

    private String saveDir = "Save/";

    public DiskStorage(String archivedFolder) {
        if (!archivedFolder.isEmpty()) {
            saveDir = archivedFolder;
            if (!archivedFolder.endsWith("/")) {
                saveDir += "/";
            }
        } else {
            log.error("The given folder for storage of Regurgitator files empty. Please fix!");
        }
    }

    @Override
    public Optional<ServerResponseKey> store(ServerResponse serverResponse) {
        if (!isValidServerResponse(serverResponse)) {
            log.warn("Cannot store invalid ServerResponse object.");
            return Optional.empty();
        }
        String metaContent = xstream.toXML(serverResponse.getMeta());
        ServerResponseKey key = ServerResponseKey.fromServerResponse(serverResponse);
        if (key == null || metaContent == null || serverResponse.getContent() == null) {
            log.debug("reject key :" + key);
            return Optional.empty();
        }
        if (saveResponse(key, serverResponse)) {
            return Optional.of(key);
        } else {
            log.error("Could not store data for {}", key);
            return Optional.empty();
        }

    }

    @Override
    public Optional<ServerResponse> read(ServerResponseKey key) {

        Optional<String> content = loadContentWithKey(key);
        Optional<ServerResponseMeta> meta = loadMetaWithKey(key);
        if (content.isPresent() && meta.isPresent()) {
            return Optional.of(new ServerResponse(content.get(), meta.get()));
        } else {
            log.warn("Could not load object.");
            return Optional.empty();
        }
    }

    @Override
    public List<ServerResponseKey> getKeys() {
        return listAllTargetFile();
    }

    @Override
    public long getSize() {
        return -1;
    }

    @Override
    public double getSizeAsKb() {
        return -1;
    }

    private boolean saveResponse(ServerResponseKey key, ServerResponse response) {
        try {
            saveFile(createContentFilePath(key), response.getContent());
            saveFile(createMetaFilePath(key), xstream.toXML(response.getMeta()));
            return true;
        } catch (IOException e) {
            log.error("Could not store files for [" + key + "] ", e);
            return false;
        }
    }

    private void saveFile(String path, String content) throws IOException {
        File file = new File(path);
        FileUtils.writeStringToFile(file, content);
    }

    private Optional<String> loadContentWithKey(ServerResponseKey key) {
        String filePath = createContentFilePath(key);
        try {
            return Optional.ofNullable(IOUtils.toString(new FileInputStream(new File(filePath))));
        } catch (IOException e) {
            log.error("Cannot read [" + filePath + "]", e);
            return Optional.empty();
        }
    }

    private Optional<ServerResponseMeta> loadMetaWithKey(ServerResponseKey key) {
        return loadMetaFromFile(new File(createMetaFilePath(key)));
    }

    private Optional<ServerResponseMeta> loadMetaFromFile(File f) {
        try {
            return Optional.ofNullable((ServerResponseMeta) xstream.fromXML(IOUtils.toString(new FileInputStream(f))));
        } catch (IOException e) {
            log.error("Could not load meta file", e);
            return Optional.empty();
        }
    }

    private String createContentFilePath(ServerResponseKey key) {
        return String.format("%s%s/%s", saveDir, key.getPath(), CONTENT_TXT);
    }

    private String createMetaFilePath(ServerResponseKey key) {
        return String.format("%s%s/%s", saveDir, key.getPath(), META_XML);
    }

    private List<ServerResponseKey> listAllTargetFile() {
        List<ServerResponseKey> allKeys = new ArrayList<>();
        Queue<File> dirs = new LinkedList<>();
        dirs.add(new File(saveDir));
        while (!dirs.isEmpty()) {
            subLoopListAllTargetFile(dirs, allKeys, dirs.poll().listFiles());
        }
        return allKeys;
    }

    private void subLoopListAllTargetFile(Queue<File> dirs, List<ServerResponseKey> allKeys, File[] allFiles) {
        if (allFiles == null) {
            return;
        }
        for (File f : allFiles) {
            if (f.isDirectory()) {
                dirs.add(f);
            } else if (f.isFile() && f.getName().equals(META_XML)) {
                Optional<ServerResponseMeta> meta = loadMetaFromFile(f);
                if (meta.isPresent()) {
                    ServerRequestMethod method = ServerRequestMethod.findRequestMethodFromString(
                            f.getPath().substring(0, f.getPath().indexOf("/")));
                    allKeys.add(new ServerResponseKey(method, meta.get().getUri()));
                } else {
                    log.warn("Could not load Meta object from {}", f.getAbsolutePath());
                }
            }
        }
    }

    private boolean isValidServerResponse(ServerResponse sr) {
        return !(sr == null || sr.getContent() == null || sr.getMeta() == null ||
                sr.getMeta().getMethod() == null || sr.getMeta().getUri() == null);
    }


}
