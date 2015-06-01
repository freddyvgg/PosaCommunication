package org.coursera.weatherservice.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.coursera.weatherservice.aidl.WeatherData;
import org.coursera.weatherservice.jsonweather.JsonWeather;
import org.coursera.weatherservice.jsonweather.WeatherJSONParser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


public class Utils {
    /**
     * Logging tag used by the debugger. 
     */
    private final static String TAG = Utils.class.getCanonicalName();


    private final static String sWeather_Web_Service_URL =
        "http://api.openweathermap.org/data/2.5/weather?q=";
    



	public static boolean isCacheValid(String weather,Map<String,Long> cacheLastCall) {
		// TODO Auto-generated method stub
		long diff = System.currentTimeMillis()-cacheLastCall.get(weather);
		Log.d(TAG, "isCacheValid "+diff);
		return diff<10000;
	}

	public static List<WeatherData> getCurrentWeather(final String weather) {
        final List<WeatherData> returnList = 
            new ArrayList<WeatherData>();

        List<JsonWeather> jsonWeathers = null;

        try {
            // Append the location to create the full URL.
            final URL url =
                new URL(sWeather_Web_Service_URL
                        + weather);
            Log.d(TAG, "Request: "+url.toString());
            HttpURLConnection urlConnection =
                (HttpURLConnection) url.openConnection();
            
            // Sends the GET request and reads the Json results.
            try (InputStream in =
                 new BufferedInputStream(urlConnection.getInputStream())) {
                 // Create the parser.
                 final WeatherJSONParser parser =
                     new WeatherJSONParser();

                 jsonWeathers = parser.parseJsonStream(in);
                 
            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
        	Log.d(TAG, "Error reading");
            e.printStackTrace();
            return null;
        }
        
        try{
	        // See if we parsed any valid data.
	        if (jsonWeathers != null && jsonWeathers.size() > 0) {

	            for (JsonWeather jsonWeather : jsonWeathers)
	                returnList.add(new WeatherData(jsonWeather.getName(),
	                								(jsonWeather.getWind()==null)?0:jsonWeather.getWind().getSpeed(),
	                								(jsonWeather.getWind()==null)?0:jsonWeather.getWind().getDeg(),
	                								(jsonWeather.getMain()==null)?0:jsonWeather.getMain().getTemp(),
	                								(jsonWeather.getMain()==null)?0:jsonWeather.getMain().getHumidity(),
	                								(jsonWeather.getSys()==null)?0:jsonWeather.getSys().getSunrise(),
	                								(jsonWeather.getSys()==null)?0:jsonWeather.getSys().getSunset(),
	                								(jsonWeather.getSys()==null)?null:jsonWeather.getSys().getCountry(),
	                								(jsonWeather.getWeather()==null||jsonWeather.getWeather().isEmpty())?0:jsonWeather.getWeather().get(0).getId(),
	                								(jsonWeather.getWeather()==null||jsonWeather.getWeather().isEmpty())?null:jsonWeather.getWeather().get(0).getDescription(),
	                								jsonWeather.getDt(),
	                								(jsonWeather.getWeather()==null||jsonWeather.getWeather().isEmpty())?null:jsonWeather.getWeather().get(0).getIcon()));

	             return returnList;
	        }  else
	            return null;
        
        }catch(Exception e)
        {
        	Log.d(TAG, "Error parsing");
        	e.printStackTrace();
        	return null;
        }
        
    }
    
    public static double kelvinToCen(double k)
    {
    	return k - 273.15;
    }
    
    public static double roundTwo(double num)
    {
    	return Math.rint(num*100)/100;
    }
    
    @SuppressLint("SimpleDateFormat")
	public static String fromEpochToTime(long epoch)
    {
    	return new SimpleDateFormat("HH:mm").format(new Date(epoch*1000));
    }
    
    public static Date fromEpochToDate(long epoch)
    {
    	return new Date(epoch*1000);
    }
    
    @SuppressLint("SimpleDateFormat")
    public static String formatDate(Date date,String format)
    {
    	return new SimpleDateFormat(format).format(date);
    }
    
    public static String windDirection(double deg)
    {
    	double diff =45;
    	deg = (deg+22.5)%360;
 
    	if(deg<1*diff)
    		return "E";
    	else if(deg<2*diff)
    		return "NE";
    	else if(deg<3*diff)
    		return "N";
    	else if(deg<4*diff)
    		return "NW";
    	else if(deg<5*diff)
    		return "W";
    	else if(deg<6*diff)
    		return "SW";
    	else if(deg<7*diff)
    		return "S";
    	else if(deg<8*diff)
    		return "SE";
    	
    	return "Undefined";
    	
    }

    /**
     * This method is used to hide a keyboard after a user has
     * finished typing the url.
     */
    public static void hideKeyboard(Activity activity,
                                    IBinder windowToken) {
        InputMethodManager mgr =
           (InputMethodManager) activity.getSystemService
            (Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken,
                                    0);
    }

    /**
     * Show a toast message.
     */
    public static void showToast(Context context,
                                 String message) {
        Toast.makeText(context,
                       message,
                       Toast.LENGTH_SHORT).show();
    }

    /**
     * Ensure this class is only used as a utility.
     */
    private Utils() {
        throw new AssertionError();
    } 
}
