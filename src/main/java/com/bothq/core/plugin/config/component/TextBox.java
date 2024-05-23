package com.bothq.core.plugin.config.component;

import com.bothq.core.dto.GeneralConfigDTO;
import com.bothq.core.service.PluginConfigurationService;
import com.bothq.lib.plugin.config.component.ITextBox;
import com.bothq.lib.plugin.config.component.ITextBoxServer;

public class TextBox extends BaseComponent<String, ITextBoxServer> implements ITextBox, ITextBoxServer {
    public TextBox(String uniqueId, String displayName, String pluginId, String defaultValue) {
        super(uniqueId, displayName, pluginId, defaultValue);
    }

    @Override
    public ITextBoxServer get(long serverId) {
        setValue(PluginConfigurationService.getInstance().getConfigurationValue(serverId, pluginId, uniqueId, defaultValue));
        return this;
    }

    @Override
    public GeneralConfigDTO getGeneralConfigDTO(long serverId) {
        // Refresh value
        get(serverId);

        return new GeneralConfigDTO("textbox", uniqueId, enabled, displayName, value);
    }
}
