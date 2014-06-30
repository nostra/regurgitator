package no.api.regurgitator.storage.key;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FilePathKey {

    private String fileName;

    private String filePath;

    private String original_key;

    public String getFullPath() {
        return this.filePath + "/" + this.fileName;
    }

    public String getPath() {
        return this.filePath;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getKey() {
        return this.original_key;
    }


    private void createKey(String key) {

        String[] partKeys = key.split("_", 2);
        String method = partKeys[0];
        String requestURI = partKeys[1];
        this.original_key = key;
        this.fileName = "save.xml";
        List<String> pathList = new ArrayList<>();

        //Create PathFile
        pathList.add(method);
        String convertURI = this.customReplaceString(requestURI, '_');
        String[] subPaths = StringUtils.split(convertURI, "_");

        for ( String eachPaths : subPaths ) {
            pathList.add(convertLinuxName(eachPaths));
        }

        this.filePath = makePathFile(pathList);

    }

    private String customReplaceString(String target, char replace) {
        StringBuilder tempString = new StringBuilder();
        for ( char singleChar : target.toCharArray() ) {

            if ( Character.isAlphabetic(singleChar) || Character.isDigit(singleChar) ) {
                tempString.append(singleChar);
            } else {
                tempString.append(replace);
            }
        }

        return tempString.toString();
    }


    private String convertLinuxName(String s) {
        if ( StringUtils.isEmpty(s) ) {
            return "null";
        } else {
            return s;
        }
    }

    private String makePathFile(List<String> pathFile) {
        String ret = StringUtils.join(pathFile, "/");
        return ret;
    }

    public FilePathKey(String key) {
        this.createKey(key);
    }
}
