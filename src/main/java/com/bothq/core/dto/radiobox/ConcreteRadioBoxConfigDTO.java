package com.bothq.core.dto.radiobox;

import com.bothq.core.dto.base.ConcreteGeneralConfigDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode(callSuper = true)
public class ConcreteRadioBoxConfigDTO extends ConcreteGeneralConfigDTO implements IConcreteRadioBoxConfigDTO {
    @Getter(onMethod_ = {@JsonProperty("elements")})
    @Setter(onMethod_ = {@JsonProperty("elements")})
    private List<?> elements;

    public ConcreteRadioBoxConfigDTO(String type, String uniqueID, String displayName, Object value, List<? extends Object> elements) {
        super(type, uniqueID, displayName, value);

        this.elements = elements;
    }
}
