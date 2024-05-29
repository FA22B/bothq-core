package com.bothq.core.dto.base;

public interface IConfigDTO {
    String getType();

    String getUniqueID();

    boolean isEnabled();

    String getDisplayName();

    void setType(String type);

    void setUniqueID(String uniqueID);

    void setEnabled(boolean enabled);

    void setDisplayName(String displayName);
}
