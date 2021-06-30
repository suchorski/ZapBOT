package com.suchorski.zapbot.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.suchorski.zapbot.constants.Constants;

@Component
public class Log {
	
	@Value("${bot.log.channel.id}") private Long logChannelId;
	
	@Autowired private Bot bot;
	
	private void sendLog(String unicode, String message) {
		bot.getJda().getTextChannelById(logChannelId).sendMessage(String.format("%s %s", unicode, message)).queue();
	}
	
	public void infof(String message, Object... args) {
		sendLog(Constants.EMOJIS.INFO, String.format(message, args));
	}
	
	public void warnf(String message, Object... args) {
		sendLog(Constants.EMOJIS.WARNING, String.format(message, args));
	}
	
	public void dangerf(String message, Object... args) {
		sendLog(Constants.EMOJIS.ERROR, String.format(message, args));
	}
	
}
