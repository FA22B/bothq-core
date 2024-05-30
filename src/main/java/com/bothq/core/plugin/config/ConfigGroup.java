package com.bothq.core.plugin.config;

import com.bothq.core.plugin.config.component.checkbox.CheckBox;
import com.bothq.core.plugin.config.component.combobox.ComboBox;
import com.bothq.core.plugin.config.component.radiobox.RadioBox;
import com.bothq.core.plugin.config.component.slider.Slider;
import com.bothq.core.plugin.config.component.textbox.TextBox;
import com.bothq.lib.plugin.config.IConfigGroup;
import com.bothq.lib.plugin.config.IConfigurable;
import com.bothq.lib.plugin.config.component.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfigGroup implements IConfigGroup {
    @Getter
    protected final String uniqueId;


    @Getter
    @Setter
    protected String displayName;

    @Getter
    protected final List<IConfigurable> children;

    @Getter
    protected final String pluginId;

    protected final ConfigGroup parent;

    public ConfigGroup(String uniqueId, String displayName, String pluginId, ConfigGroup parent) {
        this.uniqueId = uniqueId;
        this.displayName = displayName;
        this.pluginId = pluginId;

        children = new ArrayList<>();

        this.parent = parent;
    }

    @Override
    public ICheckBox addCheckBox(String uniqueId, String displayName, boolean defaultValue) {
        var newUniqueId = createUniqueId(uniqueId);
        Assert.isTrue(isUniqueIdUnique(newUniqueId), "Unique ID was already set!");

        CheckBox checkBox = new CheckBox(newUniqueId, displayName, pluginId, defaultValue);
        children.add(checkBox);
        return checkBox;
    }

    @Override
    public <T> IComboBox<T> addComboBox(String uniqueId, String displayName, List<T> elements, T defaultValue) {
        var newUniqueId = createUniqueId(uniqueId);
        Assert.isTrue(isUniqueIdUnique(newUniqueId), "Unique ID was already set!");

        var comboBox = new ComboBox<>(newUniqueId, displayName, pluginId, defaultValue, elements);
        children.add(comboBox);
        return comboBox;
    }

    @Override
    public IRadioBox addRadioBox(String uniqueId, String displayName, String defaultValue, List<String> options) {
        var newUniqueId = createUniqueId(uniqueId);
        Assert.isTrue(isUniqueIdUnique(newUniqueId), "Unique ID was already set!");

        var radioBox = new RadioBox(newUniqueId, displayName, pluginId, defaultValue, options);
        children.add(radioBox);
        return radioBox;
    }

    @Override
    public ISlider addSlider(String uniqueId, String displayName, float minValue, float maxValue, float step, float defaultValue) {
        var newUniqueId = createUniqueId(uniqueId);
        Assert.isTrue(isUniqueIdUnique(newUniqueId), "Unique ID was already set!");

        var slider = new Slider(newUniqueId, displayName, pluginId, minValue, maxValue, step, defaultValue);
        children.add(slider);
        return slider;
    }

    @Override
    public ITextBox addTextBox(String uniqueId, String displayName, String defaultValue) {
        var newUniqueId = createUniqueId(uniqueId);
        Assert.isTrue(isUniqueIdUnique(newUniqueId), "Unique ID was already set!");

        var textBox = new TextBox(newUniqueId, displayName, pluginId, defaultValue);
        children.add(textBox);
        return textBox;
    }

    protected String createUniqueId(String input) {
        return parent == null ? input : String.format("%s.%s", parent.getUniqueId(), input);
    }

    protected boolean isUniqueIdUnique(String input) {
        var rootGroup = parent != null ? parent : this;

        for (var item : rootGroup.getChildren()) {
            if (Objects.equals(item.getUniqueId(), input)) {
                return false;
            }

            if (item instanceof ConfigGroup subGroup) {
                for (var subItem : subGroup.getChildren()) {
                    if (Objects.equals(subItem.getUniqueId(), input)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
