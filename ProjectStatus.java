package com.nsu.data.entities.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class ProjectStatus {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @NotNull(message = "Please enter Name")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String Name;

    public Integer getId() { return id;}
    public void setId(Integer id) { this.id = id;}

    public String getName() { return Name;}
    public void setName(String Name) { this.Name = Name;}
    
}
