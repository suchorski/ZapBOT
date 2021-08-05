package com.suchorski.zapbot.events.commands.announce;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.models.bot.BotTextChannel;
import com.suchorski.zapbot.models.commands.youtubesubscribe.TextChannelYoutubeSubscribe;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.bot.TextChannelService;
import com.suchorski.zapbot.services.commands.youtubesubscribe.YoutubeSubscribeService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;
import com.suchorski.zapbot.utils.YoutubeUtils;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import net.dv8tion.jda.api.Permission;

@Component
public class YoutubeSubscribeCommand extends BotCommand {

	@Value("${bot.url.api}") private String url;
	@Value("${bot.youtube.subscribe.secret}") private String youtubeSubscribeSecret;

	@Autowired private GuildService guildService;
	@Autowired private TextChannelService textChannelService;
	@Autowired private YoutubeSubscribeService youtubeSubscribeService;
	@Autowired private YoutubeSubscribeListCommand youtubeSubscribeListCommand;

	@PostConstruct
	public void init() {
		this.name = "inscrever";
		this.help = "Inscreve para receber novos vídeos no canal";
		this.aliases = new String[] { "subscribe" };
		this.arguments = "<url do canal | código do canal do youtube>";
		this.children = new Command[] { youtubeSubscribeListCommand };
		addPermissions(Permission.MESSAGE_MENTION_EVERYONE);
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
			Map<String, Object> params = YoutubeUtils.generateSubscribeMap(channelId, youtubeSubscribeSecret, url);
			HttpResponse<String> response = Unirest.post("https://pubsubhubbub.appspot.com/subscribe").fields(params).asString();
			System.out.println(response.getBody());
			if (response.getStatus() == 204) {
				BotTextChannel textChannel = textChannelService.findOrCreate(event.getChannel().getIdLong(), guildService.findOrCreate(event.getGuild().getIdLong()));
				TextChannelYoutubeSubscribe youtubeSubscribe = new TextChannelYoutubeSubscribe(channelId, textChannel);
				int max = Constants.PREMIUM.MAX.YOUTUBE[textChannel.getGuild().isPremium() ? 1 : 0];
				if (textChannel.getYoutubeSubscribes().size() < max) {
					youtubeSubscribe.setTextChannel(textChannel);
					youtubeSubscribeService.create(youtubeSubscribe);
					CommandUtils.success(event, "canal inscrito");					
				} else {
					CommandUtils.warning(event, String.format("você só pode inscrever %d canais", max));	
				}
			} else {
				CommandUtils.error(event, "canal não inscrito");
			}
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		} catch (IOException e) {
			CommandUtils.error(event, e.getLocalizedMessage());
		}
	}

}
