package com.bothq.core.dto.base;

public interface IConfigDTO {
    String getType();

    String getUniqueID();

    String getDisplayName();

    void setType(String type);

    void setUniqueID(String uniqueID);

    void setDisplayName(String displayName);
}
