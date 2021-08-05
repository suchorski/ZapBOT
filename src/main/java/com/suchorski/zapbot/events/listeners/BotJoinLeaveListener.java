package com.suchorski.zapbot.events.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suchorski.zapbot.components.Log;
import com.suchorski.zapbot.services.bot.GuildService;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Component
public class BotJoinLeaveListener extends ListenerAdapter {
	
	@Autowired private Log log;
	@Autowired private GuildService guildService;
	
	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		log.infof("Bot entrou na guilda `%s` de ID `%d`!", event.getGuild().getName(), event.getGuild().getIdLong());
		guildService.findOrCreate(event.getGuild().getIdLong());
	}
	
	@Override
	public void onGuildLeave(GuildLeaveEvent event) {
		log.infof("Bot saiu da guilda `%s` de ID `%d`!", event.getGuild().getName(), event.getGuild().getIdLong());
		guildService.deleteById(event.getGuild().getIdLong());
	}

}
