package com.suchorski.zapbot.events.commands.store;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.events.commands.administration.AutoReactServerCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.social.UserAutoReact;
import com.suchorski.zapbot.services.commands.social.AutoReactService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

@Component
public class AutoReactCommand extends BotCommand {

	@Autowired private AutoReactService autoReactService;
	@Autowired private AutoReactServerCommand autoReactServerCommand;

	@PostConstruct
	public void init() {
		this.name = "autoreagir";
		this.help = String.format("Bot reage com um coração (Moedas: %d/frase)", Constants.COSTS.AUTO_REACT);
		this.aliases = new String[] { "ar" };
		this.cooldown = Constants.COOLDOWNS.FAST;
		this.cooldownScope = CooldownScope.USER;
		this.arguments = "[emoji]";
		this.children = new Command[] { autoReactServerCommand };
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			UserAutoReact autoReact = autoReactService.findById(event.getAuthor().getIdLong());
			try {
				StringBuffer arg = new StringBuffer();
				CommandUtils.splitArgs(Utils.clear(event.getArgs()), arg);
				String emoji = arg.toString();
				CommandUtils.isOneEmoji(emoji.toString());
				autoReactService.enableAutoReact(autoReact, emoji);
				CommandUtils.success(event, String.format("você **%s** recebendo reações automaticamente", autoReact.getEnabled() ? "está" : "não está"));
			} catch (CommandUtilsException e) {
				autoReactService.switchAutoReact(autoReact);
				CommandUtils.success(event, String.format("você **%s** recebendo reações automaticamente", autoReact.getEnabled() ? "está" : "não está"));
			}
		} catch (NothingFoundException e1) {
			CommandUtils.error(event, e1.getLocalizedMessage());
		}
	}

}
