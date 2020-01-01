package org.sidor.androidapps.simpletuner;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Tuning {
	public static final String TAG = "RealGuitarTuner";

	//Class for the diffferent strings
	private static class TuningType {
		public double freqs;
		public String stringNames;
		public TuningType( double f, String sn) {
			freqs = f;
			stringNames = sn;
		}
	}

	//Array containing strings with freqs and notes to populate spinner
	private static TuningType [] tuningTypes = new TuningType[]{
			new TuningType(	82.41,///{82.41, 110.00, 146.83, 196.00, 246.94, 329.63},
					"E2"),//{"E","A","D","G","B","E"}) ,
			new TuningType(
					110.00,
					"A2") ,
			new TuningType(
					146.83,
					"D3") ,
			new TuningType(
					196.00,
					"G3") ,
			new TuningType(
					246.94,
					"B3") ,
			new TuningType(
					329.63,
					"E4"),
			new TuningType(
                    65.41,
					"C2"),
			new TuningType(
					87.31,
					"F2") ,
			new TuningType(
					116.54,
					"A#2") ,
			new TuningType(
					155.56,
					"D#3") ,
			new TuningType(
					196.00,
					"G3") ,
			new TuningType(
					261.63,
					"C4")

	};

	//Fills spinner with the Array above
	public static void populateSpinner(Activity parent, Spinner s) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent, 
				android.R.layout.simple_spinner_item);
		for(int i=0; i<tuningTypes.length; ++i) {
			String label=tuningTypes[i].stringNames;
			adapter.add(label);
		}
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    s.setAdapter(adapter);
	}

	//Class for each of the available strings
	public class GuitarString {
		public int stringId; // no of string in the order of ascending frequency
		public double minFreq;
		public double maxFreq;
		public double freq;
		public String name;
		public GuitarString(int s,double f, double mif, double maf, String n) {
			stringId=s;
			freq=f;
			minFreq=mif;
			maxFreq=maf;
			name=n;
		}
	}

	private final GuitarString zeroString = new GuitarString(0,0.0,0.0,0.0,"0");
	private GuitarString strings;
	private String humanReadableName;
	private int tuningId = 0;
	
	public int getTuningId() {
		return tuningId;
	}

	//Function receives the frequency and the name of the string
	//Freq
	public void initStrings(double freqs, String names) {
		int i = 0;
		///for(int i=0; i<1; ++i) {///i<freqs.length; ++i) {
			double ldist = freqs - 2*freqs;///(i==0) ? 0.75*(2*freqs -(freqs)/2)
					              ///: (freqs)/2;
			double rdist = freqs + 2*freqs;///(i==0) ? 1.5*(2*freqs - (freqs)/2)
					                           ///: (freqs)/2;
		strings = new GuitarString(i+1,freqs,ldist,rdist, names);
		}

	public Tuning(int tuningNumber) {
		initStrings(tuningTypes[tuningNumber].freqs,
				    tuningTypes[tuningNumber].stringNames);
		humanReadableName = tuningTypes[tuningNumber].stringNames;
		tuningId = tuningNumber;
	}
	
	public String getName() {
		return humanReadableName;
	}
	
	GuitarString getString(double frequency) {
			if(strings.minFreq <=frequency && frequency<=strings.maxFreq)
				return strings;

		return zeroString;
	}
}
