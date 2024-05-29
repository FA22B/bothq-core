package com.bothq.core.dto.combobox;

import com.bothq.core.dto.base.GeneralConfigDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode(callSuper = true)
public class ComboBoxConfigDTO extends GeneralConfigDTO implements IComboBoxConfigDTO {

    @Getter(onMethod_ = {@JsonProperty("selectedIndex")})
    @Setter(onMethod_ = {@JsonProperty("selectedIndex")})
    private int selectedIndex;

    @Getter(onMethod_ = {@JsonProperty("elements")})
    @Setter(onMethod_ = {@JsonProperty("elements")})
    private List<Object> elements;

    public ComboBoxConfigDTO(String type, String uniqueID, boolean enabled, String displayName, int selectedIndex, List<Object> elements) {
        super(type, uniqueID, enabled, displayName);

        this.selectedIndex = selectedIndex;
        this.elements = elements;
    }
}
