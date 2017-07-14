package com.moonbeam.vodaclean;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;

public class Signup extends AppCompatActivity {
    EditText ed1,ed2,ed3,ed4;
    String emp,name,pass,cpass;
    WebView web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ed1=(EditText)findViewById(R.id.empid);
        ed2=(EditText)findViewById(R.id.name);
        ed3=(EditText)findViewById(R.id.pass);
        ed4=(EditText)findViewById(R.id.cpass);
        web=(WebView)findViewById(R.id.web);
    }
    public void sign(View v) throws IOException {
        emp=ed1.getText().toString();
        name=ed2.getText().toString();
        pass=ed3.getText().toString();
        cpass=ed4.getText().toString();
        if (isInternetAvailable()){
            if(emp.equals("")||pass.equals("")||name.equals("")||cpass.equals(""))
                Toast.makeText(this, "Fields can't be empty", Toast.LENGTH_SHORT).show();
            else {
                if (pass.equals(cpass)) {
                    emp = URLEncoder.encode(emp, "UTF-8");
                    pass = URLEncoder.encode(pass, "UTF-8");
                    name = URLEncoder.encode(name, "UTF-8");
                    String response = "0";
                    String wsite = "http://wsite/signup.php?read=1&emp=" + emp;
                    URL url = new URL(wsite);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true);
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    response = reader.readLine();
                    if (response.equals("0")) {
                        web.loadUrl("http://wsite/signup.php?read=0&emp=" + emp + "&pass=" + pass);
                        Intent i = new Intent(Signup.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    } else {
                        Toast.makeText(Signup.this, "Employee ID already in use", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    ed3.setText("");
                    ed4.setText("");
                }
            }
        }else{
            Toast.makeText(Signup.this,"Internet not Connected",Toast.LENGTH_LONG).show();
        }
    }
    public boolean isInternetAvailable() {
        try {
            final InetAddress address = InetAddress.getByName("www.google.com");
            return !address.equals("");
        } catch (UnknownHostException e) {}
        return false;
    }
}
