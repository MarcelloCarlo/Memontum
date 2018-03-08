package com.example.jcgut.notethunder.interfaces;

import com.example.jcgut.notethunder.domain.Memo;

import java.sql.SQLException;
import java.util.Date;



public interface DetailInterface {
    public void backToList();
    public void saveToList(Memo memo) throws SQLException;
    public void update(long position, String imgUri, String strTitle, String strContent, Date newTime) throws SQLException;
    public void delete(long position) throws SQLException;
}
