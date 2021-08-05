package com.suchorski.zapbot.events.commands.administration;

import java.time.temporal.ChronoUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.utils.CommandUtils;

import net.dv8tion.jda.api.EmbedBuilder;

@Component
public class PingCommand extends BotCommand {

	@Autowired private GuildService guildService;

	@PostConstruct
	public void init() {
		this.name = "ping";
		this.help = "Mostra o LAG do bot";
		this.aliases = new String[] { "pong", "lag" };
		this.cooldown = Constants.COOLDOWNS.FAST;
		this.cooldownScope = CooldownScope.GUILD;
		this.guildOnly = false;
	}
	
	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getHelper();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		event.reply("Ping: calculando...", m -> {
			long ping = event.getMessage().getTimeCreated().until(m.getTimeCreated(), ChronoUnit.MILLIS);
			EmbedBuilder builder = new EmbedBuilder();
			builder.setColor(Constants.COLORS.DEFAULT);
			builder.addField("Ping", String.format("%dms", ping), true);
			builder.addField("Gateway", String.format("%dms", event.getJDA().getGatewayPing()), true);
			builder.setFooter("Rodando desde", null);
			builder.setTimestamp(event.getClient().getStartTime());
			m.editMessage("Pong!").queue();
			m.editMessage(builder.build()).queue(success -> {				
				CommandUtils.success(event.getMessage());
			}, error -> {				
				CommandUtils.error(event.getMessage());
			});
		});
	}

}
