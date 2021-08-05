package com.suchorski.zapbot.events.commands.store;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.events.commands.owner.CashAddCommand;
import com.suchorski.zapbot.events.commands.owner.CashSubCommand;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.social.UserCoins;
import com.suchorski.zapbot.services.commands.social.CoinsService;
import com.suchorski.zapbot.utils.CommandUtils;

@Component
public class CoinsCommand extends BotCommand {
	
	@Value("${bot.url.page}") private String url; 

	@Autowired private CoinsService coinsService;
	@Autowired private CashAddCommand coinsAddCommand;
	@Autowired private CashSubCommand coinsSubCommand;

	@PostConstruct
	public void init() {
		this.name = "moedas";
		this.help = "Coleta moedas (uma vez por hora)";
		this.aliases = new String[] { "cash", "coins" };
		this.cooldown = Constants.COOLDOWNS.FAST;
		this.cooldownScope = CooldownScope.USER;
		this.children = new Command[] { coinsAddCommand, coinsSubCommand };
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			UserCoins coins = coinsService.findById(event.getAuthor().getIdLong());
			if (coins.getLast().plus(1, ChronoUnit.HOURS).isBefore(Instant.now())) {
				CommandUtils.success(event, String.format("Você pode pegar suas moedas no site: %s/moedas ", url));
			} else {
				event.reply(String.format("%s, você só pode pegar moedas uma vez por hora!", event.getAuthor().getAsMention()));
				CommandUtils.warning(event.getMessage());
			}
		} catch (NothingFoundException e) {
			CommandUtils.error(event, e.getLocalizedMessage());
		}
	}

}
