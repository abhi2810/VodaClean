package com.moonbeam.vodaclean;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    EditText ed1,ed2,ed3,ed4,ed5;
    String emp,name,pass,cpass,mail;
    WebView web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ed1=(EditText)findViewById(R.id.empid);
        ed2=(EditText)findViewById(R.id.name);
        ed3=(EditText)findViewById(R.id.pass);
        ed4=(EditText)findViewById(R.id.cpass);
        ed5=(EditText)findViewById(R.id.email);
        web=(WebView)findViewById(R.id.web);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void sign(View v) {
        emp=ed1.getText().toString();
        name=ed2.getText().toString();
        pass=ed3.getText().toString();
        cpass=ed4.getText().toString();
        mail=ed5.getText().toString();
        if (isInternetAvailable()){
            if(emp.equals("")||pass.equals("")||name.equals("")||cpass.equals("")||mail.equals(""))
                Toast.makeText(this, "Fields can't be empty", Toast.LENGTH_SHORT).show();
            else {
                if (name.indexOf("\"") == -1 && name.indexOf("<") == -1 && emp.indexOf("\"") == -1 && emp.indexOf(">") == -1) {
                    if (pass.length() >= 8 && pass.indexOf("\"") == -1 && pass.indexOf(">") == -1) {
                        if (isValidEmail(mail) && mail.endsWith("@vodafone.com")) {
                            if (pass.equals(cpass)) {
                                emp = URLEncoder.encode(emp);
                                pass = URLEncoder.encode(pass);
                                name = URLEncoder.encode(name);
                                final ProgressDialog loading = ProgressDialog.show(this, "Signing Up...", "Please wait...", false, false);
                                String response = "0";
                                String wsite = "http://vodacleanserver3893.000webhostapp.com/signup.php?read=1&emp=" + emp;
                                try {
                                    URL url = new URL(wsite);
                                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                                    urlConnection.setRequestMethod("POST");
                                    urlConnection.setDoOutput(true);
                                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                                    response = reader.readLine();
                                    if (response.equals("0")) {
                                        loading.dismiss();
                                        web.loadUrl("http://vodacleanserver3893.000webhostapp.com/signup.php?read=0&emp=" + emp + "&pass=" + pass + "&mail=" + mail);
                                        Intent i = new Intent(Signup.this, MainActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                    } else {
                                        loading.dismiss();
                                        Toast.makeText(Signup.this, "Employee ID already in use", Toast.LENGTH_LONG).show();
                                    }
                                } catch (IOException e) {
                                    loading.dismiss();
                                    Toast.makeText(this, "Issue with internet!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
                                ed3.setText("");
                                ed4.setText("");
                            }
                        } else {
                            Toast.makeText(this, "Invalid Email Id, Please enter a valid Vodafone Email ID.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Invalid Password, password should be of atleast 8 characters and can only have a-z,0-9,@ and _", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Really XSS injection on company property, Really!!", Toast.LENGTH_SHORT).show();
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
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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
                LayoutInflater factory = LayoutInflater.from(Signup.this);
                final View view = factory.inflate(R.layout.dialog_main, null);

                dialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(Signup.this,"Thanks",Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog.setView(view);
                dialog.show();
                break;
            case R.id.item2:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public MenuInflater getMenuInflater() {
        return super.getMenuInflater();
    }
}
