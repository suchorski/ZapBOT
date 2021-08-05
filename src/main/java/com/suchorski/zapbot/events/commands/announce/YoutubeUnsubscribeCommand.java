package com.suchorski.zapbot.events.commands.announce;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.youtubesubscribe.TextChannelYoutubeSubscribe;
import com.suchorski.zapbot.models.commands.youtubesubscribe.ids.TextChannelYoutubeSubscribeID;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.commands.youtubesubscribe.YoutubeSubscribeService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;
import com.suchorski.zapbot.utils.YoutubeUtils;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

@Component
public class YoutubeUnsubscribeCommand extends BotCommand {

	@Value("${bot.url.api}") private String url;
	@Value("${bot.youtube.subscribe.secret}") private String youtubeSubscribeSecret;

	@Autowired private GuildService guildService;
	@Autowired private YoutubeSubscribeService youtubeSubscribeService;

	@PostConstruct
	public void init() {
		this.name = "desinscrever";
		this.help = "Desinscreve para não receber novos vídeos no canal";
		this.aliases = new String[] { "unsubscribe" };
		this.arguments = "<url do canal | código do canal do youtube>";
	}
	
	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getOperator();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			StringBuffer arg = new StringBuffer();
			CommandUtils.splitArgs(Utils.clear(event.getArgs()), arg);
			String channelId = YoutubeUtils.getChannelId(arg.toString());
			Map<String, Object> params = YoutubeUtils.generateUnsubscribeMap(channelId, youtubeSubscribeSecret, url);
			HttpResponse<String> response = Unirest.post("https://pubsubhubbub.appspot.com/unsubscribe").fields(params).asString();
			if (response.getStatus() == 204) {
				TextChannelYoutubeSubscribe youtubeSubscribe = youtubeSubscribeService.findById(new TextChannelYoutubeSubscribeID(channelId, event.getChannel().getIdLong()));
				youtubeSubscribeService.delete(youtubeSubscribe);
				CommandUtils.success(event, "canal desinscrito");
			} else {
				CommandUtils.error(event, "canal não desinscrito");
			}
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		} catch (NothingFoundException e) {
			CommandUtils.error(event, e.getLocalizedMessage());
		} catch (IOException e) {
			CommandUtils.error(event, e.getLocalizedMessage());
		}
	}

}
