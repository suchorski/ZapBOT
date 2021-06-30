package com.suchorski.zapbot.converters;

import java.awt.Color;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ColorConverter implements AttributeConverter<Color, Integer> {

	@Override
	public Integer convertToDatabaseColumn(Color attribute) {
		int r = (attribute.getRed() << 16) & 0x00FF0000;
		int g = (attribute.getGreen() << 8) & 0x0000FF00;
		int b = attribute.getBlue() & 0x000000FF;
		return  0xFF000000 | r | g | b;
	}

	@Override
	public Color convertToEntityAttribute(Integer dbData) {
		return new Color(dbData);
	}

}
