package org.coursera.weatherservice.activities;

import org.coursera.weatherservice.operations.MainOperations;
import org.coursera.weatherservice.utils.RetainedFragmentManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	private final String TAG = this.getClass().getSimpleName();

	private MainOperations mOperations;
	
	private RetainedFragmentManager mRetainedFragmentManager = 
	        new RetainedFragmentManager(this.getFragmentManager(),
	                                    TAG);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mOperations = new MainOperations(this);
		handleConfigurationChanges();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mOperations.unbindService();
		super.onDestroy();
	}



	private void handleConfigurationChanges() {
		// TODO Auto-generated method stub
		if(mRetainedFragmentManager.firstTimeIn())
		{
			mOperations = new MainOperations(this);
			mRetainedFragmentManager.put(TAG, mOperations);
			mOperations.bindService();
		}
		else
		{
			mOperations = mRetainedFragmentManager.get(TAG);
			
			if(mOperations==null)
			{
				mOperations = new MainOperations(this);
				mRetainedFragmentManager.put(TAG, mOperations);
				mOperations.bindService();
			}else{
				mOperations.onConfigurationChange(this);
			}
		}
	}
	
	public void callWeatherService(View view)
	{
		mOperations.callWeatherService();
	}
	
	public void clearData(View view)
	{
		mOperations.resetViewFields();
	}

}
