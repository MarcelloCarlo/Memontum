package com.example.jcgut.notethunder;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jcgut.notethunder.domain.Memo;
import com.example.jcgut.notethunder.interfaces.ListInterface;

import java.sql.SQLException;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<Memo> data;
    Context context;
    ListInterface listInterface = null;
    boolean state = true;

    public ListAdapter(Context context, List<Memo> data) {
        this.context = context;
        this.data = data;
        listInterface = (ListInterface)context;
    }

    public ListAdapter(Context context, List<Memo> data, boolean state) {
        this.context = context;
        this.data = data;
        listInterface = (ListInterface)context;
        this.state = state;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Memo memo = data.get(position);
        if(state==true) {
            Glide.with(context).load(memo.getImg()).placeholder(R.drawable.ic_image_black_24dp).into(holder.imgThumb);
            holder.txtTitle.setText(memo.getTitle());
            holder.txtContents.setText(memo.getMemo());
            holder.mCheckBox.setChecked(false);
            holder.mCheckBox.setVisibility(View.GONE);

            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        listInterface.goDetail(memo.getId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if(state==false) {
            Glide.with(context).load(memo.getImg()).placeholder(R.drawable.ic_image_black_24dp).into(holder.imgThumb);
            holder.txtTitle.setText(memo.getTitle());
            holder.txtContents.setText(memo.getMemo());
            holder.mCheckBox.setVisibility(View.VISIBLE);
            holder.mCheckBox.setClickable(false);

            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.mCheckBox.isChecked()) {
                        holder.mCheckBox.setChecked(false);
                        ListFragment.deleteList.remove(ListFragment.deleteList.indexOf(memo.getId()));
                        Log.w("id", String.valueOf(memo.getId()));
                    } else {
                        holder.mCheckBox.setChecked(true);
                        ListFragment.deleteList.add(memo.getId());
                        Log.w("id", String.valueOf(memo.getId()));
                    }
                    for(int item:ListFragment.deleteList) {
                        Log.w("list", String.valueOf(item));
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox mCheckBox;
        ImageView imgThumb;
        TextView txtTitle, txtContents;
        CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.checkbox);
            card = itemView.findViewById(R.id.cardView);
            imgThumb = itemView.findViewById(R.id.thumbnail);
            txtTitle = itemView.findViewById(R.id.tvTitle);
            txtContents = itemView.findViewById(R.id.tvContent);
        }
    }

}
