package com.bothq.core.dto.combobox;

import com.bothq.core.dto.base.GeneralConfigDTO;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class ComboBoxConfigDTO extends GeneralConfigDTO implements IComboBoxConfigDTO {
    public ComboBoxConfigDTO(String type, String uniqueID, String displayName) {
        super(type, uniqueID, displayName);
    }
}
