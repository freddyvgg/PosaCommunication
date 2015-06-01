package org.coursera.weatherservice.services;

import java.util.List;

import org.coursera.weatherservice.aidl.WeatherData;
import org.coursera.weatherservice.aidl.WeatherRequest;
import org.coursera.weatherservice.aidl.WeatherResults;
import org.coursera.weatherservice.utils.Utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class WeatherServiceAsync extends Service {

	WeatherRequest.Stub mWeatherRequestImpl = new WeatherRequest.Stub(){

		@Override
		public void getCurrentWeather(String Weather, WeatherResults results)
				throws RemoteException {
			// TODO Auto-generated method stub
			List<WeatherData> data = Utils.getCurrentWeather(Weather);
			
			results.sendResults(Weather,data);	
		}
		
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mWeatherRequestImpl;
	}
	
	public static Intent makeIntent(Context context){
		return new Intent(context,WeatherServiceAsync.class);
	}

}
