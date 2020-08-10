package com.example.jcgut.notethunder;

import com.example.jcgut.notethunder.domain.Memo;

import java.io.Serializable;
import java.util.List;

public class MemoWrapper implements Serializable {
    private List<Memo> memoList;

    public MemoWrapper(List<Memo> dataMemo){
        this.memoList = dataMemo;
    }

    public List<Memo> getMemoList() {
        return memoList;
    }
}
