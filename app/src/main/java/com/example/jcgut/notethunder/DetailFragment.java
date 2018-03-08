package com.example.jcgut.notethunder;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.jcgut.notethunder.domain.Memo;
import com.example.jcgut.notethunder.interfaces.DetailInterface;

import java.sql.SQLException;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.example.jcgut.notethunder.MainActivity.REQ_CAMERA;
import static com.example.jcgut.notethunder.MainActivity.REQ_GALLERY;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    public ImageView imgThumb;
    Button btnSave, btnCancel, btnDelete;
    Button btnCamera, btnGallery;
    public EditText editTitle, editContent;

    View view = null;
    Context context = null;
    DetailInterface detailInterface = null;

    int id = 0;
    Uri fileUri = null;
    String title = "";
    String content = "";
    String date = "";

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance() {
        DetailFragment fragment = new DetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(view==null) {
            view =inflater.inflate(R.layout.fragment_detail, container, false);
            imgThumb = (ImageView)view.findViewById(R.id.getImg);
            editTitle = (EditText)view.findViewById(R.id.editTitle);
            editContent = (EditText)view.findViewById(R.id.editContent);
            btnSave = (Button)view.findViewById(R.id.btnSave);
            btnCancel = (Button)view.findViewById(R.id.btnCancel);
            btnDelete = (Button)view.findViewById(R.id.btnDelete);
            btnCamera = (Button)view.findViewById(R.id.btnCamera);
            btnGallery = (Button)view.findViewById(R.id.btnGallery);

            editTitle.setText("");
            editContent.setText("");

            btnSave.setOnClickListener(listener);
            btnCancel.setOnClickListener(listener);
            btnDelete.setOnClickListener(listener);
            btnCamera.setOnClickListener(listener);
            btnGallery.setOnClickListener(listener);
        }
        if (getArguments() != null) {
            btnSave.setText("UPDATE");
            btnDelete.setVisibility(View.VISIBLE);
            id = getArguments().getInt("id");
            fileUri = Uri.parse(new String(getArguments().getString("img")));
            title = new String(getArguments().getString("title"));
            content = new String(getArguments().getString("content"));
            date = new String(getArguments().getString("date"));
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.detailInterface = (DetailInterface)context;
    }

    @Override
    public void onResume() {
        super.onResume();
        Glide.with(context).load(fileUri).placeholder(R.drawable.ic_image_black_24dp).into(imgThumb);
        editTitle.setText(title);
        editContent.setText(content);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = null;
            switch (v.getId()) {
                case R.id.btnSave :
                    try {
                        if(getArguments()!=null) {
                            String strTitle = editTitle.getText().toString();
                            String strContent = editContent.getText().toString();
                            Date newTime = new Date(System.currentTimeMillis());
                            try {
                                detailInterface.update(id, String.valueOf(fileUri), strTitle, strContent, newTime);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else {
                            detailInterface.saveToList(makeMemo());
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.btnCancel :
                    detailInterface.backToList();
                    break;
                case R.id.btnDelete :
                    try {
                        detailInterface.delete(id);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.btnCamera :
                    title = editTitle.getText().toString();
                    content = editContent.getText().toString();
                    i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i, REQ_CAMERA);
                    break;
                case R.id.btnGallery :
                    title = editTitle.getText().toString();
                    content = editContent.getText().toString();
                    i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    i.setType("image/*");
                    startActivityForResult( Intent.createChooser(i,"Select Picture") , REQ_GALLERY);
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CAMERA :
                if(resultCode==RESULT_OK) {
                    fileUri = data.getData();
                    if(fileUri!=null) {
                        Glide.with(context).load(fileUri).into(imgThumb);
                    }
                }
                break;
            case REQ_GALLERY :
                if(resultCode==RESULT_OK) {
                    fileUri = data.getData();
                    if(fileUri!=null) {
                        Log.w("img", "change");
                        Glide.with(context).load(fileUri).into(imgThumb);
                    }
                }
                break;
        }
    }

    private Memo makeMemo() {
        Memo memo = new Memo();
        memo.setImg(String.valueOf(fileUri));
        memo.setTitle(editTitle.getText().toString());
        memo.setMemo(editContent.getText().toString());
        memo.setDate(new Date(System.currentTimeMillis()));
        return memo;
    }
}
