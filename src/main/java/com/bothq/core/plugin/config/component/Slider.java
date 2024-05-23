package com.bothq.core.plugin.config.component;

import com.bothq.core.dto.GeneralConfigDTO;
import com.bothq.core.dto.SliderConfigDTO;
import com.bothq.core.service.PluginConfigurationService;
import com.bothq.lib.plugin.config.component.ISlider;
import com.bothq.lib.plugin.config.component.ISliderServer;
import lombok.Getter;
import lombok.Setter;

public class Slider extends BaseComponent<Float, ISliderServer> implements ISlider, ISliderServer {
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
    public ISliderServer get(long serverId) {
        setValue(PluginConfigurationService.getInstance().getConfigurationValue(serverId, pluginId, uniqueId, defaultValue));
        return this;
    }

    @Override
    public GeneralConfigDTO getGeneralConfigDTO(long serverId) {
        // Refresh value
        get(serverId);

        return new SliderConfigDTO("slider", uniqueId, enabled, displayName, value, minValue, maxValue, step);
    }
}
