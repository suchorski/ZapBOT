package com.suchorski.zapbot.services.commands.statistics;

import java.time.LocalDate;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.models.bot.BotGuild;
import com.suchorski.zapbot.models.commands.statistics.MemberStatistic;
import com.suchorski.zapbot.models.commands.statistics.ids.MemberStatisticID;
import com.suchorski.zapbot.repositories.commands.statistics.MemberStatisticsRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;

@Service
@Transactional
public class MemberStatisticsService extends AbstractService<MemberStatisticsRepository, MemberStatistic, MemberStatisticID> {
	
	@Autowired private MemberStatisticsRepository repository;
	
	@PostConstruct
	private void init() {
		setRepository(repository);
		setNothingFoundMessage("estatística não encontrada");
	}
	
	public void deleteOldStatistics() {
		repository.deleteOldStatistics(LocalDate.now().minusDays(Constants.OPTIONS.STATISTICS_LENTH));
	}

	public List<MemberStatistic> findTop3(BotGuild guild) {
		return repository.findFirst3ByMemberGuildAndIdDateOrderByMessagesDesc(guild, LocalDate.now());
	}
	
}
