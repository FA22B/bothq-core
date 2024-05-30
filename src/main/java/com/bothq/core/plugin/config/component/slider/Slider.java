package com.bothq.core.plugin.config.component.slider;

import com.bothq.core.dto.get.slider.ConcreteSliderConfigGetDTO;
import com.bothq.core.plugin.config.component.base.BaseComponent;
import com.bothq.core.service.PluginConfigurationService;
import com.bothq.lib.plugin.config.component.ISlider;
import com.bothq.lib.plugin.config.component.ISliderServer;
import lombok.Getter;
import lombok.Setter;

public class Slider extends BaseComponent<Float, ISliderServer> implements ISlider {
    @Getter
    @Setter
    protected float minValue;

    @Getter
    @Setter
    protected float maxValue;

    @Getter
    @Setter
    protected float step;

    public Slider(String uniqueId, String displayName, String pluginId, float minValue, float maxValue, float step, float defaultValue) {
        super(uniqueId, displayName, pluginId, defaultValue);

        this.minValue = minValue;
        this.maxValue = maxValue;
        this.step = step;
    }

    @Override
    public SliderServer get(long serverId) {
        return new SliderServer(
                uniqueId,
                displayName,
                pluginId,
                PluginConfigurationService
                        .getInstance()
                        .getConfigurationValue(serverId, pluginId, uniqueId, defaultValue),
                minValue,
                maxValue,
                step
        );
    }

    @Override
    public ConcreteSliderConfigGetDTO getConcreteConfigDTO(long serverId) {
        return get(serverId)
                .getConcreteConfigDTO();
    }
}
