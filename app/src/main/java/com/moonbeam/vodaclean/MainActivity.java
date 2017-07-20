package com.moonbeam.vodaclean;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
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
import java.net.URLEncoder;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    EditText ed1,ed2;
    String emp,pass;
    boolean doubleBackToExitPressedOnce=false;
    TextView rpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ed1=(EditText)findViewById(R.id.emp);
        ed2=(EditText)findViewById(R.id.pass);
        rpass=(TextView)findViewById(R.id.resetpass);
        rpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,ResetPassword.class);
                startActivity(i);
            }
        });
    }
    public void login(View v) {
        emp=ed1.getText().toString();
        pass=ed2.getText().toString();
        if (isInternetAvailable()){
            if(emp.equals("")||pass.equals(""))
                Toast.makeText(this, "EmployeeID or Password can't be empty", Toast.LENGTH_SHORT).show();
            else{
                final ProgressDialog loading = ProgressDialog.show(this,"Logging in...","Please wait...",false,false);
                pass= URLEncoder.encode(pass);
                emp=URLEncoder.encode(emp);
                String response = "0";
                String wsite = "http://vodacleanserver3893.000webhostapp.com/login.php?emp=" + emp + "&pass=" + pass;
                try {
                    URL url = new URL(wsite);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true);
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    response = reader.readLine();
                    if(response.equals("n")) {
                        loading.dismiss();
                        Toast.makeText(this, "Account has yet to be authenticated from registered Email id", Toast.LENGTH_SHORT).show();
                    }else if (!response.equals("0")) {
                            loading.dismiss();
                            Intent i = new Intent(MainActivity.this, Main2Activity.class);
                            i.putExtra("ID", response + emp);
                            ed1.setText("");
                            ed2.setText("");
                            startActivity(i);
                    }else {
                        loading.dismiss();
                        Toast.makeText(this, "Incorrect Employee ID or Password", Toast.LENGTH_SHORT).show();
                    }
                }catch(IOException e){
                    loading.dismiss();
                    Toast.makeText(this, "Issue with internet!", Toast.LENGTH_SHORT).show();
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
                LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                final View view = factory.inflate(R.layout.dialog_main, null);

                dialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(MainActivity.this,"Thanks",Toast.LENGTH_SHORT).show();
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
