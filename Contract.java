package com.nsu.data.entities.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nsu.data.entities.contract.Client;
import com.nsu.data.entities.employees.Employees;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Contract {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;


    @NotNull(message = "Please enter client")
    @ManyToOne(targetEntity = Client.class)
    @JsonIgnoreProperties(value = {"client"})
    @JoinColumn(name = "client_id")
    private Client client;

    @NotNull(message = "Please enter DateSigning")
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm", shape = JsonFormat.Shape.STRING, timezone = "Asia/Novosibirsk")
    private String DateSigning;

    @NotNull(message = "Please enter employees")
    @ManyToOne(targetEntity = Employees.class)
    @JsonIgnoreProperties(value = {"employees"})
    @JoinColumn(name = "employees_id")
    private Employees employees;


    public Integer getId() { return id;}
    public void setId(Integer id) { this.id = id;}

    public Client getCompany() { return client;}
    public void setCompany(Client company) { this.client = company;}

    public String getDateSigning() { return DateSigning;}
    public void setDateSigning(String DateSigning) { this.DateSigning = DateSigning;}

    public Employees getEmployees() { return employees;}
    public void setEmployees(Employees employee) { this.employees = employee;}
}


