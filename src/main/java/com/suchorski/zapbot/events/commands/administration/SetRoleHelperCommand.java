package com.suchorski.zapbot.events.commands.administration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.roles.GuildRoles;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.commands.roles.RolesService;
import com.suchorski.zapbot.utils.CommandUtils;

@Component
public class SetRoleHelperCommand extends BotCommand {

	@Autowired private GuildService serverService;
	@Autowired private RolesService rolesService;

	@PostConstruct
	public void init() {
		this.name = "ajudante";
		this.help = "Define o cargo de administrador";
		this.aliases = new String[] { "helper" };
		this.arguments = "<@menção do cargo>";
		this.childOnly = true;
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return serverService.findOrCreate(event.getGuild().getIdLong()).getRoles().getAdmin();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		event.getMessage().getMentionedRoles().stream().findFirst().ifPresentOrElse(r -> {
			try {
				GuildRoles roles = rolesService.findById(event.getGuild().getIdLong());
				rolesService.setHelperRole(roles, r.getName());
				CommandUtils.success(event, "cargo definido com sucesso");
			} catch (NothingFoundException e) {
				CommandUtils.error(event, e.getLocalizedMessage());
			}
		}, () -> {			
			CommandUtils.warning(event, "você precisa mencionar o cargo a ser definido");
		});
	}

}
