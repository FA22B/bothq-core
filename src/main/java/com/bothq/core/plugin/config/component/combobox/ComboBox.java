package com.bothq.core.plugin.config.component.combobox;

import com.bothq.core.dto.get.base.IConcreteConfigGetDTO;
import com.bothq.core.plugin.config.component.base.BaseComponent;
import com.bothq.core.service.PluginConfigurationService;
import com.bothq.lib.plugin.config.component.IComboBox;
import com.bothq.lib.plugin.config.component.IComboBoxServer;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ComboBox extends BaseComponent<String, IComboBoxServer> implements IComboBox {
    @Getter
    @Setter
    protected List<String> elements;

    public ComboBox(String uniqueId,
                    String displayName,
                    String pluginId,
                    String defaultValue,
                    List<String> elements) {
        super(uniqueId, displayName, pluginId, defaultValue);

        // TODO: Handle load
        this.elements = elements;
    }
    @Override
    public ComboBoxServer get(long serverId) {
        return new ComboBoxServer(
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
