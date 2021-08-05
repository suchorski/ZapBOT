package com.suchorski.zapbot.events.commands.social;

import java.util.Set;
import java.util.StringJoiner;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.models.bot.BotUser;
import com.suchorski.zapbot.models.commands.social.UserProfileBackground;
import com.suchorski.zapbot.services.bot.UserService;
import com.suchorski.zapbot.utils.CommandUtils;

@Component
public class BackgroundListCommand extends BotCommand {

	@Autowired private UserService userService;

	@PostConstruct
	public void init() {
		this.name = "lista";
		this.help = "Lista os fundos de perfis adquiridos";
		this.aliases = new String[] { "list", "acquired", "comprados" };
		this.childOnly = true;
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		BotUser user = userService.findOrCreate(event.getAuthor().getIdLong());
		Set<UserProfileBackground> profileBackgrounds = user.getBackgrounds();
		StringJoiner joiner = new StringJoiner(" - ");
		profileBackgrounds.forEach(b -> joiner.add(String.format("%s [%d]", b.getName(), b.getId())));
		CommandUtils.success(event, String.format("seus fundos de perfil são: %s", joiner.toString()));
	}

}
