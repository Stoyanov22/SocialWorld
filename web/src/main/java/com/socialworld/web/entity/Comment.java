package com.socialworld.web.entity;

import java.util.Date;
import java.util.Objects;

public class Comment {

    private String id;

    private String text;

    private String userId;

    private String postId;

    private Date date;

    public Comment(String id, String text, String postId, String userId, Date date) {
        this.id = id;
        this.text = text;
        this.postId = postId;
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", userId='" + userId + '\'' +
                ", postId='" + postId + '\'' +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id.equals(comment.id) &&
                text.equals(comment.text) &&
                userId.equals(comment.userId) &&
                postId.equals(comment.postId) &&
                date.equals(comment.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, userId, postId, date);
    }
}
