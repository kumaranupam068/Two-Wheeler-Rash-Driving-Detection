package com.example.rashapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Collect_Data extends AppCompatActivity implements SensorEventListener {
    public static String ops[] ={"Select","Weaving","Lanechanging","Sudden Braking"};
    Spinner sp;
    Button b,b1,b2;
    Sensor accelerometer;
    SensorManager sm;
    private SensorManager sensorManager;
    // private Sensor accelerometer;

    private float deltaXMax = 0;
    private float deltaYMax = 0;
    private float deltaZMax = 0;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;
    boolean flag =false;
    private float vibrateThreshold = 0;

    public Vibrator v;
    private float lastX, lastY, lastZ;
    String data="",type= "";
    Vibrator vibrator;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect__data);
        sp = (Spinner) findViewById(R.id.sp);
        b = (Button) findViewById(R.id.button);
        b1 = (Button) findViewById(R.id.button1);
        b2=(Button)findViewById(R.id.btnTV);
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ops);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapterCategory);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer

            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            vibrateThreshold = accelerometer.getMaximumRange() / 2;
        } else {
            // fai! we dont have an accelerometer!
        }
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        //initialize vibration
        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);



        ((Button) findViewById(R.id.button)).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    // start the thread
                    Log.d(">>","Touched");
                    type=sp.getSelectedItem().toString();
                    flag =true;
                    return true;
                } else if(event.getAction() == MotionEvent.ACTION_UP){
                    // stop the thread
                    Log.d(">>","Released");
                    flag =false;
                    new update_loc().execute();
                    return true;
                }
                return false;

            }
        });
        ((Button) findViewById(R.id.button1)).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    // start the thread
                    Log.d(">>","Touched");
                    b2.setVisibility(View.INVISIBLE);
                    flag =true;
                    return true;
                } else if(event.getAction() == MotionEvent.ACTION_UP){
                    // stop the thread
                    Log.d(">>","Released");
                    flag =false;
                    new update_loc1().execute();
                    return true;
                }
                return false;

            }
        });
    }

    private class update_loc extends AsyncTask<Void, String, String>
    {
        @Override
        public String doInBackground(Void... Void)
        {
            JSONObject jsn = new JSONObject();
            String response = "";
            try {
                URL url = new URL(Global.url +"train_data");
                jsn.put("type", type);
                jsn.put("data", data);



                response = HttpClientConnection.getData(url,jsn);
                Log.d("Response",""+response);
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            return response;
        }
        @Override
        protected void onPostExecute(String s) {


            if(s.endsWith("null"))
            {

                s=s.substring(0,s.length()-4);
            }
            Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
            data="";

        }
    }
    private class update_loc1 extends AsyncTask<Void, String, String>
    {
        @Override
        public String doInBackground(Void... Void)
        {
            JSONObject jsn = new JSONObject();
            String response = "";
            try {
                URL url = new URL(Global.url +"predict_data");

                jsn.put("data", data);



                response = HttpClientConnection.getData(url,jsn);
                Log.d("Response",""+response);
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            return response;
        }
        @Override
        protected void onPostExecute(String s) {


            if(s.endsWith("null"))
            {

                s=s.substring(0,s.length()-4);
            }


            if(!s.equalsIgnoreCase(""))
            {
                b2.setVisibility(View.VISIBLE);
                b2.setText(s);
                Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(VibrationEffect.createOneShot(4000, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(4000);
                }
                mediaPlayer = MediaPlayer.create(Collect_Data.this, R.raw.m);
                mediaPlayer.start();
               // data="";
            }


        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {




        //This is done to prevent multiple values above threshold being registered as multiple potholes
        long actualTime = event.timestamp;


        // get the change of the x,y,z values of the accelerometer
        deltaX = Math.abs(lastX - event.values[0]);
        deltaY = Math.abs(lastY - event.values[1]);
        deltaZ = Math.abs(lastZ - event.values[2]);

        // if the change is below 2, it is just plain noise
        if (deltaX < 2)
            deltaX = 0;
        if (deltaY < 2)
            deltaY = 0;
        if ((deltaZ  >vibrateThreshold) || (deltaY > vibrateThreshold) || (deltaZ > vibrateThreshold)) {
            v.vibrate(50);
        }
        if(flag==true) {
            Log.d("moves", deltaX + "#" + deltaY + "#" + deltaZ + "");
            data+=deltaX + "#" + deltaY + "#" + deltaZ + "\n";
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    //onResume() register the accelerometer for listening the events
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    //onPause() unregister the accelerometer for stop listening the events
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

}
