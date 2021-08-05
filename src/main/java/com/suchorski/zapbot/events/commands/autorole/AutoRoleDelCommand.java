package com.suchorski.zapbot.events.commands.autorole;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.autoroles.GuildAutoRoles;
import com.suchorski.zapbot.models.commands.autoroles.ids.GuildAutoRoleID;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.commands.autoroles.AutoRoleService;
import com.suchorski.zapbot.services.commands.autoroles.AutoRolesService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

@Component
public class AutoRoleDelCommand extends BotCommand {

	@Autowired private GuildService serverService;
	@Autowired private AutoRolesService autoRolesService;
	@Autowired private AutoRoleService autoRoleService;

	@PostConstruct
	public void init() {
		this.name = "remover";
		this.help = "Remove regras de cargos automáticos";
		this.aliases = new String[] { "del", "remove", "remover" };
		this.childOnly = true;
		this.arguments = "<emoji>";
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return serverService.findOrCreate(event.getGuild().getIdLong()).getRoles().getAdmin();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			StringBuffer emoji = new StringBuffer();
			CommandUtils.splitArgs(Utils.clear(event.getArgs()), emoji);
			GuildAutoRoles autoRoles = autoRolesService.findById(event.getGuild().getIdLong());
			autoRoles.delAutoRole(autoRoleService.findById(new GuildAutoRoleID(emoji.toString(), event.getGuild().getIdLong())));
			autoRolesService.update(autoRoles);
			CommandUtils.success(event, "cargo automático removido");				
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());				
		} catch (NothingFoundException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());				
		}
	}

}
