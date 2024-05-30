package com.bothq.core.dto.get.group;

import com.bothq.core.dto.get.base.ConcreteGeneralConfigGetDTO;
import com.bothq.core.dto.get.base.IConfigGetDTO;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode(callSuper = true)
public class GroupConfigGetDTO extends ConcreteGeneralConfigGetDTO {

    public GroupConfigGetDTO(String type, String uniqueID, String displayName, List<IConfigGetDTO> value) {
        super(type, uniqueID, displayName, value);
    }
}
