package com.bothq.core.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "configs")
public class PluginConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long configId;

    @ManyToOne
    @JoinColumn(name = "server_id", nullable = false, foreignKey = @ForeignKey(name = "FK_SERVER"))
    private Server server;

    @ManyToOne
    @JoinColumn(name = "plugin_id", nullable = false, foreignKey = @ForeignKey(name = "FK_PLUGIN"))
    private Plugin plugin;

    @Column(name = "unique_id", nullable = false)
    private String uniqueId;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled;
}
