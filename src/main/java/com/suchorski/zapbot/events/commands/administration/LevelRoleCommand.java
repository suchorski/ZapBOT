package com.suchorski.zapbot.events.commands.administration;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.models.commands.social.GuildLevelRole;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.commands.social.LevelRoleService;
import com.suchorski.zapbot.utils.CommandUtils;

import net.dv8tion.jda.api.EmbedBuilder;

@Component
public class LevelRoleCommand extends BotCommand {

	@Autowired private GuildService guildService;
	@Autowired private LevelRoleService levelRoleService;
	@Autowired private LevelRoleAddCommand levelRoleAddCommand;
	@Autowired private LevelRoleDelCommand levelRoleDelCommand;

	@PostConstruct
	public void init() {
		this.name = "niveis";
		this.help = "Lista os níveis de experiência";
		this.aliases = new String[] { "levels" };
		this.children = new Command[] { levelRoleAddCommand, levelRoleDelCommand };
	}
	
	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getAdmin();
	}
	
	@Override
	protected void zapExecute(CommandEvent event) {
		List<GuildLevelRole> levelRoles = levelRoleService.findAllFromGuild(event.getGuild().getIdLong());
		if (levelRoles.size() > 0) {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setColor(Constants.COLORS.DEFAULT);
			builder.setDescription("Lista de cargos para os níveis de experiência:");
			levelRoles.forEach(i -> builder.addField(i.getRole(), String.format("%d", i.getId().getLevel()), true));
			event.reply(builder.build(), m -> {
				CommandUtils.success(event.getMessage());
			}, m -> {
				CommandUtils.error(event.getMessage());
			});
		} else {
			CommandUtils.success(event, "não existem níveis configurados");
		}
	}

}
