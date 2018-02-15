package com.example.jcgut.notethunder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    TextView txtWelcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtWelcome = findViewById(R.id.txtWelcomeInfo);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Dagdag Note Ala-Tumblr Aesthetic", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
               /* Intent intent = new Intent(MainActivity.this, LockScreenNoteThunderActivity.class);
                startActivity(intent);*/

               txtWelcome.setText(R.string.welcome1);
            }
        });

        Intent intentService = new Intent(this, LockScreenNoteThunderService.class);
        startService(intentService);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
            txtWelcome.setText(R.string.welcome2);
            Toast.makeText(this, "Wala Pa.", Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.action_about) {
            Toast.makeText(this, "Wala Paring Laman GOSH!", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.action_releaseNotepad) {

           /* MenuItem release = findViewById(R.id.action_releaseNotepad);
            release.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {


                    return true;
                }
            });*/

        }

        return super.onOptionsItemSelected(item);
    }
}
