package com.example.schedular10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeActivity extends AppCompatActivity {

    FloatingActionButton addtt;
    Spinner hour_Id, hour_Day;
    Button createbtn;
    Button monbtn, tuebtn, wedbtn, thubtn, fribtn;
    EditText hour_Name;
    String ttable_api="https://oakspro.com/projects/project40/janardhan/Schedular/add_timetable.php";
    String reqdata_api="https://oakspro.com/projects/project40/janardhan/Schedular/search.php";
    String api_academic="https://oakspro.com/projects/project40/janardhan/Schedular/Acadamic_cal.php";
    RecyclerView recyclerView;
    private List<CLUB> clubs;
    List<HomeActivity.ACADEMIC> acad;
    TextView today_date;
    String dayToday;
    String DATE, key;
    String selected_day;
    String dayy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        SharedPreferences sp = getSharedPreferences("secret", 0);
        key = sp.getString("v_value", "");

        addtt=findViewById(R.id.floatadd);

        monbtn = findViewById(R.id.mon);
        tuebtn = findViewById(R.id.tue);
        wedbtn = findViewById(R.id.wed);
        thubtn = findViewById(R.id.thu);
        fribtn = findViewById(R.id.fri);
        today_date=findViewById(R.id.tt_dayy);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        DATE = sdf.format(new Date());

        Calendar calendar = Calendar.getInstance();
        int day=calendar.get(Calendar.DAY_OF_WEEK);

        switch (day){
            case Calendar.SUNDAY:
                dayToday="Sunday";
                retrivedata(DATE, dayToday, key);
                break;
            case Calendar.MONDAY:
                dayToday="Monday";
                retrivedata(DATE, dayToday, key);
                break;
            case Calendar.TUESDAY:
                dayToday="Tuesday";
                retrivedata(DATE, dayToday, key);
                break;
            case Calendar.WEDNESDAY:
                dayToday="Wednesday";
                retrivedata(DATE, dayToday, key);
                break;
            case Calendar.THURSDAY:
                dayToday="Thursday";
                retrivedata(DATE, dayToday, key);
                break;
            case Calendar.FRIDAY:
                dayToday="Friday";
                retrivedata(DATE, dayToday, key);
                break;
            case Calendar.SATURDAY:
                dayToday="Saturday";
                retrivedata(DATE, dayToday, key);
                break;
        }

        today_date.setText(dayToday);



        recyclerView=findViewById(R.id.recyclartt);
        recyclerView.setHasFixedSize(true);



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TimeActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

//        RecyclarViewAdapter adapter = new RecyclarViewAdapter(clubs);




        monbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayy = "Monday";
                retrivedata(DATE, dayy, key);
            }
        });

        tuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayy = "Tuesday";
                retrivedata(DATE, dayy, key);
            }
        });

        wedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayy = "Wednesday";
                retrivedata(DATE, dayy, key);
            }
        });


        thubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayy = "Thursday";
                retrivedata(DATE, dayy, key);
            }
        });


        fribtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayy = "Friday";
                retrivedata(DATE, dayy, key);
            }
        });



        addtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(TimeActivity.this);
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_addtt);
                bottomSheetDialog.setCanceledOnTouchOutside(false);

                String [] hr_id = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
                String [] hr_day = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

                //intilizing bottom sheet layout details

                hour_Id = bottomSheetDialog.findViewById(R.id.hour_id);
                hour_Day = bottomSheetDialog.findViewById(R.id.hour_day);
                hour_Name = bottomSheetDialog.findViewById(R.id.hr_NNAme);


                ArrayAdapter hourid = new ArrayAdapter(TimeActivity.this, android.R.layout.simple_spinner_dropdown_item, hr_id);
                hourid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                hour_Id.setAdapter(hourid);

                ArrayAdapter hourday = new ArrayAdapter(TimeActivity.this, android.R.layout.simple_spinner_dropdown_item, hr_day);
                hourid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                hour_Day.setAdapter(hourday);




                createbtn = bottomSheetDialog.findViewById(R.id.addbtn);

                createbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String houri_d = hour_Id.getSelectedItem().toString();
                        String hourda_y = hour_Day.getSelectedItem().toString();
                        String hournam_e = hour_Name.getText().toString();


                        if (!TextUtils.isEmpty(houri_d) && !TextUtils.isEmpty(hourda_y) && !TextUtils.isEmpty(hournam_e) && !TextUtils.isEmpty(key)){

                            // Toast.makeText(TimeActivity.this, "Details are filled success fully...", Toast.LENGTH_SHORT).show();

                            Addtotimetable(houri_d, hourda_y, hournam_e, key);

                        }else {
                            hour_Name.setError("Enter the hour title..");
                        }
                    }


                });


                bottomSheetDialog.show();

            }
        });


        // code for the bottom navigation view......

        BottomNavigationView bottom_exams = findViewById(R.id.bottom_navh2);

        bottom_exams.setSelectedItemId(R.id.TTABLE);

        bottom_exams.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.HOME:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.TTABLE:
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
                        startActivity(new Intent(getApplicationContext(), ProfileActivitya.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }  //end of protected void

    private void startAdapter() {
        RecyclarViewAdapter adapter = new RecyclarViewAdapter(clubs, TimeActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private void retrivedata(String datee, String day, String Keyy) {

        today_date.setText(day);


        ProgressDialog progressDialog = new ProgressDialog(TimeActivity.this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data...please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        ProgressDialog finalProgressDialog = progressDialog;

        finalProgressDialog.show();

        StringRequest getdata = new StringRequest(Request.Method.POST, reqdata_api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                clubs = new ArrayList<>();
                acad = new ArrayList<>();

                if (day.equals("Saturday") || day.equals("Sunday")){
                    finalProgressDialog.dismiss();
                    retrive_sat_sund(datee, Keyy, day);
                }else{

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        JSONArray jsonArray=jsonObject.getJSONArray("details");

                        if (status.equals("1")){
                            for (int i=0; i<=jsonArray.length(); i++){

                                JSONObject object = jsonArray.getJSONObject(i);

                                String h_Id = object.getString("hourId").toString();
                                String h_Name = object.getString("hourTitle").toString();
                                String slno = object.getString("slno").toString();


                                clubs.add(new CLUB(h_Id, h_Name, slno));
//                           Toast.makeText(TimeActivity.this, "hour: "+h_Id + "hourName: "+h_Name, Toast.LENGTH_SHORT).show();
                                startAdapter();
                                finalProgressDialog.dismiss();
//                           Toast.makeText(TimeActivity.this, "hour: "+h_Id + "hourName: "+h_Name, Toast.LENGTH_SHORT).show();
                            }


                        }else {
                            finalProgressDialog.dismiss();
                            String detail="1";
                            acad.add(new HomeActivity.ACADEMIC("you dont have any class on : "+day, detail));
                            startAdapter1();

                        }

                    }catch (JSONException e) {
                        e.printStackTrace();
                        finalProgressDialog.dismiss();
                    }



                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(TimeActivity.this, "Volley: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                finalProgressDialog.dismiss();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> upload = new HashMap();
                upload.put("day", day);
                upload.put("t_key", Keyy);
                return upload;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(TimeActivity.this);
        requestQueue.add(getdata);


    }


    private void retrive_sat_sund(String datee, String keyy, String day) {

        today_date.setText(day);

        StringRequest Acadd = new StringRequest(Request.Method.POST, api_academic, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                acad = new ArrayList<>();

                String detail_one="";
                String detail_2="";
                String detail_0 = "holiday";

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    JSONArray jsonArray1 = jsonObject.getJSONArray("accaddmic");
                    if (status.equals("1")){
                        int size = jsonArray1.length();
                        for (int j=0; j<size; j++) {
                            JSONObject object2 = jsonArray1.getJSONObject(j);
                            detail_one = object2.getString("Details");
                            detail_2 = object2.getString("sl");

                        }//end of for loop

                        if (detail_one.equals("Monday") || detail_one.equals("Tuesday") || detail_one.equals("Wednesday") ||detail_one.equals("Thursday") ||
                                detail_one.equals("Friday")){
//                             Toast.makeText(HomeActivity.this, "Checked if", Toast.LENGTH_SHORT).show();
                            retrivedata(datee, day, keyy);
                        }
                        else {
                            acad.add(new HomeActivity.ACADEMIC(detail_one, detail_2));
                            startAdapter1();
                        }

                    }//end of if
                    else {
                        if (day.equals("Sunday")) {
//                        String text = "Holiday.....";
                            acad.add(new HomeActivity.ACADEMIC("Holiday", detail_2));
                            startAdapter1();
                        }else {
                            acad.add(new HomeActivity.ACADEMIC("Holiday", detail_2));
                            startAdapter1();
                        }
                    }//end of 1st ense


                } catch (JSONException e) {
//                    Toast.makeText(HomeActivity.this, "json"+e.toString(), Toast.LENGTH_LONG).show();
//                    Toast.makeText(HomeActivity.this, "json"+e.toString(), Toast.LENGTH_LONG).show();
//                    Toast.makeText(HomeActivity.this, "json"+e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TimeActivity.this, "volley"+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> upload =new HashMap<>();
                upload.put("dateofcurrent", datee);

                return upload;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(TimeActivity.this);
        requestQueue.add(Acadd);

    }

    private void startAdapter1() {
        RecyclarViewAdapter3 adapter = new RecyclarViewAdapter3 (acad, TimeActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private void Addtotimetable(String hourid, String hourday, String hourname, String Key) {

        ProgressDialog progressDialog = new ProgressDialog(TimeActivity.this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving data...please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        ProgressDialog finalProgressDialog = progressDialog;

        finalProgressDialog.show();

        StringRequest addtimetable = new StringRequest(Request.Method.POST, ttable_api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(TimeActivity.this, "Result: "+ response.toString(), Toast.LENGTH_SHORT).show();
                finalProgressDialog.dismiss();
                calltimetable();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(TimeActivity.this, "Volley: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                finalProgressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(TimeActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Note");
                builder.setMessage("There is some problem please try again later" +"\n" +error.getMessage());
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> upload = new HashMap<>();
                upload.put("hour_id", hourid);
                upload.put("hour_day", hourday);
                upload.put("hour_name", hourname);
                upload.put("t_use", Key);
                return upload;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(TimeActivity.this);
        requestQueue.add(addtimetable);

    }// end of adding data to timetable...

    private void calltimetable() {
        retrivedata(DATE, dayToday, key);
    }


    static class CLUB{

        String hourID;
        String hourNAME;
        String sl_no;

        CLUB(String hourID, String hourNAME, String sl_no){
            this.hourID = hourID;
            this.hourNAME = hourNAME;
            this.sl_no= sl_no;
        }

    }


}//end of time activity