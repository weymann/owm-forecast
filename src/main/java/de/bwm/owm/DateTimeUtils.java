package de.bwm.owm;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtils {

	public static String getDate(long unixTime) {
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

		final String formattedDtm = Instant.ofEpochSecond(unixTime).atZone(ZoneId.of("GMT+2")).format(formatter);
//		System.out.println(formattedDtm); // => '2013-06-27 09:31:00'

		return formattedDtm;

	}
}
