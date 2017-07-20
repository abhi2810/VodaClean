package com.moonbeam.vodaclean;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ResetPassword extends AppCompatActivity {
    EditText emp;
    String empl;
    WebView web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        emp=(EditText)findViewById(R.id.emplo);
        web=(WebView)findViewById(R.id.webv);

    }
    public void reset(View v){
        empl=emp.getText().toString();
        if(empl.equals("")){
            Toast.makeText(this, "Employee ID can't be empty.", Toast.LENGTH_SHORT).show();
        }else{
            final ProgressDialog loading = ProgressDialog.show(this,"Sending mail...","Please wait...",false,false);
            empl= URLEncoder.encode(empl);
            String response = "0";
            String wsite = "http://vodacleanserver3893.000webhostapp.com/signup.php?read=1&emp=" + empl;
            try {
                URL url = new URL(wsite);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                response = reader.readLine();
                if (!response.equals("0")) {
                    loading.dismiss();
                    web.loadUrl("http://vodacleanserver3893.000webhostapp.com/reset.php?&emp=" + empl);
                    Toast.makeText(this, "Reset Mail sent to your Account.", Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(this,MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                } else {
                    loading.dismiss();
                    Toast.makeText(this, "Employee ID doesn't exist.", Toast.LENGTH_SHORT).show();
                }
            }catch (IOException e){
                loading.dismiss();
                Toast.makeText(this, "Issue with internet!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater om=getMenuInflater();
        om.inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Powered by-");
                LayoutInflater factory = LayoutInflater.from(ResetPassword.this);
                final View view = factory.inflate(R.layout.dialog_main, null);

                dialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(ResetPassword.this,"Thanks",Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog.setView(view);
                dialog.show();
                break;
            case R.id.item2:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public MenuInflater getMenuInflater() {
        return super.getMenuInflater();
    }
}
