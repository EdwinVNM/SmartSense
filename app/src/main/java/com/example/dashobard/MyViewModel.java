package com.example.dashobard;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {
    public MutableLiveData<Double> gauge1 = new MutableLiveData<>();
    public MutableLiveData<Double> gauge2 = new MutableLiveData<>();
    public MutableLiveData<Double[]> chart1 = new MutableLiveData<>();
}
