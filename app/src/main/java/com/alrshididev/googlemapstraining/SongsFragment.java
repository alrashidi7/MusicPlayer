package com.alrshididev.googlemapstraining;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import static com.alrshididev.googlemapstraining.MainActivity.musicFilesArrayList;

public class SongsFragment extends Fragment {
    RecyclerView recyclerView;
    MusicAdapter musicAdapter;
    public SongsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_songs, container, false);

        recyclerView = view.findViewById(R.id.songsRecyclervi);
        recyclerView.setHasFixedSize(true);
       if (!(musicFilesArrayList.size() < 1)){
            musicAdapter = new MusicAdapter(getContext(),musicFilesArrayList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
            recyclerView.setAdapter(musicAdapter);
    }

        return view;
    }
}