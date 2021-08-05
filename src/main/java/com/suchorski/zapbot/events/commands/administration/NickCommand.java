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

	@Autowired private GuildService guildService;

	@PostConstruct
	public void init() {
		this.name = "apelido";
		this.help = "Muda o apelido do BOT";
		this.aliases = new String[] { "nickname", "nick" };
		this.arguments = "<novo apelido>";
		addPermissions(Permission.NICKNAME_CHANGE);
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getAdmin();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			if (guildService.findOrCreate(event.getGuild().getIdLong()).isPremium()) {
			StringBuffer args = new StringBuffer();
			CommandUtils.splitArgs(Utils.clear(event.getArgs()), 0, args);
			event.getGuild().modifyNickname(event.getSelfMember(), args.toString()).queue(m -> {
				CommandUtils.success(event, "apelido alterado");
			}, m -> {
				CommandUtils.error(event.getMessage());
			});
			} else {				
				CommandUtils.warning(event, "sua guilda precisa ser premium para trocar meu apelido");
			}
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		}
	}

}
