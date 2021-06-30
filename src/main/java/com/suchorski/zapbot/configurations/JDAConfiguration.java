package com.suchorski.zapbot.configurations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.suchorski.zapbot.events.abstracts.BotCommand;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Configuration
public class JDAConfiguration {
	
	@Bean
	public EventWaiter eventWaiter() {
		return new EventWaiter();
	}

	@Bean
	public List<ListenerAdapter> listenerAdapters() {
		return new ArrayList<ListenerAdapter>();
	}
	
	@Bean
	public List<BotCommand> commands() {
		return new ArrayList<BotCommand>();
	}

}
