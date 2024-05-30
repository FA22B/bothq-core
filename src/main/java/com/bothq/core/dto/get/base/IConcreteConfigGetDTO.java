package com.bothq.core.dto.get.base;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface IConcreteConfigGetDTO extends IConfigGetDTO {
    @JsonProperty("value")
    Object getValue();

    @JsonProperty("value")
    void setValue(Object value);
}
