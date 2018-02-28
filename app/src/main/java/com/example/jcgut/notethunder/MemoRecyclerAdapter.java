package com.example.jcgut.notethunder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcgut on 03/01/2018.
 */

public class MemoRecyclerAdapter extends RecyclerView.Adapter<MemoRecyclerAdapter.ViewHolder> {
    static List<MemoSerialize> memoList;
    static Context ctx;

    MemoRecyclerAdapter(Context ctx, List<MemoSerialize> memoList){
        this.memoList = new ArrayList<MemoSerialize>();
        this.ctx = ctx;
        this.memoList = memoList;
    }

    @Override
    public MemoRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.memo_list_item,null);
        ViewHolder viewHolder = RecyclerView.ViewHolder(itemLayout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MemoRecyclerAdapter.ViewHolder holder, int position){
        holder.date
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView lblMemoDate, lblMemoContent;
        Imag

        public ViewHolder(View itemLayout){
            super(itemLayout);
            lblMemoDate = itemLayout.findViewById(R.id.lblMemoDate);
            lblMemoContent = itemLayout.findViewById(R.id.lblMemoContent);

        }
    }
}
