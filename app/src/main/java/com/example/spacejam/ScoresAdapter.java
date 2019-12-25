package com.example.spacejam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScoresAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Score> scores;

    public ScoresAdapter(List<Score> scores, Context context) {
        this.scores = scores;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.score_row, parent, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Score score = scores.get(position);
        CustomViewHolder custom = (CustomViewHolder) holder;

        custom.getName().setText(score.getPlayerName());
        custom.getNumber().setText(score.getPlayerScore() +"");
//        custom.getLatitude().setText(score.getLatitude()+ "");
//        custom.getLongtitude().setText(score.getLongitude()+ "");

    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView number;
        TextView latitude;
        TextView longtitude;

        public CustomViewHolder(View view) {
            super(view);

            this.name = view.findViewById(R.id.score_name);
            this.number = view.findViewById(R.id.score_number);
            this.latitude = view.findViewById(R.id.score_latitude);
            this.longtitude = view.findViewById(R.id.score_longtitude);

        }

        public TextView getName() {
            return name;
        }

        public TextView getNumber() {
            return number;
        }

        public TextView getLatitude() {
            return latitude;
        }

        public TextView getLongtitude() {
            return longtitude;
        }
    }
}
