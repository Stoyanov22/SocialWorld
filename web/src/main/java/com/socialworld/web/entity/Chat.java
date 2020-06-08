package com.socialworld.web.entity;

import java.util.Date;
import java.util.Objects;

public class Chat {

    private String id;

    private String content;

    private Date timestamp;

    private String fromUserId;

    private String toUserId;

    public Chat(){

    }

    public Chat(String id, String content, Date timestamp, String fromUserId, String toUserId) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                ", fromUserId='" + fromUserId + '\'' +
                ", toUserId='" + toUserId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return id.equals(chat.id) &&
                content.equals(chat.content) &&
                timestamp.equals(chat.timestamp) &&
                fromUserId.equals(chat.fromUserId) &&
                toUserId.equals(chat.toUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, timestamp, fromUserId, toUserId);
    }
}
