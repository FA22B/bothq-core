package com.bothq.core.dto.radiobox;

import com.bothq.core.dto.base.IConcreteConfigDTO;

import java.util.List;

public interface IConcreteRadioBoxConfigDTO extends IRadioBoxConfigDTO, IConcreteConfigDTO {
    List<?> getElements();

    void setElements(List<?> elements);

}
