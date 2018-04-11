package com.example.jcgut.notethunder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.jcgut.notethunder.domain.Memo;
import com.example.jcgut.notethunder.interfaces.ListInterface;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    View view;
    Context context = null;
    RecyclerView recyclerView;
    ListAdapter listAdapter;

    FloatingActionButton btnPlus,btnSelect;
    boolean state = true;
    int btnCond = 1;

    private List<Memo> data = new ArrayList<>();
    private  List<Memo> memoContent;

    ListInterface listInterface = null;

    public static List<Integer> deleteList = new ArrayList<>();

    public ListFragment() {
    }

    @SuppressWarnings("unused")
    public static ListFragment newInstance(int columnCount) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(view==null) view = inflater.inflate(R.layout.layout_list, container, false);

        context = getContext();
        recyclerView = view.findViewById(R.id.recyclerView);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        listAdapter = new ListAdapter(context, data);
        recyclerView.setAdapter(listAdapter);

        setBtnListener();

        return view;
    }

    /*@Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Memo memo){

        memoContent.add(cx);
        listAdapter.notifyDataSetChanged();
    }*/



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.listInterface = (ListInterface)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setData(List<Memo> datas) {
        this.data = datas;
    }

    private void setBtnListener() {
        btnPlus = view.findViewById(R.id.btnPlus);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnCond == 1) {
                    listInterface.goDetail();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Tanggalin");
                    builder.setMessage("\n" +
                            "Sigurado ka bang gusto mong tanggalin ang lahat ng mga naka-check na tala?");
                    builder.setCancelable(false).setPositiveButton("Oo", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(deleteList.size()!=0) {
                                for(int item : deleteList) {
                                    try {
                                        listInterface.delete(item);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                                setMultiSelectMode(false);
                            }
                            deleteList.clear();
                        }
                    }).setNegativeButton("Kanselahin", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteList.clear();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        btnSelect = view.findViewById(R.id.btnSelect);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state==true) {
                    setMultiSelectMode(true);
                } else {
                    setMultiSelectMode(false);
                }
            }
        });
    }

    private void setMultiSelectMode(boolean state) {
        if(state==true) {
            listAdapter = new ListAdapter(context, data, false);
            recyclerView.setAdapter(listAdapter);
            //btnSelect.setText(R.string.back);
            btnSelect.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_arrow_back_black_24dp));
            //btnPlus.setText("-");
            btnPlus.setImageResource(R.drawable.ic_delete_black_24dp);

            this.state = false;
            btnCond = 0;

        } else {
            listAdapter = new ListAdapter(context, data, true);
            recyclerView.setAdapter(listAdapter);
            //btnSelect.setText(R.string.edit);
            //btnPlus.setText("+");
            btnSelect.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_check_black_24dp));
            btnPlus.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_edit_black_24dp));
            deleteList.clear();
            this.state = true;
            refresh();
            btnCond = 1;
        }
    }

    public void refresh() {
        listAdapter = new ListAdapter(context, data);
        recyclerView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }
}
