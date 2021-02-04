package com.example.schedular10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
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

public class HomeActivity extends AppCompatActivity {

    String api_getexam="https://oakspro.com/projects/project40/janardhan/Schedular/notify_search_exam.php";
    private List<ExamActivity.EXAM> exams;

    TextView P_NAME, Today;
    String dayToday, dayofday;
    RecyclerView recyclerView, recyclerView1;
    String api_academic="https://oakspro.com/projects/project40/janardhan/Schedular/Acadamic_cal.php";
    String api_notif = "https://oakspro.com/projects/project40/janardhan/Schedular/notif_act.php";


    List<HomeActivity.ACADEMIC> acad;

    private List<TimeActivity.CLUB> clubs;
    String reqdata_api="https://oakspro.com/projects/project40/janardhan/Schedular/search.php";


    private List<TaskActivity.TASK> tasks;
    private String api_gettask="https://oakspro.com/projects/project40/janardhan/Schedular/search_task.php";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottom_home = findViewById(R.id.bottom_navh1);
        P_NAME = findViewById(R.id.p_name);
        Today=findViewById(R.id.today);

        recyclerView=findViewById(R.id.timterecyclar);


        recyclerView1=findViewById(R.id.taskrecyclar);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);


        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(HomeActivity.this);
        recyclerView1.setLayoutManager(linearLayoutManager1);





        SharedPreferences sp = getSharedPreferences("secret", 0);
        String key = sp.getString("v_value", "");
        P_NAME.setText(key);


//        final int year =calendar.get(Calendar.YEAR);
//        int month =calendar.get(Calendar.MONTH);
//        final int date =calendar.get(Calendar.DAY_OF_MONTH);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String DATE = sdf.format(new Date());



        SimpleDateFormat ssdf = new SimpleDateFormat("HH:mm:ss");
        String TIME = ssdf.format(new Date());

        Calendar calendar = Calendar.getInstance();
        int day=calendar.get(Calendar.DAY_OF_WEEK);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        calendar.add(Calendar.DATE, 1);



//        String ADATE = year+"-"+month+"-"+date;
//        Toast.makeText(this, "Date"+ADATE, Toast.LENGTH_LONG).show();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss");
        c.add(Calendar.DATE, 1);  // number of days to add
        String end_date = df.format(c.getTime());

//        String DATE = year+"-"+month+"-"+date;
//        Toast.makeText(this, "Date"+DATE, Toast.LENGTH_LONG).show();
//        Toast.makeText(this, "Date"+end_date, Toast.LENGTH_LONG).show();
//
//        Toast.makeText(this, "Date"+DATE, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "Time"+TIME, Toast.LENGTH_SHORT).show();

        String p_n_ame = P_NAME.getText().toString();
        String timee = "18:00:00";



        switch (day){
            case Calendar.SUNDAY:
                dayToday="Sunday";
                if (TIME.compareTo(timee)<0){
//                     Toast.makeText(this, "into sunday accessed", Toast.LENGTH_SHORT).show();
                    notify_on_exam(DATE, dayToday, key);
                }else {
                    notify_on_exam(end_date, "Monday", key);
                }
                break;
            case Calendar.MONDAY:
                dayToday="Monday";
                if (TIME.compareTo(timee)<0){
//                     retrivedata(dayToday, key);
                    notify_on_exam(DATE, dayToday, key);
                }else {
                    notify_on_exam(end_date, "Tuesday", key);
                }
                break;
            case Calendar.TUESDAY:
                dayToday="Tuesday";
                if (TIME.compareTo(timee)<0){
                    notify_on_exam(DATE, dayToday, key);
                }else {
                    notify_on_exam(end_date, "Wednesday", key);
                }
                break;
            case Calendar.WEDNESDAY:
                dayToday="Wednesday";
                if (TIME.compareTo(timee)<0){
                    notify_on_exam(DATE, dayToday, key);
                }else {
                    notify_on_exam(end_date, "Thursday", key);
                }
                break;
            case Calendar.THURSDAY:
                dayToday="Thursday";
                if (TIME.compareTo(timee)<0){
                    notify_on_exam(DATE, dayToday, key);
                }else {
                    notify_on_exam(end_date, "Friday", key);
                }
                break;
            case Calendar.FRIDAY:
                dayToday="Friday";
                if (TIME.compareTo(timee)<0){
                    notify_on_exam(DATE, dayToday, key);
                }else {
                    notify_on_exam(end_date, "Saturday", key);
                }
                break;
            case Calendar.SATURDAY:
                dayToday="Saturday";
//                 Toast.makeText(this, "on saturday", Toast.LENGTH_SHORT).show();
                if (TIME.compareTo(timee)<0) {
                    notify_on_exam(DATE, dayToday, key);
                }else {
//                     Toast.makeText(this, "Into else", Toast.LENGTH_SHORT).show();
                    notify_on_exam(end_date, "Sunday", key);
                }
                break;
        }

