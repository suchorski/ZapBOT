package com.suchorski.zapbot.events.commands.announce;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.utils.CommandUtils;

@Component
public class AnnounceSetCommand extends BotCommand {

	@Autowired private GuildService guildService;
	@Autowired private AnnounceSetAuthorCommand announceSetAuthorCommand;
	@Autowired private AnnounceSetDescriptionCommand announceSetDescriptionCommand;
	@Autowired private AnnounceSetFooterCommand announceSetFooterCommand;
	@Autowired private AnnounceSetFieldsCommand announceSetFieldsCommand;

	@PostConstruct
	public void init() {
		this.name = "definir";
		this.help = "Define as configurações de anúncio de entrada";
		this.aliases = new String[] { "set", "define" };
		this.arguments = "[configurações]";
		this.childOnly = true;
		this.children = new Command[] { announceSetAuthorCommand, announceSetDescriptionCommand, announceSetFooterCommand, announceSetFieldsCommand };
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getOperator();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		CommandUtils.success(event, "as configurações são: `texto`, `título`, `descrição`, `rodapé` e `campos`");
	}

}
