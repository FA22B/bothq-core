package com.bothq.core.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
public class ServerInfo {
    @Id
    private String id;
    private String name;

    public ServerInfo() {

    }
}
