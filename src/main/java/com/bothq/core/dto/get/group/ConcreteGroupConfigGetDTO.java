package com.bothq.core.dto.get.group;

import com.bothq.core.dto.get.base.ConcreteGeneralConfigGetDTO;
import com.bothq.core.dto.get.base.IConcreteConfigGetDTO;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode(callSuper = true)
public class ConcreteGroupConfigGetDTO extends ConcreteGeneralConfigGetDTO {

    public ConcreteGroupConfigGetDTO(String type,
                                     String uniqueID,
                                     String displayName,
                                     List<IConcreteConfigGetDTO> value) {
        super(type, uniqueID, displayName, value);
    }
}
