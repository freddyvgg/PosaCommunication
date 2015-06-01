package org.coursera.weatherservice.jsonweather;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.util.JsonReader;
import android.util.JsonToken;

/**
 * Parses the Json weather data returned from the Weather Services API
 * and returns a List of JsonWeather objects that contain this data.
 */
public class WeatherJSONParser {

    /**
     * Parse the @a inputStream and convert it into a List of JsonWeather
     * objects.
     */
    public List<JsonWeather> parseJsonStream(InputStream inputStream)
        throws IOException {
        // TODO -- you fill in here.
    	try (JsonReader reader =
                new JsonReader(new InputStreamReader(inputStream,
                                                     "UTF-8"))) {
    		return parseJsonWeatherArray(reader);
    	}
    }

    /**
     * Parse a Json stream and convert it into a List of JsonWeather
     * objects.
     */
    public List<JsonWeather> parseJsonWeatherArray(JsonReader reader)
        throws IOException {
        // TODO -- you fill in here.
    	List<JsonWeather> result = new ArrayList<JsonWeather>();
    	if(reader.peek()==JsonToken.BEGIN_ARRAY){
	    	reader.beginArray();
	    	
	    	try{
		    	while(reader.hasNext())
		    	{
		    		result.add(parseJsonWeather(reader));
		    	}
	    	}finally{
	    		reader.endArray();
	    	}

    	}else
    	{
    		result.add(parseJsonWeather(reader));
    	}
    	
    	return result;
    }

    /**
     * Parse a Json stream and return a JsonWeather object.
     */
    public JsonWeather parseJsonWeather(JsonReader reader) 
        throws IOException {

    	JsonWeather jsonWeather = new JsonWeather();
        reader.beginObject();
        
        try{
        	while(reader.hasNext()){
        		String name = reader.nextName();
        		
        		switch(name){
        			
        		case JsonWeather.sys_JSON:
        			jsonWeather.setSys(parseSys(reader));
        			break;
        			
        		case JsonWeather.weather_JSON:
        			jsonWeather.setWeather(parseWeathers(reader));
        			break;
        			
        		case JsonWeather.base_JSON:
        			jsonWeather.setBase(reader.nextString());
        			break;
        			
        		case JsonWeather.main_JSON:
        			jsonWeather.setMain(parseMain(reader));
        			break;
        			
        		case JsonWeather.wind_JSON:
        			jsonWeather.setWind(parseWind(reader));
        			break;
        			
        		case JsonWeather.dt_JSON:
        			jsonWeather.setDt(reader.nextLong());
        			break;
        			
        		case JsonWeather.id_JSON:
        			jsonWeather.setId(reader.nextLong());
        			break;
        			
        		case JsonWeather.name_JSON:
        			jsonWeather.setName(reader.nextString());
        			break;
        			
        		case JsonWeather.cod_JSON:
        			jsonWeather.setCod(reader.nextLong());
        			break;
        		
        			default:
        				reader.skipValue();
        				break;
        		}
        		
        		
        	}
        }finally{
        	reader.endObject();
        }
        
        return jsonWeather;
    }
    
    /**
     * Parse a Json stream and return a List of Weather objects.
     */
    public List<Weather> parseWeathers(JsonReader reader) throws IOException {
        // TODO -- you fill in here.
    	List<Weather> result = new ArrayList<Weather>();
    	if(reader.peek()==JsonToken.BEGIN_ARRAY){
	    	reader.beginArray();
	    	
	    	try{
		    	while(reader.hasNext())
		    	{
		    		result.add(parseWeather(reader));
		    	}
	    	}finally{
	    		reader.endArray();
	    	}

    	}else
    	{
    		result.add(parseWeather(reader));
    	}
    	
    	return result;
    }

    /**
     * Parse a Json stream and return a Weather object.
     */
    public Weather parseWeather(JsonReader reader) throws IOException {
        // TODO -- you fill in here.  
    	Weather result = new Weather();
        reader.beginObject();
        
        try{
        	while(reader.hasNext()){
        		String name = reader.nextName();
        		
        		switch(name){
        			
        		case Weather.id_JSON:
        			result.setId(reader.nextLong());
        			break;
        			
        		case Weather.main_JSON:
        			result.setMain(reader.nextString());
        			break;
        			
        		case Weather.description_JSON:
        			result.setDescription(reader.nextString());
        			break;
        			
        		case Weather.icon_JSON:
        			result.setIcon(reader.nextString());
        			break;
        		
        			default:
        				reader.skipValue();
        				break;
        		}
        		
        		
        	}
        }finally{
        	reader.endObject();
        }
        
        return result;
    }
    
    /**
     * Parse a Json stream and return a Main Object.
     */
    public Main parseMain(JsonReader reader) 
        throws IOException {
        // TODO -- you fill in here.  
    	Main result = new Main();
        reader.beginObject();
        
        try{
        	while(reader.hasNext()){
        		String name = reader.nextName();
        		
        		switch(name){
        			
        		case Main.temp_JSON:
        			result.setTemp(reader.nextDouble());
        			break;
        			
        		case Main.humidity_JSON:
        			result.setHumidity(reader.nextLong());
        			break;
        			
        		case Main.pressure_JSON:
        			result.setPressure(reader.nextDouble());
        			break;
        			
        		case Main.tempMin_JSON:
        			result.setTempMin(reader.nextDouble());
        			break;
        			
        		case Main.tempMax_JSON:
        			result.setTempMax(reader.nextDouble());
        			break;
        			
        		case Main.grndLevel_JSON:
        			result.setGrndLevel(reader.nextDouble());
        			break;
        		
        			default:
        				reader.skipValue();
        				break;
        		}
        		
        		
        	}
        }finally{
        	reader.endObject();
        }
        
        return result;
    }

    /**
     * Parse a Json stream and return a Wind Object.
     */
    public Wind parseWind(JsonReader reader) throws IOException {
    	// TODO -- you fill in here.  
    	Wind result = new Wind();
        reader.beginObject();
        
        try{
        	while(reader.hasNext()){
        		String name = reader.nextName();
        		
        		switch(name){
        			
        		case Wind.deg_JSON:
        			result.setDeg(reader.nextDouble());
        			break;
        			
        		case Wind.speed_JSON:
        			result.setSpeed(reader.nextDouble());
        			break;
        		
        			default:
        				reader.skipValue();
        				break;
        		}
        		
        		
        	}
        }finally{
        	reader.endObject();
        }
        
        return result; 
    }

    /**
     * Parse a Json stream and return a Sys Object.
     * @throws IOException 
     */
    public Sys parseSys(JsonReader reader) throws IOException{
    	// TODO -- you fill in here.  
    	Sys result = new Sys();
        reader.beginObject();
        
        try{
        	while(reader.hasNext()){
        		String name = reader.nextName();
        		
        		switch(name){
        			
        		case Sys.country_JSON:
        			result.setCountry(reader.nextString());
        			break;
        			
        		case Sys.message_JSON:
        			result.setMessage(reader.nextDouble());
        			break;
        			
        		case Sys.sunrise_JSON:
        			result.setSunrise(reader.nextLong());
        			break;
        			
        		case Sys.sunset_JSON:
        			result.setSunset(reader.nextLong());
        			break;
        		
        			default:
        				reader.skipValue();
        				break;
        		}
        		
        		
        	}
        }finally{
        	reader.endObject();
        }
        
        return result;    
    }
}
