package com.bothq.core.plugin.config.component.radiobox;

import com.bothq.core.dto.base.IConcreteConfigDTO;
import com.bothq.core.plugin.config.component.base.BaseComponent;
import com.bothq.core.service.PluginConfigurationService;
import com.bothq.lib.plugin.config.component.IRadioBox;
import com.bothq.lib.plugin.config.component.IRadioBoxServer;

import java.util.List;

public class RadioBox extends BaseComponent<String, IRadioBoxServer> implements IRadioBox {

    private List<String> elements;

    public RadioBox(String uniqueId,
                    String displayName,
                    String pluginId,
                    String defaultValue,
                    List<String> elements) {
        super(uniqueId, displayName, pluginId, defaultValue);
        this.elements = elements;
    }



    @Override
    public RadioBoxServer get(long serverId) {
        return new RadioBoxServer(
                uniqueId,
                displayName,
                pluginId,
                PluginConfigurationService
                        .getInstance()
                        .getConfigurationValue(serverId, pluginId, uniqueId, defaultValue),
                elements
        );
    }

    @Override
    public IConcreteConfigDTO getConcreteConfigDTO(long serverId) {
        return get(serverId).getConcreteConfigDTO();
    }
}
