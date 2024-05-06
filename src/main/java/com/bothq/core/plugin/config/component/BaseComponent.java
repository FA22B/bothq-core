package com.bothq.core.plugin.config.component;

import com.bothq.lib.plugin.config.component.IComponent;
import lombok.Getter;
import lombok.Setter;

public abstract class BaseComponent<T> implements IComponent<T> {
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

    public BaseComponent(String uniqueId, String displayName) {
        this.uniqueId = uniqueId;
        this.displayName = displayName;
    }

    @Override
    public void enable() {
        enabled = true;
    }

    @Override
    public void disable() {
        enabled = false;
    }
}
