package com.suchorski.zapbot.events.commands.lottery;

import java.util.Arrays;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.ButtonMenu.Builder;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.NotEnoughCoinsException;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.social.UserCoins;
import com.suchorski.zapbot.services.commands.social.CoinsService;
import com.suchorski.zapbot.utils.CommandUtils;

@Component
public class ScratchoffCommand extends BotCommand {

	@Autowired private EventWaiter eventWaiter;
	@Autowired private CoinsService coinsService;

	@PostConstruct
	public void init() {
		this.name = "raspadinha";
		this.help = String.format("Compra uma raspadinha (Moedas: %d)", Constants.COSTS.SCRATCH_OFF);
		this.aliases = new String[] { "scratch", "scratchoff" };
		this.cooldown = Constants.COOLDOWNS.SLOW;
		this.cooldownScope = CooldownScope.USER;
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			UserCoins coins = coinsService.findById(event.getAuthor().getIdLong());
			coinsService.subCoins(coins, Constants.COSTS.HIGHLIGHT);
			Builder builder = new Builder();
			builder.setEventWaiter(eventWaiter);
			builder.setColor(event.getMember().getColor());
			builder.setText(String.format("%s, contém 1 prêmio de 1000 moedas e 2 prêmios de 300 moedas!", event.getAuthor().getAsMention()));
			builder.setDescription("Selecione um número para raspar.");
			for (int i = 0; i < Constants.NUMBERS.length; ++i) {
				builder.addChoice(Constants.NUMBERS[i]);
			}
			builder.addUsers(event.getAuthor());
			builder.setAction(e -> {
				int selected = Arrays.asList(Constants.NUMBERS).indexOf(e.getEmoji());
				if (selected == -1) {					
					selected = (new Random()).nextInt(Constants.NUMBERS.length);
				}
				int rolled = (new Random()).nextInt(Constants.NUMBERS.length - 1) + 1;
				int prize = 0;
				if (selected == rolled) {
					prize = 1000;
				} else if (selected == (rolled - 1) || selected == (rolled + 1)) {
					prize = 300;
				}
				if (prize > 0) {
					event.reply(String.format("%s, parabéns. Você ganhou %d moedas!", event.getAuthor().getAsMention(), prize));
				} else {
					event.reply(String.format("%s, infelizmente não foi desta vez!", event.getAuthor().getAsMention()));
				}
			});
			builder.build().display(event.getChannel());
			CommandUtils.success(event.getMessage());
		} catch (NotEnoughCoinsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		} catch (NothingFoundException e) {
			CommandUtils.error(event, e.getLocalizedMessage());
		}
	}
	
}
