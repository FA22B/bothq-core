package com.bothq.core.plugin.config.component;

import com.bothq.lib.plugin.config.component.ICheckBox;
import com.bothq.lib.plugin.config.component.ICheckBoxServer;

public class CheckBox extends BaseComponent<Boolean, ICheckBoxServer> implements ICheckBox, ICheckBoxServer {
    public CheckBox(String uniqueId, String displayName, boolean defaultValue) {
        super(uniqueId, displayName);

        // TODO: Load value
        this.setValue(defaultValue);
    }
}
