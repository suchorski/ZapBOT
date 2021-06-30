package com.suchorski.zapbot.events.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suchorski.zapbot.services.bot.GuildService;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Component
public class BotJoinLeaveListener extends ListenerAdapter {
	
	@Autowired private GuildService guildService;
	
	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		guildService.findOrCreate(event.getGuild().getIdLong());
	}
	
	@Override
	public void onGuildLeave(GuildLeaveEvent event) {
		guildService.deleteById(event.getGuild().getIdLong());
	}

}
