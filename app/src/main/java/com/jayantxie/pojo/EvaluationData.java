package com.jayantxie.pojo;

import java.util.Date;

public class EvaluationData {
    private Integer id;

    private String userName;

    private Integer scores = 0;

    private Date beginTime;

    private Date endTime;

    private Integer relaxMinutes = 0;

    private Integer distractionTimes = 0;

    private Integer relaxTimes = 0;

    private String text;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Integer getScores() {
        return scores;
    }

    public void setScores(Integer scores) {
        this.scores = scores;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getRelaxMinutes() {
        return relaxMinutes;
    }

    public void setRelaxMinutes(Integer relaxMinutes) {
        this.relaxMinutes = relaxMinutes;
    }

    public Integer getDistractionTimes() {
        return distractionTimes;
    }

    public void setDistractionTimes(Integer distractionTimes) {
        this.distractionTimes = distractionTimes;
    }

    public Integer getRelaxTimes() {
        return relaxTimes;
    }

    public void setRelaxTimes(Integer relaxTimes) {
        this.relaxTimes = relaxTimes;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text == null ? null : text.trim();
    }
}