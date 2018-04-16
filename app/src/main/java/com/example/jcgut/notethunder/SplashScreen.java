package com.example.jcgut.notethunder;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        Thread splash = new Thread(){
            @Override
                    public void  run(){
                        try {
                            sleep(900);
                            Intent  nextActivity = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(nextActivity);
                            finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
            }
        };
        splash.start();
    }
}
