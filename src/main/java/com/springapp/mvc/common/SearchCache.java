package com.springapp.mvc.common;

import com.google.gson.JsonArray;

import java.util.Date;
import java.util.List;

/**
 * Created by kobe_xuan on 2017/6/22.
 */
public class SearchCache implements Comparable {
    private String key;//搜索关键字
    private int length;//结果长度
    private int pageNum;//页面的数量
    private List<Result> docList;//结果的list
    private JsonArray jsonArray;//json分页数组
    private JsonArray fileArrary;//附件分页数组
    private Date sortTime;//用于排序的时间

    public SearchCache(String key, int length, int pageNum, List<Result> docList, JsonArray jsonArray, JsonArray fileArrary, Date sortTime) {
        this.key = key;
        this.length = length;
        this.pageNum = pageNum;
        this.docList = docList;
        this.jsonArray = jsonArray;
        this.fileArrary = fileArrary;
        this.sortTime = sortTime;
    }

    public Date getSortTime() {
        return sortTime;
    }

    public void setSortTime(Date sortTime) {
        this.sortTime = sortTime;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public List<Result> getDocList() {
        return docList;
    }

    public void setDocList(List<Result> docList) {
        this.docList = docList;
    }

    public JsonArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JsonArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public JsonArray getFileArrary() {
        return fileArrary;
    }

    public void setFileArrary(JsonArray fileArrary) {
        this.fileArrary = fileArrary;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int compareTo(Object o) {
        SearchCache other=(SearchCache)o;
        if(this.getSortTime().getTime()<other.getSortTime().getTime())
            return -1;
        else if(this.getSortTime().getTime()>other.getSortTime().getTime())
            return 1;
        else
            return 0;
    }
}
