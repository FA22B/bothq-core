package com.bothq.core.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
public class MemberInfo {
    @Id
    private String id;
    private String name;
    private String email;


    public MemberInfo() {

    }
}
