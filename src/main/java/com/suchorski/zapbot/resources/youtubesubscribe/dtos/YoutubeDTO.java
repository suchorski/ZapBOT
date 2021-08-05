package com.suchorski.zapbot.resources.youtubesubscribe.dtos;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Getter;
import lombok.Setter;

@JacksonXmlRootElement(localName = "feed")
@Getter
@Setter
public class YoutubeDTO {
	
	@JacksonXmlProperty
	private Entry entry;
	
}
