package com.example.schedular10;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclarViewAdapter2 extends RecyclerView.Adapter<RecyclarViewAdapter2.ExamViewHolder> {

    String api_examdelete="https://oakspro.com/projects/project40/janardhan/Schedular/delete_exam.php";




    List<ExamActivity.EXAM> exams;
    Context context;

    public RecyclarViewAdapter2(List<ExamActivity.EXAM> exams, Context context){
        this.context=context;
        this.exams=exams;
    }

    public static class ExamViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView TITLE, SEAT, DAY, DATE, TIME, SESSION;

        public ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.card_exam);
            TITLE = (TextView)itemView.findViewById(R.id.EXAM_TITLE);
            SEAT= (TextView)itemView.findViewById(R.id.EXAM_SEAT);
            DAY = (TextView)itemView.findViewById(R.id.EXAM_DAY);
            DATE = (TextView)itemView.findViewById(R.id.EXAM_DATE);
            TIME = (TextView)itemView.findViewById(R.id.EXAM_TIME);
            SESSION = (TextView)itemView.findViewById(R.id.EXAM_SESS);
        }
    }

    @NonNull
    @Override
    public RecyclarViewAdapter2.ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View examview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_3, parent, false);
        RecyclarViewAdapter2.ExamViewHolder evh = new RecyclarViewAdapter2.ExamViewHolder(examview);
        return  evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
        holder.TITLE.setText(exams.get(position).e_titlee);
        holder.SEAT.setText(exams.get(position).e_seat);
        holder.DAY.setText(exams.get(position).e_day);
        holder.DATE.setText(exams.get(position).e_date);
        holder.TIME.setText(exams.get(position).e_time);
        holder.SESSION.setText(exams.get(position).e_session);


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setCancelable(true);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure to delete: "+exams.get(position).e_titlee +"..?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String tit =exams.get(position).e_sl;
                        delete_exams(tit,  v);

                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();

                return true;



            }
        });
    }

    private void delete_exams(String tit, View v) {


        StringRequest del_exam = new StringRequest(Request.Method.POST, api_examdelete, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;

                        try {
                            jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, ExamActivity.class);
                                context.startActivity(intent);
                            }

                        }catch (JSONException e){
                            Toast.makeText(context, "json"+e.toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context, "volley"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param=new HashMap<>();
                param.put("E_Title", tit);
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
        requestQueue.add(del_exam);

    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return exams.size();
    }


}
