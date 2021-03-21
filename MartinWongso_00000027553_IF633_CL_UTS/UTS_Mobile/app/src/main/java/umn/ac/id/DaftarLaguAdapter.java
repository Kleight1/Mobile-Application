package umn.ac.id;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DaftarLaguAdapter extends RecyclerView.Adapter<DaftarLaguAdapter.AudioViewHolder>{
    private ArrayList<File> audioDataSet;
    private LayoutInflater mInflater;
    Context mContext;

    public DaftarLaguAdapter(Context context, ArrayList<File> audioModelList){

        mInflater = LayoutInflater.from(context);
        mContext = context;
        audioDataSet = audioModelList;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = mInflater.inflate(R.layout.audio_item_layout, viewGroup, false);

        AudioViewHolder vh = new AudioViewHolder(mContext,audioDataSet,v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder audioViewHolder, int i) {
        audioViewHolder.mTextView.setText(audioDataSet.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return audioDataSet.size();
    }

    public static class AudioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        Context nContext;
        ArrayList<File> audioList;
        public TextView mTextView;

        public AudioViewHolder(Context context, ArrayList<File> audioModelList, View v) {
            super(v);
            nContext = context;
            audioList = audioModelList;
            mTextView = v.findViewById(R.id.audioName);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){

            int itemPosition = getAdapterPosition();

            //Intent intent = new Intent(nContext,MusicDetailActivity.class);
            //intent.putExtra("audio",audioList.get(itemPosition));


            nContext.startActivity(new Intent(nContext,MusicDetailActivity.class)
                    .putExtra("pos",itemPosition).putExtra("songs",audioList).putExtra("songname",audioList.get(itemPosition).getName().toString().replace(".mp3","").replace(".wav","")));

        }
    }
}
