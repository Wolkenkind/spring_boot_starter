package com.t1.openschool.atumanov.log_http_boot_starter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "main")
public class TestEntity {

    public enum EntityType{
        TYPE_A, TYPE_B, TYPE_C
    }

    @Id
    @Column
    private String name;
    @Enumerated(EnumType.STRING)
    @Column
    private EntityType type;
    @Column(name = "int_value")
    private int intValue;
    @Column(name = "d_value")
    double doubleValue;
}
