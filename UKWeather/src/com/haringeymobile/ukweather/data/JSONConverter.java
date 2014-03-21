package com.haringeymobile.ukweather.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Resources;

public class JSONConverter {

	public WeatherInformation convert(String jsonString, Resources res) {
		WeatherInformation weatherInformation = new WeatherInformation();
		JSONObject jsonObject = getJSONObject(jsonString);

		String cityName = getStringFromJSONObject(jsonObject,
				WeatherDataParameter.CITY_NAME.getId());
		LocationCoordinates locationCoordinates = getLocationCoordinates(jsonObject);
		WeatherConditions weatherConditions = getWeatherConditions(jsonObject,
				res);
		WeatherNumericParameters weatherNumericParameters = getWeatherNumericParameters(jsonObject);

		weatherInformation.setCityName(cityName);
		weatherInformation.setCoordinates(locationCoordinates);
		weatherInformation.setWeatherConditions(weatherConditions);
		weatherInformation
				.setWeatherNumericParameters(weatherNumericParameters);

		return weatherInformation;
	}

	private JSONObject getJSONObject(String jsonString) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonString);
		} catch (JSONException e) {
			throw new RuntimeException();
		}
		return jsonObject;
	}

	private String getStringFromJSONObject(JSONObject jsonObject, String id) {
		try {
			return jsonObject.getString(id);
		} catch (JSONException e) {
			throw new RuntimeException();
		}
	}

	private JSONObject getJSONObjectFromJSONObject(JSONObject parentJSONObject,
			String id) {
		try {
			return parentJSONObject.getJSONObject(id);
		} catch (JSONException e) {
			throw new RuntimeException();
		}
	}

	private int getIntFromJSONObject(JSONObject jsonObject, String id) {
		try {
			return jsonObject.getInt(id);
		} catch (JSONException e) {
			throw new RuntimeException();
		}
	}

	private double getDoubleFromJSONObject(JSONObject jsonObject, String id) {
		try {
			return jsonObject.getDouble(id);
		} catch (JSONException e) {
			throw new RuntimeException();
		}
	}

	private LocationCoordinates getLocationCoordinates(JSONObject jsonObject) {
		JSONObject coordinatesJsonObject = getJSONObjectFromJSONObject(
				jsonObject, WeatherDataParameter.COORDINATES.getId());
		double latitude = getDoubleFromJSONObject(coordinatesJsonObject,
				WeatherDataParameter.LOCATION_LATITUDE.getId());
		double longitude = getDoubleFromJSONObject(coordinatesJsonObject,
				WeatherDataParameter.LOCATION_LONGITUDE.getId());

		LocationCoordinates coordinates = new LocationCoordinates();
		coordinates.setLatitude(latitude);
		coordinates.setLongitude(longitude);
		return coordinates;
	}

	private WeatherConditions getWeatherConditions(JSONObject jsonObject,
			Resources res) {
		JSONObject weatherJsonObject = getWeatherJsonObject(jsonObject);

		String conditionName = getStringFromJSONObject(weatherJsonObject,
				WeatherDataParameter.WEATHER_MAIN.getId());
		String iconString = getStringFromJSONObject(weatherJsonObject,
				WeatherDataParameter.WEATHER_ICON.getId());

		WeatherConditions weatherConditions = new WeatherConditions();
		weatherConditions.setConditionName(conditionName);
		weatherConditions.setIcon(iconString, res);
		return weatherConditions;
	}

	private JSONObject getWeatherJsonObject(JSONObject jsonObject) {
		JSONArray weatherJsonArray = null;
		try {
			weatherJsonArray = jsonObject
					.getJSONArray(WeatherDataParameter.WEATHER_CONDITION
							.getId());
		} catch (JSONException e) {
			throw new RuntimeException();
		}
		JSONObject weatherJsonObject = null;
		try {
			weatherJsonObject = weatherJsonArray.getJSONObject(0);
		} catch (JSONException e) {
			throw new RuntimeException();
		}
		return weatherJsonObject;
	}

	private WeatherNumericParameters getWeatherNumericParameters(
			JSONObject jsonObject) {
		JSONObject weatherNumericParametersJsonObject = getJSONObjectFromJSONObject(
				jsonObject, WeatherDataParameter.MAIN_INFO.getId());
		Temperature temperature = getTemperature(weatherNumericParametersJsonObject);
		int pressure = getIntFromJSONObject(weatherNumericParametersJsonObject,
				WeatherDataParameter.PRESSURE.getId());
		int humidity = getIntFromJSONObject(weatherNumericParametersJsonObject,
				WeatherDataParameter.HUMIDITY.getId());

		WeatherNumericParameters weatherNumericParameters = new WeatherNumericParameters();
		weatherNumericParameters.setTemperature(temperature);
		weatherNumericParameters.setPressure(pressure);
		weatherNumericParameters.setHumidity(humidity);
		return weatherNumericParameters;
	}

	private Temperature getTemperature(
			JSONObject weatherNumericParametersJsonObject) {

		double mainTemperature = getDoubleFromJSONObject(
				weatherNumericParametersJsonObject,
				WeatherDataParameter.TEMPERATURE.getId());
		double minTemperature = getDoubleFromJSONObject(
				weatherNumericParametersJsonObject,
				WeatherDataParameter.TEMPERATURE_MIN.getId());
		double maxTemperature = getDoubleFromJSONObject(
				weatherNumericParametersJsonObject,
				WeatherDataParameter.TEMPERATURE_MAX.getId());

		Temperature temperature = new Temperature();
		temperature.setMain(mainTemperature);
		temperature.setMinimum(minTemperature);
		temperature.setMaximum(maxTemperature);
		return temperature;
	}

}
