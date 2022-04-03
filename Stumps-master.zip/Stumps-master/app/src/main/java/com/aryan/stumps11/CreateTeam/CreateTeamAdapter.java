package com.aryan.stumps11.CreateTeam;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.aryan.stumps11.Model.ModelClass;
import com.aryan.stumps11.R;
import com.aryan.stumps11.api_integration.OnClick;
import com.bumptech.glide.Glide;

import java.util.List;

public class CreateTeamAdapter extends RecyclerView.Adapter<CreateTeamAdapter.ViewHolder> {
    Context cc;
    List<ModelClass> list;
    String mobile;
    static int crept;
    int hello, wk, bat, all, bwl, creditPoints;
    static int addCreditPoint;
    private SharedPreferences sp;
    OnClick onClick;
    int count = 0, full = 0, countrycount = 0;
    double creditcount;
    private db d;
    private String TAG = "sachin";
    private PointsCallback pointsCallback;

    public CreateTeamAdapter(Context cc, List<ModelClass> list, OnClick onClick, PointsCallback pointsCallback) {
        this.cc = cc;
        this.list = list;
        this.onClick = onClick;
        this.pointsCallback = pointsCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(cc);
        View view = layoutInflater.inflate(R.layout.playerlayout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ModelClass mm = list.get(position);
        DataBase db = new DataBase(cc);
        db d = new db(cc);
        SharedPreferences mob = cc.getSharedPreferences("Mobile", MODE_PRIVATE);
        mobile = mob.getString("mKey", "0");
        String key = list.get(position).getId();
        Glide.with(cc).load(list.get(position).getImages());
        holder.ttcountry.setText(list.get(position).getTname());
        holder.ttcredits.setText(list.get(position).getCredits());
        holder.ttpts.setText(list.get(position).getPts());
        holder.ttstatus.setText(list.get(position).getStatus());
        holder.ttname.setText(list.get(position).getPname());
        //boolean check = Update(list.get(position).getPname());
        boolean check = SelectedData.getSelectedData().getPlayer(key);
        Log.e(TAG, "onBindViewHolder: " + position + " " + check);

        holder.cb.setChecked(check);

        if (holder.cb.isChecked()) {
            holder.rr.setBackgroundColor(ContextCompat.getColor(cc, R.color.PlayerSelected));
        } else {
            holder.rr.setBackgroundColor(Color.WHITE);
        }


    }


    @SuppressLint("NewApi")
    void putPlayer(String key, boolean b) {
        if (!(SelectedData.getSelectedData().putPlayer(key, b))) {
            Toast.makeText(cc, "Max 11 Players.", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    boolean playerCount(SelectedData.Role role, int status, String teamName, CheckBox cb, int position, RelativeLayout rr) {
        boolean check = true;

        switch (role) {
            case WK:
                if (SelectedData.getWicketKeeper() < 4) {
                    if (cb.isChecked()){
                        SelectedData.setWicketKeeper((SelectedData.getWicketKeeper() + status));
                        SelectedData.getSelectedData().getData().put(list.get(position).getId(), new SelectedData.data(list.get(position).getId(), list.get(position).getPname(), list.get(position).getTname(), list.get(position).getRole(), list.get(position).getCredits()));
                        cb.setChecked(true);
                        cb.setBackgroundColor(ContextCompat.getColor(cc, R.color.PlayerSelected));
                    }
                    else{
                        SelectedData.removePlayer(list.get(position).getId());
                        cb.setChecked(false);
                        rr.setBackgroundColor(Color.WHITE);
                      SelectedData.setWicketKeeper((SelectedData.getWicketKeeper() -1));
                    }

                }
                else if(SelectedData.getWicketKeeper() == 4){
                    if (cb.isChecked()){
                        check = false;
                        Toast.makeText(cc, "Max 4 Wicket Keeper", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        SelectedData.removePlayer(list.get(position).getId());
                        cb.setChecked(false);
                        rr.setBackgroundColor(Color.WHITE);
                        SelectedData.setWicketKeeper((SelectedData.getWicketKeeper() -1));
                    }
                }
                else {
                    check = false;
                    SelectedData.removePlayer(list.get(position).getId());
                    rr.setBackgroundColor(Color.WHITE);
                    Toast.makeText(cc, "Max 4 Wicket Keeper.", Toast.LENGTH_SHORT).show();
                }
                break;
            case BAT:
                if (SelectedData.getBatsman() < 6) {
                    if (cb.isChecked()){
                        SelectedData.setBatsman((SelectedData.getBatsman() + status));
                        SelectedData.getSelectedData().getData().put(list.get(position).getId(), new SelectedData.data(list.get(position).getId(), list.get(position).getPname(), list.get(position).getTname(), list.get(position).getRole(), list.get(position).getCredits()));
                        cb.setChecked(true);
                        cb.setBackgroundColor(ContextCompat.getColor(cc, R.color.PlayerSelected));
                    }
                    else{
                        SelectedData.removePlayer(list.get(position).getId());
                        cb.setChecked(false);
                        rr.setBackgroundColor(Color.WHITE);
//                        SelectedData.setBatsman((SelectedData.getBatsman() -1));
                    }

                }
                else if(SelectedData.getBatsman() == 6){
                    if (cb.isChecked()){
                        check = false;
                        Toast.makeText(cc, "Max 6 Batsman.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        SelectedData.removePlayer(list.get(position).getId());
                        cb.setChecked(false);
                        rr.setBackgroundColor(Color.WHITE);
                        SelectedData.setBatsman((SelectedData.getBatsman() -1));
                    }
                }
                else {
                    check = false;
                    SelectedData.removePlayer(list.get(position).getId());
                    rr.setBackgroundColor(Color.WHITE);
                    Toast.makeText(cc, "Max 6 Batsman.", Toast.LENGTH_SHORT).show();
                }

                break;
            case BOWL:
                if (SelectedData.getBowler() < 6) {
                    if (cb.isChecked()){
                        SelectedData.setBowler((SelectedData.getBowler() + status));
                        SelectedData.getSelectedData().getData().put(list.get(position).getId(), new SelectedData.data(list.get(position).getId(), list.get(position).getPname(), list.get(position).getTname(), list.get(position).getRole(), list.get(position).getCredits()));
                        cb.setChecked(true);
                        cb.setBackgroundColor(ContextCompat.getColor(cc, R.color.PlayerSelected));
                    }
                    else{
                        SelectedData.removePlayer(list.get(position).getId());
                        cb.setChecked(false);
                        rr.setBackgroundColor(Color.WHITE);
//                        SelectedData.setBowler((SelectedData.getBowler() -1));
                    }

                }
                else if(SelectedData.getBowler() == 6){
                    if (cb.isChecked()){
                        check = false;
                        Toast.makeText(cc, "Max 6 Bowler.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        SelectedData.removePlayer(list.get(position).getId());
                        cb.setChecked(false);
                        rr.setBackgroundColor(Color.WHITE);
                        SelectedData.setBowler((SelectedData.getBowler() -1));
                    }
                }
                else {
                    check = false;
                    rr.setBackgroundColor(Color.WHITE);
                    SelectedData.removePlayer(list.get(position).getId());
                    Toast.makeText(cc, "Max 6 Bowler.", Toast.LENGTH_SHORT).show();
                }

                break;
            case ALL:
                if (SelectedData.getAllRounder() < 6) {
                    if (cb.isChecked()){
                        SelectedData.setAllRounder((SelectedData.getAllRounder() + status));
                        SelectedData.getSelectedData().getData().put(list.get(position).getId(), new SelectedData.data(list.get(position).getId(), list.get(position).getPname(), list.get(position).getTname(), list.get(position).getRole(), list.get(position).getCredits()));
                        cb.setChecked(true);
                        cb.setBackgroundColor(ContextCompat.getColor(cc, R.color.PlayerSelected));
                    }
                    else{
                        SelectedData.removePlayer(list.get(position).getId());
                        cb.setChecked(false);
                        rr.setBackgroundColor(Color.WHITE);
                        SelectedData.setAllRounder((SelectedData.getAllRounder() -1));
                    }

                }
                else if(SelectedData.getAllRounder() == 6){
                    if (cb.isChecked()){
                        check = false;
                        Toast.makeText(cc, "Max 6 All Rounder.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        SelectedData.removePlayer(list.get(position).getId());
                        cb.setChecked(false);
                        rr.setBackgroundColor(Color.WHITE);
                        SelectedData.setAllRounder((SelectedData.getAllRounder() -1));
                    }
                }
                else {
                    check = false;
                    rr.setBackgroundColor(Color.WHITE);
                    SelectedData.removePlayer(list.get(position).getId());
                    Toast.makeText(cc, "Max 6 All Rounder.", Toast.LENGTH_SHORT).show();
                }

                break;
        }
        return check;
    }


    public void Save(String Key, boolean value) {
        SharedPreferences sp = cc.getSharedPreferences("Preference", MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sp.edit();
        editor1.putBoolean(Key, value);
        editor1.apply();
    }


    public boolean Update(String Key) {
        SharedPreferences sp = cc.getSharedPreferences("Preference", MODE_PRIVATE);
        boolean kk = sp.getBoolean(Key, false);
        return kk;

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        if (holder.cb != null) {
            holder.cb.setOnClickListener(null);
        }
        super.onViewRecycled(holder);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ii;
        CheckBox cb;
        TextView ttname, ttstatus, ttpts, ttcredits, ttcountry;
        RelativeLayout rr;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rr = itemView.findViewById(R.id.PlayerLayout);
            cb = itemView.findViewById(R.id.checkBox);
//            ii=itemView.findViewById(R.id.imageView22);
            ttname = itemView.findViewById(R.id.textView54);
            ttstatus = itemView.findViewById(R.id.textView64);
            ttpts = itemView.findViewById(R.id.textView65);
            ttcredits = itemView.findViewById(R.id.textView66);
            ;
            ttcountry = itemView.findViewById(R.id.textView68);
            cb.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            String key = "Hello" + list.get(getAdapterPosition()).getPname() + getAdapterPosition();
            boolean check = SelectedData.getSelectedData().getPlayer(key);
            updateCheckValues((!check), getAdapterPosition(), cb, rr);
        }
    }

    @SuppressLint("NewApi")
    void updateCheckValues(boolean b, int position, CheckBox cb, RelativeLayout rr) {

        sp = cc.getSharedPreferences("Counts", Context.MODE_PRIVATE);

        if (SelectedData.getSelectedData().getPlayerCount() >= 11) {
            Toast.makeText(cc, "We can not add more than 11 player", Toast.LENGTH_SHORT).show();
            cb.setChecked(false);
            return;

        }

        if (list.get(position).getRole().equals("wk")) {
            if (playerCount(SelectedData.Role.WK, 1, list.get(position).getTname(), cb, position, rr)) {

                }
            else{
                cb.setChecked(false);
                rr.setBackgroundColor(Color.WHITE);
            }

        }

        if (list.get(position).getRole().equals("bat")) {
            if (playerCount(SelectedData.Role.BAT, 1, list.get(position).getTname(),cb,position,rr)) {

            }
            else{
                cb.setChecked(false);
                rr.setBackgroundColor(Color.WHITE);
            }
        }

        if (list.get(position).getRole().equals("all")) {
            if (playerCount(SelectedData.Role.ALL, 1, list.get(position).getTname(), cb, position, rr)) {


            } else {

                cb.setChecked(false);
                rr.setBackgroundColor(Color.WHITE);
            }
        }

        if (list.get(position).getRole().equals("bowl")) {
            if (playerCount(SelectedData.Role.BOWL, 1, list.get(position).getTname(), cb, position, rr)) {

                 } else {

                cb.setChecked(false);
                rr.setBackgroundColor(Color.WHITE);
               
            }
        }


        SharedPreferences.Editor editor1 = sp.edit();
        editor1.putInt("Key", SelectedData.getSelectedData().getPlayerCount());
        editor1.putInt("wKey", SelectedData.getSelectedData().getRoleCount("wk"));
        editor1.putInt("bKey", SelectedData.getSelectedData().getRoleCount("bat"));
        editor1.putInt("aKey", SelectedData.getSelectedData().getRoleCount("all"));
        editor1.putInt("bwlKey", SelectedData.getSelectedData().getRoleCount("bowl"));
        editor1.putFloat("creditPoints", SelectedData.getSelectedData().getCreditPoints());
//                editor1.clear();
        editor1.apply();
        pointsCallback.onPointsUpdate();
        //notifyItemChanged(position);
    }
}


