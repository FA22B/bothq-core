package com.bothq.core.dto.get.combobox;

import com.bothq.core.dto.get.base.GeneralConfigGetDTO;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class ComboBoxConfigGetDTO extends GeneralConfigGetDTO implements IComboBoxConfigGetDTO {
    public ComboBoxConfigGetDTO(String type, String uniqueID, String displayName) {
        super(type, uniqueID, displayName);
    }
}
