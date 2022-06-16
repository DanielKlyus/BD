package com.nsu.data.entities.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class OtherContractor {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @NotNull(message = "Please enter name")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String name;

    @NotNull(message = "Please enter phone")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String phone;

    @NotNull(message = "Please enter mail")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String mail;

    public Integer getId() { return id;}
    public void setId(Integer id) { this.id = id;}

    public String getName() { return name;}
    public void setName(String Name) { this.name = Name;}

    public String getPhoneNumber() { return phone;}
    public void setPhoneNumber(String phoneNumber) { this.phone = phoneNumber;}

    public String getMail() { return mail;}
    public void setMail(String Mail) { this.mail = Mail;}

}
