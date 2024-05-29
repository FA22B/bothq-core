package com.bothq.core.dto.base;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface IConcreteConfigDTO extends IConfigDTO {
    @JsonProperty("value")
    Object getValue();

    @JsonProperty("value")
    void setValue(Object value);
}
