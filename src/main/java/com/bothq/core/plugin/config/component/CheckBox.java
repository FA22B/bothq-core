package com.bothq.core.plugin.config.component;

import com.bothq.lib.plugin.config.component.ICheckBox;

public class CheckBox extends BaseComponent<Boolean> implements ICheckBox {
    public CheckBox(String uniqueId, String displayName, boolean defaultValue) {
        super(uniqueId, displayName);

        // TODO: Load value
        this.setValue(defaultValue);
    }
}
