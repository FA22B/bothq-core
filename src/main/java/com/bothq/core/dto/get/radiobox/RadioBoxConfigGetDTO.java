package com.bothq.core.dto.get.radiobox;

import com.bothq.core.dto.get.base.GeneralConfigGetDTO;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class RadioBoxConfigGetDTO extends GeneralConfigGetDTO implements IRadioBoxConfigGetDTO {
    public RadioBoxConfigGetDTO(String type, String uniqueID, String displayName) {
        super(type, uniqueID, displayName);
    }
}
