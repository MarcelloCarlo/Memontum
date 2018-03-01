package com.example.jcgut.notethunder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcgut on 03/01/2018.
 */

public class MemoRecyclerAdapter extends RecyclerView.Adapter<MemoRecyclerAdapter.ViewHolder> {
    static List<MemoSerialize> memoList;
    static Context ctx;

    MemoRecyclerAdapter(Context ctx, List<MemoSerialize> memoList) {
        this.memoList = new ArrayList<MemoSerialize>();
        this.ctx = ctx;
        this.memoList = memoList;
    }

    @Override
    public MemoRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.memo_list_item, null);
        ViewHolder viewHolder = new ViewHolder(itemLayout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MemoRecyclerAdapter.ViewHolder holder, int position) {
        holder.lblMemoDate.setText(memoList.get(position).getDate());
        holder.lblMemoContent.setText(memoList.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return memoList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView lblMemoDate, lblMemoContent;
        public ImageView btnEditMemo, btnDeleteMemo;

        public ViewHolder(View itemLayout) {
            super(itemLayout);
            lblMemoDate = itemLayout.findViewById(R.id.lblMemoDate);
            lblMemoContent = itemLayout.findViewById(R.id.lblMemoContent);
            btnEditMemo = itemLayout.findViewById(R.id.btnEditMemo);
            btnDeleteMemo = itemLayout.findViewById(R.id.btnDeleteMemo);
            btnDeleteMemo.setOnClickListener(this);
            btnEditMemo.setOnClickListener(this);
            lblMemoContent.setOnClickListener(this);
            lblMemoDate.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnDeleteMemo: {
                    //delete
                    break;
                }
                case R.id.btnEditMemo: {
                    //edit
                    break;
                }
                case R.id.lblMemoContent: {
                    //display
                    displayMemo();
                    break;
                }
                case R.id.lblMemoDate: {
                    //display
                    displayMemo();
                    break;
                }
            }
        }

        public void displayMemo(){
            Intent intent = new Intent(ctx, MainActivity.class);

            Bundle extras = new Bundle();
            extras.putInt("position", getAdapterPosition());
            intent.putExtras(extras);

            /*
            int i=getAdapterPosition();
            intent.putExtra("position", getAdapterPosition());*/
            ctx.startActivity(intent);

        }

    }

}