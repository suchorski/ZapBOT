package com.suchorski.zapbot.models.interfaces.statistics;

import java.util.List;

import com.suchorski.zapbot.utils.GraphDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public interface StatisticGraph extends Comparable<StatisticGraph> {
	
	public GraphDate getDate();
	
	public List<Statistic> getStatistics();
	
	@Getter
	@Setter
	@AllArgsConstructor
	public class Statistic {
		
		private String category;
		private double value;
		
	}
	
	@Override
	default int compareTo(StatisticGraph o) {
		return this.getDate().compareTo(o.getDate());
	}

}
