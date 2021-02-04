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

public class RecyclarViewAdapter extends RecyclerView.Adapter<RecyclarViewAdapter.ClubViewHolder> {

    List<TimeActivity.CLUB> clubs;
    Context context;
    String api_delete="https://oakspro.com/projects/project40/janardhan/Schedular/delete_tt.php";



    public RecyclarViewAdapter( List<TimeActivity.CLUB> clubs, Context context){
        this.clubs = clubs;
        this.context=context;
    }



    public static class ClubViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView h_Id, h_name;


        public ClubViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.CARD_VIEW);
            h_Id = (TextView)itemView.findViewById(R.id.HOUR_ID);
            h_name = (TextView)itemView.findViewById(R.id.HOUR_NAME);

        }
    }

    @NonNull
    @Override
    public ClubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View intview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        ClubViewHolder cvh = new ClubViewHolder(intview);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ClubViewHolder holder, int position) {
        holder.h_Id.setText(clubs.get(position).hourID);
        holder.h_name.setText(clubs.get(position).hourNAME);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setCancelable(true);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure to delete: "+clubs.get(position).hourID +"..?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String slno =clubs.get(position).sl_no;
                        deletehr(slno, v);

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

    private void deletehr(String slno, View View) {

        StringRequest request = new StringRequest(Request.Method.POST, api_delete, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;

                        try {
                            jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, TimeActivity.class);
                                context.startActivity(intent);
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param=new HashMap<>();
                param.put("slno", slno);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(View.getContext());
        requestQueue.add(request);

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return clubs.size();
    }

}
