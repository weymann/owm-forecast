package de.bwm.owm;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Checks {

	public static void main(String[] args) {
		long time = 1591413360;
		// convert seconds to milliseconds
		Date date = new java.util.Date(time*1000L); 
		// the format of your date
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); 
		// give a timezone reference for formatting (see comment at the bottom)
		sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-4")); 
		String formattedDate = sdf.format(date);
		System.out.println(formattedDate);
		
		final DateTimeFormatter formatter = 
			    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

			final String formattedDtm = Instant.ofEpochSecond(time)
			        .atZone(ZoneId.of("GMT+4"))
			        .format(formatter);

			System.out.println(formattedDtm);   // => '2013-06-27 09:31:00'
	}

}
