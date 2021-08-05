package com.suchorski.zapbot.events.commands.owner;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.components.Log;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.exceptions.NotEnoughCoinsException;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.social.UserCoins;
import com.suchorski.zapbot.services.commands.social.CoinsService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

@Component
public class CashSubCommand extends BotCommand {

	@Autowired private Log log;
	@Autowired private CoinsService coinsService;

	@PostConstruct
	public void init() {
		this.name = "remover";
		this.help = "Remove moedas de um usuário";
		this.aliases = new String[] { "rem", "del" };
		this.arguments = "<quantidade> <menção ao usuário>";
		this.ownerCommand = true;
		this.childOnly = true;
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			StringBuffer quantityArg = new StringBuffer();
			StringBuffer userArg = new StringBuffer();
			CommandUtils.splitArgs(Utils.clear(event.getArgs()), quantityArg, userArg);
			long quantity = Long.parseLong(quantityArg.toString());
			event.getMessage().getMentionedUsers().stream().findFirst().ifPresentOrElse(u -> {				
				try {
					UserCoins coins = coinsService.findById(u.getIdLong());
					coinsService.subCoins(coins, quantity);
					log.infof("%d %s removidas ao %s", quantity, quantity != 1 ? "moedas" : "moeda", u.getAsMention());
					CommandUtils.success(event, String.format("%d %s removidas", quantity, quantity != 1 ? "moedas" : "moeda"));
				} catch (NotEnoughCoinsException e) {
					CommandUtils.warning(event, e.getLocalizedMessage());
				} catch (NothingFoundException e) {
					CommandUtils.error(event, e.getLocalizedMessage());
				}
			}, () -> {
				CommandUtils.warning(event, "você precisa mecionar o usuário que receberá as moedas");				
			});
		} catch (NumberFormatException e) {
			CommandUtils.warning(event, "número inválido");
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());				
		}
	}

}
