package com.suchorski.zapbot.events.commands.search;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

import net.dv8tion.jda.api.Permission;

@Component
public class YoutubeCommand extends BotCommand {

	private static final String URL = "https://www.google.com/search?tbm=vid&q=%s";

	@PostConstruct
	public void init() {
		this.name = "youtube";
		this.help = "Busca um vídeo no YouTube";
		this.aliases = new String[] { "yt" };
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
			Elements elements = document.select("a");
			if (elements.size() > 0) {
				boolean found = false;
				for (Element e : elements) {
					String url = e.absUrl("href");
					if (url.contains("/watch?")) {
						event.reply(String.format("%s %s", event.getAuthor().getAsMention(), url), m -> {
							CommandUtils.success(event.getMessage());
						}, m -> {
							CommandUtils.error(event.getMessage());
						});
						found = true;
						break;
					}
				}
				if (!found) {					
					CommandUtils.warning(event, "nenhum vídeo encontrado");
				}
			} else {
				CommandUtils.warning(event, "nenhum vídeo encontrado");
			}			
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());			
		} catch (IOException e) {
			CommandUtils.warning(event, e.getLocalizedMessage().toLowerCase());			
		}
	}

}
