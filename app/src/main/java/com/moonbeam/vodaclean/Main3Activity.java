package com.moonbeam.vodaclean;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Main3Activity extends AppCompatActivity {
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
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
                    Intent i=new Intent(Main3Activity.this,MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                }
            }
        };
        n.start();
    }
}
