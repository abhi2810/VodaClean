package com.moonbeam.vodaclean;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class Main3Activity extends AppCompatActivity {
    ImageView img;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        sp=getSharedPreferences("login", Context.MODE_PRIVATE);
        img=(ImageView)findViewById(R.id.imageView2);
        Animation a= AnimationUtils.loadAnimation(Main3Activity.this,R.anim.zoom);
        img.startAnimation(a);
        Thread n=new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    sleep(5000);
                }catch (InterruptedException e){

                }finally {
                    if(sp.getString("log",null)==null) {
                        Intent i = new Intent(Main3Activity.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                    }else{
                        Intent i = new Intent(Main3Activity.this, MainMenu.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                    }
                }
            }
        };
        n.start();
    }
}
