package com.bothq.core.plugin.config.component.checkbox;

import com.bothq.core.dto.get.base.IConcreteConfigGetDTO;
import com.bothq.core.plugin.config.component.base.BaseComponent;
import com.bothq.core.service.PluginConfigurationService;
import com.bothq.lib.plugin.config.component.ICheckBox;
import com.bothq.lib.plugin.config.component.ICheckBoxServer;

public class CheckBox extends BaseComponent<Boolean, ICheckBoxServer> implements ICheckBox {
    public CheckBox(String uniqueId, String displayName, String pluginId, boolean defaultValue) {
        super(uniqueId, displayName, pluginId, defaultValue);
    }

    @Override
    public CheckBoxServer get(long serverId) {
        return new CheckBoxServer(
                getUniqueId(),
                getDisplayName(),
                pluginId,
                PluginConfigurationService
                        .getInstance()
                        .getConfigurationValue(serverId, pluginId, uniqueId, defaultValue)
        );
    }

    @Override
    public IConcreteConfigGetDTO getConcreteConfigDTO(long serverId) {
        return get(serverId).getConcreteConfigDTO();
    }
}
