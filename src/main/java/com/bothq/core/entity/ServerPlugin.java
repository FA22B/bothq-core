package com.bothq.core.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "server_plugin")
public class ServerPlugin {
    @Builder
    public ServerPlugin(Server server, Plugin plugin, Boolean isEnabled) {
        this.server = server;
        this.plugin = plugin;
        this.isEnabled = isEnabled;
        this.serverPluginId = new ServerPluginId(server.getId(), plugin.getId());
    }



    @EmbeddedId
    private ServerPluginId serverPluginId;

    @ManyToOne
    @MapsId("serverId")
    @JoinColumn(name = "server_id", nullable = false, foreignKey = @ForeignKey(name = "FK_SERVER"))
    Server server;

    @ManyToOne
    @MapsId("pluginId")
    @JoinColumn(name = "plugin_id", nullable = false, foreignKey = @ForeignKey(name = "FK_PLUGIN"))
    Plugin plugin;


    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled;
}
