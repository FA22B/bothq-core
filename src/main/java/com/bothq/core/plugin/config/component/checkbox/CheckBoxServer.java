package com.bothq.core.plugin.config.component.checkbox;

import com.bothq.core.dto.get.base.ConcreteGeneralConfigGetDTO;
import com.bothq.core.dto.get.base.IConcreteConfigGetDTO;
import com.bothq.core.plugin.config.component.base.BaseServerComponent;
import com.bothq.lib.plugin.config.component.ICheckBoxServer;

public class CheckBoxServer extends BaseServerComponent<Boolean, ICheckBoxServer> implements ICheckBoxServer {

    public CheckBoxServer(String uniqueId, String displayName, String pluginId, Boolean value) {
        super(uniqueId, displayName, pluginId, value);
    }

    @Override
    public IConcreteConfigGetDTO getConcreteConfigDTO() {
        return new ConcreteGeneralConfigGetDTO(
                "checkbox",
                getUniqueId(),
                getDisplayName(),
                getValue()
        );
    }

    @Override
    public boolean isAssignable(Object value) {
        return value instanceof Boolean;
    }
}
