package com.bothq.core.dto.get.slider;

import com.bothq.core.dto.get.base.GeneralConfigGetDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class SliderConfigGetDTO extends GeneralConfigGetDTO implements com.bothq.core.dto.get.slider.ISliderConfigGetDTO {

    @Getter(onMethod_ = {@JsonProperty("minValue")})
    @Setter(onMethod_ = {@JsonProperty("minValue")})
    private float minValue;

    @Getter(onMethod_ = {@JsonProperty("maxValue")})
    @Setter(onMethod_ = {@JsonProperty("maxValue")})
    private float maxValue;

    @Getter(onMethod_ = {@JsonProperty("step")})
    @Setter(onMethod_ = {@JsonProperty("step")})
    private float step;

    public SliderConfigGetDTO(String type, String uniqueID, String displayName, float minValue, float maxValue,
                              float step) {
        super(type, uniqueID, displayName);

        this.minValue = minValue;
        this.maxValue = maxValue;
        this.step = step;
    }
}
