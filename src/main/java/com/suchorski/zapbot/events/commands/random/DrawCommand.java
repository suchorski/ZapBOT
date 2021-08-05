package com.suchorski.zapbot.events.commands.random;

import java.time.Instant;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.events.futures.DrawFuture;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.exceptions.DateTimeUtilsException;
import com.suchorski.zapbot.models.bot.BotTextChannel;
import com.suchorski.zapbot.models.commands.lucky.TextChannelDraw;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.bot.TextChannelService;
import com.suchorski.zapbot.services.commands.lucky.DrawService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.DateTimeUtils;
import com.suchorski.zapbot.utils.Utils;
import com.vdurmont.emoji.EmojiParser;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@Component
public class DrawCommand extends BotCommand {

	@Autowired private EventWaiter eventWaiter;
	@Autowired private TaskScheduler taskScheduler;
	@Autowired private GuildService guildService;
	@Autowired private DrawService drawService;
	@Autowired private TextChannelService textChannelService;

	@PostConstruct
	public void init() {
		this.name = "sortear";
		this.help = "Inicia um sorteio";
		this.aliases = new String[] { "draw" };
		this.arguments = "<quantidade de sorteados> [emoji] <prêmio>";
		addPermissions(Permission.MESSAGE_MANAGE);
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getOperator();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			String[] args = event.getArgs().trim().split("\\s");
			CommandUtils.checkMinNumArgs(Utils.clear(event.getArgs()), 2);
			int quantity = Integer.parseInt(args[0]);
			String emoji;
			String prize;
			if (EmojiParser.extractEmojis(args[1]).size() == 1) {
				emoji = args[1];
				prize = String.join(" ", Arrays.copyOfRange(args, 2, args.length)).trim();
			} else {
				emoji = Constants.EMOJIS.MARK;
				prize = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).trim();
			}
			CommandUtils.stringMaxLength(prize, 1024);
			Message questionTime = event.getChannel().sendMessage("Digite o instante que será executado o sorteio (data, data com hora ou duração)").complete();
			eventWaiter.waitForEvent(MessageReceivedEvent.class, e -> {
				return e.getAuthor().equals(event.getAuthor()) && e.getChannel().equals(event.getChannel()) && !e.getMessage().equals(event.getMessage());
			}, e -> {
				try {
					Instant instant = DateTimeUtils.parseTime(e.getMessage().getContentRaw());
					EmbedBuilder builder = new EmbedBuilder();
					builder.setColor(Constants.COLORS.DEFAULT);
					builder.setAuthor(prize);
					builder.setDescription(String.format("Sorteio iniciado por: %s", event.getAuthor().getAsMention()));
					builder.setFooter(String.format("Participe reagindo: %s", emoji));
					builder.setTimestamp(instant);
					questionTime.delete().queue();
					e.getMessage().delete().queue();
					Message message = event.getChannel().sendMessage(builder.build()).complete();
					message.addReaction(emoji).queue();
					BotTextChannel textChannel = textChannelService.findOrCreate(event.getChannel().getIdLong(), guildService.findOrCreate(event.getGuild().getIdLong()));
					TextChannelDraw draw = new TextChannelDraw(message.getIdLong(), textChannel, instant, quantity, prize);
					draw.setEmoji(emoji);
					drawService.forceCreate(draw);
					taskScheduler.schedule(new DrawFuture(event.getChannel().getIdLong(), message.getIdLong(), emoji, quantity, prize, event.getJDA(), drawService), instant);
					event.getMessage().delete().queue();
				} catch (DateTimeUtilsException err) {
					CommandUtils.error(event, err.getLocalizedMessage());
				}
			});
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		} catch (NumberFormatException e) {			
			CommandUtils.warning(event, "o segundo argumento precisa ser um número");
		}
	}

}
