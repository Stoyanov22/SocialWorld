package com.socialworld.mobile.entities;

import java.util.Date;
import java.util.Objects;

/**
 * @author Atanas Katsarov
 */
public class NewsFeedPost extends PostEntity {
    private String username;
    private String profilePic;

    /**
     * Default constructor
     */
    public NewsFeedPost() {
    }

    /**
     * Initialization constructor
     */
    public NewsFeedPost(PostEntity post, UserEntity user) {
        super(post);
        if (user != null) {
            this.username = user.getName();
            this.profilePic = user.getPicture();
        }
    }

    public NewsFeedPost(PostEntity post, String username, String profilePic) {
        super(post);
        this.username = username;
        this.profilePic = profilePic;
    }

    public NewsFeedPost(String id, String userId, String picture, String text, Date date, UserEntity user) {
        super(id, userId, picture, text, date);
        if (user != null) {
            this.username = user.getName();
            this.profilePic = user.getPicture();
        }
    }

    public NewsFeedPost(String id, String userId, String picture, String text, Date date, String username, String profilePic) {
        super(id, userId, picture, text, date);
        this.username = username;
        this.profilePic = profilePic;
    }

    /**
     * Copy constructor
     */
    public NewsFeedPost(NewsFeedPost source) {
        super(source);
        this.username = source.username;
        this.profilePic = source.profilePic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        NewsFeedPost that = (NewsFeedPost) o;
        return username.equals(that.username) &&
                Objects.equals(profilePic, that.profilePic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, profilePic);
    }


}
