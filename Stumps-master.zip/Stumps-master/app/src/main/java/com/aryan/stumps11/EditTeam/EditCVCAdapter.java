package com.aryan.stumps11.EditTeam;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aryan.stumps11.CreateTeam.DataBase;
import com.aryan.stumps11.Model.ModelClass;
import com.aryan.stumps11.R;
import com.google.android.material.chip.Chip;

import java.util.List;

public class EditCVCAdapter extends RecyclerView.Adapter<EditCVCAdapter.ViewHolder> {
    List<EditCVCModel> list;
    Context cc;
    int index=-1,index2=-1;

    //
    public EditCVCAdapter(List<EditCVCModel> list, Context cc) {
        this.list = list;
        this.cc = cc;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_edit_cvc,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EditCVCModel mm=list.get(position);
        SharedPreferences mob=cc.getSharedPreferences("Mobile",cc.MODE_PRIVATE);
//        String mobile=mob.getString("mKey","0");
        String mobile=list.get(position).getId();
        String isVerified=list.get(position).getCaptain();
        String isVc=list.get(position).getVc();
        EditDatabase db=new EditDatabase(cc);
//        holder.i22.setImageResource(R.drawable.image);
        holder.t54.setText(list.get(position).getPname1());
        holder.t55.setText(list.get(position).getTeamname1()+" | "+list.get(position).getRole1());

        holder.tt61.setText(list.get(position).getPts1());
        holder.c.setChecked(position==index);
        holder.vc.setChecked(position==index2);
        holder.c.setChecked(Boolean.parseBoolean(isVerified));
        holder.vc.setChecked(Boolean.parseBoolean(isVc));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView i22;
        TextView t54,t55,tt61;
        Chip c,vc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            c = itemView.findViewById(R.id.edit_Captain);
            vc = itemView.findViewById(R.id.edit_ViceCaptain);
//            i22 = itemView.findViewById(R.id.imageView22);
            t54 = itemView.findViewById(R.id.edit_cvc_name);
            t55 = itemView.findViewById(R.id.edit_cvc_type);
            tt61 = itemView.findViewById(R.id.edit_cvc_points);

        }
    }
}
