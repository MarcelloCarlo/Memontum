package com.example.jcgut.notethunder;


import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jcgut on 02/27/2018.
 */

public class MemoSerialize implements Serializable {

    private Date date;
    private String text;
    private boolean fullDisplayed;
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy 'at' hh:mm aaa");

    public MemoSerialize() {
        this.date = new Date();
    }

    public MemoSerialize(Long time, String text){
        this.date = new Date();
        this.text = text;
    }

    public String getDate(){
        return dateFormat.format(date);
    }

    public Long getTime(){
        return date.getTime();
    }

    public void setTime(Long time){
        this.date = new Date(time);
    }

    public void setText(String text){
        this.text = text;
    }

    public String getText(){
        return this.text;
    }

    public String getShorttext(){
        String tmp = text.replaceAll("\n"," ");
        if (tmp.length()>25){
            return tmp.substring(0,25)+"...";
        } else {
            return tmp;
        }
    }

    public void setFullDisplayed(boolean fullDisplayed){
        this.fullDisplayed = fullDisplayed;
    }

    public boolean isFullDisplayed(){
        return this.fullDisplayed;
    }

    @Override
    public String toString(){
        return this.text;
    }
}
