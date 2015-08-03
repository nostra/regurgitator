package no.api.regurgitator.storage.filepath;

import no.api.regurgitator.storage.header.ServerRequestMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class MockFriendlyFilePathCreator implements FilePathCreator {

    private static final Logger log = LoggerFactory.getLogger(MockFriendlyFilePathCreator.class);

    private static MessageDigest md = null;

    static {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log.error("Could not create MessageDigest");
        }
    }

    @Override
    public String createFilePath(ServerRequestMethod requestMethod, String requestURI) {
        List<String> pathList = new ArrayList<>();
        pathList.add(requestMethod.toString());
        try {
            pathList.add(this.encode(requestURI));
        } catch (DigestException e) {
            log.warn("Could not encode URI. Path will not be encoded with MD5.");
            pathList.add(requestURI);
        }
        return StringUtils.join(pathList, "/");
    }

    private String encode(String str) throws DigestException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] md5hash;
            md.update(str.getBytes("UTF-8"), 0, str.length());
            md5hash = md.digest();

            StringBuilder buf = new StringBuilder();
            for (byte aMd5hash : md5hash) {
                int halfbyte = (aMd5hash >>> 4) & 0x0F; // NOSONAR We do not care that 4 is a magic number
                int twohalfs = 0;
                do {
                    if ((0 <= halfbyte) && (halfbyte <= 9)) { // NOSONAR
                        buf.append((char) ('0' + halfbyte));
                    } else {
                        buf.append((char) ('a' + (halfbyte - 10))); // NOSONAR
                    }
                    halfbyte = aMd5hash & 0x0F;
                } while (twohalfs++ < 1);
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new DigestException("Got exception from digest", e);
        }
    }

}
