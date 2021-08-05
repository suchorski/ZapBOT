package com.suchorski.zapbot.events.commands.premium.guild;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.exceptions.NotEnoughCoinsException;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.bot.BotGuild;
import com.suchorski.zapbot.models.commands.social.UserCoins;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.commands.social.CoinsService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

@Component
public class DonateCommand extends BotCommand {

	@Autowired private GuildService guildService;
	@Autowired private CoinsService coinsService;

	@PostConstruct
	public void init() {
		this.name = "doar";
		this.help = "Doa moedas para que a guilda torne-se premium";
		this.aliases = new String[] { "donate" };
		this.arguments = "<quantidade>";
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			StringBuffer quantityArg = new StringBuffer();
			CommandUtils.splitArgs(Utils.clear(event.getArgs()), quantityArg);
			long quantity = Long.parseLong(quantityArg.toString());
			UserCoins userCoins = coinsService.findById(event.getAuthor().getIdLong());
			BotGuild guild = guildService.findOrCreate(event.getGuild().getIdLong());
			guildService.donate(guild, userCoins, quantity);
			CommandUtils.success(event, String.format("%d %s doadas para a guilda", quantity, quantity != 1 ? "moedas" : "moeda"));
		} catch (NumberFormatException e) {
			CommandUtils.warning(event, "número inválido");
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());				
		} catch (NothingFoundException e) {
			CommandUtils.error(event, e.getLocalizedMessage());					
		} catch (NotEnoughCoinsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());				
		}
	}

}
