package com.bothq.core.dto.get.radiobox;

import com.bothq.core.dto.get.base.IConcreteConfigGetDTO;

import java.util.List;

public interface IConcreteRadioBoxConfigGetDTO extends IRadioBoxConfigGetDTO, IConcreteConfigGetDTO {
    List<?> getElements();

    void setElements(List<?> elements);

}
