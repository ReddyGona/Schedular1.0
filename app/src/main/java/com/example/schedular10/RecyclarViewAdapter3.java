package com.example.schedular10;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclarViewAdapter3 extends RecyclerView.Adapter<RecyclarViewAdapter3.AcademicViewHolder>{

    List<HomeActivity.ACADEMIC> acad;
    Context context;

    public RecyclarViewAdapter3(List<HomeActivity.ACADEMIC> acad, Context context){
        this.context=context;
        this.acad=acad;
    }

    public class AcademicViewHolder extends RecyclerView.ViewHolder {

        CardView CardView;
        TextView text;


        public AcademicViewHolder(@NonNull View itemView) {
            super(itemView);
            CardView=(CardView)itemView.findViewById(R.id.card_sat_sub);
            text=(TextView)itemView.findViewById(R.id.sat_sun_note);
        }
    }


    @NonNull
    @Override
    public RecyclarViewAdapter3.AcademicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View acd_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sat_sun_layout, parent, false);
        RecyclarViewAdapter3.AcademicViewHolder avh_1 = new RecyclarViewAdapter3.AcademicViewHolder(acd_view);
        return avh_1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclarViewAdapter3.AcademicViewHolder holder, int position) {

        holder.text.setText(acad.get(position).acad_data);

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return acad.size();
    }


}
