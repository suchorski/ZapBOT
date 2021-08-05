package com.suchorski.zapbot.repositories.commands.statistics;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.suchorski.zapbot.models.bot.BotGuild;
import com.suchorski.zapbot.models.commands.statistics.TextChannelStatistic;
import com.suchorski.zapbot.models.commands.statistics.ids.TextChannelStatisticID;

@Repository
public interface TextChannelStatisticsRepository extends JpaRepositoryImplementation<TextChannelStatistic, TextChannelStatisticID> {
	
	@Modifying
	@Query("delete textchannel_statistic s where id.date <= ?1")
	public void deleteOldStatistics(LocalDate olderDate);

	public List<TextChannelStatistic> findFirst3ByTextChannelGuildAndIdDateOrderByMessagesReceivedDesc(BotGuild guild, LocalDate now);

}
