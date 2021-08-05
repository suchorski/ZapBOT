package com.suchorski.zapbot.events.commands.statistics;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.utils.CommandUtils;

@Component
public class StatisticsTopCommand extends BotCommand {

	@Autowired private GuildService guildService;
	@Autowired private StatisticsTopMembersCommand statisticsTopUsersCommand;
	@Autowired private StatisticsTopTextCommand statisticsTopTextCommand;
	@Autowired private StatisticsTopVoiceCommand statisticsTopVoiceCommand;

	@PostConstruct
	public void init() {
		this.name = "top";
		this.help = "Mostra o top 3 das estatísticas";
		this.aliases = new String[] { "best", "melhores" };
		this.cooldown = Constants.COOLDOWNS.SLOW;
		this.cooldownScope = CooldownScope.CHANNEL;
		this.arguments = "[membros | texto | voz]";
		this.childOnly = true;
		this.children = new Command[] { statisticsTopUsersCommand, statisticsTopTextCommand, statisticsTopVoiceCommand };
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getHelper();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		CommandUtils.success(event, "os subcomandos disponíveis são: `membros`, `texto` ou `voz`");
	}

}
