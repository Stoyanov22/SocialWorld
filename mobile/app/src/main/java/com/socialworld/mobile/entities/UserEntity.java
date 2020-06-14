package com.socialworld.mobile.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Atanas Katsarov
 */
public class UserEntity implements Serializable {
    private String id;
    private String email;
    private String name;
    private String picture;
    private Date dateOfBirth;
    private String countryCode;
    private Integer genderId;
    private boolean isEnabled;
    private List<String> followers;
    private List<String> followedUsers;

    /**
     * Default Constructor
     */
    public UserEntity() {
    }

    /**
     * Initialization constructor
     */
    public UserEntity(String id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.isEnabled = true;
        this.followers = new ArrayList<>();
        this.followedUsers = new ArrayList<>();
    }

    public UserEntity(String id, String email, String name, String picture, Date dateOfBirth, Integer genderId, String countryCode) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.dateOfBirth = dateOfBirth;
        this.countryCode = countryCode;
        this.genderId = genderId;
        this.isEnabled = true;
        this.followers = new ArrayList<>();
        this.followedUsers = new ArrayList<>();
    }

    /**
     * Copy constructor
     */
    public UserEntity(UserEntity source) {
        this.id = source.id;
        this.email = source.email;
        this.name = source.name;
        this.picture = source.picture;
        this.dateOfBirth = new Date(source.dateOfBirth.getTime());
        this.countryCode = source.countryCode;
        this.genderId = source.genderId;
        this.isEnabled = source.isEnabled;
        this.followers = new ArrayList<>();
        this.followedUsers = new ArrayList<>();
        if (source.followers != null) {
            this.followers.addAll(source.followers);
        }
        if (source.followedUsers != null) {
            this.followedUsers.addAll(source.followedUsers);
        }
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

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public List<String> getFollowedUsers() {
        return followedUsers;
    }

    public void setFollowedUsers(List<String> followedUsers) {
        this.followedUsers = followedUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return isEnabled == that.isEnabled &&
                id.equals(that.id) &&
                email.equals(that.email) &&
                name.equals(that.name) &&
                Objects.equals(picture, that.picture) &&
                Objects.equals(dateOfBirth, that.dateOfBirth) &&
                Objects.equals(countryCode, that.countryCode) &&
                Objects.equals(genderId, that.genderId) &&
                Objects.equals(followers, that.followers) &&
                Objects.equals(followedUsers, that.followedUsers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name, picture, dateOfBirth, countryCode, genderId, isEnabled, followers, followedUsers);
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
                ", isEnabled=" + isEnabled +
                ", followers=" + followers +
                ", followedUsers=" + followedUsers +
                '}';
    }
}
