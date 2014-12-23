package no.api.regurgitator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegurgitatorConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty
    private int proxyPort;

    @Valid
    @JsonProperty
    private String archivedFolder;

    @Valid
    @NotNull
    @JsonProperty
    private Boolean recordOnStart;

    @Valid
    @NotNull
    @JsonProperty
    private String storageManager;

    public int getProxyPort() {
        return proxyPort;
    }

    public String getStorageManager() {
        return storageManager;
    }

    public Boolean getRecordOnStart() {
        return recordOnStart;
    }

    public String getArchivedFolder() {
        return archivedFolder;
    }
}
