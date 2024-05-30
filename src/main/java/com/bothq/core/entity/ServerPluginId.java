package com.bothq.core.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ServerPluginId implements Serializable
{
    // @ManyToOne
    // @JoinColumn(name = "server", nullable = false, foreignKey = @ForeignKey(name = "FK_SERVER"))
    long serverId;

    // @ManyToOne
    // @JoinColumn(name = "plugin", nullable = false, foreignKey = @ForeignKey(name = "FK_PLUGIN"))
    long pluginId;
}
