package com.suchorski.zapbot.events.abstracts;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public abstract class CommandTranslation extends Command {
	
	public CommandTranslation() {
		this.help = "Sem ajuda disponível";
		this.botMissingPermMessage = "%s Eu preciso da permissão `%s`!";
	}
	
	@Override
	public String getCooldownError(CommandEvent event, int remaining) {
		return remaining <= 0 ? null : String.format("%s Comando bloqueado por mais %d %s!", event.getClient().getWarning(), remaining, remaining != 1 ? "segundos" : "segundo");
	}

}
