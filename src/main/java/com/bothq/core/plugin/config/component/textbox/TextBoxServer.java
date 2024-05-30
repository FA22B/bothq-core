package com.bothq.core.plugin.config.component.textbox;

import com.bothq.core.dto.base.ConcreteGeneralConfigDTO;
import com.bothq.core.plugin.config.component.base.BaseServerComponent;
import com.bothq.lib.plugin.config.component.ITextBoxServer;

public class TextBoxServer extends BaseServerComponent<String, ITextBoxServer> implements ITextBoxServer {
    public TextBoxServer(String uniqueId, String displayName, String pluginId, String value) {
        super(uniqueId, displayName, pluginId, value);
    }

    @Override
    public ConcreteGeneralConfigDTO getConcreteConfigDTO() {
        return new ConcreteGeneralConfigDTO("textbox", uniqueId, displayName, value);
    }
}
