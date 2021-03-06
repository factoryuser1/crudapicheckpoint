package com.example.demo.model;

import com.example.demo.view.Views;
import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.SecretView.class)
    private Long id;
    //don't send it back with json response
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    //rename it on JSON when it is serialized to JSON or on the getter
    @JsonView(Views.SecretView.class)
    @JsonProperty("email")
    private String email;

    @JsonView(Views.DetailedView.class)
    @JsonFormat(pattern = "yyy-MM-dd hh:ss", timezone = "GMT -5")
    private Date dateOfBirth;

    @JsonView(Views.SecretView.class)
    private Long age;

    @JsonView(Views.ListView.class)
    private String firstName;
    @JsonView(Views.ListView.class)
    private String lastName;
    @JsonView(Views.ListView.class)
    private String phoneNumber;

    @JsonView(Views.DetailedView.class)
    private String streetAddress;
    @JsonView(Views.DetailedView.class)
    private String city;
    @JsonView(Views.DetailedView.class)
    private String state;
    @JsonView(Views.DetailedView.class)
    private Long zipCode;

    public Long getId() {
        return id;
    }

//    @JsonIgnore // on the getter to exclude sensitive data
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getZipCode() {
        return zipCode;
    }

    public void setZipCode(Long zipCode) {
        this.zipCode = zipCode;
    }
}
