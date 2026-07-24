package com.example.testjjsdk;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

	private ImageButton imgBtnSensor;
	private ImageButton imgBtnCamera;
	private ImageButton imgBtnDc;
	private ImageButton imgBtnAf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initUIComponent();
		checkPermission();
	}

	private void initUIComponent() {
		imgBtnSensor = findViewById(R.id.btn_sensor);
		imgBtnCamera = findViewById(R.id.btn_camera);
		imgBtnDc = findViewById(R.id.btn_device_control);
		imgBtnAf = findViewById(R.id.btn_af_activity);

		imgBtnSensor.setOnClickListener(v -> startActivity(new Intent(this,
				SensorActivity.class)));
		imgBtnCamera.setOnClickListener(v -> startActivity(new Intent(this,
				CameraActivity.class)));
		imgBtnDc.setOnClickListener(
				v -> startActivity(new Intent(this, DeviceControlActivity.class)));
		imgBtnAf.setOnClickListener(v -> startActivity(new Intent(this,
				AllFeatureActivity.class)));
	}

	private void checkPermission() {
		// Permission you need in this project.
		String[] permissions = new String[] {CAMERA, WRITE_EXTERNAL_STORAGE, RECORD_AUDIO};

		boolean check = true;
		for (String permission : permissions) {
			int result = ActivityCompat.checkSelfPermission(this, permission);
			if (result != PackageManager.PERMISSION_GRANTED) {
				check = false;
				break;
			}
		}

		if (!check) {
			ActivityCompat.requestPermissions(this, permissions, 0);
		}
	}
}
