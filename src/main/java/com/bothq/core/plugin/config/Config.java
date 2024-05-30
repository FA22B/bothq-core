package com.bothq.core.plugin.config;

import com.bothq.core.plugin.config.enabled.Enabled;
import com.bothq.lib.plugin.config.IConfig;
import com.bothq.lib.plugin.config.IConfigGroup;
import lombok.Getter;
import org.springframework.util.Assert;

public class Config extends ConfigGroup implements IConfig {
    @Getter
    protected final Enabled enabled;
    @Getter
    protected final String description;


    public Config(String uniqueId,
                  String displayName,
                  String description,
                  String pluginId) {
        super(uniqueId, displayName, pluginId, null);

        this.description = description;
        this.enabled = new Enabled(pluginId, false);
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
