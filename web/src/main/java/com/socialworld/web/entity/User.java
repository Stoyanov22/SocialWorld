package com.socialworld.web.entity;

import java.util.Date;
import java.util.Objects;

public class User {

    private String id;

    private String uid;

    private String email;

    private String name;

    private int countryId;

    private Date dateOfBirth;

    private int genderId;

    private String picture;

    public User() {

    }

    public User(String uid, String email) {
        this.uid = uid;
        this.email = email;
    }

    public User(String uid, String email, String name) {
        this.uid = uid;
        this.email = email;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
                ", uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", countryId=" + CountryConstants.getCountryName(countryId) +
                ", dateOfBirth=" + dateOfBirth +
                ", genderId=" + GenderConstants.getGenderName(genderId) +
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
                uid.equals(user.uid) &&
                email.equals(user.email) &&
                name.equals(user.name) &&
                Objects.equals(dateOfBirth, user.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uid, email, name, countryId, dateOfBirth, genderId);
    }
}
