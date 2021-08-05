package com.suchorski.zapbot.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.suchorski.zapbot.exceptions.DateTimeUtilsException;

public class DateTimeUtils {

	public static Instant parseTime(String string) throws DateTimeUtilsException {
		if (string.matches("\\d{2}/\\d{2}/\\d{4}")) {
			return parseDate(string);
		}
		if (string.matches("\\d{2}/\\d{2}/\\d{4}\\s\\d{2}:\\d{2}")) {
			return parseDateTime(string);
		}
		if (string.matches("(\\d+[smhd]\\s*)+")) {
			return parseDuration(string);
		}
		throw new DateTimeUtilsException("formatos não aceitos");
	}

	private static Instant parseDate(String string) throws DateTimeUtilsException {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			sdf.setLenient(false);
			return sdf.parse(string).toInstant();
		} catch (ParseException e) {
			throw new DateTimeUtilsException("data com formato inválido");
		}
	}

	private static Instant parseDateTime(String string) throws DateTimeUtilsException {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			sdf.setLenient(false);
			return sdf.parse(string).toInstant();
		} catch (ParseException e) {
			throw new DateTimeUtilsException("data e/ou hora com formato inválido");
		}
	}

	private static Instant parseDuration(String string) throws DateTimeUtilsException {
		try {
			Instant instant = Instant.now();
			Pattern p = Pattern.compile("(\\d+[smhd])+");
			Matcher m = p.matcher(string.replaceAll("\\s", ""));
			while (m.find()) {
				Pattern pg = Pattern.compile("(\\d+)([smhd])");
				Matcher mg = pg.matcher(m.group());
				if (mg.matches()) {
					int n = Integer.parseInt(mg.group(1));
					char t = mg.group(2).charAt(0);
					switch (t) {
					case 'd':
						instant = instant.plus(n, ChronoUnit.DAYS);
						break;
					case 'h':
						instant = instant.plus(n, ChronoUnit.HOURS);
						break;
					case 'm':
						instant = instant.plus(n, ChronoUnit.MINUTES);
						break;
					case 's':
						instant = instant.plus(n, ChronoUnit.SECONDS);
						break;
					default:
						throw new DateTimeUtilsException("tempo de duração incorreta");
					}
				} else {
					throw new DateTimeUtilsException("formato de duração incorreta");
				}
			}
			return instant;
		} catch (NumberFormatException e) {
			throw new DateTimeUtilsException("número para duração inválido");
		}
	}

}
