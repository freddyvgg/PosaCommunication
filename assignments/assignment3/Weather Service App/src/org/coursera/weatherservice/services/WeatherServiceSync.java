package org.coursera.weatherservice.services;

import java.util.List;

import org.coursera.weatherservice.aidl.WeatherCall;
import org.coursera.weatherservice.aidl.WeatherData;
import org.coursera.weatherservice.utils.Utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class WeatherServiceSync extends Service {

	WeatherCall.Stub mWeatherCallImpl = new WeatherCall.Stub(){

		@Override
		public List<WeatherData> getCurrentWeather(String Weather)
				throws RemoteException {
			return Utils.getCurrentWeather(Weather);
		}
		
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mWeatherCallImpl;
	}
	
	public static Intent makeIntent(Context context)
	{
		return new Intent(context,WeatherServiceSync.class);
	}

}
