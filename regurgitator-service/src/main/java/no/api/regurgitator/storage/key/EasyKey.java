package no.api.regurgitator.storage.key;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class EasyKey implements Key{

    private String fileName;
    private String filePath;
    private String original_key;

    private static final Logger log = LoggerFactory.getLogger(EasyKey.class);

    @Override
    public String getFullPath() {
        return this.filePath + "/" + this.fileName;
    }

    @Override
    public String getPath() {
        return this.filePath;
    }

    @Override
    public String getFileName() {
        return this.fileName;
    }

    @Override
    public String getKey() {
        return this.original_key;
    }

    @Override
    public void createKey(String key) {

        String[] partKeys = key.split("_",2);
        String method = partKeys[0];
        String requestURI = partKeys[1];
        this.original_key = key;
        this.fileName =  "save.xml";
        List<String> pathList = new ArrayList<>();
        //Create PathFile

        pathList.add(method);

        String convertURI = requestURI.replaceAll("://|//|\\.|\\?|&|#|:|,|_","/");
        String[] subPaths = StringUtils.split(convertURI,"/");
        for(String eachPaths : subPaths){
           pathList.add(convertLinuxName(eachPaths));
        }

        this.filePath = makePathFile(pathList);


    }




    private String convertLinuxName(String s){
        if(StringUtils.isEmpty(s)){
            return "null";
        }else{
            return s;
        }
    }

    private String makePathFile(List<String> pathFile){
        String ret = StringUtils.join(pathFile,"/");
        return ret;
    }

    public EasyKey(){

    }

    public EasyKey(String key){
        this.createKey(key);
    }
}
