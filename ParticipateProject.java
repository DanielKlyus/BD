package com.nsu.data.entities.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nsu.data.entities.employees.Employees;
import com.nsu.data.entities.employees.Positions;
import com.nsu.data.entities.project.Project;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
public class ParticipateProject {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm", shape = JsonFormat.Shape.STRING, timezone = "Asia/Novosibirsk")
    private Date dateStart;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm", shape = JsonFormat.Shape.STRING, timezone = "Asia/Novosibirsk")
    private Date dateEnd;

    @NotNull(message = "Please enter employees")
    @ManyToOne(targetEntity = Employees.class)
    @JsonIgnoreProperties(value = {"employee"})
    @JoinColumn(name = "employee_id")
    private Employees employees;

    @NotNull(message = "Please enter positions")
    @ManyToOne(targetEntity = Positions.class)
    @JoinColumn(name = "position_id")
    private Positions positions;

    @NotNull(message = "Please enter project")
    @ManyToOne(targetEntity = Project.class)
    @JoinColumn(name = "project_id")
    private Project project;

    public Integer getId() { return id;}
    public void setId(Integer id) { this.id = id;}

    public Date getdateStart() { return dateStart;}
    public void setdateStart(Date dateStart) { this.dateStart = dateStart;}

    public Date getdateEnd() { return dateEnd;}
    public void setdateEnd(Date dateEnd) { this.dateEnd = dateEnd;}

    public Employees getEmployees() { return employees;}
    public void setEmployees(Employees employee) { this.employees = employee;}

    public Positions getPositions() { return positions;}
    public void setPositions(Positions position) { this.positions = position;}

    public Project getProject() { return project;}
    public void setProject(Project project) { this.project = project;}

}
