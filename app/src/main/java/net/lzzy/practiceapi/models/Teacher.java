package net.lzzy.practiceapi.models;


import org.json.JSONException;
import org.json.JSONObject;

public class Teacher {
    private String teacherId;

    private String name;

    private String email;

    private String password;

    private String iphone;

    private String gender;

    private Integer valid;//0、1、2

    private String imgHead;
    private Boolean isMyTeacher;

    public Boolean isMyTeacher() {
        return isMyTeacher;
    }

    public void setIsMyTeacher(Boolean isMyTeacher) {
        isMyTeacher = isMyTeacher;
    }
    /**
     * Web to android
     * @return
     */
    public String toStudentJSON(Boolean isMyTeacher) {
    	JSONObject json=new JSONObject();
        try {
            json.put("teacherId", teacherId);
            json.put("name", name);
            json.put("email", email);
            json.put("iphone", iphone);
            json.put("gender", gender);
            json.put("imgHead", imgHead);
            json.put("isMyTeacher", isMyTeacher);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    	return json.toString();
	}

    /**
     * Web to android
     * @return
     */
    public String toTeacherJSON() {
    	JSONObject json=new JSONObject();
        try {
            json.put("teacherId", teacherId);
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
        this.iphone = iphone ;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public String getImgHead() {
        return imgHead;
    }

    public void setImgHead(String imgHead) {
        this.imgHead = imgHead;
    }
}