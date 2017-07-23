package com.jayantxie.utils;

import java.util.Date;

/**
 * Created by 天亮就出发 on 2017/4/28.
 */

public class DoubleDatesToMinutes {
    public static long translate(Date begin,Date end){
        long diff = end.getTime()- begin.getTime();
        long days = diff / (1000 * 60 * 60 * 24);
        long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
        long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
        return minutes;
    }
}
