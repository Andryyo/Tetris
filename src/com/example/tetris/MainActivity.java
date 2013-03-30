package com.example.tetris;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
			((Tetris_view) findViewById(R.id.tetrisview)).Pause();
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
		((Tetris_view) findViewById(R.id.tetrisview)).Pause();
	}
	
}