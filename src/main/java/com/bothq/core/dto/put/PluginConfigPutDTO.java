package com.bothq.core.dto.put;


import com.bothq.lib.plugin.config.IConfig;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class PluginConfigPutDTO implements IGroupConfigPutDTO {
    @Getter(onMethod_ = {@JsonProperty("enabled")})
    @Setter(onMethod_ = {@JsonProperty("enabled")})
    private Boolean enabled;

    @Getter(onMethod_ = {@JsonProperty("value")})
    @Setter(onMethod_ = {@JsonProperty("value")})
    private IBaseConfigPutDTO<?, ?>[] value;


    public void setOn(IConfig iConfig) {
        // TODO implement
    }
}