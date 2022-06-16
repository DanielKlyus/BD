package com.nsu.data.entities.staff;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class TypeEquipment {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @NotNull(message = "Please enter Specialization")
    private String Specialization;

    public Integer getId() { return id;}
    public void setId(Integer id) { this.id = id;}

    public String getSpecialization() { return Specialization;}
    public void setSpecialization(String Specialization) { this.Specialization = Specialization;}
}
