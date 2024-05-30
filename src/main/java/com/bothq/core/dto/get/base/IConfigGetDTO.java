package com.bothq.core.dto.get.base;

public interface IConfigGetDTO {
    String getType();

    String getUniqueID();

    String getDisplayName();

    void setType(String type);

    void setUniqueID(String uniqueID);

    void setDisplayName(String displayName);
}
