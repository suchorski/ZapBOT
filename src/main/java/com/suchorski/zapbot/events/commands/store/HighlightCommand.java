package com.suchorski.zapbot.events.commands.store;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.exceptions.NotEnoughCoinsException;
import com.suchorski.zapbot.models.bot.BotUser;
import com.suchorski.zapbot.services.bot.UserService;
import com.suchorski.zapbot.services.commands.social.CoinsService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

@Component
public class HighlightCommand extends BotCommand {

	@Autowired private UserService userService;
	@Autowired private CoinsService coinsService;

	@PostConstruct
	public void init() {
		this.name = "destacar";
		this.help = String.format("Destaca uma frase (Moedas: %d)", Constants.COSTS.HIGHLIGHT);
		this.aliases = new String[] { "highlight" };
		this.cooldown = Constants.COOLDOWNS.SLOWER;
		this.cooldownScope = CooldownScope.USER;
		this.arguments = "<mensagem a ser destacada>";
		addPermissions(Permission.MESSAGE_MANAGE);
	}
	
	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			StringBuffer message = new StringBuffer();
			CommandUtils.splitArgs(Utils.clear(event.getArgs()), 0, message);
			BotUser user = userService.findOrCreate(event.getAuthor().getIdLong());
			coinsService.subCoins(user.getCoins(), Constants.COSTS.HIGHLIGHT);
			EmbedBuilder builder = new EmbedBuilder();
			builder.setColor(event.getMember().getColor());
			builder.setAuthor(message.toString());
			builder.setDescription(String.format("diz, %s!", event.getAuthor().getAsMention()));
			event.reply(builder.build(), m -> {
				event.getMessage().delete().queue();					
			}, m -> {
				CommandUtils.error(event.getMessage());
			});
		} catch (NotEnoughCoinsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		}
	}

}
