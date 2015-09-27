package net.viralpatel.android.speechtotextdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegistrationActivity extends Activity{

	private Button submitButton;
	private EditText name;
	private EditText contact1;
	private EditText contact2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		
		submitButton = (Button) findViewById(R.id.submitToMainPage);
		name = (EditText) findViewById(R.id.editText1);
		contact1 = (EditText) findViewById(R.id.editText2);
		contact2 = (EditText) findViewById(R.id.editText3);

		submitButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent mainIntent = new Intent(RegistrationActivity.this, MainActivity.class);
				
				mainIntent.putExtra("name", name.getText().toString());
				mainIntent.putExtra("contact1", contact1.getText().toString());
				mainIntent.putExtra("contact2", contact2.getText().toString());

				startActivity(mainIntent);
			}
		});
	}
}
