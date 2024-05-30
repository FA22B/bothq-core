package com.bothq.core.dto.combobox;

import com.bothq.core.dto.base.ConcreteGeneralConfigDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode(callSuper = true)
public class ConcreteComboBoxConfigDTO extends ConcreteGeneralConfigDTO implements IConcreteComboBoxConfigDTO {
    @Getter(onMethod_ = {@JsonProperty("elements")})
    @Setter(onMethod_ = {@JsonProperty("elements")})
    private List<? extends Object> elements;

    public ConcreteComboBoxConfigDTO(String type, String uniqueID, String displayName, Object value, List<? extends Object> elements) {
        super(type, uniqueID, displayName, value);

        this.elements = elements;
    }
}
