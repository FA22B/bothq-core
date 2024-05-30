package com.bothq.core.plugin.config.enabled;

import com.bothq.lib.plugin.config.enabled.IEnabledServer;
import lombok.Getter;

public class EnabledServer implements IEnabledServer {
    @Getter
    Boolean value;

    public EnabledServer(
            String pluginId,
            long serverId,
            Boolean value
    ) {
    }

}
