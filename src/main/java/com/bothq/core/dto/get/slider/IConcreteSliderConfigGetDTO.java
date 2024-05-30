package com.bothq.core.dto.get.slider;

import com.bothq.core.dto.get.base.IConcreteConfigGetDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface IConcreteSliderConfigGetDTO extends IConcreteConfigGetDTO {
    @JsonProperty("minValue")
    float getMinValue();

    @JsonProperty("maxValue")
    float getMaxValue();

    @JsonProperty("step")
    float getStep();


    @JsonProperty("minValue")
    void setMinValue(float minValue);

    @JsonProperty("maxValue")
    void setMaxValue(float maxValue);

    @JsonProperty("step")
    void setStep(float step);
}
