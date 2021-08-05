package com.suchorski.zapbot.events.commands.search;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

import net.dv8tion.jda.api.Permission;

@Component
public class GoogleCommand extends BotCommand {
	
	private static final String URL = "https://www.google.com/search?q=%s";
	
	@PostConstruct
	public void init() {
		this.name = "google";
		this.help = "Busca um site no Google";
		this.aliases = new String[] { "busca" };
		this.cooldown = Constants.COOLDOWNS.FAST;
		this.cooldownScope = CooldownScope.USER;
		this.arguments = "<texto>";
		addPermissions(Permission.MESSAGE_EMBED_LINKS);
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			StringBuffer arg = new StringBuffer();
			CommandUtils.splitArgs(Utils.clear(event.getArgs()), 0, arg);
			Document document = Jsoup.connect(String.format(URL, arg.toString().replace(' ', '+'))).get();
			Element div = document.selectFirst("div#search");
			if (div != null) {
				Element a = div.selectFirst("a[href]");
				if (a != null) {
					event.reply(String.format("%s, %s", event.getAuthor().getAsMention(), a.absUrl("href")), m -> {
						CommandUtils.success(event.getMessage());
					}, m -> {
						CommandUtils.error(event.getMessage());
					});
				} else {
					CommandUtils.warning(event, "nenhum site encontrado");
				}
			} else {
				CommandUtils.warning(event, "nenhum site encontrado");
			}
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());			
		}  catch (IOException e) {
			CommandUtils.error(event, e.getLocalizedMessage().toLowerCase());
		}
	}

}
