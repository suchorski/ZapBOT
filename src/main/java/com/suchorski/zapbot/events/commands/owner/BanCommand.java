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
public class BanCommand extends BotCommand {

	@Autowired private GuildService guildService;
	@Autowired private UserService userService;

	@PostConstruct
	public void init() {
		this.name = "banir";
		this.help = "Bane usuários do BOT";
		this.aliases = new String[] { "ban" };
		this.arguments = "<@menção dos usuários>";
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
				userService.ban(user);
			}
			CommandUtils.success(event, "usuários banidos do BOT");
		} else {
			CommandUtils.warning(event, "você precisa mencionar os usuários a serem banidos");
		}
	}

}
