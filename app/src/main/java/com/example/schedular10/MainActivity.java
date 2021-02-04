package com.example.schedular10;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button Loginn, signup, createbtn;
    String login_api="https://oakspro.com/projects/project40/janardhan/Schedular/login.php";
    String signup_api="https://oakspro.com/projects/project40/janardhan/Schedular/scheduar_registration.php";

    String api_pswdrequest="https://oakspro.com/projects/project40/janardhan/Schedular/forget_passwd.php";


    TextView u_n_ame, u_p_word;




    TextInputEditText profn, usern, namen, mobilen, emailn, passwdn, cpasswdn;

//    TextView usernamee, passwordd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences login = getSharedPreferences("auth", 0);
        String log = login.getString("u_name", "");
        String pas = login.getString("u_pass", "");

        if (!log.isEmpty() && !pas.isEmpty()){
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }




        Loginn = findViewById(R.id.login_botton);
        signup = findViewById(R.id.signup_botton);

        Loginn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
                bottomSheetDialog.setCanceledOnTouchOutside(false);

                TextInputEditText usern, userp;
                Button Login;

//                usernamee=bottomSheetDialog.findViewById(R.id.USER_USERNAME);
//                passwordd=bottomSheetDialog.findViewById(R.id.USER_PASSWORD);

                usern= bottomSheetDialog.findViewById(R.id.login_user_name);
                userp= bottomSheetDialog.findViewById(R.id.login_user_pass);
                Login = bottomSheetDialog.findViewById(R.id.loginbtn);





                Login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String user_n = usern.getText().toString();
                        String user_p = userp.getText().toString();

