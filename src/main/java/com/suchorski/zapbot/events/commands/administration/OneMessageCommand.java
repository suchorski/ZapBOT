package com.suchorski.zapbot.events.commands.administration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.models.bot.BotTextChannel;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.bot.TextChannelService;
import com.suchorski.zapbot.utils.CommandUtils;

import net.dv8tion.jda.api.Permission;

@Component
public class OneMessageCommand extends BotCommand {

	@Autowired private GuildService guildService;
	@Autowired private TextChannelService textChannelService;

	@PostConstruct
	public void init() {
		this.name = "umamensagem";
		this.help = "Define o canal com somente uma mensagem por usuário";
		this.aliases = new String[] { "onemessage", "clean", "limpar" };
		addPermissions(Permission.MESSAGE_MANAGE, Permission.MESSAGE_HISTORY);
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getAdmin();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		BotTextChannel serverChannel = textChannelService.findOrCreate(event.getChannel().getIdLong(), guildService.findOrCreate(event.getGuild().getIdLong()));
		textChannelService.switchOnlyLastMessage(serverChannel);
		CommandUtils.success(event, String.format("canal de texto agora **%s** conter uma mensagem por usuário", serverChannel.getOnlyLastMessage() ? "vai" : "não vai"));
	}

}
