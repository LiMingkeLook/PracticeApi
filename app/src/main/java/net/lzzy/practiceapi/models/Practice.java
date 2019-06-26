package net.lzzy.practiceapi.models;

import java.util.Date;

public class Practice {
    private Integer id;

    private String name;

    private Integer questionCount;

    private String upTime;

    private Integer isReady;

    private String outlines;
    
    private Integer courseId;
    
    public Integer getCourseId() {
		return courseId;
	}
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	public Practice() {
		// TODO Auto-generated constructor stub
	}
    public Practice(Integer id,String name, Integer question_count, String up_time,
			Integer is_ready, String outlines,Integer course_id) {
		this.id = id;
		this.name = name;
		this.questionCount = question_count;
		this.upTime = up_time;
		this.isReady = is_ready;
		this.outlines = outlines;
		this.courseId=course_id;
	}

	public int getId() {
        return id;
    }
	
	public void setId(Integer id) {
		this.id=id;
	}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }

    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }

    public Integer getIsReady() {
        return isReady;
    }

    public void setIsReady(Integer isReady) {
    		this.isReady = isReady;
    }

    public String getOutlines() {
        return outlines;
    }

    public void setOutlines(String outlines) {
        this.outlines = outlines == null ? null : outlines.trim();
    }
}