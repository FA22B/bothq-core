package com.bothq.core.plugin.config.component;

import com.bothq.lib.plugin.config.component.IComboBox;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ComboBox<T> extends BaseComponent<T> implements IComboBox<T> {
    protected int selectedIndex;

    @Getter
    protected final List<T> elements;

    public ComboBox(String uniqueId, String displayName, List<T> elements, int defaultIndex) {
        super(uniqueId, displayName);

        // TODO: Handle load
        this.elements = new ArrayList<>();
        this.setSelectedIndex(defaultIndex);
    }

    @Override
    public int getSelectedIndex() {
        return selectedIndex;
    }

    @Override
    public void setSelectedIndex(int selectedIndex) {
        // TODO: Validate index?
        this.selectedIndex = selectedIndex;
    }
}
