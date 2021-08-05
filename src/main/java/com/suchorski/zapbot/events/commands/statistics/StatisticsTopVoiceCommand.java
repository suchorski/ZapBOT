package com.suchorski.zapbot.events.commands.statistics;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.models.bot.BotGuild;
import com.suchorski.zapbot.models.commands.statistics.VoiceChannelStatistic;
import com.suchorski.zapbot.models.interfaces.statistics.StatisticGraph;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.bot.VoiceChannelService;
import com.suchorski.zapbot.services.commands.statistics.VoiceChannelStatisticsService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.GraphDate;
import com.suchorski.zapbot.utils.GraphGenerator;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.VoiceChannel;

@Component
public class StatisticsTopVoiceCommand extends BotCommand {

	@Autowired private GuildService guildService;
	@Autowired private VoiceChannelService voiceChannelService;
	@Autowired private VoiceChannelStatisticsService voiceChannelStatisticsService;

	@PostConstruct
	public void init() {
		this.name = "voz";
		this.help = "Mostra o top 3 dos canais de voz";
		this.aliases = new String[] { "voice", "voices", "vozes" };
		this.cooldown = Constants.COOLDOWNS.SLOW;
		this.cooldownScope = CooldownScope.CHANNEL;
		this.childOnly = true;
		addPermissions(Permission.MESSAGE_ATTACH_FILES);
	}
	
	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getHelper();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		Long id = null;
		try {
			BotGuild guild = guildService.findOrCreate(event.getGuild().getIdLong());
			List<VoiceChannelStatistic> top3 = voiceChannelStatisticsService.findTop3(guild);
			List<StatisticGraph> statistics = new LinkedList<StatisticGraph>();
			for (VoiceChannelStatistic so : top3) {
				id = so.getId().getVoiceChannelId();
				VoiceChannel voiceChannel = event.getJDA().getGuildById(event.getGuild().getIdLong()).getVoiceChannelById(id);
				for (VoiceChannelStatistic si : so.getVoiceChannel().getVoiceChannelStatistics()) {
					statistics.add(new StatisticGraph() {
						@Override
						public List<Statistic> getStatistics() {
							return si.getStatistics().stream().map(s -> new Statistic(voiceChannel.getName(), s.getValue())).collect(Collectors.toList());
						}
						@Override
						public GraphDate getDate() {
							return si.getDate();
						}
					});
				}
			}
			event.getChannel().sendFile(GraphGenerator.generateGraph(String.format("Top 3 canais de voz de '%s'", event.getGuild().getName()), statistics, !guild.isPremium()), "graph.png").queue(m -> {
				CommandUtils.success(event.getMessage());
			}, m -> {
				CommandUtils.error(event.getMessage());
			});
		} catch (IOException e) {
			CommandUtils.error(event, e.getLocalizedMessage());
		} catch (NullPointerException e) {
			if (id == null) {
				CommandUtils.error(event, "canal não encontrado. Tente novamente");
			} else {				
				voiceChannelService.deleteById(id);
				CommandUtils.error(event, "canal não encontrado e removido. Tente novamente");
			}
		}
	}

}
