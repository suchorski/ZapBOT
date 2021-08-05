package com.suchorski.zapbot.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of = { "date" })
public class GraphDate implements Comparable<GraphDate> {
	
	private LocalDate date;
	
	@Override
	public String toString() {
		return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

	@Override
	public int compareTo(GraphDate o) {
		return this.date.compareTo(o.getDate());
	}

}
