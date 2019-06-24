package net.lzzy.practiceapi.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Course {
    private String teacherId;

    private String name;

    private String intro;

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId == null ? null : teacherId.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name ;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }


}