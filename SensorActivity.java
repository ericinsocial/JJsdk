package com.example.testjjsdk;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.jorjin.jjsdk.sensor.NativeSensorDataListener;
import com.jorjin.jjsdk.sensor.SensorDataListener;
import com.jorjin.jjsdk.sensor.SensorManager;
import com.jorjin.jjsdk.sensor.SensorStatusListener;

/**
 * This Activity is for SensorManager Example.
 **/

public class SensorActivity extends AppCompatActivity
		implements SensorDataListener, NativeSensorDataListener, SensorStatusListener {

	private static final int COUNT_OF_SENSORS = 7;
	private final int Acc = 0;
	private final int Gyro = 1;
	private final int Rv = 2;
	private final int Compass = 3;
	private final int Light = 4;
	private final int LinearAcc = 5;
	private final int Gravity = 6;
	private Context context = this;
	private TextView[] sensorValue = new TextView[COUNT_OF_SENSORS];
	private TextView[] updateFrequency = new TextView[COUNT_OF_SENSORS];
	private int[] count = new int[COUNT_OF_SENSORS];

	private Handler handler = new Handler();
	private Runnable rateRunnable;

	private SensorManager sensorManager;
	private SensorStatusListener sensorStatusListener = this;
	private SensorDataListener sensorDataListener = this;
	private NativeSensorDataListener nativeSensorDataListener = this;

	SwitchCompat switchAcc;
	SwitchCompat switchGravity;
	SwitchCompat switchGyro;
	SwitchCompat switchLight;
	SwitchCompat switchLinearAcc;
	SwitchCompat switchMag;
	SwitchCompat switchRv;

	private boolean isswitchAcc = false, isswitchGravity = false, isswitchGyro = false, isswitchLight = false,
					isswitchLinearAcc = false, isswitchMag = false, isswitchRv = false;

	private boolean sensorGetDataStart = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor);
		initUIComponent();
	}

	@Override
	protected void onStart() {
		super.onStart();
		sensorManager = new SensorManager(context);
		sensorManager.registerSensorStatusListener(sensorStatusListener);
		sensorManager.addSensorDataListener(sensorDataListener);
		sensorManager.addNativeSensorDataListener(nativeSensorDataListener);
		initTimer();
	}

	@Override
	protected void onStop() {
		super.onStop();
		sensorManager.removeSensorDataListener(sensorDataListener);
		sensorManager.release();
		sensorManager = null;
		//handler.removeCallbacks(rateRunnable);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if(sensorGetDataStart){
			sensorManager.addSensorDataListener(sensorDataListener);
			sensorManager.addNativeSensorDataListener(nativeSensorDataListener);
			startSensor();
		}
		//sensorManager.resume();
	}

	private void initUIComponent() {
		sensorValue[Acc] = findViewById(R.id.text_acc_v);
		sensorValue[Gravity] = findViewById(R.id.text_gravity_v);
		sensorValue[Gyro] = findViewById(R.id.text_gyro_v);
		sensorValue[Light] = findViewById(R.id.text_light_v);
		sensorValue[LinearAcc] = findViewById(R.id.text_linear_acc_v);
		sensorValue[Compass] = findViewById(R.id.text_mag_v);
		sensorValue[Rv] = findViewById(R.id.text_rv_v);

		updateFrequency[Acc] = findViewById(R.id.text_acc_r);
		updateFrequency[Gravity] = findViewById(R.id.text_gravity_r);
		updateFrequency[Gyro] = findViewById(R.id.text_gyro_r);
		updateFrequency[Light] = findViewById(R.id.text_light_r);
		updateFrequency[LinearAcc] = findViewById(R.id.text_linear_acc_r);
		updateFrequency[Compass] = findViewById(R.id.text_mag_r);
		updateFrequency[Rv] = findViewById(R.id.text_rv_r);

		initSwitch();
	}

	private void startSensor(){
		if(isswitchAcc) {
			sensorManager.close(SensorManager.SENSOR_TYPE_ACCELEROMETER_3D);
			sensorManager.open(SensorManager.SENSOR_TYPE_ACCELEROMETER_3D);
		}
		if(isswitchGravity){
			sensorManager.close(SensorManager.SENSOR_TYPE_GRAVITY_VECTOR);
			sensorManager.open(SensorManager.SENSOR_TYPE_GRAVITY_VECTOR);
		}

		if(isswitchGyro){
			sensorManager.close(SensorManager.SENSOR_TYPE_GYROMETER_3D);
			sensorManager.open(SensorManager.SENSOR_TYPE_GYROMETER_3D);
		}

		if(isswitchLight){
			sensorManager.close(SensorManager.SENSOR_TYPE_AMBIENTLIGHT);
			sensorManager.open(SensorManager.SENSOR_TYPE_AMBIENTLIGHT);
		}

		if(isswitchLinearAcc){
			sensorManager.close(SensorManager.SENSOR_TYPE_LINEAR_ACCELEROMETER);
			sensorManager.open(SensorManager.SENSOR_TYPE_LINEAR_ACCELEROMETER);
		}

		if(isswitchMag){
			sensorManager.close(SensorManager.SENSOR_TYPE_COMPASS_3D);
			sensorManager.open(SensorManager.SENSOR_TYPE_COMPASS_3D);
		}

		if(isswitchRv){
			sensorManager.close(SensorManager.SENSOR_TYPE_DEVICE_ORIENTATION);
			sensorManager.open(SensorManager.SENSOR_TYPE_DEVICE_ORIENTATION);
		}
	}

	private void initSwitch() {
		switchAcc = findViewById(R.id.switch_acc);
		switchGravity = findViewById(R.id.switch_gravity);
		switchGyro = findViewById(R.id.switch_gyro);
		switchLight = findViewById(R.id.switch_light);
		switchLinearAcc = findViewById(R.id.switch_linear_acc);
		switchMag = findViewById(R.id.switch_mag);
		switchRv = findViewById(R.id.switch_rv);

		switchAcc.setOnCheckedChangeListener((button, checked) -> {
			if (checked) {
				isswitchAcc = true;
				sensorManager.open(SensorManager.SENSOR_TYPE_ACCELEROMETER_3D);
			} else {
				isswitchAcc = false;
				sensorManager.close(SensorManager.SENSOR_TYPE_ACCELEROMETER_3D);
			}
		});

		switchGravity.setOnCheckedChangeListener((button, checked) -> {
//			state[Gravity] = checked;
			if (checked) {
				isswitchGravity = true;
				sensorManager.open(SensorManager.SENSOR_TYPE_GRAVITY_VECTOR);
			} else {
				isswitchGravity = false;
				sensorManager.close(SensorManager.SENSOR_TYPE_GRAVITY_VECTOR);
			}
		});
		switchGyro.setOnCheckedChangeListener((button, checked) -> {
			if (checked) {
				isswitchGyro = true;
				sensorManager.open(SensorManager.SENSOR_TYPE_GYROMETER_3D);
			} else {
				isswitchGyro = false;
				sensorManager.close(SensorManager.SENSOR_TYPE_GYROMETER_3D);
			}
		});
		switchLight.setOnCheckedChangeListener((button, checked) -> {
			if (checked) {
				isswitchLight = true;
				sensorManager.open(SensorManager.SENSOR_TYPE_AMBIENTLIGHT);
			} else {
				isswitchLight = false;
				sensorManager.close(SensorManager.SENSOR_TYPE_AMBIENTLIGHT);
			}
		});
		switchLinearAcc.setOnCheckedChangeListener((button, checked) -> {
			if (checked) {
				isswitchLinearAcc = true;
				sensorManager.open(SensorManager.SENSOR_TYPE_LINEAR_ACCELEROMETER);
			} else {
				isswitchLinearAcc = false;
				sensorManager.close(SensorManager.SENSOR_TYPE_LINEAR_ACCELEROMETER);
			}
		});
		switchMag.setOnCheckedChangeListener((button, checked) -> {
			if (checked) {
				isswitchMag = true;
				sensorManager.open(SensorManager.SENSOR_TYPE_COMPASS_3D);
			} else {
				isswitchMag = false;
				sensorManager.close(SensorManager.SENSOR_TYPE_COMPASS_3D);
			}
		});
		switchRv.setOnCheckedChangeListener((button, checked) -> {
			if (checked) {
				isswitchRv = true;
				sensorManager.open(SensorManager.SENSOR_TYPE_DEVICE_ORIENTATION);
			} else {
				isswitchRv = false;
				sensorManager.close(SensorManager.SENSOR_TYPE_DEVICE_ORIENTATION);
			}
		});
	}

	private void initTimer() {
		rateRunnable = () -> {
			refreshDataText();
			handler.postDelayed(rateRunnable, 1000);
		};
		handler.postDelayed(rateRunnable, 1000);
	}

	@Override
	public void onSensorDataChanged(int type, float[] values, long timestamp) {
		sensorGetDataStart = true;
		updateDataText(type, values);
	}

	private void updateDataText(int type, float[] data) {
		runOnUiThread(() -> {
			switch (type) {
				case SensorManager.SENSOR_TYPE_ACCELEROMETER_3D:
					sensorValue[Acc].setText(
							getString(R.string.sensor_value3, data[0], data[1], data[2]));
					count[Acc]++;
					break;
				case SensorManager.SENSOR_TYPE_GRAVITY_VECTOR:
					sensorValue[Gravity].setText(
							getString(R.string.sensor_value3, data[0], data[1], data[2]));
					count[Gravity]++;
					break;
				case SensorManager.SENSOR_TYPE_GYROMETER_3D:
					sensorValue[Gyro].setText(
							getString(R.string.sensor_value3, data[0], data[1], data[2]));
					count[Gyro]++;
					break;
				case SensorManager.SENSOR_TYPE_AMBIENTLIGHT:
					sensorValue[Light].setText(getString(R.string.sensor_value1f, data[0]));
					count[Light]++;
					break;
				case SensorManager.SENSOR_TYPE_LINEAR_ACCELEROMETER:
					sensorValue[LinearAcc].setText(
							getString(R.string.sensor_value3, data[0], data[1], data[2]));
					count[LinearAcc]++;
					break;
				case SensorManager.SENSOR_TYPE_COMPASS_3D:
					sensorValue[Compass].setText(
							getString(R.string.sensor_value3, data[0], data[1], data[2]));
					count[Compass]++;
					break;
				case SensorManager.SENSOR_TYPE_DEVICE_ORIENTATION:
					sensorValue[Rv].setText(
							getString(R.string.sensor_value4, data[0], data[1], data[2], data[3]));
					count[Rv]++;
					break;
			}
		});
	}

	private void refreshDataText() {
		runOnUiThread(() -> {
			updateFrequency[Acc].setText(getString(R.string.sensor_value1d, count[Acc]));
			updateFrequency[Gravity].setText(getString(R.string.sensor_value1d, count[Gravity]));
			updateFrequency[Gyro].setText(getString(R.string.sensor_value1d, count[Gyro]));
			updateFrequency[Light].setText(getString(R.string.sensor_value1d, count[Light]));
			updateFrequency[LinearAcc].setText(
					getString(R.string.sensor_value1d, count[LinearAcc]));
			updateFrequency[Compass].setText(getString(R.string.sensor_value1d, count[Compass]));
			updateFrequency[Rv].setText(getString(R.string.sensor_value1d, count[Rv]));

			for (int i = 0; i < COUNT_OF_SENSORS; i++) {
				count[i] = 0;
			}
		});
	}

	@Override
	public void onSensorDataChanged(int i, long l, long l1) {

	}

	@Override
	public void onSensorDetach(boolean isDetach) {

		Log.e("Test","onSensorDetach");
		runOnUiThread(() -> {
			switchAcc.setChecked(false);
			switchGravity.setChecked(false);
			switchGyro.setChecked(false);
			switchLight.setChecked(false);
			switchLinearAcc.setChecked(false);
			switchMag.setChecked(false);
			switchRv.setChecked(false);
		});
	}

}
