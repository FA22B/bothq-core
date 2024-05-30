package com.bothq.core.plugin.config.component.combobox;

import com.bothq.core.dto.combobox.ConcreteComboBoxConfigDTO;
import com.bothq.core.plugin.config.component.base.BaseServerComponent;
import com.bothq.lib.plugin.config.component.IComboBoxServer;
import lombok.Getter;

import java.util.List;

public class ComboBoxServer<T> extends BaseServerComponent<T, IComboBoxServer<T>> implements IComboBoxServer<T> {
    @Getter
    protected final List<T> elements;

    public ComboBoxServer(String uniqueId,
                          String displayName,
                          String pluginId,
                          T value,
                          List<T> elements) {
        super(uniqueId, displayName, pluginId, value);

        this.elements = elements;
    }


    public ConcreteComboBoxConfigDTO getConcreteConfigDTO() {
        return new ConcreteComboBoxConfigDTO(
                "combobox",
                uniqueId,
                displayName,
                getValue(),
                elements
        );
    }
}
