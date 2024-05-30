package com.bothq.core.dto.get.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@EqualsAndHashCode(callSuper = true)
public class ConcreteGeneralConfigGetDTO extends GeneralConfigGetDTO implements IConcreteConfigGetDTO {
    @Getter(onMethod_ = {@JsonProperty("value")})
    @Setter(onMethod_ = {@JsonProperty("value")})
    private Object value;

    public ConcreteGeneralConfigGetDTO(String type, String uniqueID, String displayName, Object value) {
        super(type, uniqueID, displayName);
        this.value = value;
    }
}
