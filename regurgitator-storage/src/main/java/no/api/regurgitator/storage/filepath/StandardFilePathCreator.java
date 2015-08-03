package no.api.regurgitator.storage.filepath;

import no.api.regurgitator.storage.header.ServerRequestMethod;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class StandardFilePathCreator implements FilePathCreator {

    @Override
    public String createFilePath(ServerRequestMethod requestMethod, String requestURI) {
        List<String> pathList = new ArrayList<>();
        pathList.add(requestMethod.toString());
        for (String eachPaths : StringUtils.split(this.customReplaceString(requestURI, '_'), "_")) {
            pathList.add(convertLinuxName(eachPaths));
        }
        return makePathFile(pathList);
    }

    private String customReplaceString(String target, char replace) {
        StringBuilder tempString = new StringBuilder();
        for (char singleChar : target.toCharArray()) {
            if (Character.isAlphabetic(singleChar) || Character.isDigit(singleChar)) {
                tempString.append(singleChar);
            } else {
                tempString.append(replace);
            }
        }
        return tempString.toString();
    }

    private String convertLinuxName(String s) {
        return StringUtils.isEmpty(s) ? "null" : s;
    }

    private String makePathFile(List<String> pathFile) {
        return StringUtils.join(pathFile, "/");
    }

}
