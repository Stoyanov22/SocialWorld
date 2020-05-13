package com.socialworld.mobile.entities;

import com.google.firebase.database.annotations.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class UserEntity implements Serializable {
    private String id;
    private String email;
    private String name;
    private String picture;
    private Date dateOfBirth;
    private String countryCode;
    private Integer genderId;

    public UserEntity(@NotNull String id, @NotNull String email) {
        this.id = id;
        this.email = email;
    }

    public UserEntity(String id, String email, String name, String picture, Date dateOfBirth, Integer genderId, String countryCode) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.dateOfBirth = dateOfBirth;
        this.genderId = genderId;
        this.countryCode = countryCode;
    }

    public UserEntity() {
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Integer getGenderId() {
        return genderId;
    }

    public void setGenderId(Integer genderId) {
        this.genderId = genderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id.equals(that.id) &&
                email.equals(that.email) &&
                Objects.equals(name, that.name) &&
                Objects.equals(picture, that.picture) &&
                Objects.equals(dateOfBirth, that.dateOfBirth) &&
                Objects.equals(countryCode, that.countryCode) &&
                Objects.equals(genderId, that.genderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name, picture, dateOfBirth, countryCode, genderId);
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", countryCode='" + countryCode + '\'' +
                ", genderId=" + genderId +
                '}';
    }
}
