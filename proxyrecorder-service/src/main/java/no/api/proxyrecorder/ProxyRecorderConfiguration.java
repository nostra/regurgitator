package no.api.proxyrecorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 *
 */
public class ProxyRecorderConfiguration extends Configuration {
    @Valid
    @NotNull
    @JsonProperty
    private int proxyPort;

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
}
