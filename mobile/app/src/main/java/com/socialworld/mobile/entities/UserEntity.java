package com.socialworld.mobile.entities;

import com.google.firebase.database.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

public class UserEntity implements Serializable {
    private String id;
    private String email;
    private String name;
    private String picture;
    private String dateOfBirth;
    private Integer genderId;
    private Integer countryId;

    public UserEntity(@NotNull String id, @NotNull String email) {
        this.id = id;
        this.email = email;
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getGenderId() {
        return genderId;
    }

    public void setGenderId(Integer genderId) {
        this.genderId = genderId;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
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
                Objects.equals(genderId, that.genderId) &&
                Objects.equals(countryId, that.countryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name, picture, dateOfBirth, genderId, countryId);
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", genderId=" + genderId +
                ", countryId=" + countryId +
                '}';
    }
}
