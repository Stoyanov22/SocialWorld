package com.socialworld.mobile.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.List;

/**
 * @author Atanas Katsarov
 */
public class PostEntity implements Serializable {
    private String id;
    private String userId;
    private String picture;
    private String text;
    private Date date;
    private List<String> userLikes;

    /**
     * Default Constructor
     */
    public PostEntity() {
    }

    /**
     * Initialization constructor
     */
    public PostEntity(String id, String userId, String picture, String text, Date date) {
        this.id = id;
        this.userId = userId;
        this.picture = picture;
        this.text = text;
        this.date = date;
        this.userLikes = new ArrayList<>();
    }

    public PostEntity(String id, String userId, String text, Date date) {
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.date = date;
        this.userLikes = new ArrayList<>();
    }

    public PostEntity(String id, String userId, String picture, String text, Date date, List<String> userLikes) {
        this.id = id;
        this.userId = userId;
        this.picture = picture;
        this.text = text;
        this.date = date;
        this.userLikes = userLikes;
    }

    /**
     * Copy constructor
     */
    public PostEntity(PostEntity source) {
        this.id = source.id;
        this.userId = source.userId;
        this.picture = source.picture;
        this.text = source.text;
        this.date = new Date(source.date.getTime());
        this.userLikes = new ArrayList<>();
        if (source.userLikes != null) {
            this.userLikes.addAll(source.userLikes);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<String> getUserLikes() {
        return userLikes;
    }

    public void setUserLikes(List<String> userLikes) {
        this.userLikes = userLikes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostEntity that = (PostEntity) o;
        return id.equals(that.id) &&
                userId.equals(that.userId) &&
                Objects.equals(picture, that.picture) &&
                Objects.equals(text, that.text) &&
                date.equals(that.date) &&
                userLikes.equals(that.userLikes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, picture, text, date, userLikes);
    }

    @Override
    public String toString() {
        return "PostEntity{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", picture='" + picture + '\'' +
                ", text='" + text + '\'' +
                ", date=" + date +
                ", userLikes=" + userLikes +
                '}';
    }
}
