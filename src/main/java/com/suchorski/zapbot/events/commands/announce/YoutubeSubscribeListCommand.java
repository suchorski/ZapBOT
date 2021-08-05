package com.suchorski.zapbot.events.commands.announce;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.models.commands.youtubesubscribe.TextChannelYoutubeSubscribe;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.commands.youtubesubscribe.YoutubeSubscribeService;
import com.suchorski.zapbot.utils.CommandUtils;

@Component
public class YoutubeSubscribeListCommand extends BotCommand {

	@Autowired private GuildService guildService;
	@Autowired private YoutubeSubscribeService youtubeSubscribeService;

	@PostConstruct
	public void init() {
		this.name = "lista";
		this.help = "Lista canais do youtube inscritos no canal";
		this.aliases = new String[] { "list" };
		this.childOnly = true;
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getOperator();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		List<TextChannelYoutubeSubscribe> youtubeSubscribes = youtubeSubscribeService.findAllFromGuildId(event.getGuild().getIdLong());
		if (youtubeSubscribes.size() > 0) {
			StringBuffer sb = new StringBuffer();
			sb.append("Lista de canais inscritos:\n");
			for (TextChannelYoutubeSubscribe ys : youtubeSubscribes) {
				sb.append(String.format("`%s%s`\n", "https://www.youtube.com/channel/", ys.getId().getYoutubeChannelId()));
			}
			event.getMessage().reply(sb.toString()).queue(m -> {
				CommandUtils.success(event.getMessage());				
			}, m -> {
				CommandUtils.error(event.getMessage());
			});
		} else {
			CommandUtils.success(event, "nenhum canal inscrito");
		}
	}

}
