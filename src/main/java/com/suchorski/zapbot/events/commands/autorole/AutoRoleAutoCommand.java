package com.suchorski.zapbot.events.commands.autorole;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.autoroles.GuildAutoRoles;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.commands.autoroles.AutoRolesService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

import net.dv8tion.jda.api.Permission;

@Component
public class AutoRoleAutoCommand extends BotCommand {

	@Autowired private GuildService serverService;
	@Autowired private AutoRolesService autoRolesService;
	@Autowired private AutoRoleAutoRemoveCommand autoRoleAutoRemoveCommand;

	@PostConstruct
	public void init() {
		this.name = "automatico";
		this.help = "Define o cargo que será dado automaticamente";
		this.aliases = new String[] { "auto", "automático" };
		this.childOnly = true;
		this.arguments = "<@menção do cargo>";
		this.children = new Command[] { autoRoleAutoRemoveCommand };
		addPermissions(Permission.MANAGE_ROLES);
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return serverService.findOrCreate(event.getGuild().getIdLong()).getRoles().getAdmin();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			StringBuffer role = new StringBuffer();
			CommandUtils.splitArgs(Utils.clear(event.getArgs()), role);
			event.getMessage().getMentionedRoles().stream().findAny().ifPresentOrElse(r -> {
				try {
					GuildAutoRoles autoRoles = autoRolesService.findById(event.getGuild().getIdLong());
					autoRolesService.setAutoRole(autoRoles, r.getName());
					CommandUtils.success(event, "cargo automático definido");
				} catch (NothingFoundException e) {
					CommandUtils.error(event, e.getLocalizedMessage());
				}
			}, () -> {
				CommandUtils.warning(event, "você precisa mencionar o cargo");
			});
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		}
	}

}
