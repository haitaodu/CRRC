package com.smartflow.crrc.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
@Data
@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue
    private Integer id;
    private String  workpieceid;
    private String  weldseamid;
    private Date ctime;
    private String image;
    private Date ltime;

}
