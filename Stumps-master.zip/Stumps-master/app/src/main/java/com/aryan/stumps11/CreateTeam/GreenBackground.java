package com.aryan.stumps11.CreateTeam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aryan.stumps11.Model.ModelClass;
import com.aryan.stumps11.R;

import java.util.ArrayList;
import java.util.List;

public class GreenBackground extends AppCompatActivity {
RecyclerView wk,bat,all,bwl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_background);

            SharedPreferences mob = getSharedPreferences("Mobile", MODE_PRIVATE);
            String mobile = mob.getString("mKey", "0");
            DataBase db = new DataBase(GreenBackground.this);
            wk = findViewById(R.id.WK);
            bat = findViewById(R.id.Bat);
            all = findViewById(R.id.ALl);
            bwl = findViewById(R.id.Bwl);
            List<ModelClass> list = new ArrayList<>();
            List<ModelClass> list2 = new ArrayList<>();
            List<ModelClass> list3 = new ArrayList<>();
            List<ModelClass> list4 = new ArrayList<>();
            Cursor cc = db.DisplayPlayer(mobile);
            cc.moveToFirst();
            do {
                String pname = cc.getString(2);
                String pcr = cc.getString(6);
                String role = cc.getString(4);
                String cap=cc.getString(7);
                String VC=cc.getString(8);
                if (role.equals("wk")) {
                    ModelClass mm1 = new ModelClass();
                    mm1.setPlayername(pname);
                    mm1.setPcredit(pcr + " cr");
                    if(pname.equals(cap)){
                        mm1.setCap("C");
                    }
                    else {
                        mm1.setCap(null);
                    }

                    if(pname.equals(VC)){
                        mm1.setVc("V");
                    }
                    else {
                        mm1.setVc(null);
                    }
                    list.add(mm1);
                    Method(wk, 4, list);
                }
                if (role.equals("bat")) {
                    ModelClass mm1 = new ModelClass();
                    mm1.setPlayername(pname);
                    mm1.setPcredit(pcr + " cr");
                    if(pname.equals(cap)){
                        mm1.setCap("C");
                    }
                    else {
                        mm1.setCap(null);
                    }
                    if(pname.equals(VC)){
                        mm1.setVc("V");
                    }
                    else {
                        mm1.setVc(null);
                    }
                    list2.add(mm1);
                    Method(bat, 6, list2);
                }
                if (role.equals("all")) {
                    ModelClass mm1 = new ModelClass();
                    mm1.setPlayername(pname);
                    mm1.setPcredit(pcr + " cr");
                    if(pname.equals(cap)){
                        mm1.setCap("C");
                    }
                    else {
                        mm1.setCap(null);
                    }
                    if(pname.equals(VC)){
                        mm1.setVc("V");
                    }
                    else {
                        mm1.setVc(null);
                    }
                    list3.add(mm1);
                    Method(all, 4, list3);
                }
                if (role.equals("bowl")) {
                    ModelClass mm1 = new ModelClass();
                    mm1.setPlayername(pname);
                    mm1.setPcredit(pcr + " cr");
                    if(pname.equals(cap)){
                        mm1.setCap("C");
                    }
                    else {
                        mm1.setCap(null);
                    }
                    if(pname.equals(VC)){
                        mm1.setVc("V");
                    }
                    else {
                        mm1.setVc(null);
                    }
                    list4.add(mm1);
                    Method(bwl, 6, list4);
                }
            } while (cc.moveToNext());

    }
   public void Method(RecyclerView rr,int count,List<ModelClass>list){
        rr.setLayoutManager(new GridLayoutManager(GreenBackground.this,count));
        rr.setHasFixedSize(true);
        GreenAdapter ga=new GreenAdapter(GreenBackground.this,list);
        rr.setAdapter(ga);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}