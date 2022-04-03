package com.aryan.stumps11.CreateTeam;

import static com.aryan.stumps11.CreateTeam.CreateTeams.MATCH_ID;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aryan.stumps11.R;


public class WK extends Fragment  {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_w_k, container, false);

        String matchid=MATCH_ID;

        Log.e("llkl",matchid+"");


       RecyclerView rr=view.findViewById(R.id.RecyclerView);
       PlayersClass pc=new PlayersClass();
       pc.PlayersApi(rr,getContext(),"wk", getActivity());
       return view;
    }
}