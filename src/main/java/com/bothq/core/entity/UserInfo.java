package com.bothq.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
public class UserInfo {
    @Id
    private long id;
    private String name;
    private String email;


    public UserInfo() {

    }
}
