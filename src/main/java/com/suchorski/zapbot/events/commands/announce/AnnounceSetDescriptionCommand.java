package com.suchorski.zapbot.events.commands.announce;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.announce.GuildAnnounce;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.commands.announce.AnnounceService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

@Component
public class AnnounceSetDescriptionCommand extends BotCommand {

	@Autowired private GuildService guildService;
	@Autowired private AnnounceService announceService;


	@PostConstruct
	public void init() {
		this.name = "descricao";
		this.help = "Define o descrição do anúncio de entrada";
		this.aliases = new String[] { "description", "descrição" };
		this.arguments = "<descrição>";
		this.childOnly = true;
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getOperator();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			String description = Utils.clear(event.getArgs());
			CommandUtils.stringMinLength(description, 0);
			GuildAnnounce announce = announceService.findById(event.getGuild().getIdLong());
			announceService.setDescription(announce, description.toString());
			CommandUtils.success(event, "descrição de anúncio de entrada definido");
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		} catch (NothingFoundException e) {
			CommandUtils.error(event, e.getLocalizedMessage());
		}
	}

}
