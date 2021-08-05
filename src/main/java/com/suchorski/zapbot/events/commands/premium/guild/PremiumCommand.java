package com.suchorski.zapbot.events.commands.premium.guild;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.models.bot.BotGuild;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.utils.CommandUtils;

@Component
public class PremiumCommand extends BotCommand {

	@Autowired private GuildService guildService;

	@PostConstruct
	public void init() {
		this.name = "premium";
		this.help = "Mostra o status premium da guilda";
		this.guildOnly = true;
	}
	
	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getHelper();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		BotGuild guild = guildService.findOrCreate(event.getGuild().getIdLong());
		String message = "sua guilda é premium";
		if (!guild.isPremium()) {
			message = String.format("sua guilda ainda precisa receber mais %d moedas para se tornar premium", Constants.PREMIUM.GUILD_COINS - guild.getCoinsDonated());
		}
		CommandUtils.success(event, message);
	}

}
