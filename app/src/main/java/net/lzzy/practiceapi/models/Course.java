package net.lzzy.practiceapi.models;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;


import org.json.JSONException;
import org.json.JSONObject;

public class Course implements Parcelable {
    private Integer id;

    private String teacherId;

    private String name;

    private String intro;

    private String addTime;

    private Integer applicationStatus;

    protected Course(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        teacherId = in.readString();
        name = in.readString();
        intro = in.readString();
        addTime = in.readString();
        if (in.readByte() == 0) {
            applicationStatus = null;
        } else {
            applicationStatus = in.readInt();
        }
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public Integer getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(Integer applicationStatus) {
        applicationStatus = applicationStatus;
    }

    public JSONObject toJSON() {
    	JSONObject json=new JSONObject();
        try {
            json.put("name", name);
            json.put("intro", intro);
            json.put("teacherId",teacherId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    	return json;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
        this.name = name == null ? null : name.trim();
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro == null ? null : intro.trim();
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(teacherId);
        dest.writeString(name);
        dest.writeString(intro);
        dest.writeString(addTime);
        if (applicationStatus == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(applicationStatus);
        }
    }
}