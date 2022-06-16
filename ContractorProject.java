package com.nsu.data.entities.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nsu.data.entities.contract.OtherContractor;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Data
@Entity
public class ContractorProject {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @NotNull(message = "Please enter otherContractor")
    @ManyToOne(targetEntity = OtherContractor.class)
    @JsonIgnoreProperties(value = {"otherContractor"})
    @JoinColumn(name = "contractor_id")
    private OtherContractor otherContractor;

    @NotNull(message = "Please enter project")
    @ManyToOne(targetEntity = Project.class)
    @JsonIgnoreProperties(value = {"project"})
    @JoinColumn(name = "project_id")
    private Project project;


    public Integer getId() { return id;}
    public void setId(Integer id) { this.id = id;}

    public OtherContractor getCompany() { return otherContractor;}
    public void setCompany(OtherContractor contractor) { this.otherContractor = contractor;}

    public Project getEmployee() { return project;}
    public void setEmployee(Project project) { this.project = project;}
}


