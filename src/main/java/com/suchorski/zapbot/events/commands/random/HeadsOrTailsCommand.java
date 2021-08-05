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
public class HeadsOrTailsCommand extends BotCommand {

	@Autowired private CoinsService coinsService;

	@PostConstruct
	public void init() {
		this.name = "caraoucoroa";
		this.help = "Sorteia cara ou coroa";
		this.aliases = new String[] { "headsortails", "moeda" };
		this.cooldown = Constants.COOLDOWNS.FAST;
		this.cooldownScope = CooldownScope.USER;
		this.guildOnly = false;
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			UserCoins coins = coinsService.findById(event.getAuthor().getIdLong());
			coinsService.subCoins(coins, Constants.COSTS.AUTO_REACT);
			boolean cara = (new Random()).nextBoolean();
			CommandUtils.success(event, String.format("saiu **%s**", cara ? "cara" : "coroa"));
		} catch (NotEnoughCoinsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		} catch (NothingFoundException e) {
			CommandUtils.error(event, e.getLocalizedMessage());
		}
	}

}
