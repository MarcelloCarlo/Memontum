package com.example.jcgut.notethunder.interfaces;

import java.sql.SQLException;


public interface ListInterface {
    public void goDetail();
    public void goDetail(long position) throws SQLException;
    public void delete(int position) throws SQLException;
}
