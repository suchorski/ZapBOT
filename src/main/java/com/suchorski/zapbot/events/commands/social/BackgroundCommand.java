package com.suchorski.zapbot.events.commands.social;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Slideshow.Builder;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.models.bot.BotUser;
import com.suchorski.zapbot.models.commands.social.UserProfileBackground;
import com.suchorski.zapbot.services.bot.UserService;
import com.suchorski.zapbot.services.commands.social.ProfileBackgroundService;
import com.suchorski.zapbot.utils.CommandUtils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

@Component
public class BackgroundCommand extends BotCommand {
	
	@Value("${bot.url.api}") private String url;

	@Autowired private EventWaiter eventWaiter;
	@Autowired private UserService userService;
	@Autowired private ProfileBackgroundService profileBackgroundService;
	@Autowired private BackgroundBuyCommand backgroundBuyCommand;
	@Autowired private BackgroundSetCommand backgroundSetCommand;
	@Autowired private BackgroundListCommand backgroundListCommand;

	@PostConstruct
	public void init() {
		this.name = "fundo";
		this.help = "Mostra os fundos disponíveis para compra";
		this.aliases = new String[] { "background" };
		this.cooldown = Constants.COOLDOWNS.SLOWER;
		this.cooldownScope = CooldownScope.CHANNEL;
		this.children = new Command[] { backgroundBuyCommand, backgroundSetCommand, backgroundListCommand };
		addPermissions(Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_MANAGE);
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		BotUser user = userService.findOrCreate(event.getAuthor().getIdLong());
		List<UserProfileBackground> profileBackgrounds = profileBackgroundService.listAvailableToBuy();
		profileBackgrounds.removeAll(user.getBackgrounds());
		if (profileBackgrounds.size() > 1) {
			Builder builder = new Builder();
			builder.setEventWaiter(eventWaiter);
			builder.setColor(Constants.COLORS.DEFAULT);
			builder.setUsers(event.getAuthor());
			builder.setTimeout(5, TimeUnit.MINUTES);
			builder.setBulkSkipNumber(1);
			builder.setText((index, total) -> {
				UserProfileBackground bg = profileBackgrounds.get(index - 1);
				return String.format("Código do fundo para compra: %d", bg.getId());
			});
			builder.setDescription((index, total) -> {
				UserProfileBackground bg = profileBackgrounds.get(index - 1);
				return String.format("%s (Moedas: %d)", bg.getName(), bg.getPrice());
			});
			builder.setUrls(profileBackgrounds.stream().map(i -> String.format("%s/background/%d", url, i.getId())).collect(Collectors.toList()).toArray(new String[profileBackgrounds.size()]));
			event.getMessage().delete().queue(m -> {
				builder.build().display(event.getChannel());
			}, m -> {
				CommandUtils.error(event.getMessage());
			});
		} else if (profileBackgrounds.size() == 1) {
			UserProfileBackground bg = profileBackgrounds.get(0);
			EmbedBuilder builder = new EmbedBuilder();
			builder.setColor(Constants.COLORS.DEFAULT);
			builder.setImage(String.format("%s/background/%d", url, bg.getId()));
			builder.setAuthor(String.format("Código do fundo para compra: %d", bg.getId()));
			builder.setDescription(String.format("%s (Moedas: %d)", bg.getName(), bg.getPrice()));
			event.reply(builder.build(), m -> {
				CommandUtils.success(event.getMessage());
			}, m -> {
				CommandUtils.error(event.getMessage());
			});
		} else {
			CommandUtils.warning(event, "nenhum fundo disponível para compra");
		}
	}

}
