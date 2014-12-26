package no.api.regurgitator.views;

import io.dropwizard.views.View;
import no.api.regurgitator.storage.ServerResponseStore;

import java.nio.charset.Charset;

public class IndexView extends View {

    private Boolean toRecord;

    private ServerResponseStore storage;

    public IndexView(Boolean toRecord, ServerResponseStore storage) {
        super("index.ftl", Charset.forName("UTF-8"));
        this.toRecord = toRecord;
        this.storage = storage;
    }

    public Boolean getToRecord() {
        return toRecord;
    }

    public ServerResponseStore getStorage() {
        return storage;
    }
}
