package com.suchorski.zapbot.events.commands.announce;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.announce.GuildAnnounce;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.commands.announce.AnnounceService;
import com.suchorski.zapbot.utils.CommandUtils;

@Component
public class AnnounceCommand extends BotCommand {

	@Autowired private GuildService guildService;
	@Autowired private AnnounceService announceService;
	@Autowired private AnnounceShowCommand announceShowCommand;
	@Autowired private AnnounceSetCommand announceSetCommand;

	@PostConstruct
	public void init() {
		this.name = "anunciar";
		this.help = "Define o canal de anúncios";
		this.aliases = new String[] { "announce", "joinmessages" };
		this.arguments = "[configurações]";
		this.children = new Command[] { announceShowCommand, announceSetCommand };
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getOperator();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			GuildAnnounce announce = announceService.findById(event.getGuild().getIdLong());
			announceService.setChannelId(announce, event.getChannel().getIdLong());
			CommandUtils.success(event, "canal de anúncio de boas vindas definido");
		} catch (NothingFoundException e) {
			CommandUtils.error(event, e.getLocalizedMessage());
		}
	}

}
