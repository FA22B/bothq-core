package com.bothq.core.plugin.config.component;

import com.bothq.core.dto.base.ConcreteGeneralConfigDTO;
import com.bothq.core.service.PluginConfigurationService;
import com.bothq.lib.plugin.config.component.ICheckBox;
import com.bothq.lib.plugin.config.component.ICheckBoxServer;

public class CheckBox extends BaseComponent<Boolean, ICheckBoxServer> implements ICheckBox, ICheckBoxServer {
    public CheckBox(String uniqueId, String displayName, String pluginId, boolean defaultValue) {
        super(uniqueId, displayName, pluginId, defaultValue);
    }

    @Override
    public ICheckBoxServer get(long serverId) {
        setValue(PluginConfigurationService.getInstance().getConfigurationValue(serverId, pluginId, uniqueId, defaultValue));
        return this;
    }

    @Override
    public ConcreteGeneralConfigDTO getConcreteConfigDTO(long serverId) {
        // Refresh value
        get(serverId);

        return new ConcreteGeneralConfigDTO("checkbox", uniqueId, enabled, displayName, value);
    }
}
