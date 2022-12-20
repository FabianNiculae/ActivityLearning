package com.example.activitylearning;

import static androidx.core.content.ContextCompat.getSystemService;



import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils;


public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    private SensorManager mSensorManager;
    private Sensor mLinearAccSensor;
    private Sensor mGyroSensor;

    ConverterUtils.DataSource source;
    Instances data;
    Classifier mClassifier = null;

    double TotalAccelerate;
    ArrayList<Double> listPeaks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.fView);
        tabLayout = findViewById(R.id.tabs);

        viewPagerAdapter = new ViewPagerAdapter(
                getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        // It is used to join TabLayout with ViewPager.
        tabLayout.setupWithViewPager(viewPager);

        mSensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        mLinearAccSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mGyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        mSensorManager.registerListener(listener, mLinearAccSensor, 20000);
        mSensorManager.registerListener(listener, mGyroSensor, 20000);

        try {
            Log.d("debugTST", "testtrymodelbefore");
            source = new ConverterUtils.DataSource(getAssets().open("activity.arff"));
            Log.d("debugTST", "testtrymodelafter");
            Instances trainDataSet = source.getDataSet();
            Log.d("debugTST", "testtryinstances atetrre");
            trainDataSet.setClassIndex(trainDataSet.numAttributes() - 1);
            Log.d("debugTST", "testtryindex after");
            int numClasses = trainDataSet.numClasses();
            Log.d("debugTST", "NumCalsse: " + String.valueOf(numClasses));
            for (int i = 0; i < numClasses; i++){
                String classValue = trainDataSet.classAttribute().value(i);
                Log.d("tstCls", "Clas value "+i+" is " + classValue);
            }
        } catch (Exception e) {
            Log.d("debugTST", "exception");
            e.printStackTrace();
        }

        /*
        AssetManager assetManager = getAssets();
        try {
            mClassifier = (Classifier) SerializationHelper.read(assetManager.open("rightpocket.model"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(mClassifier==null){
            Toast.makeText(this, "Model not loaded!", Toast.LENGTH_LONG).show();
            return;
        }
        else{
            Log.d("modelTST", "Classifier not null. Starting feature extraction");
            Toast.makeText(this, "Model loaded.", Toast.LENGTH_LONG).show();
            final Attribute attributeAX = new Attribute("Right_pocket_Ax");
            final Attribute attributeAY = new Attribute("Right_pocket_Ay");
            final Attribute attributeAZ = new Attribute("Right_pocket_Az");
            final Attribute attributeGX = new Attribute("Right_pocket_Gx");
            final Attribute attributeGY = new Attribute("Right_pocket_Gy");
            final Attribute attributeGZ = new Attribute("Right_pocket_Gz");
            final Attribute attributeAvgAX = new Attribute("AVG_AX");
            final Attribute attributeAvgAY = new Attribute("AVG_AY");
            final Attribute attributeAvgAZ = new Attribute("AVG_AZ");
            final Attribute attributeAvgGX = new Attribute("AVG_GX");
            final Attribute attributeAvgGY = new Attribute("AVG_GY");
            final Attribute attributeAvgGZ = new Attribute("AVG_GZ");
            final Attribute attributeStdAX = new Attribute("STD_AX");
            final Attribute attributeStdAY = new Attribute("STD_AY");
            final Attribute attributeStdAZ = new Attribute("STD_AZ");
            final Attribute attributeActivity = new Attribute("Activity");
            final List<String> activities = new ArrayList<String>() {
                {
                    add("walking");
                    add("standing");
                    add("jogging");
                    add("sitting");
                    add("biking");
                    add("upstairs");
                    add("downstairs");
                }
            };

            ArrayList<Attribute> attributeList = new ArrayList<Attribute>(2) {
                {
                    add(attributeAX);
                    add(attributeAY);
                    add(attributeAZ);
                    add(attributeGX);
                    add(attributeGY);
                    add(attributeGZ);
                    add(attributeAvgAX);
                    add(attributeAvgAY);
                    add(attributeAvgAZ);
                    add(attributeAvgGX);
                    add(attributeAvgGY);
                    add(attributeAvgGZ);
                    add(attributeStdAX);
                    add(attributeStdAY);
                    add(attributeStdAZ);
                    add(attributeActivity);
                    Attribute attributeActivity = new Attribute("@@activity@@", activities);
                    add(attributeActivity);
                }
            };

            //Instances dataUnpredicted = new Instances("TestInstances", attributeList, 1);
            // last feature is target variable
            //dataUnpredicted.setClassIndex(dataUnpredicted.numAttributes() - 1);

            final Activity s = mActivities[mRandom.nextInt(mSamples.length)];
            DenseInstance newInstance = new DenseInstance(dataUnpredicted.numAttributes()) {
                {
                    setValue(attributeSepalLength, s.features[0]);
                    setValue(attributeSepalWidth, s.features[1]);
                    setValue(attributePetalLength, s.features[2]);
                    setValue(attributePetalWidth, s.features[3]);
                }
            };

        }*/
    }


    public String getTimestamp() {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        return ts;
    }

    private SensorEventListener listener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() != Sensor.TYPE_LINEAR_ACCELERATION) {
                float ax = event.values[0];
                float ay = event.values[1];
                float az = event.values[2];

                /*TotalAccelerate = Math.round(Math.sqrt(Math.pow(ax, 2)
                        + Math.pow(ay, 2)
                        + Math.pow(az, 2)));
                Log.i("tstACC", "Accelerometer = " + TotalAccelerate);
*/
                //listPeaks.add(TotalAccelerate);


                Log.i("tstACC", "list values " + listPeaks);
                //Find moving average - window size= 4
                //MovingAverage ma = new MovingAverage(5);
                //ma.newNum(TotalAccelerate);
                //float valueAfterMovingAverage = (float) ma.getAvg();

                Log.d("tstACC", "=ACC= X: " + String.valueOf(ax) + "Y: " +String.valueOf(ay) +"Z: " + String.valueOf(ax) + "\n MA: " + "valueAfterMovingAverage");
            } else if (event.sensor.getType() != Sensor.TYPE_GYROSCOPE) {
                float gx = event.values[0];
                float gy = event.values[1];
                float gz = event.values[2];

                Log.d("tstGYR", "=GYRO= X: " + String.valueOf(gx) + "Y: " +String.valueOf(gy) +"Z: " + String.valueOf(gx));
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
    public Queue<Double> window = new LinkedList<Double>();
    public int p;
    public double sum;

    ///Moving Average class.
    public class MovingAverage {
        public MovingAverage(int period) {
            assert period > 0 : "Period must be a positive integer";
            p = period;
        }

        public void newNum(double num) {
            sum += num;
            window.add(num);
            if (window.size() > p) {
                sum -= window.remove();
            }
        }

        public double getAvg() {
            if (window.isEmpty()) return 0; // technically the average is undefined
            return sum / window.size();
        }

    }

    public class Activity {
        public int label;
        public double [] features;

        public Activity(int _label, double[] _features) {
            this.label = _label;
            this.features = _features;
        }

        @Override
        public String toString() {
            return "cls " + label +
                    ", feat: " + Arrays.toString(features);
        }
    }

}