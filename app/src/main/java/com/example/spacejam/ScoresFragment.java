package com.example.spacejam;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class ScoresFragment extends Fragment {

    private  List<Score> scores;
    private RecyclerView recycler;

    public ScoresFragment() {
        // Required empty public constructor

    }

    private ScoresFragment(List<Score> scores) {
        this.scores = scores;
    }


    public static ScoresFragment newInstance(List<Score> scores ) {
        ScoresFragment fragment = new ScoresFragment(scores);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scores, container, false);

        recycler = (RecyclerView)view.findViewById(R.id.recycler);
        ScoresAdapter adapter = new ScoresAdapter(scores, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recycler.setAdapter(adapter);
        recycler.setLayoutManager(layoutManager);

//        recycler.invalidate();


        return view;
    }

}
