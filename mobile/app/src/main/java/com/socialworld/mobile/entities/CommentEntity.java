package com.socialworld.mobile.entities;

import java.util.Date;
import java.util.Objects;

/**
 * @author Atanas Katsarov
 */
public class CommentEntity {
    private String id;
    private String userId;
    private String postId;
    private String text;
    private Date date;

    /**
     * Default Constructor
     */
    public CommentEntity() {
    }

    /**
     * Initialization constructor
     */
    public CommentEntity(String id, String userId, String postId, String text, Date date) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.text = text;
        this.date = date;
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

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentEntity that = (CommentEntity) o;
        return id.equals(that.id) &&
                userId.equals(that.userId) &&
                postId.equals(that.postId) &&
                text.equals(that.text) &&
                date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, postId, text, date);
    }

    @Override
    public String toString() {
        return "CommentEntity{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", postId='" + postId + '\'' +
                ", text='" + text + '\'' +
                ", date=" + date +
                '}';
    }
}
