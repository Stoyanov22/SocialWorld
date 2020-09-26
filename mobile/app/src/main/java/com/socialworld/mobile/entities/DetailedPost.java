package com.socialworld.mobile.entities;

import java.util.Date;
import java.util.Objects;

/**
 * @author Atanas Katsarov
 */
public class DetailedPost extends PostEntity {
    private String username;
    private String profilePic;

    /**
     * Default constructor
     */
    public DetailedPost() {
    }

    /**
     * Initialization constructor
     */
    public DetailedPost(PostEntity post, UserEntity user) {
        super(post);
        if (user != null) {
            this.username = user.getName();
            this.profilePic = user.getPicture();
        }
    }

    public DetailedPost(PostEntity post, String username, String profilePic) {
        super(post);
        this.username = username;
        this.profilePic = profilePic;
    }

    public DetailedPost(String id, String userId, String picture, String text, Date date, UserEntity user) {
        super(id, userId, picture, text, date);
        if (user != null) {
            this.username = user.getName();
            this.profilePic = user.getPicture();
        }
    }

    public DetailedPost(String id, String userId, String picture, String text, Date date, String username, String profilePic) {
        super(id, userId, picture, text, date);
        this.username = username;
        this.profilePic = profilePic;
    }

    /**
     * Copy constructor
     */
    public DetailedPost(DetailedPost source) {
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
        DetailedPost that = (DetailedPost) o;
        return username.equals(that.username) &&
                Objects.equals(profilePic, that.profilePic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, profilePic);
    }


}
