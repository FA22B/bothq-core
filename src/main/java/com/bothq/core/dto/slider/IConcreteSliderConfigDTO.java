package com.bothq.core.dto.slider;

import com.bothq.core.dto.base.IConcreteConfigDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface IConcreteSliderConfigDTO extends IConcreteConfigDTO {
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
