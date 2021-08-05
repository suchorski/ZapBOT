package com.suchorski.zapbot.events.commands.autorole;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.autoroles.GuildAutoRole;
import com.suchorski.zapbot.models.commands.autoroles.GuildAutoRoleName;
import com.suchorski.zapbot.models.commands.autoroles.GuildAutoRoles;
import com.suchorski.zapbot.models.commands.autoroles.ids.GuildAutoRoleNameID;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.commands.autoroles.AutoRoleNameService;
import com.suchorski.zapbot.services.commands.autoroles.AutoRolesService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

@Component
public class AutoRoleAddCommand extends BotCommand {

	@Autowired private GuildService guildService;
	@Autowired private AutoRolesService autoRolesService;
	@Autowired private AutoRoleNameService autoRoleNameService;

	@PostConstruct
	public void init() {
		this.name = "adicionar";
		this.help = "Adiciona regras de cargos automáticos";
		this.aliases = new String[] { "add" };
		this.childOnly = true;
		this.arguments = "<emoji> <@menção do cargo> <nome da lista>";
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getAdmin();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			StringBuffer emoji = new StringBuffer();
			StringBuffer role = new StringBuffer();
			StringBuffer name = new StringBuffer();
			CommandUtils.splitArgs(Utils.clear(event.getArgs()), 2, emoji, role, name);
			CommandUtils.isOneEmoji(emoji.toString());
			event.getMessage().getMentionedRoles().stream().findAny().ifPresentOrElse(r -> {
				try {
					GuildAutoRoles autoRoles = autoRolesService.findById(event.getGuild().getIdLong());
					GuildAutoRoleName autoRoleName = autoRoleNameService.findOrCreate(new GuildAutoRoleNameID(name.toString(), event.getGuild().getIdLong()), autoRoles);
					GuildAutoRole autoRole = new GuildAutoRole(emoji.toString(), autoRoles, r.getName(), autoRoleName);
					autoRolesService.addAutoRole(autoRoles, autoRole);
					CommandUtils.success(event, "cargo automático adicionado");
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
