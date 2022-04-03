package com.aryan.stumps11.CreateTeam;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aryan.stumps11.R;

public class BAT extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_b_a_t, container, false);
       RecyclerView rr=view.findViewById(R.id.RecyclerView);
        PlayersClass pc=new PlayersClass();
        pc.PlayersApi(rr,getContext(),"bat", getActivity());
        return view;
    }
}