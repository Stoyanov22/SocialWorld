package com.socialworld.web.entity;

import java.util.Date;
import java.util.Objects;

public class Post {

    private String id;

    private String text;

    private String picture;

    private Date date;

    private int likes;

    private User user;

    public Post() {

    }

    public Post(Date date){
        this.date = date;
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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", picture='" + picture + '\'' +
                ", date=" + date +
                ", likes=" + likes +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return likes == post.likes &&
                id.equals(post.id) &&
                Objects.equals(text, post.text) &&
                Objects.equals(picture, post.picture) &&
                date.equals(post.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, picture, date, likes);
    }
}
