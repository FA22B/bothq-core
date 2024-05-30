package com.bothq.core.plugin.config.component.checkbox;

import com.bothq.core.dto.base.ConcreteGeneralConfigDTO;
import com.bothq.core.dto.base.IConcreteConfigDTO;
import com.bothq.core.plugin.config.component.base.BaseServerComponent;
import com.bothq.lib.plugin.config.component.ICheckBoxServer;

public class CheckBoxServer extends BaseServerComponent<Boolean, ICheckBoxServer> implements ICheckBoxServer {

    public CheckBoxServer(String uniqueId, String displayName, String pluginId, Boolean value) {
        super(uniqueId, displayName, pluginId, value);
    }

    @Override
    public IConcreteConfigDTO getConcreteConfigDTO() {
        return new ConcreteGeneralConfigDTO(
                "checkbox",
                getUniqueId(),
                getDisplayName(),
                getValue()
        );
    }
}
