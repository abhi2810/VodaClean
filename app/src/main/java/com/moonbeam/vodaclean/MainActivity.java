package com.moonbeam.vodaclean;

import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    EditText ed1,ed2;
    String emp,pass;
    boolean doubleBackToExitPressedOnce=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ed1=(EditText)findViewById(R.id.emp);
        ed2=(EditText)findViewById(R.id.pass);
    }
    public void login(View v) throws IOException {
        emp=ed1.getText().toString();
        pass=ed2.getText().toString();
        if (isInternetAvailable()){
            if(emp.equals("")||pass.equals(""))
                Toast.makeText(this, "EmployeeID or Password can't be empty", Toast.LENGTH_SHORT).show();
            else{
                String response = "0";
                String wsite = "http://vodacleanserver3893.000webhostapp.com/login.php?emp=" + emp + "&pass=" + pass;
                URL url = new URL(wsite);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                response = reader.readLine();
                if (!response.equals("0")) {
                    Intent i = new Intent(MainActivity.this, Main2Activity.class);
                    i.putExtra("ID", response + emp);
                    ed1.setText("");
                    ed2.setText("");
                    startActivity(i);
                }else{
                    Toast.makeText(this, "Incorrect Employee ID or Password", Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            Toast.makeText(MainActivity.this,"Internet not Connected",Toast.LENGTH_LONG).show();
        }
    }
    public void signup(View v){
        Intent i=new Intent(MainActivity.this,Signup.class);
        startActivity(i);
    }
    public boolean isInternetAvailable() {
        try {
            final InetAddress address = InetAddress.getByName("www.google.com");
            return !address.equals("");
        } catch (UnknownHostException e) {}
        return false;
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            moveTaskToBack(true);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
