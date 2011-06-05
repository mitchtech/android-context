package edu.fsu.cs.contextprovider.monitor;

import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.smart_entity.EntityManager;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import edu.fsu.cs.contextprovider.weather.GoogleWeatherHandler;
import edu.fsu.cs.contextprovider.weather.WeatherCurrentCondition;
import edu.fsu.cs.contextprovider.weather.WeatherSet;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

import android.util.Log;


public class WeatherMonitor extends TimerTask {
	private static final String TAG = "WeatherMonitor";
	private static final boolean DEBUG = true;
	private static final boolean DEBUG_TTS = false;

	private static Timer timer = new Timer();
	private static WeatherMonitor weatherObj = new WeatherMonitor();
	private static boolean running = false;
	
	WeatherSet ws;
	GoogleWeatherHandler gwh;
	private static String weatherZip = "NA";
	
	private static String weatherCond = "NA";
	private static Integer weatherTemp = -1;
	private static String weatherHumid = "NA";
	private static String weatherWindCond = "NA";
	private static String weatherHazard = "NA";	
	
	EntityManager entityManager;
	
	
	
	


	/**
	 * Create a timer/thread to continuous run and keep the state up to date
	 * 
	 * @param interval rate at which to run the thread, in seconds
	 */
	public static void StartThread(int interval) {
		if (running == true) {
			return;
		}
		Log.i(TAG, "Start()");
		timer.schedule(weatherObj, 100, interval*1000);
		running = true;
	}

	/**
	 * Stop the thread/timer that keeps the movement state up to date
	 */
	public static void StopThread() {
		Log.i(TAG, "Stop()");
		timer.purge();
		weatherObj = new WeatherMonitor();
		running = false;
	}

	@Override
	public void run() {
		
		weatherZip = LocationMonitor.getZip();
		URL url;

		try {
			String tmpStr = null;
			String cityParamString = weatherZip;
			Log.d(TAG, "cityParamString: " + cityParamString);
			String queryString = "http://www.google.com/ig/api?weather=" + cityParamString;
			url = new URL(queryString.replace(" ", "%20"));
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			gwh = new GoogleWeatherHandler();
			xr.setContentHandler(gwh);
			xr.parse(new InputSource(url.openStream()));
			ws = gwh.getWeatherSet();
			if (ws == null)
				return;
			WeatherCurrentCondition wcc = ws.getWeatherCurrentCondition();
			
			if (wcc != null) {
				weatherTemp = null;
				Integer weatherTempInt = wcc.getTempFahrenheit();
				if (weatherTempInt != null) {
					weatherTemp = weatherTempInt;
				}
				weatherCond = wcc.getCondition();
				weatherHumid = wcc.getHumidity();
				weatherWindCond = wcc.getWindCondition();
				weatherHazard = calcHazard();
			}
		} catch (Exception e) {
			Log.e(TAG, "WeatherQueryError", e);
		}
	}

	private String calcHazard() {
		if (weatherTemp > 80 || weatherTemp < 40)
		{
			return "HIGH";
		}
		else if (weatherTemp > 80 || weatherTemp < 40)
		{
			return "MEDIUM";
		}
		else
		{
			return "LOW";
		}
	}

	public static boolean isRunning() {
		return running;
	}

	public static void setRunning(boolean running) {
		WeatherMonitor.running = running;
	}

	public static String getWeatherZip() {
		return weatherZip;
	}

	public static void setWeatherZip(String weatherZip) {
		WeatherMonitor.weatherZip = weatherZip;
	}

	public static Integer getWeatherTemp() {
		return weatherTemp;
	}

	public static void setWeatherTemp(Integer weatherTemp) {
		WeatherMonitor.weatherTemp = weatherTemp;
	}

	public static String getWeatherCond() {
		return weatherCond;
	}

	public static void setWeatherCond(String weatherCond) {
		WeatherMonitor.weatherCond = weatherCond;
	}

	public static String getWeatherHumid() {
		return weatherHumid;
	}

	public static void setWeatherHumid(String weatherHumid) {
		WeatherMonitor.weatherHumid = weatherHumid;
	}

	public static String getWeatherWindCond() {
		return weatherWindCond;
	}

	public static void setWeatherWindCond(String weatherWindCond) {
		WeatherMonitor.weatherWindCond = weatherWindCond;
	}
	
	public static String getWeatherHazard() {
		return weatherWindCond;
	}

	public static void setWeatherHazard(String weatherHazard) {
		WeatherMonitor.weatherHazard  = weatherHazard;
	}

}
