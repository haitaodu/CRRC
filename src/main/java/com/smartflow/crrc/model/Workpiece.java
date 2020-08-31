package com.smartflow.crrc.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
@Data
@Entity
@Table(name = "workpiece")
public class Workpiece {
    private String plant_code;
    @Id
    @GeneratedValue
    private String workpieceid;
    private String weldseamid;
    private String workpiecename;
    private String materialname;
    private BigDecimal settingcurrent;
    private BigDecimal settingvoltage;
    private BigDecimal speed;
    private BigDecimal flowrate;
    private String groovetype;
    private BigDecimal thickness;
}
