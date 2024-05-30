package com.bothq.core.plugin.config.component.base;

import com.bothq.core.dto.get.base.IConcreteConfigGetDTO;
import com.bothq.lib.plugin.config.IConfigurable;
import com.bothq.lib.plugin.config.component.IComponent;
import com.bothq.lib.plugin.config.component.IUnselectedServerComponent;
import lombok.Getter;
import lombok.Setter;

public abstract class BaseComponent<T, V extends IComponent<T>> implements IUnselectedServerComponent<V>, IConfigurable {
    @Getter
    protected final String uniqueId;

    @Getter
    @Setter
    protected String displayName;

    protected String pluginId;

    @Getter
    protected T defaultValue;

    public BaseComponent(String uniqueId, String displayName, String pluginId, T defaultValue) {
        this.uniqueId = uniqueId;
        this.displayName = displayName;
        this.pluginId = pluginId;
        this.defaultValue = defaultValue;
    }

    public abstract IConcreteConfigGetDTO getConcreteConfigDTO(long serverId);
}
