package org.coursera.weatherservice.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class is a Plain Old Java Object (POJO) used for data
 * transport within the WeatherService app.  This POJO implements the
 * Parcelable interface to enable IPC between the WeatherActivity and
 * the WeatherServiceSync and WeatherServiceAsync. It represents the
 * response Json obtained from the Open Weather Map API, e.g., a call
 * to http://api.openweathermap.org/data/2.5/weather?q=Nashville,TN
 * might return the following Json data:
 * 
 * { "coord":{ "lon":-86.78, "lat":36.17 }, "sys":{ "message":0.0138,
 * "country":"United States of America", "sunrise":1431427373,
 * "sunset":1431477841 }, "weather":[ { "id":802, "main":"Clouds",
 * "description":"scattered clouds", "icon":"03d" } ],
 * "base":"stations", "main":{ "temp":289.847, "temp_min":289.847,
 * "temp_max":289.847, "pressure":1010.71, "sea_level":1035.76,
 * "grnd_level":1010.71, "humidity":76 }, "wind":{ "speed":2.42,
 * "deg":310.002 }, "clouds":{ "all":36 }, "dt":1431435983,
 * "id":4644585, "name":"Nashville", "cod":200 }
 *
 * The meaning of these Json fields is documented at 
 * http://openweathermap.org/weather-data#current.
 *
 * Parcelable defines an interface for marshaling/de-marshaling
 * https://en.wikipedia.org/wiki/Marshalling_(computer_science)
 * to/from a format that Android uses to allow data transport between
 * processes on a device.  Discussion of the details of Parcelable is
 * outside the scope of this assignment, but you can read more at
 * https://developer.android.com/reference/android/os/Parcelable.html.
 */
public class WeatherData implements Parcelable {
    /*
     * These data members are the local variables that will store the
     * WeatherData's state
     */
    private String mName;
    private double mSpeed;
    private double mDeg;
    private double mTemp;
    private long mHumidity;
    private long mSunrise;
    private long mSunset;
    private String mCountry;
    private long mDt;
    private String mDescription;
    private long mCode;
    private String mIcon;

    /**
     * Constructor
     * 
     * @param name
     * @param speed
     * @param deg
     * @param temp
     * @param humidity
     * @param sunrise
     * @param sunset
     * @param country
     */
    public WeatherData(String name,
                       double speed,
                       double deg,
                       double temp,
                       long humidity,
                       long sunrise,
                       long sunset,
                       String country,
                       long code,
                       String description,
                       long dt,
                       String icon) {
        mName = name;
        mSpeed = speed;
        mDeg = deg;
        mTemp = temp;
        mHumidity = humidity;
        mSunrise = sunrise;
        mSunset = sunset;
        mCountry = country;
        mCode = code;
        mDescription = description;
        mDt = dt;
        mIcon = icon;
    }

    /*
     * BELOW THIS is related to Parcelable Interface.
     */

    /**
     * A bitmask indicating the set of special object types marshaled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Write this instance out to byte contiguous memory.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeDouble(mSpeed);
        dest.writeDouble(mDeg);
        dest.writeDouble(mTemp);
        dest.writeLong(mHumidity);
        dest.writeLong(mSunrise);
        dest.writeLong(mSunset);
        dest.writeString(mCountry);
        dest.writeLong(mCode);
        dest.writeString(mDescription);
        dest.writeLong(mDt);
        dest.writeString(mIcon);

    }

    /**
     * Private constructor provided for the CREATOR interface, which
     * is used to de-marshal an WeatherData from the Parcel of data.
     * <p>
     * The order of reading in variables HAS TO MATCH the order in
     * writeToParcel(Parcel, int)
     *
     * @param in
     */
    private WeatherData(Parcel in) {
        mName = in.readString();
        mSpeed = in.readDouble();
        mDeg = in.readDouble();
        mTemp = in.readDouble();
        mHumidity = in.readLong();
        mSunrise = in.readLong();
        mSunset = in.readLong();
        mCountry = in.readString();
        mCode = in.readLong();
        mDescription = in.readString();
        mDt = in.readLong();
        mIcon = in.readString();
    }

    
    
    public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public double getSpeed() {
		return mSpeed;
	}

	public void setSpeed(double mSpeed) {
		this.mSpeed = mSpeed;
	}

	public double getDeg() {
		return mDeg;
	}

	public void setDeg(double mDeg) {
		this.mDeg = mDeg;
	}

	public double getTemp() {
		return mTemp;
	}

	public void setTemp(double mTemp) {
		this.mTemp = mTemp;
	}

	public long getHumidity() {
		return mHumidity;
	}

	public void setHumidity(long mHumidity) {
		this.mHumidity = mHumidity;
	}

	public long getSunrise() {
		return mSunrise;
	}

	public void setSunrise(long mSunrise) {
		this.mSunrise = mSunrise;
	}

	public long getSunset() {
		return mSunset;
	}

	public void setSunset(long mSunset) {
		this.mSunset = mSunset;
	}

	public String getCountry() {
		return mCountry;
	}

	public void setCountry(String mCountry) {
		this.mCountry = mCountry;
	}



	public long getDt() {
		return mDt;
	}

	public void setDt(long mDt) {
		this.mDt = mDt;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String mDescription) {
		this.mDescription = mDescription;
	}

	public long getCode() {
		return mCode;
	}

	public void setCode(long mCode) {
		this.mCode = mCode;
	}

	public String getIcon() {
		return mIcon;
	}

	public void setIcon(String mIcon) {
		this.mIcon = mIcon;
	}



	/**
     * public Parcelable.Creator for WeatherData, which is an
     * interface that must be implemented and provided as a public
     * CREATOR field that generates instances of your Parcelable class
     * from a Parcel.
     */
    public static final Parcelable.Creator<WeatherData> CREATOR =
        new Parcelable.Creator<WeatherData>() {
            public WeatherData createFromParcel(Parcel in) {
                return new WeatherData(in);
            }

            public WeatherData[] newArray(int size) {
                return new WeatherData[size];
            }
        };

	@Override
	public String toString() {
		return "WeatherData [mName=" + mName + ", mSpeed=" + mSpeed + ", mDeg="
				+ mDeg + ", mTemp=" + mTemp + ", mHumidity=" + mHumidity
				+ ", mSunrise=" + mSunrise + ", mSunset=" + mSunset
				+ ", mCountry=" + mCountry + ", mDt=" + mDt + ", mDescription="
				+ mDescription + ", mCode=" + mCode + ", mIcon=" + mIcon + "]";
	}

        
	
        
     
}
