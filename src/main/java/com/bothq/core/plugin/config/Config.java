package com.bothq.core.plugin.config;

import com.bothq.lib.plugin.config.IConfig;
import com.bothq.lib.plugin.config.IConfigGroup;
import org.springframework.util.Assert;

public class Config extends ConfigGroup implements IConfig {
    public Config(String uniqueId, String displayName, String pluginId) {
        super(uniqueId, displayName, pluginId, null);
    }

    @Override
    public IConfigGroup addConfigGroup(String uniqueId, String displayName) {
        var newUniqueId = createUniqueId(uniqueId);
        Assert.isTrue(isUniqueIdUnique(newUniqueId), "Unique ID was already set!");

        var configGroup = new ConfigGroup(newUniqueId, displayName, pluginId, this);
        children.add(configGroup);

        return configGroup;
    }
}
