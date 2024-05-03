package com.bothq.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Entity
@Table(name = "plugin_configurations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PluginConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "server_id", nullable = false)
    private Server server;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "plugin_properties", joinColumns = @JoinColumn(name = "plugin_id"))
    @MapKeyColumn(name = "property_key")
    @Column(name = "value")
    private Map<String, Object> properties; // Stores configuration properties
}
