package com.suchorski.zapbot.events.commands.statistics;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.models.bot.BotGuild;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.GraphGenerator;

import net.dv8tion.jda.api.Permission;

@Component
public class StatisticsGuildCommand extends BotCommand {

	@Autowired private GuildService guildService;

	@PostConstruct
	public void init() {
		this.name = "guilda";
		this.help = "Mostra as estatísticas de entrada e saída de usuários";
		this.aliases = new String[] { "guild", "server", "servidor" };
		this.cooldown = Constants.COOLDOWNS.SLOW;
		this.cooldownScope = CooldownScope.CHANNEL;
		this.childOnly = true;
		addPermissions(Permission.MESSAGE_ATTACH_FILES);
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getHelper();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			BotGuild guild = guildService.findOrCreate(event.getGuild().getIdLong());
			event.getChannel().sendFile(GraphGenerator.generateGraph(String.format("Estatísticas de '%s'", event.getGuild().getName()), guild.getGuildStatistics(), !guild.isPremium()), "graph.png").queue(m -> {
				CommandUtils.success(event.getMessage());
			}, m -> {
				CommandUtils.error(event.getMessage());
			});
		} catch (IOException e) {
			CommandUtils.error(event, e.getLocalizedMessage());
		}
	}

}
