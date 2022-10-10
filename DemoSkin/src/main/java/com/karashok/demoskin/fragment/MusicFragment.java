package com.karashok.demoskin.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karashok.demoskin.R;
import com.karashok.demoskin.adapter.MusicContentAdapter;

import java.util.ArrayList;
import java.util.List;

public class MusicFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = view.findViewById(R.id.music_rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        MusicContentAdapter adapter = new MusicContentAdapter(getItemList());
        rv.setAdapter(adapter);
    }

    private List<String> getItemList() {
        List<String> itemList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            itemList.add(String.valueOf(i));
        }
        return itemList;
    }
}