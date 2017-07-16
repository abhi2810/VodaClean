package com.moonbeam.vodaclean;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {
    private FloatingActionButton buttonChoose;
    private FloatingActionButton buttonUpload;

    private ImageView imageView;

    private EditText disc;
    private EditText city;
    private EditText loc;
    private EditText floor;

    private Bitmap bitmap;

    private int PICK_IMAGE_REQUEST = 1;

    private String UPLOAD_URL ="http://vodacleanserver3893.000webhostapp.com/picture.php";

    private String KEY_IMAGE = "image";
    private String KEY_DISC = "disc";
    private String KEY_LOC="location";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        buttonChoose = (FloatingActionButton) findViewById(R.id.buttonChoose);
        buttonUpload = (FloatingActionButton) findViewById(R.id.buttonUpload);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        disc = (EditText) findViewById(R.id.editText);
        city = (EditText) findViewById(R.id.editText1);
        loc = (EditText) findViewById(R.id.editText2);
        floor=(EditText) findViewById(R.id.editText3);

        imageView  = (ImageView) findViewById(R.id.imageView);

        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
                disc.requestFocus();
            }
        });
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(Main2Activity.this, s , Toast.LENGTH_LONG).show();
                        disc.setText("");
                        city.setText("");
                        loc.setText("");
                        floor.setText("");
                        imageView.setImageResource(0);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(Main2Activity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name
                String dis = disc.getText().toString().trim();
                String loca=city.getText().toString()+", "+loc.getText().toString().trim()+", "+floor.getText().toString().trim();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String t = sdf.format(new Date());
                String time=""+t.substring(9,11)+"H"+t.substring(11,13)+"M, "+t.substring(6,8)+"/"+t.substring(4,6)+"/"+t.substring(0,4);
                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_DISC, dis);
                params.put(KEY_LOC,loca);
                params.put("date",time);
                //returning parameter
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater om=getMenuInflater();
        om.inflate(R.menu.main2,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Created by-");
                dialog.setMessage("\tLovely Singh\n\t11211636");
                dialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(Main2Activity.this,"Thanks",Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
                break;
            case R.id.item2:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                break;
            case R.id.action_settings:
                AlertDialog.Builder dial = new AlertDialog.Builder(this);
                dial.setTitle("Do You Want to LogOut?");
                dial.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(Main2Activity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(Main2Activity.this,MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(i);
                            }
                        });
                dial.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog alertDial = dial.create();
                alertDial.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public MenuInflater getMenuInflater() {
        return super.getMenuInflater();
    }
}
