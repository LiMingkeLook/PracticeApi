package net.lzzy.practiceapi.models;


import org.json.JSONException;
import org.json.JSONObject;

public class Student {

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
            json.put("imghead", imghead);
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

    private String imghead;

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

    public String getImghead() {
        return imghead;
    }

    public void setImghead(String imghead) {
        this.imghead = imghead;
    }
}