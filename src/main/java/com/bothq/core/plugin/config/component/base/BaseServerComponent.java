package com.bothq.core.plugin.config.component.base;

import com.bothq.core.dto.base.IConcreteConfigDTO;
import com.bothq.lib.plugin.config.component.IComponent;
import lombok.Getter;
import lombok.Setter;

public abstract class BaseServerComponent<T, V extends IComponent<T>> implements IComponent<T> {
    @Getter
    protected final String uniqueId;

    @Getter
    @Setter
    protected String displayName;

    @Getter
    @Setter
    protected T value;

    protected String pluginId;


    public BaseServerComponent(String uniqueId, String displayName, String pluginId, T value) {
        this.uniqueId = uniqueId;
        this.displayName = displayName;
        this.pluginId = pluginId;
        this.value = value;
    }

    public abstract IConcreteConfigDTO getConcreteConfigDTO();
}
