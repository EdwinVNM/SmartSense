package com.example.dashobard;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.dashobard.ui.main.SectionsPagerAdapter;
import com.example.dashobard.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MyViewModel model;
    private Handler generator;

    private Runnable generatorRunnable = new Runnable() {
        @Override
        public void run() {
            model.gauge1.setValue(Math.round((Math.random() * 100) * 100.0) / 100.0);
            model.gauge2.setValue(Math.round((Math.random() * 100) * 100.0) / 100.0);

            Double[] data = new Double[]{
                    Math.random() * 150,
                    Math.random() * 150,
                    Math.random() * 150
            };
            model.chart1.setValue(data);

            generator.postDelayed(this,2000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(MyViewModel.class);
        generator = new Handler();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        generator.postDelayed(generatorRunnable,2000);
    }
}