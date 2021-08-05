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
import com.suchorski.zapbot.models.commands.statistics.TextChannelStatistic;
import com.suchorski.zapbot.models.interfaces.statistics.StatisticGraph;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.bot.TextChannelService;
import com.suchorski.zapbot.services.commands.statistics.TextChannelStatisticsService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.GraphDate;
import com.suchorski.zapbot.utils.GraphGenerator;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

@Component
public class StatisticsTopTextCommand extends BotCommand {

	@Autowired private GuildService guildService;
	@Autowired private TextChannelService textChannelService;
	@Autowired private TextChannelStatisticsService textChannelStatisticsService;

	@PostConstruct
	public void init() {
		this.name = "texto";
		this.help = "Mostra o top 3 dos canais de texto";
		this.aliases = new String[] { "text", "canais" };
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
			List<TextChannelStatistic> top3 = textChannelStatisticsService.findTop3(guild);
			List<StatisticGraph> statistics = new LinkedList<StatisticGraph>();
			for (TextChannelStatistic so : top3) {
				id = so.getId().getTextChannelId();
				TextChannel textChannel = event.getJDA().getGuildById(event.getGuild().getIdLong()).getTextChannelById(id);
				for (TextChannelStatistic si : so.getTextChannel().getTextChannelStatistics()) {
					statistics.add(new StatisticGraph() {
						@Override
						public List<Statistic> getStatistics() {
							return si.getStatistics().stream().filter(s -> s.getCategory().equalsIgnoreCase(TextChannelStatistic.MSG_MESSAGES_SENT)).map(s -> new Statistic(textChannel.getName(), s.getValue())).collect(Collectors.toList());
						}
						@Override
						public GraphDate getDate() {
							return si.getDate();
						}
					});
				}
			}
			event.getChannel().sendFile(GraphGenerator.generateGraph(String.format("Top 3 canais de texto de '%s'", event.getGuild().getName()), statistics, !guild.isPremium()), "graph.png").queue(m -> {
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
				textChannelService.deleteById(id);
				CommandUtils.error(event, "canal não encontrado e removido. Tente novamente");
			}
		}
	}

}
