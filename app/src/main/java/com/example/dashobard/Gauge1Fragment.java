package com.example.dashobard;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;

import java.util.Random;

public class Gauge1Fragment extends Fragment {
    private Random random = new Random();
    private MqttModel model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gauge1, container, false);

        HalfGauge gauge = view.findViewById(R.id.halfGauge);
        Button btn = view.findViewById(R.id.random_btn);

        Range range = new Range();
        range.setColor(Color.parseColor("#ce0000"));
        range.setFrom(80.0);
        range.setTo(100.0);

        Range range4 = new Range();
        range4.setColor(Color.parseColor("#FFA500"));
        range4.setFrom(60.0);
        range4.setTo(80.0);

        Range range2 = new Range();
        range2.setColor(Color.parseColor("#00b20b"));
        range2.setFrom(40.0);
        range2.setTo(60.0);

        Range range5 = new Range();
        range5.setColor(Color.parseColor("#FFA500"));
        range5.setFrom(20.0);
        range5.setTo(40.0);

        Range range3 = new Range();
        range3.setColor(Color.parseColor("#ce0000"));
        range3.setFrom(0.0);
        range3.setTo(20.0);

        //add color ranges to gauge
        gauge.addRange(range);
        gauge.addRange(range2);
        gauge.addRange(range3);
        gauge.addRange(range4);
        gauge.addRange(range5);

        //set min max and current value
        gauge.setMinValue(0.0);
        gauge.setMaxValue(100.0);
        gauge.setValue(0.0);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // randomly generate number in range 0 - 150
                double value = random.nextInt(100) + 1;
                // set gauge value
                gauge.setValue(value);
                //gauge.setValue(Double.parseDouble(value+"%"));
            }
        });

        model = new ViewModelProvider(requireActivity()).get(MqttModel.class);
        model.ambientLight.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                gauge.setValue(aDouble);
                //gauge.setValue(Double.parseDouble(aDouble+"%"));
            }
        });

        return view;
    }







}