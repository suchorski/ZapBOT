package com.suchorski.zapbot.events.commands.administration;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.models.bot.BotGuild;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

@Component
public class AutoReactServerCommand extends BotCommand {

	@Autowired private GuildService serverService;

	@PostConstruct
	public void init() {
		this.name = "configurar";
		this.help = "Habilita ou desabilita a auto reação no servidor";
		this.aliases = new String[] { "configurate", "config", "conf" };
		this.cooldown = Constants.COOLDOWNS.FAST;
		this.cooldownScope = CooldownScope.USER;
		this.arguments = "[verdadeiro ou falso]";
		this.childOnly = true;
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return serverService.findOrCreate(event.getGuild().getIdLong()).getRoles().getOperator();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		BotGuild guild = serverService.findOrCreate(event.getGuild().getIdLong());
		try {
			List<String> on = Arrays.asList("verdadeiro", "true", "1", "on", "ligado");
			List<String> off = Arrays.asList("falso", "false", "0", "off", "desligado");
			StringBuffer status = new StringBuffer();
			CommandUtils.splitArgs(Utils.clear(event.getArgs()), status);
			if (on.contains(status.toString())) {
				serverService.setAutoReact(guild, true);
				CommandUtils.success(event, "auto reação no servidor está **habilitada**");
			} else if (off.contains(status.toString())) {
				serverService.setAutoReact(guild, false);
				CommandUtils.success(event, "auto reação no servidor está **desabilitada**");
			} else {
				CommandUtils.warning(event, "uso incorreto");
			}
		} catch (CommandUtilsException e) {
			serverService.switchAutoReact(guild);
			CommandUtils.success(event, String.format("auto reação no servidor está **%s**", guild.getAutoReact() ? "habilitada" : "desabilitada"));
		}
	}

}
