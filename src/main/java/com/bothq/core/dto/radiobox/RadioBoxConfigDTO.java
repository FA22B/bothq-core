package com.bothq.core.dto.radiobox;

import com.bothq.core.dto.base.GeneralConfigDTO;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class RadioBoxConfigDTO extends GeneralConfigDTO implements IRadioBoxConfigDTO {
    public RadioBoxConfigDTO(String type, String uniqueID, String displayName) {
        super(type, uniqueID, displayName);
    }
}
