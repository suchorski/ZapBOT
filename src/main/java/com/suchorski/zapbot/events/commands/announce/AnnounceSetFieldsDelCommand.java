package com.suchorski.zapbot.events.commands.announce;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.announce.GuildAnnounce;
import com.suchorski.zapbot.models.commands.announce.GuildAnnounceField;
import com.suchorski.zapbot.models.commands.announce.ids.GuildAnnounceFieldID;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.commands.announce.AnnounceFieldService;
import com.suchorski.zapbot.services.commands.announce.AnnounceService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

@Component
public class AnnounceSetFieldsDelCommand extends BotCommand {

	@Autowired private GuildService guildService;
	@Autowired private AnnounceService announceService;
	@Autowired private AnnounceFieldService announceFieldService;

	@PostConstruct
	public void init() {
		this.name = "remover";
		this.help = "Remove um campo do anúncio de entrada";
		this.aliases = new String[] { "del" };
		this.arguments = "<título>";
		this.childOnly = true;
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getOperator();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			StringBuffer title = new StringBuffer();
			CommandUtils.splitArgs(Utils.clear(event.getArgs()), 0, title);
			GuildAnnounceField announceField = announceFieldService.findById(new GuildAnnounceFieldID(title.toString(), event.getGuild().getIdLong()));
			GuildAnnounce announce = announceService.findById(event.getGuild().getIdLong());
			announceService.delField(announce, announceField);
			CommandUtils.success(event, "campo de anúncio de entrada removido");
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		} catch (NothingFoundException e) {
			CommandUtils.error(event, e.getLocalizedMessage());
		}
	}

}
