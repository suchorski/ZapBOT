package com.suchorski.zapbot.events.commands.announce;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.announce.GuildAnnounce;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.commands.announce.AnnounceService;
import com.suchorski.zapbot.utils.CommandUtils;

import net.dv8tion.jda.api.EmbedBuilder;

@Component
public class AnnounceSetFieldsCommand extends BotCommand {

	@Autowired private GuildService guildService;
	@Autowired private AnnounceService announceService;
	@Autowired private AnnounceSetFieldsAddCommand announceSetFieldsAddCommand;
	@Autowired private AnnounceSetFieldsDelCommand announceSetFieldsDelCommand;

	@PostConstruct
	public void init() {
		this.name = "campos";
		this.help = "Lista ou edita os campos de anúncio da mensagem de entrada";
		this.aliases = new String[] { "fields" };
		this.arguments = "[add <titulo> | del <código>]";
		this.childOnly = true;
		this.children = new Command[] { announceSetFieldsAddCommand, announceSetFieldsDelCommand };
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getOperator();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			GuildAnnounce announce = announceService.findById(event.getGuild().getIdLong());
			if (announce.getFields().size() > 0) {
				EmbedBuilder builder = new EmbedBuilder();
				builder.setColor(Constants.COLORS.DEFAULT);
				builder.setDescription("Lista de campos do anúncio de entrada:");
				announce.getFields().forEach(f -> {
					builder.addField(f.getId().getTitle(), f.getDescription(), false);			
				});
				event.reply(builder.build(), m -> {
					CommandUtils.success(event.getMessage());
				}, m -> {
					CommandUtils.error(event.getMessage());				
				});
			} else {
				CommandUtils.success(event, "nenhum campo de anúncio de entrada definido");			
			}
		} catch (NothingFoundException e) {
			CommandUtils.error(event, e.getLocalizedMessage());			
		}
	}

}
