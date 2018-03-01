package com.example.jcgut.notethunder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int TAKE_PHOTO_COUNT = 0;
    public static int imgCount = 0;
    RelativeLayout relativeNotesLayout;
    TextView txtWelcome;
    ImageButton btnImgCamera, btnImgVoice;
    View mainView;
    Uri uriFileOutput;
    ImageView imgCardNote;
    String dir = "";
    MemoDatabaseOpenHelper memoHelper;
    MemoDatabaseAccess memoDatabaseAccess;
    List<MemoSerialize> memoDBList;
    RecyclerView recViewMemos;
    RecyclerView.Adapter adapterMemo;
    RecyclerView.LayoutManager layoutManagerMemo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LayoutInflater mainLayout = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = mainLayout.inflate(R.layout.activity_main, null);
//        imgCardNote = findViewById(R.id.imageViewNote);
//        imgCardNote.setImageURI(null);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // txtWelcome = findViewById(R.id.txtWelcomeInfo);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Dagdag Note Ala-Tumblr Aesthetic", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
               Intent intent = new Intent(MainActivity.this, LockScreenNoteThunderActivity.class);
                startActivity(intent);

                txtWelcome.setText(R.string.welcome1);*/
                onAddMemoClicked();
            }
        });

        Intent intentService = new Intent(this, LockScreenMemontumService.class);
        startService(intentService);

       /* Intent intentActivity = new Intent(this,LockScreenNoteThunderActivity.class);
        startActivity(intentActivity);*/

        dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/NoteThunder/";
        File newdir = new File(dir);
        newdir.mkdirs();

        //btnImgCamera = findViewById(R.id.btnAddImageNote);
       /* btnImgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgCount++;
                String file = dir + "NoteImage" + imgCount + ".jpg";
                File newPic = new File(file);
                try {
                    newPic.createNewFile();
                } catch (IOException e) {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }

                uriFileOutput = Uri.fromFile(newPic);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFileOutput);
                startActivityForResult(cameraIntent, TAKE_PHOTO_COUNT);

            }
        });*/

        memoDatabaseAccess = new MemoDatabaseAccess(this);
        memoDBList = new ArrayList<MemoSerialize>();
        memoDBList = memoDatabaseAccess.getAllMemos();

        recViewMemos = findViewById(R.id.recviewMemos);
        recViewMemos.setHasFixedSize(true);

        layoutManagerMemo = new LinearLayoutManager(this);
        recViewMemos.setLayoutManager(layoutManagerMemo);

        adapterMemo = new MemoRecyclerAdapter(this, memoDBList);
        recViewMemos.setAdapter(adapterMemo);
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent intentData) {
        super.onActivityResult(reqCode, resCode, intentData);

        if (reqCode == TAKE_PHOTO_COUNT && resCode == RESULT_OK) {

            Log.d("ImageCapture", "Picture Note Saved");
        }

        uriFileOutput = Uri.parse(dir + "NoteImage" + imgCount + ".jpg");
        imgCardNote.setImageURI(uriFileOutput);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onAddMemoClicked() {
        Intent intentAddEditMemo = new Intent(this, AddEditMemoActivity.class);
        startActivity(intentAddEditMemo);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            txtWelcome.setText(R.string.welcome2);
            Toast.makeText(this, "Wala Pa.", Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.action_about) {
            Toast.makeText(this, "Wala Paring Laman GOSH!", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.action_releaseNotepad) {


            Snackbar.make(mainView, "Image Saved", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            Intent intentLockActivity = new Intent(MainActivity.this, LockScreenNoteThunderActivity.class);
            startActivity(intentLockActivity);

        }
        return super.onOptionsItemSelected(item);
    }

}
