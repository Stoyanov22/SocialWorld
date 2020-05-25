package com.socialworld.web.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.List;

public class Post implements Serializable {

    private String id;

    private String text;

    private String picture;

    private Date date;

    private List<String> userLikes;

    private String userId;

    public Post() {

    }

    public Post(Date date) {
        this.date = date;
    }

    //Creation constructor - sets ID
    public Post(String text, String picture, Date date, List<String> userLikes, String userId){
        this.id = String.valueOf(Objects.hash(text, picture, date, userLikes, userId));
        this.text = text;
        this.picture = picture;
        this.date = date;
        this.userLikes = userLikes;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", picture='" + picture + '\'' +
                ", date=" + date +
                ", userLikes=" + userLikes +
                ", userId='" + userId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id.equals(post.id) &&
                Objects.equals(text, post.text) &&
                Objects.equals(picture, post.picture) &&
                date.equals(post.date) &&
                userLikes.equals(post.userLikes) &&
                userId.equals(post.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, picture, date, userLikes, userId);
    }
}
