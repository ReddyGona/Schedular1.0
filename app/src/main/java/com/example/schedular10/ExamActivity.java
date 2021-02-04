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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

public class ExamActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton add_exm;
    Spinner spinner, dayspinn;
    TextView date_picker, time_picker;
    DatePickerDialog.OnDateSetListener setListener;
    Button add_exam_btn;
    int hour, min;
    EditText examtit, examseat;
    String api_addexam="https://oakspro.com/projects/project40/janardhan/Schedular/add_exma.php";
    String api_getexam="https://oakspro.com/projects/project40/janardhan/Schedular/search_exam.php";

    private List<ExamActivity.EXAM> exams;
    List<HomeActivity.ACADEMIC> acad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);


        SharedPreferences sp = getSharedPreferences("secret", 0);
        String key = sp.getString("v_value", "");
        Retrive_data(key);



        BottomNavigationView bottom_exam = findViewById(R.id.bottom_navh4);
        add_exm=findViewById(R.id.floatexamadd);

        recyclerView=findViewById(R.id.recyclarexam);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ExamActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);




        add_exm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ExamActivity.this);
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_exam);
                bottomSheetDialog.setCanceledOnTouchOutside(false);

                date_picker = bottomSheetDialog.findViewById(R.id.date_btn);
                time_picker = bottomSheetDialog.findViewById(R.id.time_btn);
                add_exam_btn = bottomSheetDialog.findViewById(R.id.add_exm);
                examtit = bottomSheetDialog.findViewById(R.id.exm_sub);
                examseat=bottomSheetDialog.findViewById(R.id.exm_seating);


                String [] spinn = {"FN", "AN"};
                String [] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

                spinner = bottomSheetDialog.findViewById(R.id.fnspinn);
                ArrayAdapter SPINN = new ArrayAdapter(ExamActivity.this, android.R.layout.simple_spinner_dropdown_item, spinn);
                SPINN.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(SPINN);

                dayspinn = bottomSheetDialog.findViewById(R.id.dayspinner);
                ArrayAdapter DAYS = new ArrayAdapter(ExamActivity.this, android.R.layout.simple_spinner_dropdown_item, days);
                DAYS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dayspinn.setAdapter(DAYS);



                Calendar calendar =  Calendar.getInstance();
                final int year =calendar.get(Calendar.YEAR);
                final int month =calendar.get(Calendar.MONTH);
                final int day =calendar.get(Calendar.DAY_OF_MONTH);


                date_picker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                ExamActivity.this, android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth,
                                setListener,year,month,day);
                        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        datePickerDialog.show();
                    }
                });

                setListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = year + "-" +month+ "-" +dayOfMonth;
                        date_picker.setText(date);
                    }
                };

                time_picker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(ExamActivity.this, android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth,
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
                                            time_picker.setText(f12hrs.format(date));

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


                add_exam_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String e_title = examtit.getText().toString();
                        String e_seat = examseat.getText().toString();
                        String e_date = date_picker.getText().toString();
                        String e_time = time_picker.getText().toString();
                        String e_day = dayspinn.getSelectedItem().toString();
                        String e_af = spinner.getSelectedItem().toString();


                        if (!TextUtils.isEmpty(e_title) && !TextUtils.isEmpty(e_seat) && !TextUtils.isEmpty(e_date) && !TextUtils.isEmpty(e_time)
                                && !TextUtils.isEmpty(e_day) && !TextUtils.isEmpty(e_af) && !TextUtils.isEmpty(key) ){
//                            Toast.makeText(ExamActivity.this, "All details are filled...", Toast.LENGTH_SHORT).show();
                            addtotheexam(e_title, e_seat, e_date, e_time, e_day, e_af, key);
                        }else {
                            Toast.makeText(ExamActivity.this, "Fill all the details..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



                bottomSheetDialog.show();
            }
        });




        bottom_exam.setSelectedItemId(R.id.EXAMS);




        bottom_exam.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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
                        Retrive_data(key);
                        return true;
                    case R.id.PROFILE:
                        startActivity(new Intent(getApplicationContext(), ProfileActivitya.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }

        });

//        //testing purpose//
//
//        ItemTouchHelper.SimpleCallback simpleCallback =
//                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
//                    @Override
//                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
//                                          RecyclerView.ViewHolder target) {
//                        return false;
//                    }
//
//                    @Override
//                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                        //do things
//
//                    }
//                };
//
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);
//
//
//
//        //end of testing//


    }




    private void Retrive_data(String key) {


//        Toast.makeText(this, "key: "+key, Toast.LENGTH_SHORT).show();

        ProgressDialog progressDialog = new ProgressDialog(ExamActivity.this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data...please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        ProgressDialog finalProgressDialog = progressDialog;

        finalProgressDialog.show();

//        Toast.makeText(this, "starting...", Toast.LENGTH_SHORT).show();


        StringRequest exmreq = new StringRequest(Request.Method.POST, api_getexam, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                exams = new ArrayList<>();
                acad = new ArrayList<>();

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

                            exams.add(new EXAM(e_title, e_seat, e_day, e_date, e_time, e_session, e_sl));
                            Start_Adapterr();


                        }//end of for loop
                        finalProgressDialog.dismiss();

                    }else {
//                        Toast.makeText(ExamActivity.this, "No Data Found....", Toast.LENGTH_SHORT).show();
                        finalProgressDialog.dismiss();
                        String detail="1";
                        acad.add(new HomeActivity.ACADEMIC("you don't have any exam", detail));
                        startAdapter1();

                    }//end of if else

                }//end of try
                catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(ExamActivity.this, "Json: "+e.toString(), Toast.LENGTH_SHORT).show();
                    finalProgressDialog.dismiss();
                }// end of catch

            }//end of onresponse
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ExamActivity.this, "Volley: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                finalProgressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(ExamActivity.this);
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

        RequestQueue requestQueue = Volley.newRequestQueue(ExamActivity.this);
        requestQueue.add(exmreq);

    }

    private void startAdapter1() {
        RecyclarViewAdapter3 adapter = new RecyclarViewAdapter3 (acad, ExamActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private void Start_Adapterr() {
        RecyclarViewAdapter2 adapter = new RecyclarViewAdapter2(exams, ExamActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private void addtotheexam(String e_title, String e_seat, String e_date, String e_time, String e_day, String e_af, String keyy) {

        ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data...please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        progressDialog.show();

        ProgressDialog finalProgressDialog = progressDialog;


        StringRequest request = new StringRequest(Request.Method.POST, api_addexam, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(ExamActivity.this, "Result: "+ response.toString(), Toast.LENGTH_SHORT).show();
                finalProgressDialog.dismiss();
                Retrive_data(keyy);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(ExamActivity.this, "Volley: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                finalProgressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(ExamActivity.this);
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
                upload.put("e_title", e_title);
                upload.put("e_seat", e_seat);
                upload.put("e_day", e_day);
                upload.put("e_date", e_date);
                upload.put("e_time", e_time);
                upload.put("e_af", e_af);
                upload.put("e_key", keyy);
                return upload;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ExamActivity.this);
        requestQueue.add(request);


    }

    public static class EXAM {
        String e_titlee;
        String e_seat;
        String e_day;
        String e_date;
        String e_time;
        String e_session;
        String e_sl;


        EXAM(String e_titlee, String e_seat, String e_day, String e_date, String e_time, String e_session, String e_sl){
            this.e_titlee=e_titlee;
            this.e_seat=e_seat;
            this.e_day=e_day;
            this.e_date=e_date;
            this.e_time=e_time;
            this.e_session=e_session;
            this.e_sl=e_sl;

        }
    }


}//end of exam