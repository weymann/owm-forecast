package de.bwm.owm;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

	public static void main(String[] args) {
		String configStr = JSONReader.readResourceFileInString("config.json");
		JSONObject config = new JSONObject(configStr);

		String query = "https://api.openweathermap.org/data/2.5/onecall?lat=" + config.getFloat("latitude") + "&lon="
				+ config.getFloat("longtitude") + "&appid=" + config.getString("appid");

		while (true) {
			String now = DateTimeUtils.getDate(System.currentTimeMillis() / 1000L);
			System.out.println(now);

			Hashtable forecastMatrix = readForecastMatrix();

			JSONObject forecast = getForecast(query);

			JSONObject currentWeather = forecast.getJSONObject(Strings.CURRENT);
			float currentClouds = currentWeather.getFloat(Strings.CLOUDS);
			System.out.println("Current Clouds: " + currentClouds);

			JSONArray hourlyForecast = forecast.getJSONArray(Strings.HOURLY);
			for (int i = 0; i < hourlyForecast.length(); i++) {
				JSONObject forecastObj = hourlyForecast.getJSONObject(i);
				long unixTime = forecastObj.getLong(Strings.DATETIME);
				String dateTime = DateTimeUtils.getDate(unixTime);
				float clouds = forecastObj.getFloat(Strings.CLOUDS);
				System.out.println("Forecast: " + clouds + " - " + dateTime);
				Vector forecastVector;
				if (forecastMatrix.get(dateTime) == null) {
					forecastVector = new Vector();
					forecastMatrix.put(dateTime, forecastVector);
				} else {
					forecastVector = (Vector) forecastMatrix.get(dateTime);
				}
				Hashtable h = new Hashtable();
				h.put(Strings.FORECAST_TIME, dateTime);
				h.put(Strings.FORECAST_QUERY, now);
				h.put(Strings.CLOUDS, clouds);
				forecastVector.add(h);
			}
			System.out.println("Forecast Matrix");
			System.out.println(new JSONObject(forecastMatrix));

			storeForecastMatrix(forecastMatrix);
			try {
				System.out.println("Wait 1h");
				Thread.sleep(60 * 60 * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static JSONObject getForecast(String query) {
		StringBuffer response = new StringBuffer();
		try {
			URL obj = new URL(query);
			// LOGGER.info("openConnection");
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setRequestMethod("GET");

			/**
			 * Prevent connection hangs when calling Bondora API. Actually happend e.g.
			 * 01.05.2019!
			 * https://stackoverflow.com/questions/28785085/how-to-prevent-hangs-on-socketinputstream-socketread0-in-java
			 */
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);

			int responseCode = con.getResponseCode();
			String responseMessage = con.getResponseMessage();
			// LOGGER.info(responseCode+"");
			// LOGGER.info(YLogger.shorten(message));
			// LOGGER.info(message);
			if (responseCode != 202 && responseCode != 200) {
				// raise error for listeners
				// handling will be done in Infrastructure
			} else {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

			}

		} catch (Throwable t) {
			t.printStackTrace();
		}
		JSONObject forecast = new JSONObject(response.toString());
		System.out.println(forecast.toString());
		return forecast;

	}

	public static Hashtable readForecastMatrix() {
		FileInputStream fis;
		try {
			fis = new FileInputStream("forecast.xml");
			XMLDecoder d = new XMLDecoder(fis);
			Hashtable h = (Hashtable) d.readObject();
			d.close();
			return h;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Hashtable();
		}
	}

	public static void storeForecastMatrix(Hashtable forecast) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("forecast.xml");
			XMLEncoder e = new XMLEncoder(fos);
			e.writeObject(forecast);
			e.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
