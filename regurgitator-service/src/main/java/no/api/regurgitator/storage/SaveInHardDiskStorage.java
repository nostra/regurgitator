package no.api.regurgitator.storage;

import com.thoughtworks.xstream.XStream;
import no.api.regurgitator.RegurgitatorConfiguration;
import no.api.regurgitator.storage.key.EasyKey;
import no.api.regurgitator.storage.key.Key;
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

/**
 *
 */
public class SaveInHardDiskStorage implements ServerResponseStore {

    private static final Logger log = LoggerFactory.getLogger(SaveInHardDiskStorage.class);

    private static final XStream xstream = new XStream();
    private String saveDir = "Save/";

    int size = 0;

    public SaveInHardDiskStorage(RegurgitatorConfiguration conf ) {
        //TO DO load savedir from config
        String saveConfig =  conf.getArchivedFolder();
        if(!StringUtils.isEmpty(saveConfig) && saveConfig.endsWith("/")) {
            saveDir = saveConfig;
        }else{
            log.warn("Bad name or missing. Switch to default archived folder name.");
            saveDir = "Save/";
        }

        //updating size
        this.listAllTargetFile(saveDir);
    }

    @Override
    public void store(String key, ServerResponse page) {

        Key singleKey = new EasyKey(key);

        try {
            store(singleKey,xstream.toXML(page));
        }catch (IOException e){
            //TO DO Handle IOException
            log.error(e.toString());
        }
    }

    private void store(Key key,String content) throws IOException{

        if(key == null || content == null) {
            log.debug("reject key :" + key.getKey());
            return;
        }

        saveContentWithKey(key, content);
    }

    private boolean saveContentWithKey(Key key,String content){
        try {
            File file = new File(saveDir + key.getFullPath());
            log.debug("save: " +saveDir + key.getFullPath());
            FileUtils.writeStringToFile(file, content);

            //To Do Fix this later

            File keyFile = new File(saveDir + key.getPath() + "/data.key");
            FileUtils.writeStringToFile(keyFile,key.getKey());

            return true;
        }catch (IOException e) {
            //TO DO Handle Alert
            log.error("save fail: [" +key.getKey() +"] " + e.toString());
            return false;
        }
    }

    private String loadContentWithKey(Key key){
        try {
            log.debug("load data :" + saveDir + key.getFullPath());
            return IOUtils.toString(new FileInputStream(new File(saveDir + key.getFullPath())));
        }catch (IOException e) {
            //TO DO Handle Alert
            return null;
        }
    }

    private String readKeyFile(File key){
        try {
            return IOUtils.toString(new FileInputStream(key));
        }catch (IOException e) {
            //TO DO Handle Alert
            return null;
        }
    }

    public List<String> listAllTargetFile(String startDir){
        List<String> allKeys = new ArrayList<>();
        Queue<File> dirs = new LinkedList<>();
        dirs.add(new File(startDir));
        size = 0;

        while (!dirs.isEmpty()) {
            File[] listFiles = dirs.poll().listFiles();
            if(listFiles != null) {
                for (File f : listFiles) {

                    if (f.isDirectory()) {
                        dirs.add(f);
                    } else if (f.isFile()) {
                        if(f.getName().equals("data.key")) {
                            // Temp Solution
                            String key = readKeyFile(f);
                            if (key != null)
                                allKeys.add(key);
                        }else if(f.getName().equals("save.xml")){
                            // Update Sized
                            size += f.length();
                        }
                    }
                }
            }
        }
        return allKeys;
    }

    @Override
    public ServerResponse read(String key) {
        Key singleKey = new EasyKey(key);
        String content = loadContentWithKey(singleKey);
        if(content != null) {
            return (ServerResponse) xstream.fromXML(content);
        }else{

            return null;
        }
    }


    @Override
    public List<String> getKeys() {
        return listAllTargetFile(saveDir);
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public long getSizeAsKb() {
        return size / 1024L;
    }

}
