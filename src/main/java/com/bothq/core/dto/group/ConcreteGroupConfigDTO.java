package com.bothq.core.dto.group;

import com.bothq.core.dto.base.ConcreteGeneralConfigDTO;
import com.bothq.core.dto.base.IConcreteConfigDTO;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode(callSuper = true)
public class ConcreteGroupConfigDTO extends ConcreteGeneralConfigDTO {

    public ConcreteGroupConfigDTO(String type,
                                  String uniqueID,
                                  boolean enabled,
                                  String displayName,
                                  List<? extends IConcreteConfigDTO> value) {
        super(type, uniqueID, enabled, displayName, value);
    }
}
