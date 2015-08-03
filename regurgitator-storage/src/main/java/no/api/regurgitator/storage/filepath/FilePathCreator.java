package no.api.regurgitator.storage.filepath;

import no.api.regurgitator.storage.header.ServerRequestMethod;

public interface FilePathCreator {

    String createFilePath(ServerRequestMethod requestMethod, String requestURI);
}
