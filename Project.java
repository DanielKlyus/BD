package com.nsu.data.entities.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nsu.data.entities.contract.Contract;
import com.nsu.data.entities.employees.Employees;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @NotNull(message = "Please enter Name")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String Name;

    @NotNull(message = "Please enter Price")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private Integer Price;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm", shape = JsonFormat.Shape.STRING, timezone = "Asia/Novosibirsk")
    private Date dateStart;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm", shape = JsonFormat.Shape.STRING, timezone = "Asia/Novosibirsk")
    private Date dateEnd;

    @NotNull(message = "Please enter supervisor")
    @ManyToOne(targetEntity = Employees.class)
    @JsonIgnoreProperties(value = {"supervisor"})
    @JoinColumn(name = "supervisor_id")
    private Employees supervisor;

    @NotNull(message = "Please enter contract")
    @ManyToOne(targetEntity = Contract.class)
    @JoinColumn(name = "contract_id")
    private Contract contract;


    @OneToOne(targetEntity = ProjectStatus.class)
    @JsonIgnoreProperties(value = {"Name"})
    @JoinColumn(name = "project_status")
    private ProjectStatus project_status;

    public Integer getId() { return id;}
    public void setId(Integer id) { this.id = id;}

    public String getName() { return Name;}
    public void setName(String Name) { this.Name = Name;}

    public Integer getPrice() { return Price;}
    public void setPrice(Integer Price) { this.Price = Price;}

    public Date getdateStart() { return dateStart;}
    public void setdateStart(Date dateStart) { this.dateStart = dateStart;}

    public Date getdateEnd() { return dateEnd;}
    public void setdateEnd(Date dateEnd) { this.dateEnd = dateEnd;}

    public Employees getSupervisor() { return supervisor;}
    public void setSupervisor(Employees supervisor) { this.supervisor = supervisor;}

    public Contract getContract() { return contract;}
    public void setContract(Contract contract) { this.contract = contract;}


    public ProjectStatus getStatus() { return project_status;}
    public void setStatus(ProjectStatus status) { this.project_status = status;}

}
