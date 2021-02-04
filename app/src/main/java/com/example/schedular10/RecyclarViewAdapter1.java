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

public class RecyclarViewAdapter1 extends RecyclerView.Adapter<RecyclarViewAdapter1.TaskViewHolder> {

    List<TaskActivity.TASK> tasks;
    Context context;
    String api_taskdel="https://oakspro.com/projects/project40/janardhan/Schedular/delete_task.php";

    public RecyclarViewAdapter1(List<TaskActivity.TASK> tasks, Context context){
        this.tasks = tasks;
        this.context=context;
    }



    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView t_title, t_date, t_time;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.card_task);
            t_title = (TextView)itemView.findViewById(R.id.task_text);
            t_date= (TextView)itemView.findViewById(R.id.task_duedate);
            t_time = (TextView)itemView.findViewById(R.id.task_duetime);

        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View taskview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_2, parent, false);
        TaskViewHolder tvh = new TaskViewHolder(taskview);
        return  tvh;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.t_title.setText(tasks.get(position).ta_text);
        holder.t_date.setText(tasks.get(position).ta_date);
        holder.t_time.setText(tasks.get(position).ta_time);

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String DATE = sdf.format(new Date());
//
//        SimpleDateFormat ssdf = new SimpleDateFormat("HH:mm:ss");
//        String TIME = ssdf.format(new Date());




        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setCancelable(true);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure to delete: "+tasks.get(position).ta_text +"..?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String SL_NO =tasks.get(position).ta_slno;
                        delete_tas(SL_NO, v);

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

    private void delete_tas(String SL_NO, View v) {
        StringRequest del_req = new StringRequest(Request.Method.POST, api_taskdel, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;

                        try {
                            jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, TaskActivity.class);
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
                param.put("serial", SL_NO);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
        requestQueue.add(del_req);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

}
