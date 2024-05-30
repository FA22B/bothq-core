package com.bothq.core.dto.put;

import com.bothq.lib.plugin.config.component.IComponent;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
abstract public class BaseConfigPutDTO<TValue, TComponent extends IComponent<TValue>> implements IBaseConfigPutDTO<TValue, TComponent> {
    @Getter(onMethod_ = {@JsonProperty("uniqueId")})
    @Setter(onMethod_ = {@JsonProperty("uniqueId")})
    String uniqueId;


    @Getter(onMethod_ = {@JsonProperty("value")})
    @Setter(onMethod_ = {@JsonProperty("value")})
    TValue value;

    public void setOn(TComponent component) {
        component.setValue(value);
    }
}
