package com.smartflow.crrc.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
@Data
@Entity
@Table(name = "current")
public class Current {
    @Id
    @GeneratedValue
    private Integer id;
    private String  workpieceid;
    private String  weldseamid;
    private Date ctime;
    private String  current;
    private Date ltime;

}
