package com.bothq.core.plugin.config.component.radiobox;

import com.bothq.core.dto.get.radiobox.ConcreteRadioBoxConfigGetDTO;
import com.bothq.core.plugin.config.component.base.BaseServerComponent;
import com.bothq.lib.plugin.config.component.IRadioBoxServer;

import java.util.List;

public class RadioBoxServer extends BaseServerComponent<String, IRadioBoxServer> implements IRadioBoxServer {
    private final List<String> elements;

    public RadioBoxServer(String uniqueId,
                          String displayName,
                          String pluginId,
                          String value,
                          List<String> elements) {
        super(uniqueId, displayName, pluginId, value);
        this.elements = elements;
    }



    @Override
    public ConcreteRadioBoxConfigGetDTO getConcreteConfigDTO() {
        return new ConcreteRadioBoxConfigGetDTO(
                "radiobox",
                uniqueId,
                displayName,
                getValue(),
                elements
        );
    }
}
