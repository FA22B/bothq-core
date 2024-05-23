package com.bothq.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class SliderConfigDTO extends GeneralConfigDTO {

    @Getter(onMethod_ = {@JsonProperty("minValue")})
    @Setter(onMethod_ = {@JsonProperty("minValue")})
    private float minValue;

    @Getter(onMethod_ = {@JsonProperty("maxValue")})
    @Setter(onMethod_ = {@JsonProperty("maxValue")})
    private float maxValue;

    @Getter(onMethod_ = {@JsonProperty("step")})
    @Setter(onMethod_ = {@JsonProperty("step")})
    private float step;

    public SliderConfigDTO(String type, String uniqueID, boolean enabled, String displayName, Object value, float minValue, float maxValue,
                           float step) {
        super(type, uniqueID, enabled, displayName, value);

        this.minValue = minValue;
        this.maxValue = maxValue;
        this.step = step;
    }
}
