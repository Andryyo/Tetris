package com.example.tetris;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener{
    Tetris_view tetris_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("autorotate",true))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        tetris_view = (Tetris_view)findViewById(R.id.tetrisview);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        menu.findItem(R.id.pause).setEnabled(!tetris_view.isGameOver());
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId()==R.id.new_game)
		{
            tetris_view.Game_Over();
            tetris_view.init();
		}
		if (item.getItemId()==R.id.pause)
		{
            tetris_view.switchPause();
		}
		if (item.getItemId()==R.id.settings)
		{
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
		}
		
		return true;
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
        tetris_view.Pause();
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
        tetris_view.unPause();
	}

	@Override
	public void onStop()
	{
		super.onStop();
        tetris_view.vibrator.cancel();
	}

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key)
    {
        if (key.equalsIgnoreCase("vibration"))
        {
            tetris_view.setVibration(preferences.getBoolean(key,true));
        }
        else
        if (key.equalsIgnoreCase("scaling"))
        {
            tetris_view.setScaling(preferences.getBoolean(key,false));
        }
        else
        if (key.equalsIgnoreCase("autorotate"))
        {
            if (!preferences.getBoolean(key,true))
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            else
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }

    }
}