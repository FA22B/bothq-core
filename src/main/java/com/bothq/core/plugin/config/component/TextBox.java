package com.bothq.core.plugin.config.component;

import com.bothq.lib.plugin.config.component.ITextBox;
import com.bothq.lib.plugin.config.component.ITextBoxServer;

public class TextBox extends BaseComponent<String, ITextBoxServer> implements ITextBox, ITextBoxServer {
    public TextBox(String uniqueId, String displayName, String defaultValue) {
        super(uniqueId, displayName);

        // TODO: Handle load
        this.setValue(defaultValue);
    }
}
