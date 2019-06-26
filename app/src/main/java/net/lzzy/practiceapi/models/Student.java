package net.lzzy.practiceapi.models;


import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Student implements Parcelable {

    protected Student(Parcel in) {
        studentId = in.readString();
        name = in.readString();
        email = in.readString();
        password = in.readString();
        iphone = in.readString();
        gender = in.readString();
        imgHead = in.readString();
        takeEffect=in.readInt();
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	JSONObject json=new JSONObject();
        try {
            json.put("studentId", studentId);
            json.put("name", name);
            json.put("email", email);
            json.put("iphone", iphone);
            json.put("gender", gender);
            json.put("imgHead", imgHead);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    	return json.toString();
    }
    public Student(){
        password="";
    }
    private String studentId;

    private String name;

    private String email;

    private String password;

    private String iphone;

    private String gender;

    private String imgHead;

    public int getTakeEffect() {
        return takeEffect;
    }

    public void setTakeEffect(int takeEffect) {
        this.takeEffect = takeEffect;
    }

    private int takeEffect;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId == null ? null : studentId.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getIphone() {
        return iphone;
    }

    public void setIphone(String iphone) {
        this.iphone = iphone == null ? null : iphone.trim();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    public String getImgHead() {
        return imgHead;
    }

    public void setImgHead(String imgHead) {
        this.imgHead = imgHead;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(studentId);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(iphone);
        dest.writeString(gender);
        dest.writeString(imgHead);
        dest.writeInt(takeEffect);
    }
}