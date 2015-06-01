package org.coursera.weatherservice.operations;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.coursera.weatherservice.R;
import org.coursera.weatherservice.aidl.WeatherCall;
import org.coursera.weatherservice.aidl.WeatherData;
import org.coursera.weatherservice.aidl.WeatherRequest;
import org.coursera.weatherservice.aidl.WeatherResults;
import org.coursera.weatherservice.services.WeatherServiceAsync;
import org.coursera.weatherservice.services.WeatherServiceSync;
import org.coursera.weatherservice.utils.GenericServiceConnection;
import org.coursera.weatherservice.utils.Utils;

import android.app.Activity;
import android.app.Service;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainOperations extends Operations {

	private final String TAG = this.getClass().getSimpleName();
	
	private WeakReference<TextView> mCityCountryTextView;
	private WeakReference<TextView> mTemperatureTextView;
	private WeakReference<TextView> mHumidityTextView;
	private WeakReference<TextView> mWindTextView;
	private WeakReference<TextView> mWinddTextView;
	private WeakReference<TextView> mSunriseTextView;
	private WeakReference<TextView> mSunsetTextView;
	private WeakReference<TextView> mTimeTextView;
	private WeakReference<TextView> mDescriptionTextView;
	
	private WeakReference<ScrollView> mScrollView;
	
	private WeakReference<EditText> mInputEditText;
	
	private WeakReference<RadioButton> mAsyncRadioButton;
	private WeakReference<RadioButton> mSyncRadioButton;
	
	private GenericServiceConnection<WeatherRequest> mServiceConnectionAsync;
	
	private GenericServiceConnection<WeatherCall> mServiceConnectionSync;
	private WeatherResults.Stub mWeatherResults = new WeatherResults.Stub(){

		@Override
		public void sendResults(String request,List<WeatherData> results)
				throws RemoteException {
			// TODO Auto-generated method stub
			Log.d(TAG, "Result="+results);
			if(results!=null){
				cacheData(request,results);
				doResult(results);
			}else
				processError();
		}
		
	};

	private List<WeatherData> resultData;
	
	
	private ConcurrentHashMap<String, List<WeatherData>> cache = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, Long> cacheLastCall = new ConcurrentHashMap<>();
	
	public MainOperations(Activity mActivity) {
		super(mActivity);
		// TODO Auto-generated constructor stub
		initializeViewFields();
		initializeNonViewFields();
	}

	private void initializeViewFields() {
		// TODO Auto-generated method stub
		getActivity().setContentView(R.layout.activity_main);

		mCityCountryTextView = new WeakReference<TextView>(
				(TextView)getActivity().findViewById(R.id.city_country));
		
		mTemperatureTextView = new WeakReference<TextView>(
				(TextView)getActivity().findViewById(R.id.temp));
		
		mHumidityTextView = new WeakReference<TextView>(
				(TextView)getActivity().findViewById(R.id.humidity));
		
		mWindTextView = new WeakReference<TextView>(
				(TextView)getActivity().findViewById(R.id.wind));
		
		mWinddTextView = new WeakReference<TextView>(
				(TextView)getActivity().findViewById(R.id.wind_d));
		
		mSunriseTextView = new WeakReference<TextView>(
				(TextView)getActivity().findViewById(R.id.sunrise));
		
		mSunsetTextView = new WeakReference<TextView>(
				(TextView)getActivity().findViewById(R.id.sunset));
		
		mTimeTextView = new WeakReference<TextView>(
				(TextView)getActivity().findViewById(R.id.time));
		
		mDescriptionTextView = new WeakReference<TextView>(
				(TextView)getActivity().findViewById(R.id.description));
		
		mScrollView = new WeakReference<ScrollView>(
				(ScrollView)getActivity().findViewById(R.id.data_scroll));
		
		mInputEditText = new WeakReference<EditText>(
				(EditText)getActivity().findViewById(R.id.input_text));
		
		mAsyncRadioButton = new WeakReference<RadioButton>(
				(RadioButton)getActivity().findViewById(R.id.async));
		
		mSyncRadioButton = new WeakReference<RadioButton>(
				(RadioButton)getActivity().findViewById(R.id.sync));
		
		loadData();
	}
	
	
	private void initializeNonViewFields() {

        mServiceConnectionSync = 
            new GenericServiceConnection<WeatherCall>(WeatherCall.class);

        mServiceConnectionAsync =
            new GenericServiceConnection<WeatherRequest>(WeatherRequest.class);

    }

	@Override
	public void onConfigurationChange(Activity activity) {
		// TODO Auto-generated method stub
		super.setActivity(activity);
		initializeViewFields();
		
	}

	private void loadData() {
		// TODO Auto-generated method stub
		if(null!=resultData)
			display(resultData);
		
	}

	public void callWeatherService(){
		String requestData = mInputEditText.get().getText().toString();
		
		if(isCached(requestData))
		{
			callWeatherCache(requestData);
		}else
		{
			callWeatherWebService(requestData);
		}
	}
	
	
	private synchronized void callWeatherCache(String request) {
		// TODO Auto-generated method stub
		List<WeatherData> result = cache.get(request);
		if(result!=null){
			Log.d(TAG,"Restoring data from cache");
			Log.d(TAG, "Result="+result);
			doResult(result);
		}
		else{
			Log.d(TAG,"cache failed");
			callWeatherWebService(request);
		}
		
	}

	private synchronized boolean isCached(String request) {
		// TODO Auto-generated method stub
		return cache.containsKey(request)&&cacheLastCall.containsKey(request)&&Utils.isCacheValid(request, cacheLastCall);
	}
	
	protected synchronized void cacheData(String request,List<WeatherData> result) {
		// TODO Auto-generated method stub
		cache.put(request, result);
		cacheLastCall.put(request, System.currentTimeMillis());
		
	}

	public void callWeatherWebService(String request) {
		// TODO Auto-generated method stub
		if(mAsyncRadioButton.get().isChecked()){
			WeatherRequest weatherRequest = mServiceConnectionAsync.getInterface();
			Log.d(TAG, "Async Service call "+weatherRequest);
			if(null!=weatherRequest){
				try {
					weatherRequest.getCurrentWeather(request, 
													mWeatherResults);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					processError();
				}
			}
		}else if(mSyncRadioButton.get().isChecked()){
			
			final WeatherCall weatherCall = mServiceConnectionSync.getInterface();
			Log.d(TAG, "Sync Service call "+weatherCall);
			
			if(null!=weatherCall){
				
				new AsyncTask<String, Void, List<WeatherData>>() {
					String request;
					@Override
					protected List<WeatherData> doInBackground(String... params) {
						// TODO Auto-generated method stub
						request = params[0];
						try {
							return weatherCall.getCurrentWeather(request);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}

					@Override
					protected void onPostExecute(List<WeatherData> result) {
						// TODO Auto-generated method stub
						Log.d(TAG, "Result="+result);
						if(result!=null){
							cacheData(request,result);
							doResult(result);
						}
						else{
							processError();
						}
					}
					
					
					
				}.execute(request);

			}
		}
		
	}

	protected void processError() {
		Log.d(TAG, "processError() called");
		// TODO Auto-generated method stub
		getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), R.string.error_message, Toast.LENGTH_SHORT).show();
			}
		});
		
	}

	protected void doResult(final List<WeatherData> result) {
		// TODO Auto-generated method stub
		Log.d(TAG, "doResult()");
		resultData=result;
		getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				display(result);
			}
		});
		
	}

	protected void display(List<WeatherData> result) {
		// TODO Auto-generated method stub
		
		
		if(result!=null&&!result.isEmpty())
		{
			WeatherData weatherData =result.get(0);
			if(weatherData.getName()!=null&&!weatherData.getName().isEmpty())
				mCityCountryTextView.get().setText(weatherData.getName()+", "+weatherData.getCountry());
			else
				mCityCountryTextView.get().setText(weatherData.getCountry());
			
			
			mHumidityTextView.get().setText(weatherData.getHumidity()+"%");
			
			mTemperatureTextView.get().setText(Math.round(Utils.kelvinToCen(weatherData.getTemp()))
												+"°C");
			
			mWindTextView.get().setText(weatherData.getSpeed()+"m/s");
			
			mWinddTextView.get().setText(Utils.windDirection(weatherData.getDeg()));
			
			mSunriseTextView.get().setText(Utils.fromEpochToTime(weatherData.getSunrise()));
			
			mSunsetTextView.get().setText(Utils.fromEpochToTime(weatherData.getSunset()));
			
			mTimeTextView.get().setText(Utils.formatDate(Utils.fromEpochToDate(weatherData.getDt()), "EEE, d MMM yyyy HH:mm:ss"));
			
			mDescriptionTextView.get().setText(weatherData.getDescription());
		
			mScrollView.get().setVisibility(View.VISIBLE);
		}
		else
			clearScreen();
		
	}
	
	protected void clearScreen() {
		// TODO Auto-generated method stub
		{
			mCityCountryTextView.get().setText(getActivity().getApplicationContext()
					.getResources().getString(R.string.no_data));
			mHumidityTextView.get().setText("");
			mTemperatureTextView.get().setText("");
			mWindTextView.get().setText("");
			mSunriseTextView.get().setText("");
			mSunsetTextView.get().setText("");
			mTimeTextView.get().setText("");
			mDescriptionTextView.get().setText("");
			mScrollView.get().setVisibility(View.GONE);
		}
		
	}

	public void resetViewFields(){
		Utils.hideKeyboard(getActivity(), mInputEditText.get().getWindowToken());
		
		resultData=null;
		clearScreen();
		mInputEditText.get().setText("");
		
		
	}
	
	public void bindService(){
		Log.d(TAG,"bindService() Called");
		if(null==mServiceConnectionAsync.getInterface())
		{
			getActivity().getApplicationContext().bindService(WeatherServiceAsync.makeIntent(getActivity()),
					mServiceConnectionAsync,
					Service.BIND_AUTO_CREATE);
		}
		
		if(null==mServiceConnectionSync.getInterface())
		{
			getActivity().getApplicationContext().bindService(WeatherServiceSync.makeIntent(getActivity()),
					mServiceConnectionSync,
					Service.BIND_AUTO_CREATE);
		}
		
	}

	public void unbindService(){
		
		if (getActivity().isChangingConfigurations()) 
            Log.d(TAG,
                  "just a configuration change - unbindService() not called");
        else {
            Log.d(TAG,
                  "calling unbindService()");
			if(null!=mServiceConnectionAsync.getInterface())
			{
				Log.d(TAG, "unbinding mServiceConnectionAsync");
				getActivity().getApplicationContext().unbindService(mServiceConnectionAsync);
			}
			
			if(null!=mServiceConnectionSync.getInterface())
			{
				Log.d(TAG, "unbinding mServiceConnectionSync");
				getActivity().getApplicationContext().unbindService(mServiceConnectionSync);
			}
		}
	}

}
