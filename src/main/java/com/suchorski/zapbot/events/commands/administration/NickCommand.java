package com.suchorski.zapbot.events.commands.administration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

import net.dv8tion.jda.api.Permission;

@Component
public class NickCommand extends BotCommand {

	@Autowired private GuildService guidlService;

	@PostConstruct
	public void init() {
		this.name = "apelido";
		this.help = "Muda o apelido do BOT";
		this.aliases = new String[] { "nickname", "nick" };
		this.arguments = "<novo apelido>";
		this.botPermissions = new Permission[] { Permission.MESSAGE_ADD_REACTION };
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return guidlService.findOrCreate(event.getGuild().getIdLong()).getRoles().getAdmin();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			StringBuffer args = new StringBuffer();
			CommandUtils.splitArgs(Utils.clear(event.getArgs()), 0, args);
			event.getGuild().modifyNickname(event.getSelfMember(), args.toString()).queue(m -> {
				CommandUtils.success(event, "apelido alterado");
			}, m -> {
				CommandUtils.error(event.getMessage());
			});
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		}
	}

}
