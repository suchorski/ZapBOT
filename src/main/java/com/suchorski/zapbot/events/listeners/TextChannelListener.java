package com.suchorski.zapbot.events.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.bot.TextChannelService;

import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Component
public class TextChannelListener extends ListenerAdapter {
	
	@Autowired private GuildService guildService;
	@Autowired private TextChannelService textChannelService;
	
	@Override
	public void onTextChannelCreate(TextChannelCreateEvent event) {
		textChannelService.findOrCreate(event.getChannel().getIdLong(), guildService.findOrCreate(event.getGuild().getIdLong()));
	}
	
	@Override
	public void onTextChannelDelete(TextChannelDeleteEvent event) {
		textChannelService.deleteById(event.getChannel().getIdLong());
	}

}
