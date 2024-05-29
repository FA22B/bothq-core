package com.bothq.core.dto.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@EqualsAndHashCode(callSuper = true)
public class ConcreteGeneralConfigDTO extends GeneralConfigDTO implements IConcreteConfigDTO {
    @Getter(onMethod_ = {@JsonProperty("value")})
    @Setter(onMethod_ = {@JsonProperty("value")})
    private Object value;

    public ConcreteGeneralConfigDTO(String type, String uniqueID, boolean enabled, String displayName, Object value) {
        super(type, uniqueID, enabled, displayName);
        this.value = value;
    }
}
