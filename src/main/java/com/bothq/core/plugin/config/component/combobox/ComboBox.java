package com.bothq.core.plugin.config.component.combobox;

import com.bothq.core.dto.get.base.IConcreteConfigGetDTO;
import com.bothq.core.plugin.config.component.base.BaseComponent;
import com.bothq.core.service.PluginConfigurationService;
import com.bothq.lib.plugin.config.component.IComboBox;
import com.bothq.lib.plugin.config.component.IComboBoxServer;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ComboBox<T> extends BaseComponent<T, IComboBoxServer<T>> implements IComboBox<T> {
    protected int selectedIndex;

    @Getter
    @Setter
    protected List<T> elements;

    public ComboBox(String uniqueId,
                    String displayName,
                    String pluginId,
                    T defaultValue,
                    List<T> elements) {
        super(uniqueId, displayName, pluginId, defaultValue);

        // TODO: Handle load
        this.elements = elements;
    }
    @Override
    public ComboBoxServer<T> get(long serverId) {
        return new ComboBoxServer<T>(
                getUniqueId(),
                getDisplayName(),
                pluginId,
                PluginConfigurationService
                        .getInstance()
                        .getConfigurationValue(serverId, pluginId, uniqueId, defaultValue),
                elements
        );
    }

    @Override
    public IConcreteConfigGetDTO getConcreteConfigDTO(long serverId) {
        return get(serverId).getConcreteConfigDTO();
    }
}
