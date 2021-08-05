package com.suchorski.zapbot.events.commands.announce;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
public class AnnounceShowCommand extends BotCommand {

	@Autowired private GuildService guildService;
	@Autowired private AnnounceService announceService;

	@PostConstruct
	public void init() {
		this.name = "configuracoes";
		this.help = "Mostra as configurações de anúncio de entrada";
		this.aliases = new String[] { "config", "list", "lista", "configurações" };
		this.childOnly = true;
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getOperator();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			GuildAnnounce announce = announceService.findById(event.getGuild().getIdLong());
			EmbedBuilder builder = new EmbedBuilder();
			builder.setColor(Constants.COLORS.DEFAULT);
			builder.addField("Título", announce.getAuthor(), false);
			builder.addField("Descrição", announce.getDescription(), false);
			builder.addField("Rodapé", announce.getFooter(), false);
			builder.addField("", "Lista de campos do anúncio de entrada:", false);
			announce.getFields().forEach(f -> {
				builder.addField(f.getId().getTitle(), f.getDescription(), false);			
			});
			event.reply(builder.build(), m -> {
				CommandUtils.success(event.getMessage());
			}, m -> {
				CommandUtils.error(event.getMessage());				
			});
		} catch (NothingFoundException e) {
			CommandUtils.error(event, e.getLocalizedMessage());
		}
	}

}
