package com.suchorski.zapbot.services.commands.statistics;

import java.time.LocalDate;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.models.bot.BotGuild;
import com.suchorski.zapbot.models.commands.statistics.TextChannelStatistic;
import com.suchorski.zapbot.models.commands.statistics.ids.TextChannelStatisticID;
import com.suchorski.zapbot.repositories.commands.statistics.TextChannelStatisticsRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;

@Service
@Transactional
public class TextChannelStatisticsService extends AbstractService<TextChannelStatisticsRepository, TextChannelStatistic, TextChannelStatisticID> {
	
	@Autowired private TextChannelStatisticsRepository repository;
	
	@PostConstruct
	private void init() {
		setRepository(repository);
		setNothingFoundMessage("estatística não encontrada");
	}
	
	public void deleteOldStatistics() {
		repository.deleteOldStatistics(LocalDate.now().minusDays(Constants.OPTIONS.STATISTICS_LENTH));
	}

	public List<TextChannelStatistic> findTop3(BotGuild guild) {
		return repository.findFirst3ByTextChannelGuildAndIdDateOrderByMessagesReceivedDesc(guild, LocalDate.now());
	}
	
}
