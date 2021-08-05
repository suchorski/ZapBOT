package com.suchorski.zapbot.events.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suchorski.zapbot.components.Bot;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.models.bot.BotMember;
import com.suchorski.zapbot.models.bot.ids.BotMemberID;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.bot.MemberService;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Component
public class MemberJoinLeaveListener extends ListenerAdapter {

	@Autowired private Bot bot;
	@Autowired private GuildService guildService;
	@Autowired private MemberService memberService;

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		BotMember member = memberService.findOrCreate(event.getUser().getIdLong(), event.getGuild().getIdLong());
		if (!event.getUser().isBot()) {
			guildService.addMemberJoinedToStatistics(guildService.findOrCreate(event.getGuild().getIdLong()));
			if (member.getGuild().getAutoRoles().getAutoRole() != null) {
				event.getGuild().getRolesByName(member.getGuild().getAutoRoles().getAutoRole(), true).stream().findAny().ifPresent(r -> {
					event.getGuild().addRoleToMember(event.getUser().getIdLong(), r).queue();
				});
			}
			if (member.getGuild().getAnnounce().getChannelId() != null) {
				EmbedBuilder builder = new EmbedBuilder();
				builder.setColor(Constants.COLORS.DEFAULT);
				builder.setThumbnail(event.getUser().getEffectiveAvatarUrl());
				builder.setAuthor(String.format(member.getGuild().getAnnounce().getAuthor(), event.getUser().getName(), event.getUser().getDiscriminator()));
				builder.setDescription(String.format(member.getGuild().getAnnounce().getDescription(), event.getUser().getAsMention()));
				builder.setFooter(String.format(member.getGuild().getAnnounce().getFooter(), event.getMember().getEffectiveName()));
				member.getGuild().getAnnounce().getFields().forEach(f -> {
					builder.addField(f.getId().getTitle(), f.getDescription(), true);
				});
				bot.getJda().getTextChannelById(member.getGuild().getAnnounce().getChannelId()).sendMessage(builder.build()).queue();
			}
		}
	}
	
	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
		memberService.deleteByIdWithoutException(new BotMemberID(event.getUser().getIdLong(), event.getGuild().getIdLong()));
		if (!event.getUser().isBot()) {
			guildService.addMemberLeftToStatistics(guildService.findOrCreate(event.getGuild().getIdLong()));
		}
	}

}
