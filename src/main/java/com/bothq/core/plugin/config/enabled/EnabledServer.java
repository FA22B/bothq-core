package com.bothq.core.plugin.config.enabled;

import com.bothq.lib.plugin.config.enabled.IEnabledServer;
import lombok.Getter;
import lombok.Setter;

public class EnabledServer implements IEnabledServer {
    @Getter
    private final String pluginId;
    @Getter
    private final long serverId;

    @Getter
    @Setter
    boolean enabled;

    public EnabledServer(
            String pluginId,
            long serverId,
            boolean enabled
    ) {

        this.pluginId = pluginId;
        this.serverId = serverId;
        this.enabled = enabled;
    }

}
