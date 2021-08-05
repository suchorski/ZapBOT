package com.suchorski.zapbot.events.commands.announce;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.memory.listeners.Messages;
import com.suchorski.zapbot.models.commands.announce.GuildAnnounce;
import com.suchorski.zapbot.models.commands.announce.GuildAnnounceField;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.commands.announce.AnnounceService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@Component
public class AnnounceSetFieldsAddCommand extends BotCommand {

	@Autowired private EventWaiter eventWaiter;
	@Autowired private GuildService guildService;
	@Autowired private AnnounceService announceService;

	@PostConstruct
	public void init() {
		this.name = "adicionar";
		this.help = "Adiciona um campo no anúncio de entrada";
		this.aliases = new String[] { "add" };
		this.arguments = "<título>";
		this.childOnly = true;
		addPermissions(Permission.MESSAGE_MANAGE);
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getOperator();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			String title = Utils.clear(event.getArgs());
			CommandUtils.stringMinLength(title, 0);
			Message message = event.getChannel().sendMessage("Digite a descrição do campo:").complete();
			Messages.addPass(event.getChannel().getIdLong(), event.getAuthor().getIdLong());
			eventWaiter.waitForEvent(MessageReceivedEvent.class, e -> {
				return e.getAuthor().equals(event.getAuthor()) && e.getChannel().equals(event.getChannel()) && !e.getMessage().equals(event.getMessage());
			}, e -> {
				try {
					String description = Utils.clear(e.getMessage().getContentRaw());
					CommandUtils.stringMinLength(description, 0);
					GuildAnnounce announce = announceService.findById(event.getGuild().getIdLong());
					announceService.addField(announce, new GuildAnnounceField(title, announce, description));
					message.delete().queue();
					e.getMessage().delete().queue();
					CommandUtils.success(event, "campo de anúncio de entrada adicionado");
				} catch (CommandUtilsException er) {
					CommandUtils.warning(event, er.getLocalizedMessage());
				} catch (NothingFoundException er) {
					CommandUtils.error(event, er.getLocalizedMessage());
				}
			}, 5, TimeUnit.MINUTES, () -> {
				CommandUtils.warning(event, "você demorou muito para digitar as opções");
			});
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		}
	}

}
