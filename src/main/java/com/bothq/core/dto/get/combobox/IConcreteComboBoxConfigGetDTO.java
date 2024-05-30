package com.bothq.core.dto.get.combobox;

import com.bothq.core.dto.get.base.IConcreteConfigGetDTO;

import java.util.List;

public interface IConcreteComboBoxConfigGetDTO extends IComboBoxConfigGetDTO, IConcreteConfigGetDTO {
    List<?> getElements();

    void setElements(List<?> elements);

}
