package com.nsu.data.entities.staff;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class StaffEquipment {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @NotNull(message = "Please enter Name")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String Name;

    @NotNull(message = "Please enter article")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private Integer article;

    @NotNull(message = "Please enter type")
    @ManyToOne(targetEntity = TypeEquipment.class)
    @JsonIgnoreProperties(value = {"type"})
    @JoinColumn(name = "type_id")
    private TypeEquipment type;



    public Integer getId() { return id;}
    public void setId(Integer id) { this.id = id;}

    public Integer getArticle() { return article;}
    public void setArticle(Integer article) { this.article = article;}

    public String getName() { return Name;}
    public void setName(String Name) { this.Name = Name;}

    public TypeEquipment getType() { return type;}
    public void setType(TypeEquipment type) { this.type = type;}
}
