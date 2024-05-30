package com.bothq.core.dto.put;

import com.bothq.lib.plugin.config.IConfigGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class GroupConfigPutDTO implements IBaseConfigPutDTO<IBaseConfigPutDTO<?, ?>[], IConfigGroup>, IGroupConfigPutDTO {
    @Getter(onMethod_ = {@JsonProperty("uniqueId")})
    @Setter(onMethod_ = {@JsonProperty("uniqueId")})
    private String uniqueId;

    @Getter(onMethod_ = {@JsonProperty("value")})
    @Setter(onMethod_ = {@JsonProperty("value")})
    private IBaseConfigPutDTO<?, ?>[] value;



    @Override
    public void setOn(IConfigGroup iConfigGroup) {
        // TODO implement
    }
}
