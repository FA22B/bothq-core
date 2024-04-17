package com.bothq.core.plugin.config;

import com.bothq.lib.plugin.config.IConfig;
import com.bothq.lib.plugin.config.IConfigGroup;

public class Config extends ConfigGroup implements IConfig {
    public Config(String uniqueId, String displayName) {
        super(uniqueId, displayName);
    }

    @Override
    public IConfigGroup addConfigGroup(String uniqueId, String displayName) {
        var configGroup = new ConfigGroup(uniqueId, displayName);
        children.add(configGroup);

        return configGroup;
    }
}
