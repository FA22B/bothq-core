package com.bothq.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "configs")
public class PluginConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long configId;

    @ManyToOne
    @JoinColumn(name = "server_id")
    private Server server;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private PluginConfig parentConfig;

    @OneToMany(mappedBy = "parentConfig")
    private List<PluginConfig> childrenConfigs;

    private String uniqueId;
    private String displayName;
    private String type;
    private String value; // JSON string or simple string based on type
    private Boolean isEnabled;

    // Constructors, getters, and setters
}
