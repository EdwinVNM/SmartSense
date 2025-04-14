package com.example.dashobard;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Chart1Fragment extends Fragment {
    private LineChart lineChart;
    private MqttModel model;
    private Double[] data = new Double[2];//3
    private boolean t1IsSet = false;
    private boolean t2IsSet = false;
    //private boolean t3IsSet = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart1, container, false);
        lineChart = view.findViewById(R.id.line_chart);

        configureChart();
        configureData();

        model = new ViewModelProvider(requireActivity()).get(MqttModel.class);
        //ambientLight  //temperature1
        model.ambientLight.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                data[0] = aDouble;
                t1IsSet = true;
                updateChart();
            }
        });//noise  //temperature2
        model.noise.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                data[1] = aDouble;
                t2IsSet = true;
                updateChart();
            }
        });
//        model.temperature3.observe(getViewLifecycleOwner(), new Observer<Double>() {
//            @Override
//            public void onChanged(Double aDouble) {
//                data[2] = aDouble;
//                t3IsSet = true;
//                updateChart();
//            }
//        });
        
        return view;
    }

    private void updateChart() {
        if(t1IsSet && t2IsSet){// && t3IsSet
            addEntries(data);
            t1IsSet = false;
            t2IsSet = false;
            //t3IsSet = false;
        }
    }

    private void configureChart() {
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragDecelerationFrictionCoef(0.9f);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setHighlightPerDragEnabled(true);
        lineChart.setPinchZoom(true);

        // set an alternative background color
//        lineChart.setBackgroundColor(Color.LTGRAY);

        lineChart.animateX(1500);

        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(11f);
        l.setTextColor(Color.WHITE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setYOffset(11f);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setAxisMaximum(100f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setTextColor(Color.RED);
        rightAxis.setAxisMaximum(50);
        rightAxis.setAxisMinimum(0);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(false);
    }

    private void configureData() {
        LineDataSet set1, set2;//, set3

        // create a dataset and give it a type
        set1 = new LineDataSet(new ArrayList<Entry>(), "Humidity");

        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
//            set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(2f);
        set1.setCircleRadius(3f);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);
        //set1.setFillFormatter(new MyFillFormatter(0f));
        //set1.setDrawHorizontalHighlightIndicator(false);
        //set1.setVisible(false);
        //set1.setCircleHoleColor(Color.WHITE);

        // create a dataset and give it a type
        set2 = new LineDataSet(new ArrayList<Entry>(), "Temperature");
        set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set2.setColor(Color.RED);
        set2.setCircleColor(Color.WHITE);
        set2.setLineWidth(2f);
        set2.setCircleRadius(3f);
        set2.setFillAlpha(65);
        set2.setFillColor(Color.RED);
        set2.setDrawCircleHole(false);
        set2.setHighLightColor(Color.rgb(244, 117, 117));
        //set2.setFillFormatter(new MyFillFormatter(900f));

//        set3 = new LineDataSet(new ArrayList<Entry>(), "DataSet 3");
//        set3.setAxisDependency(YAxis.AxisDependency.RIGHT);
//        set3.setColor(Color.YELLOW);
//        set3.setCircleColor(Color.WHITE);
//        set3.setLineWidth(2f);
//        set3.setCircleRadius(3f);
//        set3.setFillAlpha(65);
//        set3.setFillColor(ColorTemplate.colorWithAlpha(Color.YELLOW, 200));
//        set3.setDrawCircleHole(false);
//        set3.setHighLightColor(Color.rgb(244, 117, 117));

        // create a data object with the data sets
        LineData data = new LineData(set1, set2);//, set3
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(9f);

        // set data
        lineChart.setData(data);
    }

    private void addEntries(Double[] entry) {
        LineData data = lineChart.getData();

        if (data != null) {
            ILineDataSet s1, s2;//, s3

            // current data sets
            s1 = data.getDataSetByIndex(0);
            s2 = data.getDataSetByIndex(1);
            //s3 = data.getDataSetByIndex(2);

            // randomly generated entries per set, where .getEntryCount() define the position of new new entry
            Entry e1 = new Entry(s1.getEntryCount(), entry[0].floatValue());
            Entry e2 = new Entry(s2.getEntryCount(), entry[1].floatValue());
            //Entry e3 = new Entry(s3.getEntryCount(), entry[2].floatValue());

            // add the new entry to dataset
            data.addEntry(e1, 0);
            data.addEntry(e2, 1);
            //data.addEntry(e3, 2);

            data.notifyDataChanged();
            lineChart.notifyDataSetChanged();

            // optional moving window
            lineChart.setVisibleXRangeMaximum(40);
            lineChart.moveViewToX(data.getEntryCount());
            lineChart.moveViewTo(data.getEntryCount() - 7, 40f, YAxis.AxisDependency.LEFT);
        }
    }
}