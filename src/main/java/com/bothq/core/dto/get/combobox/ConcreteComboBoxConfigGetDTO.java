package com.bothq.core.dto.get.combobox;

import com.bothq.core.dto.get.base.ConcreteGeneralConfigGetDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode(callSuper = true)
public class ConcreteComboBoxConfigGetDTO extends ConcreteGeneralConfigGetDTO implements IConcreteComboBoxConfigGetDTO {
    @Getter(onMethod_ = {@JsonProperty("elements")})
    @Setter(onMethod_ = {@JsonProperty("elements")})
    private List<? extends Object> elements;

    public ConcreteComboBoxConfigGetDTO(String type, String uniqueID, String displayName, Object value, List<? extends Object> elements) {
        super(type, uniqueID, displayName, value);

        this.elements = elements;
    }
}
