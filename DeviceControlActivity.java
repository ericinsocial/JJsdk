package com.example.testjjsdk;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jorjin.jjsdk.display.DisplayManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * This Activity is for AudioManager and DisplayManager Example.
 **/

public class DeviceControlActivity extends AppCompatActivity {
	private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
	private Context context = this;

	private DisplayManager displayManager;

	private int brightness, volume;
	private int displayMode;
	private boolean isMuted = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_devicecontrol);

		initUIComponent();
	}

	@Override
	protected void onStart() {
		super.onStart();
		displayManager = new DisplayManager(context);
		displayManager.open();
		executorService.schedule(this::getDisplaySettings, 100, TimeUnit.MILLISECONDS);

		initUIComponent();
	}

	@Override
	protected void onStop() {
		super.onStop();
		displayManager.close();
		displayManager.release();
		displayManager = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void getDisplaySettings() {
		if (displayManager.getDeviceState()) {
			// update the UI based on the current configurations
			brightness = displayManager.getBrightness();
			displayMode = displayManager.getDisplayMode();
			runOnUiThread(()->{
				((SeekBar)findViewById(R.id.seekbar_brightness)).setProgress(brightness);
				((RadioButton)findViewById(
						(displayMode == DisplayManager.DISPLAY_2D)?
								R.id.btn_display_2D : R.id.btn_display_3D)).callOnClick();
				Toast.makeText(getApplicationContext(),
						"Display settings have been successfully loaded",
						Toast.LENGTH_SHORT).show();
			});
		} else {
			// use schedule() to do the polling
			executorService.schedule(this::getDisplaySettings, 100, TimeUnit.MILLISECONDS);
		}
	}

	private void initUIComponent() {
		RadioButton radioDisplay2D = findViewById(R.id.btn_display_2D);
		RadioButton radioDisplay3D = findViewById(R.id.btn_display_3D);

		((Button)findViewById(R.id.testMute)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				isMuted = !isMuted;
				displayManager.setMute(isMuted);
				Toast.makeText(getApplicationContext(),
						"Display is " + ((isMuted)? "" : "not ") + "muted",
						Toast.LENGTH_SHORT).show();
			}
		});

		((Button)findViewById(R.id.tempAndLimit)).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				String limit;
				switch (displayManager.getLimitation()) {
					case 0:
						limit = "100%";
						break;
					case 1:
						limit = "80%";
						break;
					case 2:
						limit = "50%";
						break;
					default:
						limit = "Unknown";
				}
				Toast.makeText(getApplicationContext(), "Max brightness is set to " + limit,
						Toast.LENGTH_SHORT).show();
			}

		});
		radioDisplay2D.setOnCheckedChangeListener((button, isChecked) -> {
			if (isChecked) {
				displayManager.setDisplayMode(DisplayManager.DISPLAY_2D);
			}
		});
		radioDisplay3D.setOnCheckedChangeListener((button, isChecked) -> {
			if (isChecked) {
				displayManager.setDisplayMode(DisplayManager.DISPLAY_3D);
			}
		});

		TextView textBrightnessValue = findViewById(R.id.text_brightness_value);
		SeekBar seekBrightness = findViewById(R.id.seekbar_brightness);

		seekBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar bar, int value, boolean b) {
				runOnUiThread(() -> textBrightnessValue.setText(String.valueOf(value)));
				displayManager.setBrightness(value);
			}

			@Override
			public void onStartTrackingTouch(SeekBar bar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar bar) {

			}
		});
	}
}
