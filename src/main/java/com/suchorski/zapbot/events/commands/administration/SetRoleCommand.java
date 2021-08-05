package com.suchorski.zapbot.events.commands.administration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.roles.GuildRoles;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.commands.roles.RolesService;
import com.suchorski.zapbot.utils.CommandUtils;

import net.dv8tion.jda.api.EmbedBuilder;

@Component
public class SetRoleCommand extends BotCommand {

	@Autowired private GuildService serverService;
	@Autowired private RolesService rolesService;
	@Autowired private SetRoleAdministratorCommand setRoleAdministratorCommand;
	@Autowired private SetRoleOperatorCommand setRoleOperatorCommand;
	@Autowired private SetRoleHelperCommand setRoleHelperCommand;

	@PostConstruct
	public void init() {
		this.name = "cargo";
		this.help = "Lista ou define o cargo do servidor";
		this.aliases = new String[] { "role" };
		this.arguments = "[tipo do cargo <@menção do cargo>]";
		this.children = new Command[] { setRoleAdministratorCommand, setRoleOperatorCommand, setRoleHelperCommand };
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return serverService.findOrCreate(event.getGuild().getIdLong()).getRoles().getAdmin();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			GuildRoles roles = rolesService.findById(event.getGuild().getIdLong());
			String aRole = event.getGuild().getRolesByName(roles.getAdmin(), true).stream().findFirst().map(r -> r.getAsMention()).orElse("Não definido");
			String oRole = event.getGuild().getRolesByName(roles.getOperator(), true).stream().findFirst().map(r -> r.getAsMention()).orElse("Não definido");
			String hRole = event.getGuild().getRolesByName(roles.getHelper(), true).stream().findFirst().map(r -> r.getAsMention()).orElse("Não definido");
			EmbedBuilder builder = new EmbedBuilder();
			builder.setColor(Constants.COLORS.DEFAULT);
			builder.setDescription(String.format("%s, lista de cargos do bot!", event.getAuthor().getAsMention()));
			builder.addField("Administrador", aRole, true);
			builder.addField("Operador", oRole, true);
			builder.addField("Ajudante", hRole, true);
			event.reply(builder.build(), m -> {
				CommandUtils.success(event.getMessage());
			}, m-> {
				CommandUtils.error(event.getMessage());
			});
		} catch (NothingFoundException e) {
			CommandUtils.error(event, e.getLocalizedMessage());
		}
	}

}
