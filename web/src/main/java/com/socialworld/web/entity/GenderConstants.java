package com.socialworld.web.entity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

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

    public static Map<Integer, String> getGendersMap(){
        Map<Integer, String> result = new HashMap<>();
        GenderConstants genderConstants = new GenderConstants();
        for(Field field : genderConstants.getClass().getDeclaredFields()){
            try {
                result.put(field.getInt(genderConstants), getGenderName(field.getInt(genderConstants)));
            } catch (IllegalAccessException e) {
                System.err.println("Couldn't load all genders!");
            }
        }

        return result;
    }
}
