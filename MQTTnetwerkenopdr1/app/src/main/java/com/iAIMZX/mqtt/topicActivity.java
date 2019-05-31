package com.iAIMZX.mqtt;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;


import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;




public class topicActivity extends AppCompatActivity {

    //constructor
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        TextView textView = (TextView) findViewById(R.id.text_view);
        textView.setMovementMethod(new ScrollingMovementMethod());


        try {
            MainActivity.getclient.subscribe("#",0);
            if (MainActivity.getclient.isConnected()) {
                MainActivity.getclient.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        Context context = getApplicationContext();

                        CharSequence text = "Connection timed out...";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }

                    //als er een bericht succesvol is ontvangen, print dit in een Textveld onder messages.
                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        StringBuilder log = new StringBuilder();
                        String line = "topic :" + topic + " message: " + message ;
                        log.append(line + "\n");

                        TextView tv = (TextView) findViewById(R.id.text_view);
                        tv.append(log);

                    }
                    //als het bericht succesvol is aangekomen
                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {

                    }
                });


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // deze functie uitvoeren als er op de knop publish wordt gedrukt.
    public void buttonpublish(View view) {
        EditText publishtext = (EditText) findViewById(R.id.editText2);
        EditText topictext = (EditText) findViewById(R.id.editText4);
        String topic = topictext.getText().toString();
        String payload = publishtext.getText().toString();
        byte[] encodedPayload;
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setRetained(true);
            MainActivity.getclient.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();

        }
    }


    @Override
    public void onBackPressed() {
        try{
            MainActivity.getclient.close();
            topicActivity.super.onBackPressed();


        }
        catch(Exception e){
            e.printStackTrace();
            Context context = getApplicationContext();
            CharSequence text = "Something went wrong";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            topicActivity.super.onBackPressed();
        }
    }
}







