package com.bothq.core.dto;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class GroupConfigDTO extends GeneralConfigDTO {

    public GroupConfigDTO(String type, String uniqueID, boolean enabled, String displayName, Object value) {
        super(type, uniqueID, enabled, displayName, value);
    }

    public void fetchAndApplyDatabaseValue()
}
