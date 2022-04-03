package com.aryan.stumps11.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aryan.stumps11.ApiModel.profile.DummyTeamResponse.MyDummyTeamData;
import com.aryan.stumps11.ApiModel.profile.DummyTeamResponse.MyDummyTeamPlayer11;
import com.aryan.stumps11.ApiModel.profile.DummyTeamResponse.MyTeamDummyModel;
import com.aryan.stumps11.CreateTeam.CreateTeams;
import com.aryan.stumps11.CreateTeam.DataBase;
import com.aryan.stumps11.CreateTeam.GreenBackground;
import com.aryan.stumps11.EditTeam.EditTeamActivity;
import com.aryan.stumps11.HomePageClick.Contests;
import com.aryan.stumps11.HomePageClick.db.MyTeamModel;
import com.aryan.stumps11.Model.ModelClass;
import com.aryan.stumps11.R;

import java.util.List;

public class MyTeamAdapter extends RecyclerView.Adapter<MyTeamAdapter.ViewHolder> {
    private List<MyTeamDummyModel> modelClasses;
    private Context contests;


   private String mobile;
   private int hello,wk,bat,all,bwl;

    public MyTeamAdapter(List<MyTeamDummyModel> modelClasses, Context contests) {
        this.modelClasses = modelClasses;
        this.contests = contests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_my_team,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MyTeamDummyModel myDummyTeamPlayer11=modelClasses.get(position);
        holder.tvCaptinName.setText(myDummyTeamPlayer11.getcName());
        holder.tvVCName.setText(myDummyTeamPlayer11.getVcName());
        holder.tvMyTeamId.setText("My Match ID : "+myDummyTeamPlayer11.getTeamID());

        String cid=myDummyTeamPlayer11.getCid();



//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent teamPreview=new Intent(contests, GreenBackground.class);
//                contests.startActivity(teamPreview);
//
//            }
//        });
        holder.imageViewEditTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(contests, EditTeamActivity.class);
                intent .putExtra("match_id",myDummyTeamPlayer11.getTeamID());
                intent.putExtra("cid",cid);
                intent.putExtra("id",myDummyTeamPlayer11.getUserId());
//                intent.putExtra("wk",myDummyTeamPlayer11.getTotalWk());
//                intent.putExtra("all",myDummyTeamPlayer11.getTotalAR());
//                intent.putExtra("bat",myDummyTeamPlayer11.getTotalBat());
//                intent.putExtra("bwl",myDummyTeamPlayer11.getTotalBwl());

                contests.startActivity(intent);
            }
        });
//        holder.tvVCName.setText(myDummyTeamPlayer11.getVcaptain().toString());
//        holder.tvTeamNameA.setText(myDummyTeamPlayer11);




    }

    @Override
    public int getItemCount() {
        return modelClasses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTeamNameA,tvTeamNameB,tvTeamTotalPlayerA,tvTeamTotalPlayerB,tvCaptinName,tvVCName;
        private ImageView imgCaptain,imgVCNAme,imageViewEditTeam;
        private TextView tvMyTeamId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCaptinName=itemView.findViewById(R.id.custom_my_team_c_name);
            imgCaptain=itemView.findViewById(R.id.custom_my_team_c_player);

            tvVCName=itemView.findViewById(R.id.custom_my_team_vc_name);
            imgVCNAme=itemView.findViewById(R.id.custom_my_team_vc_player);

//            tvTeamNameA=itemView.findViewById(R.id.custom_my_team_a_name);
//            tvTeamTotalPlayerA=itemView.findViewById(R.id.custom_my_team_a_total_number_player);
//
//            tvTeamNameB=itemView.findViewById(R.id.custom_my_team_b_name);
//            tvTeamTotalPlayerB=itemView.findViewById(R.id.custom_my_team_b_total_player);

            imageViewEditTeam=itemView.findViewById(R.id.custom_my_team_edit);
            tvMyTeamId=itemView.findViewById(R.id.custom_my_team_id);



        }
    }
}
