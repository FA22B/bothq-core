package com.bothq.core.plugin.config;

import com.bothq.core.plugin.config.component.*;
import com.bothq.lib.plugin.config.IConfigGroup;
import com.bothq.lib.plugin.config.IConfigurable;
import com.bothq.lib.plugin.config.component.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class ConfigGroup implements IConfigGroup {
    @Getter
    protected final String uniqueId;

    @Getter
    protected boolean enabled = true;

    @Getter
    @Setter
    protected String displayName;

    @Getter
    protected final List<IConfigurable> children;

    public ConfigGroup(String uniqueId, String displayName) {
        this.uniqueId = uniqueId;
        this.displayName = displayName;

        children = new ArrayList<>();
    }

    @Override
    public void enable() {
        enabled = true;
    }

    @Override
    public void disable() {
        enabled = false;
    }

    @Override
    public ICheckBox addCheckBox(String uniqueId, String displayName, boolean defaultValue) {
        var checkBox = new CheckBox(uniqueId, displayName, defaultValue);
        children.add(checkBox);
        return checkBox;
    }

    @Override
    public <T> IComboBox<T> addComboBox(String uniqueId, String displayName, List<T> elements, int defaultIndex) {
        var comboBox = new ComboBox<>(uniqueId, displayName, elements, defaultIndex);
        children.add(comboBox);
        return comboBox;
    }

    @Override
    public IRadioBox addRadioBox(String uniqueId, String displayName, boolean defaultValue) {
        var radioBox = new RadioBox(uniqueId, displayName, defaultValue, this);
        children.add(radioBox);
        return radioBox;
    }

    @Override
    public ISlider addSlider(String uniqueId, String displayName, float minValue, float maxValue, float step, float defaultValue) {
        var slider = new Slider(uniqueId, displayName, minValue, maxValue, step, defaultValue);
        children.add(slider);
        return slider;
    }

    @Override
    public ITextBox addTextBox(String uniqueId, String displayName, String defaultValue) {
        var textBox = new TextBox(uniqueId, displayName, defaultValue);
        children.add(textBox);
        return textBox;
    }
}
