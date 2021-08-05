package com.suchorski.zapbot.events.commands.announce;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.social.GuildBirthday;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.commands.announce.BirthdaysService;
import com.suchorski.zapbot.utils.CommandUtils;

@Component
public class BirthdaySetCommand extends BotCommand {

	@Autowired private GuildService guildService;
	@Autowired private BirthdaysService birthdaysService;

	@PostConstruct
	public void init() {
		this.name = "definir";
		this.help = "Define o canal de texto para anunciar os aniversariantes";
		this.aliases = new String[] { "define", "set" };
		this.childOnly = true;
	}
	
	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getAdmin();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			GuildBirthday birthday = birthdaysService.findById(event.getGuild().getIdLong());
			birthdaysService.setChannelToAnnounce(birthday, event.getChannel().getIdLong());
			CommandUtils.success(event, "canal definido para anúncio dos aniversariantes");
		} catch (NothingFoundException e) {
			CommandUtils.error(event, e.getLocalizedMessage());
		}
	}

}
