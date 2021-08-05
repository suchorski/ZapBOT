package com.suchorski.zapbot.events.commands.owner;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.components.Log;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;

@Component
public class ShutdownCommand extends BotCommand {
	
	@Autowired private Log log;
	
	@PostConstruct
	public void init() {
		this.name = "desligar";
		this.help = "Desliga o BOT com segurança";
		this.aliases = new String[] { "shutdown" };
		this.guildOnly = false;
		this.ownerCommand = true;
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		log.warnf("Desligando! executado por: %s", event.getAuthor().getAsMention());
		event.getMessage().clearReactions().complete();
		event.getMessage().addReaction(Constants.EMOJIS.WARNING).complete();
		event.getJDA().shutdown();
		System.exit(0);
	}

}
