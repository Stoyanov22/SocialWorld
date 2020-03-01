package com.socialworld.mobile.entities;

import java.io.Serializable;
import java.util.Objects;

public class UserEntity implements Serializable {
    private int id;
    private String email;

    public UserEntity(int id, String email) {
        this.id = id;
        this.email = email;
    }

    public UserEntity(String email) {
        id = 31 * Objects.hash(email);
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id == that.id &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }
}
