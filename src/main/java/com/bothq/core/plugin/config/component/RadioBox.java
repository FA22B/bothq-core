package com.bothq.core.plugin.config.component;

import com.bothq.core.plugin.config.ConfigGroup;
import com.bothq.lib.plugin.config.component.IRadioBox;
import com.bothq.lib.plugin.config.component.IRadioBoxServer;

public class RadioBox extends BaseComponent<Boolean, IRadioBoxServer> implements IRadioBox, IRadioBoxServer {
    protected ConfigGroup parentGroup;

    public RadioBox(String uniqueId, String displayName, boolean defaultValue, ConfigGroup parentGroup) {
        super(uniqueId, displayName);

        this.parentGroup = parentGroup;

        // TODO: Load value
        this.setValue(defaultValue);
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
}
