package com.suchorski.zapbot.events.commands.survey;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@Component
public class VoteCommand extends BotCommand {

	@Autowired private EventWaiter eventWaiter;
	@Autowired private GuildService guildService;

	@PostConstruct
	public void init() {
		this.name = "votar";
		this.help = "Inicia uma votação";
		this.aliases = new String[] { "vote", "votacao", "enquete" };
		this.arguments = "<título da enquete>";
		addPermissions(Permission.MESSAGE_MANAGE);
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getHelper();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			StringBuffer arg = new StringBuffer();
			CommandUtils.splitArgs(Utils.clear(event.getArgs()), 0, arg);
			event.reply("Digite as alternativas da votação (uma por linha)");
			eventWaiter.waitForEvent(MessageReceivedEvent.class, e -> {
				return e.getAuthor().equals(event.getAuthor()) && e.getChannel().equals(event.getChannel()) && !e.getMessage().equals(event.getMessage());
			}, e -> {
				EmbedBuilder builder = new EmbedBuilder();
				builder.setColor(Constants.COLORS.DEFAULT);
				builder.setAuthor(String.format("Enquete: %s", arg.toString()));
				String options[] = e.getMessage().getContentRaw().trim().split("\n");
				if (options.length > 0 && options.length <= 10) {
					int count = 0;
					for (String o : options) {
						builder.addField(Constants.NUMBERS[count++], o, false);
					}
					final int counted = count;
					event.reply(builder.build(), m -> {
						for (int i = 0; i < counted; ++i) {
							CommandUtils.addReation(m, Constants.NUMBERS[i]);
						}
					});
					e.getMessage().delete().queue();
					CommandUtils.success(event.getMessage());
				} else {
					CommandUtils.warning(event, "a votação pode ter no máximo 10 opções");
				}
			}, 10, TimeUnit.MINUTES, () -> {
				CommandUtils.warning(event, "você demorou muito para digitar as alternativas");
			});
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		}
	}

}
