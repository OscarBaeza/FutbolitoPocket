package com.oscar.futbolitoPocket;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private TextView txt1, txt2;
    int goles1 = 0;
    int goles2 = 0;
    private final float[] gravity = new float[4];
    private float xPos, xAcceleration, xVelocity = 0.0f;
    private float yPos, yAcceleration, yVelocity = 0.0f;
    private float screenWidth, screenHeight;
    private SensorManager sensorManager;
    private Sensor sensor;
    private ImageView ball;


    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_FASTEST);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ball = (ImageView) findViewById(R.id.ball);
        txt1 = findViewById(R.id.txtGoles1);
        txt2 = findViewById(R.id.txtGoles2);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = (float) displayMetrics.widthPixels;
        screenHeight = (float) displayMetrics.heightPixels;

        centro();
    }

    private void update(float xOrientation, float yOrientation) {
        xAcceleration = xOrientation;
        yAcceleration = yOrientation;
        updateX();
        updateY();

        int dist=150;

        //izquierda
        if(xPos<0){
            xPos=0;
            xVelocity = -xVelocity/4;
        }
        //derecha
        if(xPos>screenWidth-dist){
            xPos = screenWidth-dist;
            xVelocity = -xVelocity/4;
        }
        //arriba
        if(yPos<0){
            yPos=0;
            yVelocity = -yVelocity/4;
            goles2++;
            goles();
            centro();
        }
        //abajo
        if(yPos>screenHeight-dist){
            yPos = screenHeight-dist;
            yVelocity = -yVelocity/4;
            goles1++;
            goles();
            centro();
        }

        ball.setX(xPos);
        ball.setY(yPos);
    }

    private void centro() {
        xVelocity = 0;
        xAcceleration = 0;
        yVelocity = 0;
        yAcceleration = 0;
        xPos=screenWidth/2;
        yPos=screenHeight/2;
        ball.setX(xPos);
        ball.setY(yPos);
    }

    private void goles() {
        txt1.setText(""+goles1);
        txt2.setText(""+goles2);
    }

    void updateX() {
        xVelocity -= xAcceleration * 0.3f;
        xPos += xVelocity;
    }

    void updateY() {
        yVelocity -= yAcceleration * 0.3f;
        yPos += yVelocity;
    }

    @Override
    public void onSensorChanged(SensorEvent movimiento) {

        final float alpha = (float) 0.9;

        gravity[0] = alpha * gravity[0] + (1 - alpha) * movimiento.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * (movimiento.values[1]);
        gravity[2] = alpha * gravity[2] + (1 - alpha) * movimiento.values[2];

        float x = movimiento.values[0] - gravity[0];
        float y = movimiento.values[1] - gravity[1];
        float z = movimiento.values[2] - gravity[2];


        update(x,(y*-1));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}