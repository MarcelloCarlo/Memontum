package com.example.jcgut.notethunder.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;


@DatabaseTable(tableName = "memo")
public class Memo {
    @DatabaseField(generatedId = true)
    private int Id;

    @DatabaseField
    private String img;

    @DatabaseField
    private String title;

    @DatabaseField
    private String memo;

    @DatabaseField
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return Id;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Memo(String memo, Date date) {
        setMemo(memo);
        setDate(date);
    }
}
