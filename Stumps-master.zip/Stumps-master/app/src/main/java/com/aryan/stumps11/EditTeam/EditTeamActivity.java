package com.aryan.stumps11.EditTeam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aryan.stumps11.Adapters.TabAdapter;
import com.aryan.stumps11.ApiModel.profile.createTeam.CreateReqData;
import com.aryan.stumps11.ApiModel.profile.createTeam.CreateTeamReq;
import com.aryan.stumps11.ApiModel.profile.dummyCreateRes.DummyResponse;
import com.aryan.stumps11.CreateTeam.AR;
import com.aryan.stumps11.CreateTeam.BAT;
import com.aryan.stumps11.CreateTeam.BWL;
import com.aryan.stumps11.CreateTeam.ChooseCaptainandVC;
import com.aryan.stumps11.CreateTeam.CreateTeams;
import com.aryan.stumps11.CreateTeam.DataBase;
import com.aryan.stumps11.CreateTeam.EditTabAdapter;
import com.aryan.stumps11.CreateTeam.WK;
import com.aryan.stumps11.Extra.CommonData;
import com.aryan.stumps11.Model.ModelClass;
import com.aryan.stumps11.R;
import com.aryan.stumps11.api_integration.CheckConnection;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditTeamActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{


    public static String MATCH_ID;
    private TabLayout tt;
    private ViewPager vv;
    private TextView t80;
    private MaterialButton bb,preview;
    private SharedPreferences sp;
    private int hello,wk,bat,all,bwl;
    private ProgressBar pp;
    private String teamNameA;
    private String teamNameB;
    private EditDatabase db;
    public static  String C_I_D;
    private CircleImageView circleImageViewTeamA,circleImageViewTeamB;
    private TextView tvNameA,tvNameB;
    private TextView tvCreditPoints;
    private  int creditPoints;
    private Cursor cc;
    private  String mobile;
    private List<Integer> addCredit;
    private String pname,prole,C,vc,pid,credit;
    private TextView tvCreateTeamName;
    private TextView tvCreateTeamTime;
    private String  totalWk;
    private String totalAll;
    private String totalBat;
    private String totalBwl;


    private ImageView imgBacKBtn;
    private List<EditTeamModel> list;
    private CreateReqData createReqData;
    private List<CreateReqData> createReqData1;
    private String ppts;
    private String captainName,viceCaptain;
    public static String teamId;
    public static String comId;
    public static String ID;
    private  int all1,wk1,bat1,bwl1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_team);

        circleImageViewTeamA=findViewById(R.id.edit_team_create_team_a_logo);
        circleImageViewTeamB=findViewById(R.id.edit_team_create_team_b_logo);
        tvNameA=findViewById(R.id.edit_team_create_team_a_name);
        tvNameB=findViewById(R.id.edit_team_create_team_b_name);
        tvCreateTeamName=findViewById(R.id.edit_team_create_team_name);
        tvCreateTeamTime=findViewById(R.id.edit_team_create_team_time);
        imgBacKBtn=findViewById(R.id.edit_team_create_team_image_back_btn);
        preview=findViewById(R.id.edit_team_Preview);
        bb=findViewById(R.id.edit_team_Next);
        tt=findViewById(R.id.edit_team_tablayout);
        vv=findViewById(R.id.edit_team_viewpager);
        pp=findViewById(R.id.edit_team_progressBar);
        t80=findViewById(R.id.edit_team_count_player);
        createReqData1=new ArrayList<>();


        sp=getSharedPreferences("Counts",MODE_PRIVATE);
        sp.registerOnSharedPreferenceChangeListener(this);


        hello=sp.getInt("Key",0);


        wk=sp.getInt("wKey",0);
        bat=sp.getInt("bKey",0);
        all=sp.getInt("aKey",0);
        bwl=sp.getInt("bwlKey",0);

        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.EditdeleteReminder();
                startActivity(new Intent(EditTeamActivity.this,EditCVCActivity.class));
                finish();
            }
        });


        setup(vv);
        tt.setupWithViewPager(vv);


    }

    private void setup(ViewPager viewPager){
        try{
            EditTabAdapter tabadapter=new EditTabAdapter(getSupportFragmentManager());
            tabadapter.EditFrag("WK("+wk+")",new EditWkFargment());
            tabadapter.EditFrag("BAT("+bat+")",new EditBatFragments());
            tabadapter.EditFrag("AR("+all+")",new EditAllRounderFragments());
            tabadapter.EditFrag("BWL("+bwl+")",new EditBallFargment());
            viewPager.setAdapter(tabadapter);

        }catch (Exception e){
            Toast.makeText(this, "Exception "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        sharedPreferences=getSharedPreferences("Counts",MODE_PRIVATE);
        hello=sharedPreferences.getInt("Key",0);
        wk=sharedPreferences.getInt("wKey",0);
        bat=sharedPreferences.getInt("bKey",0);
        all=sharedPreferences.getInt("aKey",0);
        bwl=sharedPreferences.getInt("bwlKey",0);
        creditPoints=sharedPreferences.getInt("creditPoints",0);
        pp.setProgress(hello*100/(11000/1000));

        t80.setText(""+hello+" Players Selected");

        tt.getTabAt(0).setText("WK"+"("+wk+")");
        tt.getTabAt(1).setText("BAT"+"("+bat+")");
        tt.getTabAt(2).setText("AR"+"("+all+")");
        tt.getTabAt(3).setText("BWL"+"("+bwl+")");
    }


    private void addPlayer11(){



        SharedPreferences mob=getSharedPreferences("Mobile",MODE_PRIVATE);
        mobile=mob.getString("mKey","0");
        SharedPreferences sp=getSharedPreferences("MY_APP", Context.MODE_PRIVATE);


        if (!db.equals(MATCH_ID)){
            cc=db.EditDisplayPlayer(mobile);
            cc.moveToFirst();
            do{
                pid=cc.getString(1);
                String secoind=cc.getString(2);
                pname=cc.getString(3);
//            prole=cc.getString(4);
                String pcountry=cc.getString(4);

                ppts=cc.getString(5);

                credit=cc.getString(7);
                C=cc.getString(8);
                vc=cc.getString(9);


                try{
                    int i=Integer.parseInt(credit);

                    addCredit=new ArrayList<Integer>();
                    addCredit.add(i);
                    for (int totalCredit : addCredit){

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }



                createReqData=new CreateReqData();

                createReqData.setPid(pid);
                createReqData.setCredit(credit);
                createReqData.setName(pname);


                createReqData1.add(createReqData);


            }while (cc.moveToNext());
        }
        SharedPreferences preferences = EditTeamActivity.this.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);//second parameter default value.
        String tokenName="Bearer "+retrivedToken;

        CreateTeamReq createTeamReq=new CreateTeamReq();
        createTeamReq.setTeamId(MATCH_ID);
        createTeamReq.setPlayer11(createReqData1);







        try {
            CheckConnection.api.creteTeamTempData(tokenName,createTeamReq).enqueue(new Callback<DummyResponse>() {
                @Override
                public void onResponse(Call<DummyResponse> call, Response<DummyResponse> response) {
                    if (response.isSuccessful()){

                        db.EditdeleteReminder();

                    assert response.body() != null;
                        Toast.makeText(EditTeamActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        Intent intent1=new Intent(EditTeamActivity.this, ChooseCaptainandVC.class);
                        intent1.putExtra(CommonData.Match_id,MATCH_ID);
                        startActivity(intent1);
                        finish();
                    }else if(response.code()==500){
                        Toast.makeText(EditTeamActivity.this, "Server Error 500"+response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }else if(response.code()==422){
                        Toast.makeText(EditTeamActivity.this, "Error 422", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(EditTeamActivity.this, "ewrfdvn", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DummyResponse> call, Throwable t) {
                    Toast.makeText(EditTeamActivity.this, "onFailure "+t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }catch (Exception e){
            Toast.makeText(this, "Exception "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        clearKeys();
    }

    private void clearKeys(){
        SharedPreferences  sharedPreferences=getSharedPreferences("Save",MODE_PRIVATE);
        SharedPreferences.Editor s=sharedPreferences.edit();
        s.clear();
        s.apply();
    }

}