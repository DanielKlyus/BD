package com.nsu.data.entities.staff;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nsu.data.entities.employees.Division;
import com.nsu.data.entities.project.Project;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
public class TakenStaffEquipment {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @NotNull(message = "Please enter staffEquipment")
    @ManyToOne(targetEntity = StaffEquipment.class)
    @JsonIgnoreProperties(value = {"staff_equipment"})
    @JoinColumn(name = "staff_equipment_id")
    private StaffEquipment staffEquipment;

    @ManyToOne(targetEntity = Division.class)
    @JsonIgnoreProperties(value = {"division"})
    @JoinColumn(name = "department_id")
    private Division department;

    @NotNull(message = "Please enter project")
    @ManyToOne(targetEntity = Project.class)
    @JsonIgnoreProperties(value = {"project"})
    @JoinColumn(name = "project_id")
    private Project project;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm", shape = JsonFormat.Shape.STRING, timezone = "Asia/Novosibirsk")
    private Date dateStart;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm", shape = JsonFormat.Shape.STRING, timezone = "Asia/Novosibirsk")
    private Date dateEnd;

    public Integer getId() { return id;}
    public void setId(Integer id) { this.id = id;}

    public StaffEquipment getType() { return staffEquipment;}
    public void setType(StaffEquipment staffEquipment) { this.staffEquipment = staffEquipment;}

    public Division getDivision() { return department;}
    public void setDivision(Division division) { this.department = division;}

    public Date getDateStart() { return dateStart;}
    public void setDateStart(Date dateStart) { this.dateStart = dateStart;}

    public Date getDateEnd() { return dateEnd;}
    public void setDateEnd(Date dateEnd) { this.dateEnd = dateEnd;}

    public Project getProject() { return project;}
    public void setProject(Project project) { this.project = project;}
}

