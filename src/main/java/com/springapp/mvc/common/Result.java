package com.springapp.mvc.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kobe_xuan on 2017/6/2.
 */
public class Result implements Comparable {
    private String title;//标题
    private String type;//类型
    private String unit;//发文单位
    private String link;//链接
    private String anchor1;//anchor1
    private String anchor2;//anchor2
    private String time;//发文时间
    private String clickNum;//点击数
    private double score;//得分
    private  int sortTime;//排序用的时间

    public int getSortTime() {
        return sortTime;
    }

    public void setSortTime(int sortTime) {
        this.sortTime = sortTime;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getClickNum() {
        return clickNum;
    }

    public void setClickNum(String clickNum) {
        this.clickNum = clickNum;
    }

    public String getAnchor1() {
        return anchor1;
    }

    public void setAnchor1(String anchor1) {
        this.anchor1 = anchor1;
    }

    public String getAnchor2() {
        return anchor2;
    }

    public void setAnchor2(String anchor2) {
        this.anchor2 = anchor2;
    }

    public Result() {
    }
    public Result(String title,String type,String unit,String link,String anchor1,String anchor2,String time,String clickNum,double score) {
        this.title=title;
        this.type=type;
        this.unit=unit;
        this.link=link;
        this.anchor1=anchor1;
        this.anchor2=anchor2;
        this.time=time;
        this.clickNum=clickNum;
        this.score=score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public int compareTo(Object o) { //考虑时效性
        Result r2=(Result)o;
        if(getScore()>r2.getScore())
            return 1;
        else if(getScore()<r2.getScore())
            return -1;
        else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date1 = sdf.parse(getTime());
                Date date2=sdf.parse(r2.getTime());
                String strTime1 = date1.getTime() + "";
                String strTime2= date2.getTime()+"";
                strTime1 = strTime1.substring(0, 10);
                strTime2 = strTime2.substring(0,10);
                int sortTime1 = Integer.parseInt(strTime1);
                int sortTime2=Integer.parseInt(strTime2);
                if(sortTime1>sortTime2)
                    return 1;
                else if(sortTime1<sortTime2)
                    return -1;
                else
                    return 0;
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return 0;
    }
}
