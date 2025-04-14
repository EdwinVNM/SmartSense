package com.example.dashobard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class MessageFragment extends androidx.fragment.app.Fragment {
    private Random random = new Random();
    private MqttModel model;
    private static final String TAG = "MQTT";
    private EditText messageET;
    private TextView messageTV;
    //    private HalfGauge gauge;
//    private Gauge gauge;
    private MqttAsyncClient mqttClient;
    private String broker = "tcp://192.168.0.101:1883";//Connected_Devices_AP //Original 3
    private String clientID;
    private MemoryPersistence persistence = new MemoryPersistence();

    MqttMessageSend messageIh;
    private static final String TOPIC_T3 = "inTopic"; //demo/soil_moisture
    //    private static final String TOPIC1 = "demo/soil_moisture";
    //private static final String TOPIC = "dht/temperature";
    private MqttCallbackExtended mqttCallback = new MqttCallbackExtended() {
        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            Log.d(TAG, "connectComplete: ");
        }

        @Override
        public void connectionLost(Throwable cause) {
            Log.e(TAG, "connectionLost: "+cause.getMessage(), cause);
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageTV.setText(payload);
                }
            });


        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    };

    private void runOnUiThread(Runnable runnable) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment, container, false);
//        model = new ViewModelProvider(requireActivity()).get(MqttModel.class);
        //HalfGauge gauge = view.findViewById(R.id.messageET);
        Button btn = view.findViewById(R.id.send_btn);
        //clientID = MqttClient.generateClientId();
        messageET = view.findViewById(R.id.messageET);
        messageTV = view.findViewById(R.id.messageTV);

        messageIh = (LauncherActivity) requireActivity();

//        model.mqttClient.observe(getViewLifecycleOwner(), new Observer<MqttAsyncClient>() {
//            @Override
//            public void onChanged(MqttAsyncClient mqttAsyncClient) {
//                mqttClient= mqttAsyncClient;
//            }
//        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // randomly generate number in range 0 - 150
                //double value = random.nextInt(50) + 1;
                // set gauge value
                //btn.setValue(value);
                //gauge.setValue(Double.parseDouble(value+"`C"));
                sendMessage(view);
            }
        });
//        Range range3 = new Range();
//        range3.setColor(Color.parseColor("#00b20b"));
//        range3.setFrom(65.0);
//        range3.setTo(70.0);

        //add color ranges to gauge
        //gauge.addRange(range);

        //set min max and current value
//        gauge.setMinValue(0.0);
//        gauge.setMaxValue(50.0);
//        gauge.setValue(0.0);

//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // randomly generate number in range 0 - 150
//                double value = random.nextInt(50) + 1;
//                // set gauge value
//                gauge.setValue(value);
//                //gauge.setValue(Double.parseDouble(value+"`C"));
//            }
//        });

//        model = new ViewModelProvider(requireActivity()).get(MqttModel.class);
//        model.temperature3.observe(getViewLifecycleOwner(), new Observer<Double>() {
//            @Override
//            public void onChanged(Double aDouble) {
//                //gauge.setValue(Double.parseDouble(aDouble+"`C"));
//                gauge.setValue(aDouble);
//            }
//        });
//
        return view;
    }


//        @Override
//        protected void onResume () {
//            super.onResume();
//            try {
////            mqttClient.disconnect();
//                mqttClient = new MqttAsyncClient(broker, clientID, persistence);
//                MqttConnectOptions options = new MqttConnectOptions();
//                options.setCleanSession(true);
//                options.setAutomaticReconnect(true);
//                mqttClient.connect(options, new IMqttActionListener() {
//                    @Override
//                    public void onSuccess(IMqttToken asyncActionToken) {
//                        try {
//                            Log.d(TAG, "onSuccess: connected");
//                            mqttClient.subscribe(TOPIC_T3, 0);
//                        } catch (MqttException e) {
//                            throw new RuntimeException(e);
//                        }
//                        mqttClient.setCallback(mqttCallback);
//                        //Toast.makeText(getApplicationContext(), "Connected...", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                        Log.e(TAG, "onFailure: " + exception.getMessage(), exception);
//                        //Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
//                        exception.printStackTrace();
//                    }
//                });
//            } catch (MqttException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        @Override
//        protected void onPause () {
//            super.onPause();
//            try {
//                mqttClient.disconnect();
//            } catch (MqttException e) {
//                throw new RuntimeException(e);
//            }
//        }
        public void sendMessage (View view){
            String payload = messageET.getText().toString();

            messageIh.sendMessage(payload);

//            MqttMessage message = new MqttMessage(payload.getBytes(StandardCharsets.UTF_8));
//            message.setQos(0);
//            try {
//                mqttClient.publish(TOPIC_T3, message);
//            } catch (MqttException e) {
//                Log.e(TAG, "run: " + e.getMessage(), e);
//            }
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    //mqttClient.publish(TOPIC, new MqttMessage(("Hello there".getBytes((StandardCharsets.UTF_8)))));
//
//                    try {
//                        mqttClient.publish(TOPIC_T3, message);
//                    } catch (MqttException e) {
//                        Log.e(TAG, "run: " + e.getMessage(), e);
//                    }
//                }
//            });

        }



}