package com.example.dashobard;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;

public class MqttModel extends ViewModel {
    public MutableLiveData<Double> ambientLight = new MutableLiveData<>();
    public MutableLiveData<Double> noise = new MutableLiveData<>();
    public MutableLiveData<Double> temperature1 = new MutableLiveData<>();
    public MutableLiveData<Double> temperature2 = new MutableLiveData<>();
    public MutableLiveData<Double> temperature3 = new MutableLiveData<>();
//    public MutableLiveData<MqttAsyncClient> mqttClient = new MutableLiveData<>();
}