//                      Toast.makeText(MainActivity.this, "credentials are took"+user_n.toString(), Toast.LENGTH_SHORT).show();

                        if (!TextUtils.isEmpty(user_n) && !TextUtils.isEmpty(user_p)){
//                          Toast.makeText(MainActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                            Loginreq(user_n, user_p);
                        }else {
                            if (TextUtils.isEmpty(user_n)){
                                usern.setError("please fill the details....");
                            }else userp.setError("please fill the details...");
                        }
                    }
                });
                bottomSheetDialog.show();
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_signup);
                bottomSheetDialog.setCanceledOnTouchOutside(false);



                profn = bottomSheetDialog.findViewById(R.id.prof_name);
                usern=bottomSheetDialog.findViewById(R.id.user_name);
                namen=bottomSheetDialog.findViewById(R.id.name);
                mobilen=bottomSheetDialog.findViewById(R.id.user_mob);
                emailn=bottomSheetDialog.findViewById(R.id.user_email);
                passwdn=bottomSheetDialog.findViewById(R.id.user_password);
                cpasswdn=bottomSheetDialog.findViewById(R.id.user_cpassword);

                createbtn=bottomSheetDialog.findViewById(R.id.create_btn);

                createbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String u_prof = profn.getText().toString();
                        String u_uname = usern.getText().toString();
                        String u_name = namen.getText().toString();
                        String u_mobile = mobilen.getText().toString();
                        String u_email = emailn.getText().toString();
                        String u_pass = passwdn.getText().toString();
                        String u_cpass = cpasswdn.getText().toString();




                        if (!TextUtils.isEmpty(u_prof) && !TextUtils.isEmpty(u_name) && !TextUtils.isEmpty(u_uname) && !TextUtils.isEmpty(u_mobile) && !TextUtils.isEmpty(u_email) &&
                                !TextUtils.isEmpty(u_pass))  {


                            if (u_pass.equals(u_cpass)){

                                // Toast.makeText(MainActivity.this, "Password matched...", Toast.LENGTH_SHORT).show();
                                Signupreq(u_prof, u_uname, u_name, u_email, u_mobile, u_pass);

                            }else {
                                //Toast.makeText(MainActivity.this, "Password not matched...", Toast.LENGTH_SHORT).show();
                                cpasswdn.setError("*password not matched");
                            }

                        }else {

                            Toast.makeText(MainActivity.this, "Please fill all the details.....", Toast.LENGTH_SHORT).show();
                        }

                    }//end of create btn....


                });

                bottomSheetDialog.show();

            }
        });





        //completion of protected void
    }

    private void forgetpasswdvalidator(String email_e, String mobile_e) {

        ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loging in...please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        progressDialog.show();

        ProgressDialog finalProgressDialog = progressDialog;
        StringRequest passwdreq= new StringRequest(Request.Method.POST, api_pswdrequest, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                String userofuser = "";
                String paswwdofuser = "";

                try {
//                    Toast.makeText(MainActivity.this, "Entered into try block", Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = new JSONObject(response);
                    String success= jsonObject.getString("success").toString();
                    JSONArray jsonArray = jsonObject.getJSONArray("forget");

                    if (success.equals("1")){

                        for (int i=0; i<jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            userofuser = object.getString("username").toString();
                            paswwdofuser = object.getString("password").toString();

                        }
                        finalProgressDialog.dismiss();
                        u_n_ame.setText(userofuser);
                        u_p_word.setText(paswwdofuser);


                    }else {
                        finalProgressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }

                }catch (JSONException e){
                    finalProgressDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Json Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Check your Network", Toast.LENGTH_SHORT).show();
                finalProgressDialog.dismiss();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> upload = new HashMap<>();
                upload.put("email", email_e);
                upload.put("mobile", mobile_e);

                return upload;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(passwdreq);



    }

    private void Loginreq(String user_n, String user_p) {




        ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loging in...please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        progressDialog.show();

        ProgressDialog finalProgressDialog = progressDialog;
        StringRequest loginreq= new StringRequest(Request.Method.POST, login_api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                String profile_name = "";
                String user_id="";

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success= jsonObject.getString("success").toString();
                    JSONArray jsonArray = jsonObject.getJSONArray("login");

                    if (success.equals("1")){

                        for (int i=0; i<jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            profile_name = object.getString("profile_name").toString();
                            user_id = object.getString("user_id").toString();

                        }
                        finalProgressDialog.dismiss();
//                        Toast.makeText(MainActivity.this, "started intent"+profile_name, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                        intent.putExtra("pname", profile_name);
                        SharedPreferences sp = getSharedPreferences("secret", 0);
                        SharedPreferences.Editor sedt = sp.edit();
                        sedt.putString("v_value", profile_name.toString());
                        sedt.putString("v_user", user_id.toString());
                        sedt.commit();

                        SharedPreferences login = getSharedPreferences("auth", 0);
                        SharedPreferences.Editor editor = login.edit();
                        editor.putString("u_name", user_n.toString());
                        editor.putString("u_pass", user_p.toString());
                        editor.commit();

                        startActivity(intent);
                        finish();


                    }else {
                        finalProgressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }

                }catch (JSONException e){
                    finalProgressDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Json Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                finalProgressDialog.dismiss();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> upload = new HashMap<>();
                upload.put("user", user_n);
                upload.put("passwd", user_p);

                return upload;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(loginreq);

    }

    //completion of login request....


    private void Signupreq(String u_prof, String u_uname, String u_name, String u_email, String u_mobile, String u_pass) {

        ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loging in...please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);



        progressDialog.show();

        ProgressDialog finalProgressDialog = progressDialog;
        StringRequest singuprew = new StringRequest(Request.Method.POST, signup_api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(MainActivity.this, "Result: "+response.toString(), Toast.LENGTH_SHORT).show();
                finalProgressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                finalProgressDialog.dismiss();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> upload = new HashMap<>();
                upload.put("profile_name", u_prof);
                upload.put("user_name", u_uname);
                upload.put("name", u_name);
                upload.put("email", u_email);
                upload.put("mobile", u_mobile);
                upload.put("password", u_pass);
                return upload;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(singuprew);


    }

    public void FORGETPASSWOD(View view) {


        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        bottomSheetDialog.setContentView(R.layout.botttom_sheet_forgetpass);
        bottomSheetDialog.setCanceledOnTouchOutside(false);


        TextInputEditText EMAIL, MOBILE;
        Button pswdcheck;

        EMAIL = bottomSheetDialog.findViewById(R.id.USER_EMAIL);
        MOBILE=bottomSheetDialog.findViewById(R.id.USER_MOBILE);
        pswdcheck=bottomSheetDialog.findViewById(R.id.passwd_validate);

        u_n_ame=bottomSheetDialog.findViewById(R.id.user_username);
        u_p_word=bottomSheetDialog.findViewById(R.id.user_password);

        pswdcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_e = EMAIL.getText().toString();
                String mobile_e = MOBILE.getText().toString();

                if (!TextUtils.isEmpty(email_e) && !TextUtils.isEmpty(mobile_e)){
                    forgetpasswdvalidator(email_e, mobile_e);
                }else {
                    Toast.makeText(MainActivity.this, "fill the details", Toast.LENGTH_SHORT).show();
                }

            }
        });

        bottomSheetDialog.show();

    }


    //completion of main activity
}