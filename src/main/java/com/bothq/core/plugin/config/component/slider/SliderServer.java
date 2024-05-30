package com.bothq.core.plugin.config.component.slider;

import com.bothq.core.dto.get.slider.ConcreteSliderConfigGetDTO;
import com.bothq.core.plugin.config.component.base.BaseServerComponent;
import com.bothq.lib.plugin.config.component.ISliderServer;
import lombok.Getter;
import lombok.Setter;

public class SliderServer extends BaseServerComponent<Float, ISliderServer> implements ISliderServer {
    @Getter
    @Setter
    protected float minValue;

    @Getter
    @Setter
    protected float maxValue;

    @Getter
    @Setter
    protected float step;

    public SliderServer(String uniqueId, String displayName, String pluginId, float value, float minValue, float maxValue, float step) {
        super(uniqueId, displayName, pluginId, value);

        this.minValue = minValue;
        this.maxValue = maxValue;
        this.step = step;
    }


    @Override
    public ConcreteSliderConfigGetDTO getConcreteConfigDTO() {
        return new ConcreteSliderConfigGetDTO(
                "slider",
                uniqueId,
                displayName,
                value,
                minValue,
                maxValue,
                step);
    }

    @Override
    public boolean isAssignable(Object value) {
        return (value instanceof Float num)
                && (minValue < num)
                && (num < maxValue);
    }
}
