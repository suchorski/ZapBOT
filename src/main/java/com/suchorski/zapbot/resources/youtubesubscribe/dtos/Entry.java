package com.suchorski.zapbot.resources.youtubesubscribe.dtos;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Entry {
	
	private static final String YOUTUBE_URL = "http://www.youtube.com/watch?v=";
	
	@JacksonXmlProperty
	private String title;
	
	@JacksonXmlProperty(namespace = "yt")
	private String videoId;
		
	@JacksonXmlProperty(namespace = "yt")
	private String channelId;
	
	public String getUrl() {
		return String.format("%s%s", YOUTUBE_URL, videoId);
	}

}
