package com.example.tetris;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        Tetris_view.can_vibrate = getSharedPreferences("settings",MODE_PRIVATE).getBoolean("vibration",true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId()==R.id.new_game)
		{
			((Tetris_view) findViewById(R.id.tetrisview)).Game_Over();
			((Tetris_view) findViewById(R.id.tetrisview)).init();
		}
		if (item.getItemId()==R.id.pause)
		{
			((Tetris_view) findViewById(R.id.tetrisview)).switchPause();
		}
		if (item.getItemId()==R.id.settings)
		{
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
		}
		
		return true;
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		((Tetris_view) findViewById(R.id.tetrisview)).Pause();
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		((Tetris_view) findViewById(R.id.tetrisview)).unPause();
	}

	@Override
	public void onStop()
	{
		super.onStop();
		((Tetris_view) findViewById(R.id.tetrisview)).vibrator.cancel();
	}
}