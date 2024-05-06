package com.bothq.core.plugin.config.component;

import com.bothq.lib.plugin.config.component.ITextBox;

public class TextBox extends BaseComponent<String> implements ITextBox {
    public TextBox(String uniqueId, String displayName, String defaultValue) {
        super(uniqueId, displayName);

        // TODO: Handle load
        this.setValue(defaultValue);
    }
}
