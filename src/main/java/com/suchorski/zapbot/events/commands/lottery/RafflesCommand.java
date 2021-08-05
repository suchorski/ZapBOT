package com.suchorski.zapbot.events.commands.lottery;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.lucky.UserRaffles;
import com.suchorski.zapbot.services.commands.lucky.RafflesService;
import com.suchorski.zapbot.utils.CommandUtils;

@Component
public class RafflesCommand extends BotCommand {

	@Autowired private RafflesService rafflesService;
	@Autowired private RafflesBuyCommand rafflesBuyCommand;

	@PostConstruct
	public void init() {
		this.name = "rifas";
		this.help = "Mostra a quantidade de rifas compradas (Sorteio todos os dias às 14h)";
		this.aliases = new String[] { "raffles" };
		this.cooldown = Constants.COOLDOWNS.SLOW;
		this.cooldownScope = CooldownScope.USER;
		this.children = new Command[] { rafflesBuyCommand };
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			UserRaffles raffles = rafflesService.findById(event.getAuthor().getIdLong());
			CommandUtils.success(event, String.format("você possui %d rifas para o próximo sorteio", raffles.getQuantity()));
		} catch (NothingFoundException e) {
			CommandUtils.error(event, e.getLocalizedMessage());
		}
	}

}
