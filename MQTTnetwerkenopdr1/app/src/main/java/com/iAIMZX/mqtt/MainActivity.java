package com.iAIMZX.mqtt;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class MainActivity extends AppCompatActivity {


    String broker = "";
    public static MqttAndroidClient getclient;


    //constructor
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //functie voor de knoppie om een IP te zetten waarmee het apparaat kan connecten
    public void setIP(View view){
        EditText iptext = (EditText)findViewById(R.id.editText);
        broker =  "tcp://" + iptext.getText().toString();

        Context context = getApplicationContext();
        CharSequence text = "Set IP to: " + iptext.getText().toString() + " with port: 1883";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    //functie voor de connect knop om met de lokale broker te connecten
    public void connectTobroker(View view) {
            try {
                String clientId = MqttClient.generateClientId();
                final MqttAndroidClient client = new MqttAndroidClient(this.getApplicationContext(), broker,clientId,new MemoryPersistence());
                IMqttToken token = client.connect();
                token.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        // We are connected
                        Context context = getApplicationContext();
                        CharSequence text = "Connecting was a success!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        getclient = client;

                        runwhenconnected();


                    }
                    //als er geen verbinding kan worden gemaakt:
                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        // Something went wrong e.g. connection timeout or firewall problems

                        Context context = getApplicationContext();
                        CharSequence text = "Error connecting to broker";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();


                    }
                });


            } catch (MqttException e) {
                e.printStackTrace();

                Context context = getApplicationContext();
                CharSequence text = "Error connecting to broker";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

    }

    //aparte functie die aangeroepen wordt als de verbinding met de broker is gelukt.
    public void runwhenconnected(){
        Intent intent = new Intent(this, topicActivity.class);
        startActivity(intent);
    }
}
