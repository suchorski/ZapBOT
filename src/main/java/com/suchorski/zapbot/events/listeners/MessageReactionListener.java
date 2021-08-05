package com.suchorski.zapbot.events.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.autoroles.GuildAutoRole;
import com.suchorski.zapbot.models.commands.autoroles.ids.GuildAutoRoleID;
import com.suchorski.zapbot.services.commands.autoroles.AutoRoleService;

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Component
public class MessageReactionListener extends ListenerAdapter {

	@Autowired private AutoRoleService autoRoleService;

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		try {
			if (event != null && !event.getUser().isBot()) {
				GuildAutoRole autoRole = autoRoleService.findById(new GuildAutoRoleID(event.getReactionEmote().getEmoji(), event.getGuild().getIdLong()));
				event.getGuild().getRolesByName(autoRole.getRole(), true).stream().findAny().ifPresentOrElse(r -> {
					event.getGuild().addRoleToMember(event.getUserIdLong(), r).queue();
				}, () -> {
					event.getReaction().removeReaction().queue();
				});
			}
		} catch (NothingFoundException e) { }
	}

	@Override
	public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
		try {
			if (event != null && !event.getUser().isBot()) {
				GuildAutoRole autoRole = autoRoleService.findById(new GuildAutoRoleID(event.getReactionEmote().getEmoji(), event.getGuild().getIdLong()));
				event.getGuild().getRolesByName(autoRole.getRole(), true).stream().findAny().ifPresentOrElse(r -> {
					event.getGuild().removeRoleFromMember(event.getUserIdLong(), r).queue();
				}, () -> {
					event.getReaction().removeReaction().queue();
				});
			}
		} catch (NothingFoundException e) { }
	}

}
