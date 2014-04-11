package com.haringeymobile.ukweather.data.objects;

import com.google.gson.annotations.SerializedName;
import com.haringeymobile.ukweather.R;

public class Wind {

	public enum WindSpeedMeasurementUnit {

		METERS_PER_SECOND(10,
				R.string.weather_info_wind_speed_unit_meters_per_second) {

			@Override
			double convertSpeed(double speedInMetersPerSecond) {
				return speedInMetersPerSecond;
			}

		},

		KILOMETERS_PER_HOUR(20,
				R.string.weather_info_wind_speed_unit_kilometers_per_hour) {

			@Override
			double convertSpeed(double speedInMetersPerSecond) {
				return speedInMetersPerSecond * 3600 / 1000;
			}

		},

		MILES_PER_HOUR(30, R.string.weather_info_wind_speed_unit_miles_per_hour) {

			@Override
			double convertSpeed(double speedInMetersPerSecond) {
				return speedInMetersPerSecond * 3600 / 1609.344;
			}

		};

		private int id;
		private int displayResourceId;

		private WindSpeedMeasurementUnit(int id, int displayResourceId) {
			this.id = id;
			this.displayResourceId = displayResourceId;
		}

		public int getId() {
			return id;
		}

		public int getDisplayResourceId() {
			return displayResourceId;
		}

		abstract double convertSpeed(double speedInMetersPerSecond);

		public static WindSpeedMeasurementUnit getWindSpeedMeasurementUnitById(
				int id) {
			switch (id) {
			case 10:
				return METERS_PER_SECOND;
			case 20:
				return KILOMETERS_PER_HOUR;
			case 30:
				return MILES_PER_HOUR;
			default:
				throw new IllegalArgumentException(
						"Unsupported windSpeedMeasurementUnitId: " + id);
			}
		}

	}

	@SerializedName("deg")
	private int directionInDegrees;

	@SerializedName("speed")
	private double speed;

	public int getDirectionInDegrees() {
		return directionInDegrees;
	}

	public double getSpeed(WindSpeedMeasurementUnit windSpeedMeasurementUnit) {
		return windSpeedMeasurementUnit.convertSpeed(speed);
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public void setDirectionInDegrees(int directionInDegrees) {
		this.directionInDegrees = directionInDegrees;
	}

	public int getCardinalDirectionStringResource() {
		if (directionInDegrees <= 11 || directionInDegrees >= 349) {
			return R.string.direction_n;
		} else if (directionInDegrees <= 33) {
			return R.string.direction_nne;
		} else if (directionInDegrees <= 56) {
			return R.string.direction_ne;
		} else if (directionInDegrees <= 78) {
			return R.string.direction_ene;
		} else if (directionInDegrees <= 101) {
			return R.string.direction_e;
		} else if (directionInDegrees <= 123) {
			return R.string.direction_ese;
		} else if (directionInDegrees <= 146) {
			return R.string.direction_se;
		} else if (directionInDegrees <= 168) {
			return R.string.direction_sse;
		} else if (directionInDegrees <= 191) {
			return R.string.direction_s;
		} else if (directionInDegrees <= 213) {
			return R.string.direction_ssw;
		} else if (directionInDegrees <= 236) {
			return R.string.direction_sw;
		} else if (directionInDegrees <= 258) {
			return R.string.direction_wsw;
		} else if (directionInDegrees <= 281) {
			return R.string.direction_w;
		} else if (directionInDegrees <= 303) {
			return R.string.direction_wnw;
		} else if (directionInDegrees <= 326) {
			return R.string.direction_nw;
		} else {
			return R.string.direction_nnw;
		}
	}

}
