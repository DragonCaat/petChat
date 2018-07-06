package com.pet.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pet.R;
import com.pet.adapter.ViewPagerAdapter;
import com.pet.fragment.PetKindFragment;
import com.pet.view.NavitationLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 品种选择的界面
 **/
public class PetSelectActivity extends AppCompatActivity {
    private NavitationLayout navitationLayout;
    private ViewPager viewPager;
    private String[] titles = {"萌猫", "萌狗", "萌兔", "鼠类", "鸟类", "其他"};
    private ViewPagerAdapter viewPagerAdapter;
    private List<Fragment> fragments;

    private PetKindFragment dogFragment;
    private PetKindFragment catFragment;
    private PetKindFragment rabbitFragment;
    private PetKindFragment mouseFragment;
    private PetKindFragment birdFragment;
    private PetKindFragment otherFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_select);
        ButterKnife.bind(this);
        initFragment();
        initShowNaviationBar();
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    //初始化fragment
    private void initFragment() {
        String[] cats = getResources().getStringArray(R.array.cat);
        String[] dogs = getResources().getStringArray(R.array.dog);
        String[] rabbits = getResources().getStringArray(R.array.rabbit);
        String[] mouses = getResources().getStringArray(R.array.mouse);
        String[] birds = getResources().getStringArray(R.array.bird);
        String[] others = getResources().getStringArray(R.array.other);
        catFragment = new PetKindFragment(cats);
        dogFragment = new PetKindFragment(dogs);
        rabbitFragment = new PetKindFragment(rabbits);
        mouseFragment = new PetKindFragment(mouses);
        birdFragment = new PetKindFragment(birds);
        otherFragment = new PetKindFragment(others);
    }

    /**
     * 展示搜索标题栏
     */
    private void initShowNaviationBar() {
        navitationLayout = findViewById(R.id.bar1);
        viewPager = findViewById(R.id.viewpager1);

        fragments = new ArrayList<>();
        fragments.add(catFragment);
        fragments.add(dogFragment);
        fragments.add(rabbitFragment);
        fragments.add(mouseFragment);
        fragments.add(birdFragment);
        fragments.add(otherFragment);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);

        viewPager.setAdapter(viewPagerAdapter);

        navitationLayout.setViewPager(this, titles, viewPager, R.color.black, R.color.white, 14, 16, 0, 0, true);
        navitationLayout.setBgLine(this, 1, R.color.white);
        navitationLayout.setNavLine(this, 3, R.color.white, 0);

    }

}
