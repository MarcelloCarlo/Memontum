package com.example.jcgut.notethunder;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.jcgut.notethunder.domain.Memo;
import com.example.jcgut.notethunder.interfaces.DetailInterface;

import java.sql.SQLException;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.jcgut.notethunder.MainActivity.REQ_CAMERA;
import static com.example.jcgut.notethunder.MainActivity.REQ_GALLERY;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    public ImageView imgThumb;
    Button btnSave, btnCancel,btnPin/*, btnDelete*/;
    Button btnCamera, btnGallery;
    public EditText editTitle, editContent;

    View view = null;
    Context context = null;
    DetailInterface detailInterface = null;

    int id = 0;
    Uri fileUri = null;
    Bitmap imgX;
    String title = "";
    String content = "";
    String date = "";
    String CHANNEL_ID = "001";
    CharSequence chName = "Notes on Fly";

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
            imgThumb = view.findViewById(R.id.getImg);
            editTitle = view.findViewById(R.id.editTitle);
            editContent = view.findViewById(R.id.editContent);
            btnSave = view.findViewById(R.id.btnSave);
            btnCancel = view.findViewById(R.id.btnCancel);
           /* btnDelete = (Button)view.findViewById(R.id.btnDelete);*/
            btnCamera = view.findViewById(R.id.btnCamera);
            btnGallery = view.findViewById(R.id.btnGallery);
            btnPin = view.findViewById(R.id.btnPin);


            editTitle.setText("");
            editContent.setText("");

            btnSave.setOnClickListener(listener);
            btnCancel.setOnClickListener(listener);
           /* btnDelete.setOnClickListener(listener);*/
            btnCamera.setOnClickListener(listener);
            btnGallery.setOnClickListener(listener);
            btnPin.setOnClickListener(listener);
        }
        if (getArguments() != null) {
            btnSave.setText("UPDATE");
           btnPin.setVisibility(View.VISIBLE);
            id = getArguments().getInt("id");
            fileUri = Uri.parse(getArguments().getString("img"));
            title = getArguments().getString("title");
            content = getArguments().getString("content");
            date = getArguments().getString("date");
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
        @RequiresApi(api = Build.VERSION_CODES.O)
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
                case R.id.btnPin :
                    try {
                        /*BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(String.valueOf(fileUri),false);
                        Bitmap region = decoder.decodeRegion(new Rect(10, 10, 50, 50), null);*/
                        imgX = MediaStore.Images.Media.getBitmap(context.getContentResolver(), fileUri);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            int importance = NotificationManager.IMPORTANCE_HIGH;
                            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, chName, importance);
                            Notification notification = new NotificationCompat.Builder(context,CHANNEL_ID)
                                    .setSmallIcon(R.drawable.ic_event_note_black_24dp)
                                    .setContentTitle(title)
                                    .setContentText(content)
                                    .setLargeIcon(imgX)
                                    .setStyle(new NotificationCompat.BigPictureStyle()
                                            .bigPicture(imgX)
                                            .bigLargeIcon(null))
                                    .setChannelId(CHANNEL_ID).build();

                            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                            mNotificationManager.createNotificationChannel(mChannel);
                            // Issue the notification.
                            mNotificationManager.notify(1, notification);
                        } else {
                            Notification notification = new Notification.Builder(context)
                                    .setSmallIcon(R.drawable.ic_event_note_black_24dp)
                                    .setContentTitle(chName +": "+title)
                                    .setContentText(content)
                                    .setLargeIcon(imgX)
                                    .setPriority(Notification.PRIORITY_MAX)
                                    .build();

                            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                            notificationManager.notify(001, notification);
                        }

                        } catch (Exception e) {
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
