package com.suchorski.zapbot.events.commands.random;

import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.NotEnoughCoinsException;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.social.UserCoins;
import com.suchorski.zapbot.services.commands.social.CoinsService;
import com.suchorski.zapbot.utils.CommandUtils;

@Component
public class DiceCommand extends BotCommand {

	@Autowired private CoinsService coinsService;

	@PostConstruct
	public void init() {
		this.name = "dado";
		this.help = "Joga um dado";
		this.aliases = new String[] { "dice", "rollthedice", "rtd" };
		this.cooldown = Constants.COOLDOWNS.FAST;
		this.cooldownScope = CooldownScope.USER;
		this.guildOnly = false;
		this.arguments = "[numero de lados]";
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			UserCoins coins = coinsService.findById(event.getAuthor().getIdLong());
			coinsService.subCoins(coins, Constants.COSTS.AUTO_REACT);
			int sides = 6;
			try {
				sides = Integer.parseInt(event.getArgs());				
			} catch (NumberFormatException e) {
				sides = 6;
			}
			int rolled = (new Random()).nextInt(sides) + 1;
			CommandUtils.success(event, String.format("dado rolou %d de %d (%.2f%%)", rolled, sides, (float) rolled * 100f / (float) sides));
		} catch (NotEnoughCoinsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		} catch (NothingFoundException e) {
			CommandUtils.error(event, e.getLocalizedMessage());
		}
	}

}
