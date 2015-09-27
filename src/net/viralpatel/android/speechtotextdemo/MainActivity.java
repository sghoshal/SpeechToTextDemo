package net.viralpatel.android.speechtotextdemo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.telephony.gsm.SmsManager;
import android.app.PendingIntent;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener {

	private static final String DISTRESS_KEYWORD = "help";
	private static final String DISTRESS_PREFIX = "Some one needs help";
	
	protected static final int RESULT_SPEECH = 1;
	
	protected LocationManager locationManager;
	protected LocationListener locationListener;
	
	private String name = "";
	private String contact1 = "";
	private String contact2 = "";

	double lat;
	private double lon;
	
	private ImageButton btnSpeak;
	private TextView txtText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent intent = getIntent();
		
		name = intent.getStringExtra("name");
		contact1 = intent.getStringExtra("contact1");
		contact2 = intent.getStringExtra("contact2");

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		
		txtText = (TextView) findViewById(R.id.txtText);

		btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

		btnSpeak.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

				try {
					startActivityForResult(intent, RESULT_SPEECH);
					txtText.setText("");
				} catch (ActivityNotFoundException a) {
					Toast t = Toast.makeText(getApplicationContext(),
							"Ops! Your device doesn't support Speech to Text",
							Toast.LENGTH_SHORT);
					t.show();
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SPEECH: {
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				String convertedText = text.get(0);
				txtText.setText(convertedText);
				
				if( hasKeyword ( convertedText ) ) {
					sendText(convertedText);
				}
			}
			break;
		}
		}
	}
	
	private boolean hasKeyword( String text ) {
		text = text.trim();
		
		String[] splitString = text.split(" ");
		for ( int i = 0; i < splitString.length; i++ ) {
			if( splitString[i].toLowerCase().equals(DISTRESS_KEYWORD)) {
				return true;
			}
		}
		return false;
	}
	
	private void sendText(String message) {           

		String final_message = "ALERT : "+ name.toUpperCase() +" requesting for help. \n Location: "+ lat +" , "+lon;
		if (contact1.length()>0 && message.length()>0) {              
			sendSMS(contact1, final_message);                
		}
		if (contact2.length()>0 && message.length()>0) {              
				sendSMS(contact2, final_message);                
		}

	} 
	
	private void sendSMS(String phoneNumber, String message)
    {   
        PendingIntent pi = PendingIntent.getActivity(this, 0,
            new Intent(this, MainActivity.class), 0);     

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, pi, null);        
    }    
	
	@Override
	public void onLocationChanged(Location location) {
		lat = location.getLatitude();
		lon = location.getLongitude();
	}

	@Override
	public void onProviderDisabled(String provider) {
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}
}
