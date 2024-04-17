package com.bothq.core.plugin.config.component;

import com.bothq.lib.plugin.config.component.ISlider;
import lombok.Getter;
import lombok.Setter;

public class Slider extends BaseComponent<Float> implements ISlider {
    @Getter
    @Setter
    protected float minValue;

    @Getter
    @Setter
    protected float maxValue;

    @Getter
    @Setter
    protected float step;

    public Slider(String uniqueId, String displayName, float minValue, float maxValue, float step, float defaultValue) {
        super(uniqueId, displayName);

        this.minValue = minValue;
        this.maxValue = maxValue;
        this.step = step;

        // TODO: Handle load
        this.setValue(defaultValue);
    }
}
