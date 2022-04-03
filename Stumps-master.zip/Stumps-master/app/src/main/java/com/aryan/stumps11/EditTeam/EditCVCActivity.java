package com.aryan.stumps11.EditTeam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.aryan.stumps11.ApiModel.profile.createTeam.CreateReqData;
import com.aryan.stumps11.ApiModel.profile.createTeam.CreateTeamReq;
import com.aryan.stumps11.ApiModel.profile.dummyCreateRes.DummyResponse;
import com.aryan.stumps11.ApiModel.profile.elevenPlayer.ElevenPlayer11;
import com.aryan.stumps11.CreateTeam.CVCAdapter;
import com.aryan.stumps11.CreateTeam.ChooseCaptainandVC;
import com.aryan.stumps11.CreateTeam.CreateTeams;
import com.aryan.stumps11.CreateTeam.DataBase;
import com.aryan.stumps11.CreateTeam.GreenBackground;
import com.aryan.stumps11.Home.HomePage;
import com.aryan.stumps11.Model.ModelClass;
import com.aryan.stumps11.R;
import com.aryan.stumps11.api_integration.CheckConnection;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCVCActivity extends AppCompatActivity {

    private MaterialButton bb,next;
    private String pname,prole,C,vc,pid,credit;
    private ImageView imgBacKBtn;
    private  String mobile;
    private EditDatabase db;
    private Cursor cc;
    private List<EditCVCModel> list;
    private CreateReqData createReqData;
    private List<CreateReqData> createReqData1;
    private String ppts;
    public static String MATCH_ID;
    private int addCreditPoint;
    private List<Integer> addCredit;
    private String userID;
    private List<ElevenPlayer11> elevenPlayer11List;
    private RecyclerView rr;
    private String captainName,viceCaptain;
    private String captainNameTrue,viceCaptainTrue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cvcactivity);

        next=findViewById(R.id.edit_Next);
        bb=findViewById(R.id.edit_Preview);
        imgBacKBtn=findViewById(R.id.edit_cvcs_back_btn);

        rr=findViewById(R.id.edit_CVC);
        rr.setHasFixedSize(false);
        rr.setLayoutManager(new LinearLayoutManager(EditCVCActivity.this));
        list=new ArrayList<>();
        createReqData1=new ArrayList<>();
        elevenPlayer11List=new ArrayList<>();


        imgBacKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditCVCActivity.this, GreenBackground.class));
                overridePendingTransition(0,0);
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addPlayer11();
            }
        });




        SharedPreferences mob=getSharedPreferences("Mobile",MODE_PRIVATE);
        mobile=mob.getString("mKey","0");
        SharedPreferences sp=getSharedPreferences("MID", Context.MODE_PRIVATE);
        String mid=sp.getString("MatchID","0");

        rr.setHasFixedSize(false);
        rr.setLayoutManager(new LinearLayoutManager(EditCVCActivity.this));
        list=new ArrayList<>();
        createReqData1=new ArrayList<>();

        db=new EditDatabase(EditCVCActivity.this);
        cc=db.EditDisplayPlayer(mobile);
        cc.moveToFirst();
        do{
            pid=cc.getString(1);
            pname=cc.getString(3);
            prole=cc.getString(5);
            String pcountry=cc.getString(4);
            ppts=cc.getString(6);
            credit=cc.getString(7);


            C= cc.getString(8); // captain name
            vc= cc.getString(9); // vice captain




//            createReqData.setCaptain(Boolean.parseBoolean(C.replace(C,"true")));

            EditCVCModel mm=new EditCVCModel();
            mm.setId(pid);
            mm.setPname1(pname);
            mm.setRole1(prole);
            mm.setTeamname1(pcountry);
            mm.setPts1(ppts);
            mm.setCaptain(C);
            mm.setVc(vc);
            list.add(mm);



            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addPlayer11();
                }
            });
            EditCVCAdapter ca=new EditCVCAdapter(list,EditCVCActivity.this);
            rr.setAdapter(ca);

        }while (cc.moveToNext());

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EditCVCActivity.this, CreateTeams.class));
        CustomIntent.customType(EditCVCActivity.this,"right-to-left");
        finish();
    }




    private void addPlayer11(){




        SharedPreferences preferences1 = EditCVCActivity.this.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken1  = preferences1.getString("TOKEN",null);//second parameter default value.
        String tokenName="Bearer "+retrivedToken1;

        CreateTeamReq createTeamReq=new CreateTeamReq();
//        createTeamReq.setTeamId("53420");
        //C.replace(C,"0");
        SharedPreferences CN=EditCVCActivity.this.getSharedPreferences("CName",MODE_PRIVATE);



        // db=new DataBase(ChooseCaptainandVC.this);
        db=new EditDatabase(EditCVCActivity.this);
        cc=db.EditDisplayPlayer(mobile);
        cc.moveToFirst();
        do{
            pid=cc.getString(1);
            pname=cc.getString(3);
            prole=cc.getString(5);
            String pcountry=cc.getString(4);
            ppts=cc.getString(6);
            credit=cc.getString(7);
            createReqData=new CreateReqData();

            createReqData.setPid(pid);
            createReqData.setCredit(credit);
            createReqData.setName(pname);
            createReqData.setRole(prole);

            C= cc.getString(8); // captain name
            vc= cc.getString(9); // vice captain

//

//            createReqData.setCaptain(C);
//            createReqData.setVcaptain(vc);
            createReqData1.add(createReqData);


            createTeamReq.setPlayer11(createReqData1);



        }while (cc.moveToNext());

//        captainName=CN.getString("CNAME",null);
        SharedPreferences VCN=EditCVCActivity.this.getSharedPreferences("VCName",MODE_PRIVATE);

//        viceCaptain=VCN.getString("VCName",null);
//
//        Toast.makeText(this, "sgsf "+C, Toast.LENGTH_SHORT).show();





        //  Toast.makeText(ChooseCaptainandVC.this,"Captain Name : "+b1,Toast.LENGTH_SHORT).show();
//        Toast.makeText(ChooseCaptainandVC.this,"Vice Captain Captain Name : "+b2,Toast.LENGTH_SHORT).show();



        createTeamReq.setTeamId(MATCH_ID);

        //    Toast.makeText(this, "", Toast.LENGTH_SHORT).show();




        try {
            CheckConnection.api.addPlayer11(tokenName,createTeamReq).enqueue(new Callback<DummyResponse>() {
                @Override
                public void onResponse(Call<DummyResponse> call, Response<DummyResponse> response) {
                    if (response.isSuccessful()){
                        startActivity(new Intent(EditCVCActivity.this, HomePage.class));
                        finish();
//                             db.removeOldId();
                        db.EditdeleteReminder();
                        Toast.makeText(EditCVCActivity.this, " "+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }else if(response.code()==500){
                        Toast.makeText(EditCVCActivity.this, "Server Error 500"+response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }else if(response.code()==422){
                        Toast.makeText(EditCVCActivity.this, "Error 422", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(EditCVCActivity.this, "ewrfdvn", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DummyResponse> call, Throwable t) {
                    Toast.makeText(EditCVCActivity.this, "onFailure "+t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }catch (Exception e){
//            Toast.makeText(this, "Exception "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}