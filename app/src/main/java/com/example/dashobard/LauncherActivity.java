package com.example.dashobard;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.dashobard.databinding.ActivityMainBinding;
import com.example.dashobard.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.StandardCharsets;

public class LauncherActivity extends AppCompatActivity implements MqttMessageSend {
    private static final String TAG = "MQTT";
    private static final String TOPIC_T1 = "dht/temperature";
    private static final String TOPIC_T2 = "dht/humidity";
    private static final String TOPIC_T3 = "inTopic";
    private static final String TOPIC_AL = "dht/humidity";
    private static final String TOPIC_NL = "dht/temperature";

    private ActivityMainBinding binding;
    private MqttModel model;

    private MqttAsyncClient mqttClient;
    private String broker = "tcp://<IPADDRESS>:1883";
    private String clientID;
    private MemoryPersistence persistence = new MemoryPersistence();

    private MqttCallback mqttCallback = new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {}
        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {}

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            String payload = new String(message.getPayload(), StandardCharsets.UTF_8);

            Log.d(TAG, "messageArrived: T: "+topic + ": "+payload);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(topic.equals(TOPIC_AL)){
                        model.ambientLight.setValue(Double.parseDouble(payload));
                    }
                    else if(topic.equals(TOPIC_NL)){
                        model.noise.setValue(Double.parseDouble(payload));
                    }
                    else if(topic.equals(TOPIC_T1)){
                        model.temperature1.setValue(Double.parseDouble(payload));
                    }
                    else if(topic.equals(TOPIC_T2)){
                        model.temperature2.setValue(Double.parseDouble(payload));
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(MqttModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        clientID = MqttClient.generateClientId();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mqttClient = new MqttAsyncClient(broker,clientID,persistence);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setAutomaticReconnect(true);

            mqttClient.connect(options, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "onSuccess: Connected");
                    try {
                        mqttClient.subscribe(TOPIC_T1,0);
                        mqttClient.subscribe(TOPIC_T2,0);
                        mqttClient.subscribe(TOPIC_T3,0);
                        mqttClient.subscribe(TOPIC_AL,0);
                        mqttClient.subscribe(TOPIC_NL,0);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                    mqttClient.setCallback(mqttCallback);

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, "onFailure: "+exception.getMessage(), exception);
                    exception.printStackTrace();
                }
            });


        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(String messString) {

        try {
            mqttClient.publish(TOPIC_T3, new MqttMessage((messString.getBytes(StandardCharsets.UTF_8))));
        } catch (MqttException e) {
            Log.e(TAG, "run: "+e.getMessage(), e);
        }
    }


}
