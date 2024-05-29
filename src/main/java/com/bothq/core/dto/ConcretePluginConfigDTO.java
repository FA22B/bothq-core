package com.bothq.core.dto;

import com.bothq.core.dto.base.IConcreteConfigDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@AllArgsConstructor
public class ConcretePluginConfigDTO {

    @Getter(onMethod_ = {@JsonProperty("statusCode")})
    @Setter(onMethod_ = {@JsonProperty("statusCode")})
    private long statusCode;

    @Getter(onMethod_ = {@JsonProperty("message")})
    @Setter(onMethod_ = {@JsonProperty("message")})
    private String message;

    @Getter(onMethod_ = {@JsonProperty("pluginId")})
    @Setter(onMethod_ = {@JsonProperty("pluginId")})
    private long pluginID;

    @Getter(onMethod_ = {@JsonProperty("type")})
    @Setter(onMethod_ = {@JsonProperty("type")})
    private String type;

    @Getter(onMethod_ = {@JsonProperty("uniqueId")})
    @Setter(onMethod_ = {@JsonProperty("uniqueId")})
    private String uniqueID;

    @Getter(onMethod_ = {@JsonProperty("enabled")})
    @Setter(onMethod_ = {@JsonProperty("enabled")})
    private boolean enabled;

    @Getter(onMethod_ = {@JsonProperty("displayName")})
    @Setter(onMethod_ = {@JsonProperty("displayName")})
    private String displayName;

    @Getter(onMethod_ = {@JsonProperty("description")})
    @Setter(onMethod_ = {@JsonProperty("description")})
    private String description;

    @Getter(onMethod_ = {@JsonProperty("value")})
    @Setter(onMethod_ = {@JsonProperty("value")})
    private List<? extends IConcreteConfigDTO> value;
}
