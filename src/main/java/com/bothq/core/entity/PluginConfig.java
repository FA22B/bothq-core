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
    private Long id;


    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "server_id", referencedColumnName = "server_id", nullable = false, foreignKey = @ForeignKey(name = "FK_SERVER")),
            @JoinColumn(name = "plugin_id", referencedColumnName = "plugin_id", nullable = false, foreignKey = @ForeignKey(name = "FK_PLUGIN"))
    })
    private ServerPlugin serverPlugin;


    @Column(name = "unique_id", nullable = false)
    private String uniqueId;

    @Column(name = "value", nullable = false)
    private String value;
}
