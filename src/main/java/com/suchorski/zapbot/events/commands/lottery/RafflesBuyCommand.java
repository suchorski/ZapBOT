package com.suchorski.zapbot.events.commands.lottery;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.components.Log;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.exceptions.NotEnoughCoinsException;
import com.suchorski.zapbot.models.bot.BotUser;
import com.suchorski.zapbot.services.bot.UserService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

@Component
public class RafflesBuyCommand extends BotCommand {

	@Autowired private Log log;
	@Autowired private UserService userService;

	@PostConstruct
	public void init() {
		this.name = "comprar";
		this.help = String.format("Compra rifas para o próximo sorteio (Moedas: %d/rifa)", Constants.COSTS.RAFFLE);
		this.aliases = new String[] { "buy" };
		this.cooldown = Constants.COOLDOWNS.SLOW;
		this.cooldownScope = CooldownScope.USER;
		this.arguments = "[quantidade]";
		this.childOnly = true;
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			StringBuffer arg = new StringBuffer();
			CommandUtils.splitArgs(Utils.clear(event.getArgs()), arg);
			int quantity = Integer.parseInt(arg.toString());
			BotUser user = userService.findOrCreate(event.getAuthor().getIdLong());
			userService.buyRaffles(user, quantity);
			log.infof("%s comprou %d rifas", event.getAuthor().getAsMention(), quantity);
			CommandUtils.success(event, String.format("você comprou %d rifas para o próximo sorteio", quantity));
		} catch (NumberFormatException e) {
			CommandUtils.warning(event, "número inválido");
		} catch (NotEnoughCoinsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		}
	}
	
}
