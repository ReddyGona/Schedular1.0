package com.example.schedular10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskActivity extends AppCompatActivity {


    FloatingActionButton fladd;
    RecyclerView recyclerView;
    Button saveBtn;
    EditText task_text;
    TextView timepicker, datepicker;
    int hour, min;
    DatePickerDialog.OnDateSetListener setListener;
    private String api_gettask="https://oakspro.com/projects/project40/janardhan/Schedular/search_task.php";
    String api_task="https://oakspro.com/projects/project40/janardhan/Schedular/task_add.php";

    //    SwipeRefreshLayout swipeRefreshLayout;
    List<HomeActivity.ACADEMIC> acad;



    private List<TaskActivity.TASK> tasks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        SharedPreferences sp = getSharedPreferences("secret", 0);
        String key = sp.getString("v_value", "");







        BottomNavigationView bottom_task = findViewById(R.id.bottom_navh3);
        fladd = findViewById(R.id.floattaskadd);
        recyclerView = findViewById(R.id.recyclartask);
//        swipeRefreshLayout = findViewById(R.id.swiperefresh_1);
        retrive_data(key);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TaskActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                retrive_data(key);
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });


        fladd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(TaskActivity.this);
                bottomSheetDialog.setCanceledOnTouchOutside(false);
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_taskadd);

                timepicker=bottomSheetDialog.findViewById(R.id.timetext);
                datepicker=bottomSheetDialog.findViewById(R.id.datetext);
                saveBtn=bottomSheetDialog.findViewById(R.id.savebtn);
                task_text=bottomSheetDialog.findViewById(R.id.tasktext);

                Calendar calendar =  Calendar.getInstance();
                final int year =calendar.get(Calendar.YEAR);
                final int month =calendar.get(Calendar.MONTH);
                final int day =calendar.get(Calendar.DAY_OF_MONTH);
//                final int Hour=calendar.get(Calendar.HOUR);
//                final int Min=calendar.get(Calendar.MINUTE);
//                final int Sec=calendar.get(Calendar.SECOND);

//                String current_time = Hour + ":" + Min + ":" + Sec;
//
//                Toast.makeText(TaskActivity.this, "Time"+current_time, Toast.LENGTH_SHORT).show();







                datepicker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                TaskActivity.this, android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth,
                                setListener,year,month,day);
                        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        datePickerDialog.show();
                    }
                });

                setListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date =  year + "-" + month+ "-" +dayOfMonth;
                        datepicker.setText(date);
                    }
                };



//                String current_date = datepicker.getText().toString();
//                Toast.makeText(TaskActivity.this, "date"+current_date, Toast.LENGTH_SHORT).show();




                timepicker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(TaskActivity.this, android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                        hour = hourOfDay;
                                        min = minute;

                                        String time = hour + ":" + min ;
                                        SimpleDateFormat f24hrs = new SimpleDateFormat("HH:mm");

                                        try {

                                            Date date = f24hrs.parse(time);
                                            SimpleDateFormat f12hrs = new SimpleDateFormat("HH:mm aa");
                                            timepicker.setText(f12hrs.format(date));

                                        }catch (ParseException e){
                                            e.printStackTrace();
                                        }

                                    }
                                }, 12,0, false);

                        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        timePickerDialog.updateTime(hour, min);
                        timePickerDialog.show();

                    }

                });

                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String t_text = task_text.getText().toString();
                        String t_day = datepicker.getText().toString();
                        String t_time = timepicker.getText().toString();


                        if (!TextUtils.isEmpty(t_text) && !TextUtils.isEmpty(t_day) && !TextUtils.isEmpty(t_time) && !TextUtils.isEmpty(key)){
                            task_activity(t_text, t_day, t_time, key);
                        }else {
                            Toast.makeText(TaskActivity.this, "please provide all the details...", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                bottomSheetDialog.show();

            }
        });

        bottom_task.setSelectedItemId(R.id.TASK);

        bottom_task.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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
                        retrive_data(key);
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


    }// end of protected void...



    private void retrive_data(String key) {

//        Toast.makeText(this, "key: "+key, Toast.LENGTH_SHORT).show();
        acad = new ArrayList<>();

        ProgressDialog progressDialog = new ProgressDialog(TaskActivity.this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data...please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        ProgressDialog finalProgressDialog = progressDialog;

        finalProgressDialog.show();

//        Toast.makeText(this, "starting...", Toast.LENGTH_SHORT).show();


        StringRequest taskreq = new StringRequest(Request.Method.POST, api_gettask, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                tasks = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    JSONArray jsonArray=jsonObject.getJSONArray("details");

                    if (status.equals("1")){

                        for (int i=0; i<jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            String T_title = object.getString("t_title").toString();
                            String T_date = object.getString("t_date").toString();
                            String T_time = object.getString("t_time").toString();
                            String T_slno = object.getString("t_slno").toString();

                            tasks.add(new TASK (T_title, T_date, T_time, T_slno));
                            Start_Adapter();

                        }//end of for loop
                        finalProgressDialog.dismiss();

                    }else {
//                        Toast.makeText(TaskActivity.this, "No Data Found....", Toast.LENGTH_SHORT).show();
                        finalProgressDialog.dismiss();
                        String detail="1";
                        acad.add(new HomeActivity.ACADEMIC("you don't have any task", detail));
                        startAdapter1();
                    }//end of if else

                }//end of try
                catch (JSONException e){
                    e.printStackTrace();
//                     Toast.makeText(TaskActivity.this, "Json: "+e.toString(), Toast.LENGTH_SHORT).show();
                    finalProgressDialog.dismiss();
                }// end of catch

            }//end of onresponse
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(TaskActivity.this, "Volley: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                finalProgressDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(TaskActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Note");
                builder.setMessage("Some problem with internet "+ "\n" + "please try again later");
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();


            }//end of error response listener
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> upload = new HashMap();
                upload.put("key", key);
                return upload;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(TaskActivity.this);
        requestQueue.add(taskreq);




    }//retrive data closed

    private void startAdapter1() {
        RecyclarViewAdapter3 adapter = new RecyclarViewAdapter3 (acad, TaskActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private void Start_Adapter() {
        RecyclarViewAdapter1 adapter = new RecyclarViewAdapter1(tasks, TaskActivity.this);
        recyclerView.setAdapter(adapter);
    }


    private void task_activity(String t_text, String t_day, String t_time, String Key) {

        ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data...please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        progressDialog.show();

        ProgressDialog finalProgressDialog = progressDialog;

        StringRequest request = new StringRequest(Request.Method.POST, api_task, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                Toast.makeText(TaskActivity.this, "Result: "+ response.toString(), Toast.LENGTH_SHORT).show();
                finalProgressDialog.dismiss();
                retrive_data(Key);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(TaskActivity.this, "Volley: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                finalProgressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(TaskActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Note");
                builder.setMessage("Some problem with internet "+ "\n" + "please try again later");
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
                upload.put("t_text", t_text);
                upload.put("t_day", t_day);
                upload.put("t_time", t_time);
                upload.put("t_u_name", Key);
                return upload;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(TaskActivity.this);
        requestQueue.add(request);
    }


    public static class TASK {
        String ta_text;
        String ta_date;
        String ta_time;
        String ta_slno;


        TASK(String ta_text, String ta_date, String ta_time, String ta_slno){
            this.ta_text=ta_text;
            this.ta_date=ta_date;
            this.ta_time=ta_time;
            this.ta_slno=ta_slno;


        }
    }
}