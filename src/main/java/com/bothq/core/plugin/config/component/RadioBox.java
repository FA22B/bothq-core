package com.bothq.core.plugin.config.component;

import com.bothq.core.plugin.config.ConfigGroup;
import com.bothq.core.service.PluginConfigurationService;
import com.bothq.lib.plugin.config.component.IRadioBox;
import com.bothq.lib.plugin.config.component.IRadioBoxServer;

public class RadioBox extends BaseComponent<Boolean, IRadioBoxServer> implements IRadioBox, IRadioBoxServer {
    protected ConfigGroup parentGroup;

    public RadioBox(String uniqueId, String displayName, String pluginId, boolean defaultValue, ConfigGroup parentGroup) {
        super(uniqueId, displayName, pluginId, defaultValue);

        this.parentGroup = parentGroup;
    }

    @Override
    public void setValue(Boolean value) {
        super.setValue(value);

        if (value) {
            // Set all other radio boxes to false
            for (var component : parentGroup.getChildren()) {
                if (component instanceof RadioBox radioBox) {
                    radioBox.setValue(false);
                }
            }
        }
    }

    @Override
    public IRadioBoxServer get(long serverId) {
        setValue(PluginConfigurationService.getInstance().getConfigurationValue(serverId, pluginId, uniqueId, defaultValue));
        return this;
    }
}
