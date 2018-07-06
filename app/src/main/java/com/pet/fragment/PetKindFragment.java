package com.pet.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pet.R;
import com.pet.adapter.PetKindAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dragon on 2018/7/4.
 * 宠物种类的fragment
 */

@SuppressLint("ValidFragment")
public class PetKindFragment extends Fragment {

    @BindView(R.id.lv)
    ListView listView;


    String[] pets;

    @SuppressLint("ValidFragment")
    public PetKindFragment(String[] pets) {
        this.pets = pets;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_kind, null);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        PetKindAdapter adapter = new PetKindAdapter(pets,getActivity());
        listView.setAdapter(adapter);
    }
}
