package com.socialworld.web.entity;

public class GenderConstants {

    public static final int MALE = 0;
    public static final int FEMALE = 1;

    public static String getGenderName(int id) {
        switch (id) {
            case 0:
                return "Male";
            case 1:
                return "Female";
            default:
                throw new IllegalStateException("Wrong gender Id : " + id);
        }
    }
}
