package com.bothq.core.plugin.config.component;

import com.bothq.core.dto.combobox.ConcreteComboBoxConfigDTO;
import com.bothq.core.service.PluginConfigurationService;
import com.bothq.lib.plugin.config.component.IComboBox;
import com.bothq.lib.plugin.config.component.IComboBoxServer;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ComboBox<T> extends BaseComponent<T, IComboBoxServer<T>> implements IComboBox<T>, IComboBoxServer<T> {
    protected int selectedIndex;

    @Getter
    protected final List<T> elements;

    public ComboBox(String uniqueId, String displayName, String pluginId, T defaultValue, List<T> elements, int defaultIndex) {
        super(uniqueId, displayName, pluginId, defaultValue);

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

    @Override
    public IComboBoxServer<T> get(long serverId) {
        setValue(PluginConfigurationService.getInstance().getConfigurationValue(serverId, pluginId, uniqueId, defaultValue));
        return this;
    }

    @Override
    public ConcreteComboBoxConfigDTO getConcreteConfigDTO(long serverId) {
        // Refresh value
        get(serverId);

        return new ConcreteComboBoxConfigDTO("combobox", uniqueId, enabled, displayName, value, selectedIndex, castToListObject(elements));
    }

    private static <T> List<Object> castToListObject(List<T> list) {
        return list.stream()
                .map(Object.class::cast)
                .collect(Collectors.toList());
    }
}
