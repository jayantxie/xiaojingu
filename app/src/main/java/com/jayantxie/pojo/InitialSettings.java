package com.jayantxie.pojo;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by 天亮就出发 on 2017/4/14.
 */

public class InitialSettings implements Serializable{
    private Date beginTime;

    private Date endTime;

    private int relaxTime;

    private Integer relaxTimes;

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

    public int getRelaxTime() {
        return relaxTime;
    }

    public void setRelaxTime(int relaxTime) {
        this.relaxTime = relaxTime;
    }

    public Integer getRelaxTimes() {
        return relaxTimes;
    }

    public void setRelaxTimes(Integer relaxTimes) {
        this.relaxTimes = relaxTimes;
    }
}
