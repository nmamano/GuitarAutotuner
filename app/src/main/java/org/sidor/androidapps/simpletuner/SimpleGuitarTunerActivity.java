package org.sidor.androidapps.simpletuner;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;



public class SimpleGuitarTunerActivity extends SampleActivityBase {
	// switch off gc logs.
	// System.setProperty("log.tag.falvikvm", "SUPPRESS");

	private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;///
	private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;///
	private static final int REQUEST_ENABLE_BT = 3;///


	public static final String TAG = "RealGuitarTuner";


	private BluetoothAdapter mBluetoothAdapter = null;///


	private final boolean LAUNCHANALYZER = true;


	private Spinner tuningSelector = null;
	private SoundAnalyzer soundAnalyzer = null ;
	private UiController uiController = null;
	private TextView mainMessage = null;
	private TextView freqMssg = null;
	private PitchView pitchView = null;

/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {///
		getMenuInflater().inflate(R.menu.bluetooth_chat, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {///
			case R.id.secure_connect_scan: {
				// Launch the DeviceListActivity to see devices and do scan
				Intent serverIntent = new Intent(this, DeviceListActivity.class);
				startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
				return true;
			}
			case R.id.insecure_connect_scan: {
				// Launch the DeviceListActivity to see devices and do scan
				Intent serverIntent = new Intent(this, DeviceListActivity.class);
				startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
				return true;
			}
			case R.id.discoverable: {
				// Ensure this device is discoverable by others
				///ensureDiscoverable();
				return true;
			}
		}
		return false;
	} *///


	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.main);
        uiController = new UiController(this);

		if (savedInstanceState == null) {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			BluetoothChatFragment fragment = new BluetoothChatFragment();
			transaction.replace(R.id.sample_content_fragment, fragment);
			transaction.commit();
		}


        if(LAUNCHANALYZER) {
	        try {
	        	soundAnalyzer = new SoundAnalyzer();
	        } catch(Exception e) {
	        	Toast.makeText(this, "The are problems with your microphone :(", Toast.LENGTH_LONG ).show();
	        	Log.e(TAG, "Exception when instantiating SoundAnalyzer: " + e.getMessage());
	        }
	        soundAnalyzer.addObserver(uiController);
        }

        mainMessage = (TextView)findViewById(R.id.mainMessage);
		freqMssg = (TextView) findViewById(R.id.freqAnalyzed);
        tuningSelector = (Spinner)findViewById(R.id.tuningSelector);
        Tuning.populateSpinner(this, tuningSelector);
        tuningSelector.setOnItemSelectedListener(uiController);
		pitchView = (PitchView) findViewById(R.id.pitch_view);

    }

	
	public void dumpArray(final double [] inputArray, final int elements) {
		Log.d(TAG, "Starting File writer thread...");
		final double [] array = new double[elements];
		for(int i=0; i<elements; ++i)
			array[i] = inputArray[i];
		new Thread(new Runnable() {
			@Override
			public void run() {
				try { // catches IOException below
					// Location: /data/data/your_project_package_structure/files/samplefile.txt         
					String name = "Chart_" + (int)(Math.random()*1000) + ".data";
					FileOutputStream fOut = openFileOutput(name,
							Context.MODE_WORLD_READABLE);
					OutputStreamWriter osw = new OutputStreamWriter(fOut); 

					// Write the string to the file
					for(int i=0; i<elements; ++i) 
							osw.write("" + array[i] + "\n");
					/* ensure that everything is
					 * really written out and close */
					osw.flush();
					osw.close();
					Log.d(TAG, "Successfully dumped array in file " + name);
				} catch(Exception e) {
					Log.e(TAG,e.getMessage());
				}
			}
		}).start();
	}
	
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(ConfigFlags.menuKeyCausesAudioDataDump) {
		    if (keyCode == KeyEvent.KEYCODE_MENU) {
		        Log.d(TAG,"Menu button pressed");
		        Log.d(TAG,"Requesting audio data dump");
		        soundAnalyzer.dumpAudioDataRequest();
		        return true;
		    }
		}
	    return false;
	}

    int oldString = 0;
    // 1-6 strings (ascending frequency), 0 - no string.
    public void changeString(int stringId) {
    	if(oldString!=stringId) {
        	oldString=stringId;
    	}
    }
    
    public void displayMessage(String msg,String fmsg, boolean positiveFeedback) {
    	int textColor = positiveFeedback ? Color.rgb(34,139,34) : Color.rgb(255,36,0);
    	mainMessage.setText(msg);
    	mainMessage.setTextColor(textColor);
		freqMssg.setText(fmsg);
		freqMssg.setTextColor(textColor);
    }
    
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
        Log.d(TAG,"onDestroy()");

	}

	@Override
	protected void onPause() {
		super.onPause();
        Log.d(TAG, "onPause()");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
        Log.d(TAG, "onRestart()");

	}

	@Override
	protected void onResume() {
		super.onResume();
        Log.d(TAG,"onResume()");
        if(soundAnalyzer!=null)
        	soundAnalyzer.ensureStarted();
	}

	@Override
	protected void onStart() {
		super.onStart();
        Log.d(TAG, "onStart()");
        if(soundAnalyzer!=null)
        	soundAnalyzer.start();
	}

	@Override
	protected void onStop() {
		super.onStop();
        Log.d(TAG,"onStop()");
        if(soundAnalyzer!=null)
        	soundAnalyzer.stop();
	}

	public void updatePitchView(double currentFreq, double targetFreq){
		pitchView.setCurrentPitch(currentFreq);
		pitchView.setCenterPitch(targetFreq);
	}

}

