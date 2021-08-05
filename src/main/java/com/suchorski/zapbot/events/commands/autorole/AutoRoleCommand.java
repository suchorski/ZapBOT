package com.suchorski.zapbot.events.commands.autorole;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.autoroles.GuildAutoRoleName;
import com.suchorski.zapbot.models.commands.autoroles.ids.GuildAutoRoleNameID;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.commands.autoroles.AutoRoleNameService;
import com.suchorski.zapbot.utils.CommandUtils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

@Component
public class AutoRoleCommand extends BotCommand {

	@Autowired private GuildService guildService;
	@Autowired private AutoRoleNameService autoRoleNameService;
	@Autowired private AutoRoleAutoCommand autoRoleAutoCommand;
	@Autowired private AutoRoleAddCommand autoRoleAddCommand;
	@Autowired private AutoRoleDelCommand autoRoleDelCommand;
	@Autowired private AutoRoleListCommand autoRoleListCommand;

	@PostConstruct
	public void init() {
		this.name = "cargos";
		this.help = "Mostra a mensagem de cargos automáticos";
		this.aliases = new String[] { "autorole" };
		this.arguments = "[nome da lista]";
		this.children = new Command[] { autoRoleAutoCommand, autoRoleAddCommand, autoRoleDelCommand, autoRoleListCommand };
		addPermissions(Permission.MANAGE_ROLES);
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getAdmin();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			String name = event.getArgs().trim();
			if (name.isEmpty()) {
				List<GuildAutoRoleName> autoRoleNames = autoRoleNameService.findAllFromGuildId(event.getGuild().getIdLong());
				StringBuilder sb = new StringBuilder();
				sb.append("Lista de cargos disponíveis:");
				for (GuildAutoRoleName n : autoRoleNames) {
					sb.append(String.format(" `%s`", n.getId().getName()));
				}
				CommandUtils.success(event, sb.toString());
			} else {
				GuildAutoRoleName autoRoleName = autoRoleNameService.findById(new GuildAutoRoleNameID(name.toString(), event.getGuild().getIdLong()));
				if (autoRoleName.getAutoRoles().getAutoRoles().size() > 0) {
					EmbedBuilder builder = new EmbedBuilder();
					builder.setColor(Constants.COLORS.DEFAULT);
					builder.setDescription("Lista de regras para cargos automáticos:");
					autoRoleName.getAutoRoles().getAutoRoles().forEach(i -> builder.addField(i.getId().getEmoji(), i.getRole(), true));
					event.reply(builder.build(), m -> {
						autoRoleName.getAutoRoles().getAutoRoles().forEach(i -> m.addReaction(i.getId().getEmoji()).queue());
						autoRoleNameService.setMessageId(autoRoleName, m.getIdLong());
						CommandUtils.success(event.getMessage());
					}, m -> {
						CommandUtils.error(event.getMessage());				
					});
				} else {
					CommandUtils.success(event, "não existem cargos configurados");
				}
			}
		} catch (NothingFoundException e) {
			CommandUtils.error(event, e.getLocalizedMessage());				
		}
	}

}
