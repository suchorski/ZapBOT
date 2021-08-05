package com.suchorski.zapbot.events.commands.autorole;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.autoroles.GuildAutoRoles;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.commands.autoroles.AutoRolesService;
import com.suchorski.zapbot.utils.CommandUtils;

@Component
public class AutoRoleAutoRemoveCommand extends BotCommand {

	@Autowired private GuildService serverService;
	@Autowired private AutoRolesService autoRolesService;

	@PostConstruct
	public void init() {
		this.name = "remover";
		this.help = "Remove o cargo automático";
		this.aliases = new String[] { "remove", "delete", "rem", "del" };
		this.childOnly = true;
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return serverService.findOrCreate(event.getGuild().getIdLong()).getRoles().getAdmin();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			GuildAutoRoles autoRoles = autoRolesService.findById(event.getGuild().getIdLong());
			autoRolesService.unsetAutoRole(autoRoles);
			CommandUtils.success(event, "cargo automático removido");
		} catch (NothingFoundException e) {
			CommandUtils.error(event, e.getLocalizedMessage());
		}
	}

}
