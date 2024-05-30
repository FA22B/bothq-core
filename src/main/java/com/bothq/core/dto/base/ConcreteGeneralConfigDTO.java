package com.bothq.core.dto.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@EqualsAndHashCode(callSuper = true)
public class ConcreteGeneralConfigDTO extends GeneralConfigDTO implements IConcreteConfigDTO {
    @Getter(onMethod_ = {@JsonProperty("value")})
    @Setter(onMethod_ = {@JsonProperty("value")})
    private Object value;

    public ConcreteGeneralConfigDTO(String type, String uniqueID, String displayName, Object value) {
        super(type, uniqueID, displayName);
        this.value = value;
    }
}
