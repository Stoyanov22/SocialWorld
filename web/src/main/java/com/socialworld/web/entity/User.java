package com.socialworld.web.entity;

import java.util.Date;
import java.util.Objects;

public class User {

    private String id;

    private String email;

    private String name;

    private int countryId;

    private Date dateOfBirth;

    private int genderId;

    private String picture;

    public User() {

    }

    public User(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public User(String id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", country=" + CountryConstants.getCountryName(countryId) +
                ", dateOfBirth=" + dateOfBirth +
                ", gender=" + GenderConstants.getGenderName(genderId) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return countryId == user.countryId &&
                genderId == user.genderId &&
                id.equals(user.id) &&
                email.equals(user.email) &&
                name.equals(user.name) &&
                Objects.equals(dateOfBirth, user.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name, countryId, dateOfBirth, genderId);
    }
}
