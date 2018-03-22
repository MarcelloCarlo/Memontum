package com.example.jcgut.notethunder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.jcgut.notethunder.data.DBHelper;
import com.example.jcgut.notethunder.domain.Memo;
import com.example.jcgut.notethunder.interfaces.DetailInterface;
import com.example.jcgut.notethunder.interfaces.ListInterface;
import com.example.jcgut.notethunder.permission.PermissionControl;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListInterface, DetailInterface {

    public static final int REQ_PERM = 100;
    public static final int REQ_CAMERA = 101;
    public static final int REQ_GALLERY = 102;

    ListFragment list;
    DetailFragment detail;

    FrameLayout main;

    FragmentManager fm;

    List<Memo> datas;
    Dao<Memo, Long> memoDao;
    View mainView;
    int inflateConfig =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFragment();
        try {
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        list.setData(datas);
        checkPermission();

        if (inflateConfig == 1) {
            /*Intent lockscreenService = new Intent(this, LockScreenMemontumService.class);
            startService(lockscreenService);*/
           Intent lockscreenActivity = new Intent(this, LockScreenMomentumActivity.class);
           startActivity(lockscreenActivity);
        } else if (inflateConfig == 0){
            Snackbar.make(findViewById(R.id.relay),"Inflater on Lockscreen is Off",Snackbar.LENGTH_SHORT).setAction("action",null).show();
        } else {
            Snackbar.make(findViewById(R.id.relay), "Inflater settings unknown. Configure it on settings", Snackbar.LENGTH_SHORT).setAction("acxtion", null).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Settings")
                    .setMessage("You, Mimi Imfurst. Mimi Imfurst was number third in the voting? I could not believe it.")
                    .setPositiveButton("ON", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            inflateConfig = 1;
                            Snackbar.make(findViewById(R.id.relay), "Infalter is now enabled, you have nothing to do with it until forever", Snackbar.LENGTH_LONG).setAction("acxtion", null).show();
                        }
                    })
                    .setNegativeButton("OFF", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            inflateConfig = 0;
                            Snackbar.make(findViewById(R.id.relay),"Inflater on Lockscreen is Off",Snackbar.LENGTH_LONG).setAction("action",null).show();
                        }
                    })
                    .setIcon(R.drawable.ic_settings_black_24dp)
                    .show();
        }

        if (id == R.id.action_about) {
            Toast.makeText(this, "Developed By MarcelloBlue, JsPark, and the Teletubbies", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.action_inflate) {
            Snackbar.make(findViewById(R.id.relay), "C'mon Teletubby, Teleport us to Mars! ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            Intent lockscreenActivity = new Intent(this, LockScreenMomentumActivity.class);
            startActivity(lockscreenActivity);
        }
        return super.onOptionsItemSelected(item);
    }
    private void loadData() throws SQLException {
        DBHelper dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        memoDao = dbHelper.getMemoDao();
        datas = memoDao.queryForAll();
    }


    private void setList() {
        getFm();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.activity_main, list);
        ft.commit();
    }

    @Override
    public void goDetail() {
        detail = DetailFragment.newInstance();
        getFm();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.activity_main, detail);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void goDetail(long position) throws SQLException {
        Bundle bundle = new Bundle();
        getFm();
        FragmentTransaction ft = fm.beginTransaction();
        bundle.putInt("id", memoDao.queryForId(position).getId());
        bundle.putString("img",memoDao.queryForId(position).getImg());
        bundle.putString("title",memoDao.queryForId(position).getTitle());
        bundle.putString("content",memoDao.queryForId(position).getMemo());
        bundle.putString("date", String.valueOf(memoDao.queryForId(position).getDate()));
        detail.setArguments(bundle);
        Log.w("title", memoDao.queryForId(position).getTitle());
        Log.w("content", memoDao.queryForId(position).getMemo());
        ft.add(R.id.activity_main, detail);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void delete(int position) throws SQLException {
        Long positionL = (long)position;
        memoDao.delete(memoDao.queryForId(positionL));

        loadData();
        list.setData(datas);
        list.refresh();
    }

    @Override
    public void backToList() {
        super.onBackPressed();
    }

    @Override
    public void saveToList(Memo memo) throws SQLException {
        DBHelper dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        memoDao = dbHelper.getMemoDao();
        memoDao.create(memo);

        loadData();
        list.setData(datas);
        super.onBackPressed();
        list.refresh();
    }

    @Override
    public void update(long position, String imgUri, String strTitle, String strContent, Date newTime) throws SQLException {
        Memo memoTemp = memoDao.queryForId(position);
        memoTemp.setImg(imgUri);
        memoTemp.setTitle(strTitle);
        memoTemp.setMemo(strContent);
        memoTemp.setDate(newTime);
        memoDao.update(memoTemp);

        loadData();
        list.setData(datas);
        super.onBackPressed();
        list.refresh();
    }

    @Override
    public void delete(long position) throws SQLException {
        memoDao.delete(memoDao.queryForId(position));

        loadData();
        list.setData(datas);
        super.onBackPressed();
        list.refresh();
    }

    private void setFragment() {
        list = ListFragment.newInstance(1);
        detail = DetailFragment.newInstance();
        main = (FrameLayout)findViewById(R.id.activity_main);
    }

    private void checkPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean permBool = PermissionControl.checkPermission(this, REQ_PERM);
            if(permBool) {
                setList();
            }
        } else {
            setList();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQ_PERM) {
            if(PermissionControl.onCheckResult(grantResults)) {
                setList();
            } else {
                Toast.makeText(this, "Pagkatapos i-restart ang application, mangyaring sumang-ayon sa mga setting ng pahintulot", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void getFm() {
        fm = getSupportFragmentManager();
    }
}
