package com.bothq.core.plugin.config.component.textbox;

import com.bothq.core.dto.get.base.ConcreteGeneralConfigGetDTO;
import com.bothq.core.plugin.config.component.base.BaseComponent;
import com.bothq.core.service.PluginConfigurationService;
import com.bothq.lib.plugin.config.component.ITextBox;
import com.bothq.lib.plugin.config.component.ITextBoxServer;

public class TextBox extends BaseComponent<String, ITextBoxServer> implements ITextBox {
    public TextBox(String uniqueId, String displayName, String pluginId, String defaultValue) {
        super(uniqueId, displayName, pluginId, defaultValue);
    }

    @Override
    public TextBoxServer get(long serverId) {
        return new TextBoxServer(
                uniqueId,
                displayName,
                pluginId,
                PluginConfigurationService
                        .getInstance()
                        .getConfigurationValue(serverId, pluginId, uniqueId, defaultValue)
        );
    }

    @Override
    public ConcreteGeneralConfigGetDTO getConcreteConfigDTO(long serverId) {
        return get(serverId).getConcreteConfigDTO();
    }
}
