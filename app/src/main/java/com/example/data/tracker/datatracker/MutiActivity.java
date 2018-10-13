package com.example.data.tracker.datatracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.data.tracker.datatracker.adapter.HorizontalListAdapter;
import com.example.data.tracker.datatracker.adapter.ListViewAdapter;
import com.example.data.tracker.datatracker.adapter.ViewPagerFragmentAdapter;
import com.example.data.tracker.datatracker.fragment.ViewPagerFragment1;
import com.example.data.tracker.datatracker.fragment.ViewPagerFragment2;
import com.example.data.tracker.datatracker.fragment.ViewPagerFragment3;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MutiActivity extends FragmentActivity {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.search_result)
    ImageView searchResult;
    @BindView(R.id.search_page)
    FrameLayout searchPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muti);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        //ViewPager
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ViewPagerFragment1());
        fragments.add(new ViewPagerFragment2());
        fragments.add(new ViewPagerFragment3());
        ViewPagerFragmentAdapter viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(viewPagerFragmentAdapter);

        //recyclerView
        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dataList.add("item" + i);
        }
        HorizontalListAdapter horizontalListAdapter = new HorizontalListAdapter(dataList);
        recyclerView.setAdapter(horizontalListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //listView
        List<String> listViewData = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            listViewData.add("listView item" + i);
        }
        listView.setAdapter(new ListViewAdapter(listViewData));

        //search Button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPage.setVisibility(searchPage.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });
    }
}
