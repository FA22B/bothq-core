package com.bothq.core.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
public class PluginConfig {
    @Id
    private String id;
    private String name;

    public PluginConfig() {

    }
}
