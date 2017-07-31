package com.moonbeam.vodaclean;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

public class HSW extends AppCompatActivity {
    private FloatingActionButton buttonChoose;
    private FloatingActionButton buttonUpload;

    private ImageView imageView;

    private EditText disc;
    private MaterialBetterSpinner city;
    private MaterialBetterSpinner loc;
    private MaterialBetterSpinner floor;
    private MaterialBetterSpinner materialDesignSpinner;
    private Bitmap bitmap;

    private int PICK_IMAGE_REQUEST = 1;
    SharedPreferences sp;
    private String UPLOAD_URL ="http://vodacleanserver3893.000webhostapp.com/picture.php";

    private String KEY_IMAGE = "image";
    private String KEY_DISC = "disc";
    private String KEY_LOC="location";
    String[] SPINNERLIST = {"Dangerous working condition:", "Incident report:", "Medical emergency:"};
    String[] cit={"Ahamdabad","Bangalore","Pune"};
    String[] Pune={"Mantri","EON"};
    String[] Bangalore={"GTP","PTP"};
    String[] Ahamdabad={"Vodafone house"};
    String[] Mantri={"3A","3B","4A","4B"};
    String[] EON={"Cluster D 1st","Cluster D 2nd","Cluster D 3rd","Cluster B 1st"};
    String[] GTP={"9A","10A","10B"};
    String[] PTP={"5th Floor"};
    String[] Voda={"Building A 1st","Building A 2nd","Building A 3rd","Building A 4th","Building B 1st","Building B 2nd","Building B 3rd","Building B 4th","Building B 5th","Building B 6th","Building B 7th","Building B 8th","Building B 9th","Building B 10th"};
    String[] nu={"Select a city"};
    String[] nul={"Select a building"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hsw);
        sp=getSharedPreferences("login",Context.MODE_PRIVATE);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        ArrayAdapter<String> ci = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, cit);
        final ArrayAdapter<String> pu = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, Pune);
        final ArrayAdapter<String> ban = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, Bangalore);
        final ArrayAdapter<String> aham = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, Ahamdabad);
        final ArrayAdapter<String> man = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, Mantri);
        final ArrayAdapter<String> eo = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, EON);
        final ArrayAdapter<String> pt = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, PTP);
        final ArrayAdapter<String> gt = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, GTP);
        final ArrayAdapter<String> vo = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, Voda);
        ArrayAdapter<String> n1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, nu);
        final ArrayAdapter<String> n2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, nul);
        materialDesignSpinner = (MaterialBetterSpinner)
                findViewById(R.id.android_material_design_spinner);
        materialDesignSpinner.setAdapter(arrayAdapter);
        buttonChoose = (FloatingActionButton) findViewById(R.id.buttonChoose);
        buttonUpload = (FloatingActionButton) findViewById(R.id.buttonUpload);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        disc = (EditText) findViewById(R.id.editText);
        city = (MaterialBetterSpinner) findViewById(R.id.editText1);
        loc = (MaterialBetterSpinner) findViewById(R.id.editText2);
        floor=(MaterialBetterSpinner) findViewById(R.id.editText3);

        imageView  = (ImageView) findViewById(R.id.imageView);
        city.setAdapter(ci);
        loc.setAdapter(n1);
        floor.setAdapter(n2);
        city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                loc.setText("");
                String c=city.getText().toString();
                if(c.equals("Pune"))
                    loc.setAdapter(pu);
                else if(c.equals("Bangalore"))
                    loc.setAdapter(ban);
                else if(c.equals("Ahamdabad"))
                    loc.setAdapter(aham);
                floor.setText("");
                floor.setAdapter(n2);
            }
        });
        loc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                floor.setText("");
                String lo=loc.getText().toString();
                if(lo.equals("Mantri"))
                    floor.setAdapter(man);
                else if(lo.equals("EON"))
                    floor.setAdapter(eo);
                else if(lo.equals("GTP"))
                    floor.setAdapter(gt);
                else if(lo.equals("PTP"))
                    floor.setAdapter(pt);
                else if(lo.equals("Vodafone house"))
                    floor.setAdapter(vo);

            }
        });
        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(disc.getText().toString().equals("")||city.getText().toString().equals("")||floor.getText().toString().equals("")||loc.getText().toString().equals("")||materialDesignSpinner.getText().toString().equals("")) {
                    Toast.makeText(HSW.this, "Fields Can't be empty!", Toast.LENGTH_SHORT).show();
                }else
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
        if(bmp!=null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        }else{
            bmp= BitmapFactory.decodeResource(getResources(), R.drawable.noimage);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        }
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
                        Toast.makeText(HSW.this, s , Toast.LENGTH_LONG).show();
                        disc.setText("");
                        city.setText("");
                        loc.setText("");
                        floor.setText("");
                        materialDesignSpinner.setText("");
                        imageView.setImageResource(0);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        if(volleyError.getMessage()!=null)
                            Toast.makeText(HSW.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                        else{
                            disc.setText("");
                            city.setText("");
                            loc.setText("");
                            floor.setText("");
                            materialDesignSpinner.setText("");
                            imageView.setImageResource(0);
                            Toast.makeText(HSW.this, "Succesfully Submitted!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name
                String topic=materialDesignSpinner.getText().toString();
                String dis = topic+"\n"+disc.getText().toString().trim();
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
                params.put("Type","HSW");
                params.put("emp",sp.getString("log",null));
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
        MenuItem im=menu.findItem(R.id.item0);
        im.setTitle(sp.getString("log",null));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Powered by-");
                LayoutInflater factory = LayoutInflater.from(HSW.this);
                final View view = factory.inflate(R.layout.dialog_main, null);

                dialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(HSW.this,"Thanks",Toast.LENGTH_SHORT).show();
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
            case R.id.action_settings:
                AlertDialog.Builder dial = new AlertDialog.Builder(this);
                dial.setTitle("Do You Want to LogOut?");
                dial.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                sp.edit().clear().commit();
                                Toast.makeText(HSW.this, "Logged Out", Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(HSW.this,MainActivity.class);
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
