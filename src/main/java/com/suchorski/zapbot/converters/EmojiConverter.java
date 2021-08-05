package com.suchorski.zapbot.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class EmojiConverter implements AttributeConverter<String, Long> {

	@Override
	public Long convertToDatabaseColumn(String attribute) {
		if (attribute.codePoints().count() == 2L) {
			return ((long) attribute.codePointAt(0) << 32L) + (long) attribute.codePointAt(1);
		}
		return (long) attribute.codePointAt(0);
	}

	@Override
	public String convertToEntityAttribute(Long dbData) {
		if (dbData == null) {
			return null;
		}
		int[] emoji = new int[2];
		emoji[0] = (int) ((dbData & 0xFFFFFFFF00000000L) >> 32);
		emoji[1] = (int) (dbData & 0xFFFFFFFFL);
		if (emoji[0] == 0) {
			return new String(emoji, 1, 1);
		}
		return new String(emoji, 0, 2); 
	}

}
