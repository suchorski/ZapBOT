package com.suchorski.zapbot.events.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suchorski.zapbot.memory.statistics.VoiceChannelUsers;
import com.suchorski.zapbot.models.bot.BotGuild;
import com.suchorski.zapbot.models.bot.BotMember;
import com.suchorski.zapbot.models.bot.BotVoiceChannel;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.bot.MemberService;
import com.suchorski.zapbot.services.bot.VoiceChannelService;

import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Component
public class VoiceChannelListener extends ListenerAdapter {

	@Autowired private GuildService guildService;
	@Autowired private MemberService memberService;
	@Autowired private VoiceChannelService voiceChannelService;

	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
		if (!event.getMember().getUser().isBot()) {
			VoiceChannelUsers.addUser(event.getMember().getIdLong());
		}
	}

	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		if (!event.getMember().getUser().isBot()) {
			int minutes = VoiceChannelUsers.delUser(event.getMember().getIdLong());
			BotGuild guild = guildService.findOrCreate(event.getGuild().getIdLong());
			BotVoiceChannel voiceChannel = voiceChannelService.findOrCreate(event.getChannelLeft().getIdLong(), guild);
			voiceChannelService.addMinutesToStatistics(voiceChannel, minutes);
			BotMember member = memberService.findOrCreate(event.getEntity().getUser().getIdLong(), event.getGuild().getIdLong());
			memberService.addMinutesToStatistics(member, minutes);
		}
	}
	
	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
		if (!event.getMember().getUser().isBot()) {
			int minutes = VoiceChannelUsers.delUser(event.getMember().getIdLong());
			BotGuild guild = guildService.findOrCreate(event.getGuild().getIdLong());
			BotVoiceChannel voiceChannel = voiceChannelService.findOrCreate(event.getChannelLeft().getIdLong(), guild);
			voiceChannelService.addMinutesToStatistics(voiceChannel, minutes);
			BotMember member = memberService.findOrCreate(event.getEntity().getUser().getIdLong(), event.getGuild().getIdLong());
			memberService.addMinutesToStatistics(member, minutes);
			VoiceChannelUsers.addUser(event.getMember().getIdLong());		
		}
	}
	
	@Override
	public void onVoiceChannelCreate(VoiceChannelCreateEvent event) {
		voiceChannelService.findOrCreate(event.getChannel().getIdLong(), guildService.findOrCreate(event.getGuild().getIdLong()));
	}
	
	@Override
	public void onVoiceChannelDelete(VoiceChannelDeleteEvent event) {
		voiceChannelService.deleteById(event.getChannel().getIdLong());
	}
	
}
