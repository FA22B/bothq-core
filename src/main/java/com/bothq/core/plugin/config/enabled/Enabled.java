package com.bothq.core.plugin.config.enabled;

import com.bothq.core.service.PluginConfigurationService;
import com.bothq.lib.plugin.config.enabled.IEnabled;
import com.bothq.lib.plugin.config.enabled.IEnabledServer;
import lombok.Getter;

public class Enabled implements IEnabled {
    @Getter
    private String pluginId;
    @Getter
    private Boolean defaultValue;

    public Enabled(String pluginId,
                   Boolean defaultValue) {
        this.pluginId = pluginId;
        this.defaultValue = defaultValue;
    }

    @Override
    public IEnabledServer get(long serverId) {
        return new EnabledServer(
                pluginId,
                serverId,
                PluginConfigurationService
                        .getInstance()
                        .getEnabled(pluginId, serverId, defaultValue)
        );
    }
}
