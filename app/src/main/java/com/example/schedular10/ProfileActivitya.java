package com.example.schedular10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivitya extends AppCompatActivity {

    Button logout;
    String userid;
    ImageView prof_IMG;
    TextView prof_TEXT, profn, profe;
    String encodedpicture;
    String oldpic;


    String api_get_data = "https://oakspro.com/projects/project40/janardhan/Schedular/get_profile_details.php";
    String api_send_data="https://oakspro.com/projects/project40/janardhan/Schedular/upload_prof_img.php";
    String web_address_img="https://oakspro.com/projects/project40/janardhan/Schedular/profile_pics/";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activitya);


        logout=findViewById(R.id.logout_app);


        prof_TEXT = findViewById(R.id.profile_text_name);
        prof_IMG = findViewById(R.id.profile_pic_image);
        profn = findViewById(R.id.PROF_NAME);
        profe= findViewById(R.id.PROF_EMAIL);

        SharedPreferences sp = getSharedPreferences("secret", 0);
        userid = sp.getString("v_value", "");

        prof_TEXT.setText(userid);


        getDataProfile(userid);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences login = getSharedPreferences("auth", 0);
                SharedPreferences.Editor editor =login.edit();
                editor.clear();
                editor.commit();
                SharedPreferences sp = getSharedPreferences("secret", 0);
                SharedPreferences.Editor EEditor = sp.edit();
                EEditor.clear();
                EEditor.commit();

                Intent intent = new Intent(ProfileActivitya.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });


        BottomNavigationView bottom_profile = findViewById(R.id.bottom_navh_prof);

        bottom_profile.setSelectedItemId(R.id.PROFILE);

        bottom_profile.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.HOME:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.TTABLE:
                        startActivity(new Intent(getApplicationContext(), TimeActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.TASK:
                        startActivity(new Intent(getApplicationContext(), TaskActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.EXAMS:
                        startActivity(new Intent(getApplicationContext(), ExamActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.PROFILE:
                        return true;
                }
                return false;
            }
        });







        prof_IMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dexter.withContext(ProfileActivitya.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                               Intent intent = new Intent();
                               intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                               Uri uri = Uri.fromParts("package",getPackageName(), null);
                               intent.setData(uri);
                               startActivity(intent);
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.cancelPermissionRequest();
                            }
                        }).check();

            }
        });

    }//end of profile activity

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==1 && resultCode==RESULT_OK && data!=null){
            Uri filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                prof_IMG.setImageBitmap(bitmap);
                imageprocess(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void imageprocess(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        encodedpicture=android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Uploadprofile(encodedpicture, userid, oldpic);

    }

    private void Uploadprofile(String encodedpicture, String userid, String oldpic) {
        ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Image...please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        progressDialog.show();

        ProgressDialog finalProgressDialog = progressDialog;

        StringRequest requestdown = new StringRequest(Request.Method.POST, api_send_data, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(ProfileActivitya.this, "Success", Toast.LENGTH_SHORT).show();
                finalProgressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileActivitya.this, "Check Network", Toast.LENGTH_SHORT).show();
                finalProgressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> upd = new HashMap<>();
                upd.put("profile_pic", encodedpicture);
                upd.put("userid", userid);
                upd.put("oldpic", oldpic);
                return upd;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivitya.this);
        requestQueue.add(requestdown);
    }

    private void getDataProfile(String userid) {

        StringRequest getdata = new StringRequest(Request.Method.POST, api_get_data, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String pro_name="";
                String pro_email="";
                String pro_img=null;

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("details");

                    if (status.equals("1")){
                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject object0 = jsonArray.getJSONObject(i);
                            pro_img = object0.getString("profile_img");
                            pro_name = object0.getString("profile_name");
                            pro_email = object0.getString("profile_email");
                            oldpic=pro_img;
                        }

                        profn.setText(pro_name);
                        profe.setText(pro_email);
                        if (!pro_img.equals(null)){

                            String pic_address=web_address_img+pro_img.toString();
                            Picasso.with(ProfileActivitya.this).load(pic_address).memoryPolicy(MemoryPolicy.NO_STORE).networkPolicy(NetworkPolicy.NO_CACHE).into(prof_IMG);
                        }else {
                            Toast.makeText(ProfileActivitya.this, "Please upload image", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(ProfileActivitya.this, "No data found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileActivitya.this, "json"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileActivitya.this, "Internet Error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String > upl = new HashMap<>();
                upl.put("userid", userid);
                return upl;
            }
        };

        RequestQueue requestQueue =Volley.newRequestQueue(ProfileActivitya.this);
        requestQueue.add(getdata);

    }
}