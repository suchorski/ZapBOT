package com.suchorski.zapbot.events.commands.social;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.models.commands.social.UserCoins;
import com.suchorski.zapbot.services.commands.social.CoinsService;
import com.suchorski.zapbot.utils.CommandUtils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

@Component
public class ProfileGlobalCommand extends BotCommand {

	@Autowired private CoinsService coinsService;

	@PostConstruct
	public void init() {
		this.name = "global";
		this.help = "Mostra o top rank dos usuários globalmente";
		this.aliases = new String[] { "all" };
		this.cooldown = Constants.COOLDOWNS.FAST;
		this.cooldownScope = CooldownScope.CHANNEL;
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		List<UserCoins> top = coinsService.top3();
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Constants.COLORS.DEFAULT);
		builder.setAuthor("TOP 3!");
		int i = 0;
		for (UserCoins c : top) {
			User discordUser = event.getJDA().retrieveUserById(c.getId()).complete(); 
			builder.addField(String.format("Usuário %s", Constants.PLACES[i++]), discordUser.getAsMention(), true);
			builder.addField("Moedas", c.getQuantity().toString(), true);
			builder.addBlankField(true);
		}
		event.reply(builder.build(), m -> {
			CommandUtils.success(event.getMessage());
		}, m -> {
			CommandUtils.error(event.getMessage());
		});
	}

}