//         Today.setText(dayToday);
//        Toast.makeText(this, "day: "+dayToday, Toast.LENGTH_LONG).show();
//         String today=Today.getText().toString();
        retrive_data(p_n_ame);
//         retrivedata(dayToday, p_n_ame);

//        notification_hone(DATE, key, dayToday);













        bottom_home.setSelectedItemId(R.id.HOME);

        bottom_home.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.HOME:
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
                        startActivity(new Intent(getApplicationContext(), ProfileActivitya.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }

        });


    }

    // to check wether there is exam or not......

    public  void  notify_on_exam(String c_date, String c_day, String c_user){

        Today.setText(c_day);

        StringRequest check_exam = new StringRequest(Request.Method.POST, api_notif, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    JSONArray jsonArray=jsonObject.getJSONArray("details");
//                    Toast.makeText(HomeActivity.this, "Status"+status, Toast.LENGTH_SHORT).show();

                    if (status.equals("1")){
                        Retrive_data(c_user, c_date);
                    }else {
                        if (!c_day.equals("Saturday") && !c_day.equals("Sunday")){
                            retrivedata(c_day, c_user);
                        }else{
                                retrive_sat_sund(c_date, c_user, c_day);
                            }


                    }

                } catch (JSONException e){
                    e.printStackTrace();
//                    Toast.makeText(HomeActivity.this, "catch: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }//end of on response
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(HomeActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> up = new HashMap<>();
                up.put("curr_user", c_user);
                up.put("curr_date", c_date);
                return up;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        requestQueue.add(check_exam);

    }




    private void Retrive_data(String keyy, String cu_date) {
        StringRequest exmreq = new StringRequest(Request.Method.POST, api_getexam, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                exams = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    JSONArray jsonArray=jsonObject.getJSONArray("details");

                    if (status.equals("1")){

                        for (int i=0; i<jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);

                            String e_title = object.getString("e_titlee").toString();
                            String e_seat = object.getString("e_seat").toString();
                            String e_day = object.getString("e_day").toString();
                            String e_date = object.getString("e_date").toString();
                            String e_time = object.getString("e_time").toString();
                            String e_session = object.getString("e_session").toString();
                            String e_sl = object.getString("e_slno").toString();

                            exams.add(new ExamActivity.EXAM(e_title, e_seat, e_day, e_date, e_time, e_session, e_sl));
                            Start_Adapterr();

                        }//end of for loop

                    }else {

                        String detail="1";
                        acad.add(new ACADEMIC("you don't have any exam", detail));
                        startAdapter1();

//                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
//                        builder.setCancelable(true);
//                        builder.setTitle("Note");
//                        builder.setMessage("No Exam Found " +"\n" + "please add the Exam");
//                        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        });
//                        builder.create().show();
                    }//end of if else

                }//end of try
                catch (JSONException e){
                    e.printStackTrace();
//                    Toast.makeText(HomeActivity.this, "Json: "+e.toString(), Toast.LENGTH_SHORT).show();

                }// end of catch

            }//end of onresponse
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
//                finalProgressDialog.dismiss();
//                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
//                builder.setCancelable(true);
//                builder.setTitle("Note");
//                builder.setMessage("There is some problem please try again later" +"\n" +error.getMessage());
//                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                builder.create().show();


            }//end of error response listener
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> upload = new HashMap();
                upload.put("key", keyy);
                upload.put("cu_datee", cu_date);
                return upload;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        requestQueue.add(exmreq);
    }

    private void Start_Adapterr() {
        RecyclarViewAdapter2 adapter = new RecyclarViewAdapter2(exams, HomeActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private void retrive_sat_sund(String Datee, String keyy, String dayy) {

//        Toast.makeText(this, "Started saturday", Toast.LENGTH_SHORT).show();

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
                            retrivedata(detail_one, keyy);
                        }
                        else {
                            acad.add(new ACADEMIC(detail_one, detail_2));
                            startAdapter1();
                        }

                    }//end of if
                    else {
                        if (dayy.equals("Sunday")) {
//                        String text = "Holiday.....";
                            acad.add(new ACADEMIC("Holiday", detail_2));
                            startAdapter1();
                        }else {
                            acad.add(new ACADEMIC("Holiday", detail_2));
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
//                Toast.makeText(HomeActivity.this, "Network Error", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> upload =new HashMap<>();
                upload.put("dateofcurrent", Datee);

                return upload;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(HomeActivity.this);
        requestQueue.add(Acadd);

    }

    private void startAdapter1() {
        RecyclarViewAdapter3 adapter = new RecyclarViewAdapter3 (acad, HomeActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private void retrive_data(String key) {

//        Toast.makeText(this, "key: "+key, Toast.LENGTH_SHORT).show();
//
//        ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
//
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading data...please wait");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//
//        ProgressDialog finalProgressDialog = progressDialog;
//
//        finalProgressDialog.show();

//        Toast.makeText(this, "starting...", Toast.LENGTH_SHORT).show();


        StringRequest taskreq = new StringRequest(Request.Method.POST, api_gettask, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                tasks = new ArrayList<>();
                acad = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    JSONArray jsonArray=jsonObject.getJSONArray("details");

                    if (status.equals("1")){

                        for (int i=0; i<jsonArray.length(); i++) {

                            JSONObject object1 = jsonArray.getJSONObject(i);

                            String T_title = object1.getString("t_title").toString();
                            String T_date = object1.getString("t_date").toString();
                            String T_time = object1.getString("t_time").toString();
                            String T_slno = object1.getString("t_slno").toString();

                            tasks.add(new TaskActivity.TASK(T_title, T_date, T_time, T_slno));

                            Start_Adapter();
                        }//end of for loop

                    }else {
                        String detail="1";
                        acad.add(new ACADEMIC("you don't have any task", detail));
                        startAdapter_1();
//                        Toast.makeText(HomeActivity.this, "No Data Found....", Toast.LENGTH_SHORT).show();
//                        finalProgressDialog.dismiss();
                    }//end of if else

                }//end of try
                catch (JSONException e){
                    e.printStackTrace();
//                    Toast.makeText(HomeActivity.this, "Json: "+e.toString(), Toast.LENGTH_SHORT).show();
//                    finalProgressDialog.dismiss();
                }// end of catch

            }//end of onresponse
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(HomeActivity.this, "Volley: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
//                finalProgressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Note");
                builder.setMessage("There is some problem please try again later" +"\n" +error.getMessage());
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

        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        requestQueue.add(taskreq);




    }

    private void startAdapter_1() {
        RecyclarViewAdapter3 adapter = new RecyclarViewAdapter3 (acad, HomeActivity.this);
        recyclerView1.setAdapter(adapter);
    }

    private void Start_Adapter() {
        RecyclarViewAdapter1 adapter = new RecyclarViewAdapter1(tasks, HomeActivity.this);
        recyclerView1.setAdapter(adapter);
    }

    private void retrivedata(String day, String Keyy) {


//        ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
//
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading data...please wait");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//
//        ProgressDialog finalProgressDialog = progressDialog;
//
//        finalProgressDialog.show();

        StringRequest getdata = new StringRequest(Request.Method.POST, reqdata_api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                clubs = new ArrayList<>();
                acad = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    JSONArray jsonArray=jsonObject.getJSONArray("details");

                    if (status.equals("1")){
                        for (int i=0; i<jsonArray.length(); i++){

                            JSONObject object = jsonArray.getJSONObject(i);

                            String h_Id = object.getString("hourId").toString();
                            String h_Name = object.getString("hourTitle").toString();
                            String slno = object.getString("slno").toString();


                            clubs.add(new TimeActivity.CLUB(h_Id, h_Name, slno));
//                           Toast.makeText(TimeActivity.this, "hour: "+h_Id + "hourName: "+h_Name, Toast.LENGTH_SHORT).show();
                            startAdapter();
//                            finalProgressDialog.dismiss();
//                           Toast.makeText(TimeActivity.this, "hour: "+h_Id + "hourName: "+h_Name, Toast.LENGTH_SHORT).show();
                        }


                    }else {

                        String detail="1";
                        acad.add(new ACADEMIC("you don't have any class ", detail));
                        startAdapter1();
//                        Toast.makeText(HomeActivity.this, "No Data Found....on "+day, Toast.LENGTH_SHORT).show();
////                        finalProgressDialog.dismiss();
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
//                    finalProgressDialog.dismiss();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(HomeActivity.this, "Volley: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
//                finalProgressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
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
                Map<String, String> upload = new HashMap();
                upload.put("day", day);
                upload.put("t_key", Keyy);
                return upload;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        requestQueue.add(getdata);



    }

    private void startAdapter() {
        RecyclarViewAdapter adapter = new RecyclarViewAdapter(clubs, HomeActivity.this);
        recyclerView.setAdapter(adapter);
    }

    public static class ACADEMIC {

        String acad_data;
        String ser_ial;

        ACADEMIC( String acad_data, String ser_ial){
            this.acad_data = acad_data;
            this.ser_ial=ser_ial;
        }
    }
}
