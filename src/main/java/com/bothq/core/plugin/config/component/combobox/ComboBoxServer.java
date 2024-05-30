package com.bothq.core.plugin.config.component.combobox;

import com.bothq.core.dto.get.combobox.ConcreteComboBoxConfigGetDTO;
import com.bothq.core.plugin.config.component.base.BaseServerComponent;
import com.bothq.lib.plugin.config.component.IComboBoxServer;
import lombok.Getter;

import java.util.List;

public class ComboBoxServer extends BaseServerComponent<String, IComboBoxServer> implements IComboBoxServer {
    @Getter
    protected final List<String> elements;

    public ComboBoxServer(String uniqueId,
                          String displayName,
                          String pluginId,
                          String value,
                          List<String> elements) {
        super(uniqueId, displayName, pluginId, value);

        this.elements = elements;
    }


    public ConcreteComboBoxConfigGetDTO getConcreteConfigDTO() {
        return new ConcreteComboBoxConfigGetDTO(
                "combobox",
                uniqueId,
                displayName,
                getValue(),
                elements
        );
    }
}
