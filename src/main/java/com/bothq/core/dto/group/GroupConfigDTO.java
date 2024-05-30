package com.bothq.core.dto.group;

import com.bothq.core.dto.base.ConcreteGeneralConfigDTO;
import com.bothq.core.dto.base.IConfigDTO;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode(callSuper = true)
public class GroupConfigDTO extends ConcreteGeneralConfigDTO {

    public GroupConfigDTO(String type, String uniqueID, String displayName, List<IConfigDTO> value) {
        super(type, uniqueID, displayName, value);
    }
}
