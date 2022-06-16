package com.nsu.data.entities.contract;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @NotNull(message = "Please enter mail")
    private String mail;

    @NotNull(message = "Please enter phoneNumber")
    private String phoneNumber;

    @NotNull(message = "Please enter companyName")
    private String companyName;

    public Integer getId() { return id;}
    public void setId(Integer id) { this.id = id;}

    public String getCompanyName() { return companyName;}
    public void setCompanyName(String CompanyName) { this.companyName = CompanyName;}

    public String getPhoneNumber() { return phoneNumber;}
    public void setPhoneNumber(String PhoneNumber) { this.phoneNumber = PhoneNumber;}

    public String getMail() { return mail;}
    public void setMail(String Mail) { this.mail = Mail;}
}
