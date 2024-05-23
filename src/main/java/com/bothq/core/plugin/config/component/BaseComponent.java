package com.bothq.core.plugin.config.component;

import com.bothq.core.dto.GeneralConfigDTO;
import com.bothq.lib.plugin.config.component.IComponent;
import com.bothq.lib.plugin.config.component.IUnselectedServerComponent;
import lombok.Getter;
import lombok.Setter;

public abstract class BaseComponent<T, V extends IComponent<?>> implements IComponent<T>, IUnselectedServerComponent<V> {
    @Getter
    protected final String uniqueId;

    @Getter
    @Setter
    protected String displayName;

    @Getter
    @Setter
    protected T value;

    @Getter
    protected boolean enabled = true;

    protected String pluginId;

    @Getter
    protected T defaultValue;

    public BaseComponent(String uniqueId, String displayName, String pluginId, T defaultValue) {
        this.uniqueId = uniqueId;
        this.displayName = displayName;
        this.pluginId = pluginId;
        this.defaultValue = defaultValue;
    }

    @Override
    public void enable() {
        enabled = true;
    }

    @Override
    public void disable() {
        enabled = false;
    }

    public abstract GeneralConfigDTO getGeneralConfigDTO(long serverId);
}
