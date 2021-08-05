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
public class BirthdayDisableCommand extends BotCommand {

	@Autowired private GuildService guildService;
	@Autowired private BirthdaysService birthdaysService;

	@PostConstruct
	public void init() {
		this.name = "desabilitar";
		this.help = "Desabilita o anúncio de aniversariantes";
		this.aliases = new String[] { "disable", "desativar" };
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
			birthdaysService.disableBirthday(birthday);
			CommandUtils.success(event, "anúncio de aniversariantes desativado com sucesso");
		} catch (NothingFoundException e) {
			CommandUtils.error(event, e.getLocalizedMessage());			
		}
	}

}
