package org.coursera.weatherservice.operations;

import java.lang.ref.WeakReference;

import android.app.Activity;

public abstract class Operations {
	
	protected WeakReference<Activity> mActivity;
	
	public Operations(Activity mActivity) {
		super();
		this.mActivity = new WeakReference<Activity>(mActivity);
	}

	public final Activity getActivity()
	{
		return mActivity.get();
	}
	
	public final void setActivity(Activity activity)
	{
		mActivity = new WeakReference<Activity>(activity);
	}
	
	public abstract void onConfigurationChange(Activity activity);

}
