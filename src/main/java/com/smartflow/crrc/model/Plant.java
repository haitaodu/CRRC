package com.smartflow.crrc.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "plant")
public class Plant {
    @Id
    @GeneratedValue
    private Integer id;
    private String plant_code;
}
