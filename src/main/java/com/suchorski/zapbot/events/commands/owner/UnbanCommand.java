package com.suchorski.zapbot.events.commands.owner;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.models.bot.BotUser;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.bot.UserService;
import com.suchorski.zapbot.utils.CommandUtils;

import net.dv8tion.jda.api.entities.User;

@Component
public class UnbanCommand extends BotCommand {

	@Autowired private GuildService guildService;
	@Autowired private UserService userService;

	@PostConstruct
	public void init() {
		this.name = "desbanir";
		this.help = "Desbane usuários do BOT";
		this.aliases = new String[] { "unban" };
		this.arguments = "<menção dos usuários>";
		this.ownerCommand = true;
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getAdmin();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		List<User> users = event.getMessage().getMentionedUsers();
		if (!users.isEmpty()) {
			for (User u : users) {
				BotUser user = userService.findOrCreate(u.getIdLong());
				userService.unban(user);
			}
			CommandUtils.success(event, "usuários desbanidos do BOT");
		} else {
			CommandUtils.warning(event, "você precisa mencionar os usuários a serem desbanidos");
		}
	}

}
