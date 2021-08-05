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
import com.suchorski.zapbot.models.bot.ids.BotMemberID;
import com.suchorski.zapbot.models.commands.statistics.MemberStatistic;
import com.suchorski.zapbot.models.interfaces.statistics.StatisticGraph;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.bot.MemberService;
import com.suchorski.zapbot.services.commands.statistics.MemberStatisticsService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.GraphDate;
import com.suchorski.zapbot.utils.GraphGenerator;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

@Component
public class StatisticsTopMembersCommand extends BotCommand {

	@Autowired private GuildService guildService;
	@Autowired private MemberService memberService;
	@Autowired private MemberStatisticsService memberStatisticsService;

	@PostConstruct
	public void init() {
		this.name = "membros";
		this.help = "Mostra o top 3 dos usuários";
		this.aliases = new String[] { "members", "member", "users", "user", "membro", "usuarios", "usuario" };
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
		BotMemberID id = null;
		try {
			BotGuild guild = guildService.findOrCreate(event.getGuild().getIdLong());
			List<MemberStatistic> top3 = memberStatisticsService.findTop3(guild);
			List<StatisticGraph> statistics = new LinkedList<StatisticGraph>();
			for (MemberStatistic so : top3) {
				id = so.getId().getMemberId();
				Member member = event.getJDA().getGuildById(id.getGuildId()).retrieveMemberById(id.getUserId()).complete();
				for (MemberStatistic si : so.getMember().getMemberStatistics()) {
					statistics.add(new StatisticGraph() {
						@Override
						public List<Statistic> getStatistics() {
							return si.getStatistics().stream().map(s -> new Statistic(member.getEffectiveName(), s.getValue())).collect(Collectors.toList());
						}
						@Override
						public GraphDate getDate() {
							return si.getDate();
						}
					});
				}
			}
			event.getChannel().sendFile(GraphGenerator.generateGraph(String.format("Top 3 usuários de '%s'", event.getGuild().getName()), statistics, !guild.isPremium()), "graph.png").queue(m -> {
				CommandUtils.success(event.getMessage());
			}, m -> {
				CommandUtils.error(event.getMessage());
			});
		} catch (IOException e) {
			CommandUtils.error(event, e.getLocalizedMessage());
		} catch (NullPointerException e) {
			if (id == null) {
				CommandUtils.error(event, "membro não encontrado. Tente novamente");
			} else {				
				memberService.deleteById(id);
				CommandUtils.error(event, "membro não encontrado e removido. Tente novamente");
			}
		}
	}

}
