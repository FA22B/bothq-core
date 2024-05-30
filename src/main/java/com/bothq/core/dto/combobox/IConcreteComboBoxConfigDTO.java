package com.bothq.core.dto.combobox;

import com.bothq.core.dto.base.IConcreteConfigDTO;

import java.util.List;

public interface IConcreteComboBoxConfigDTO extends IComboBoxConfigDTO, IConcreteConfigDTO {
    List<?> getElements();

    void setElements(List<?> elements);

}
