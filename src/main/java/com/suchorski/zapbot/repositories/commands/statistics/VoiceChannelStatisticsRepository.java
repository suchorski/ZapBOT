package com.suchorski.zapbot.repositories.commands.statistics;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.suchorski.zapbot.models.bot.BotGuild;
import com.suchorski.zapbot.models.commands.statistics.VoiceChannelStatistic;
import com.suchorski.zapbot.models.commands.statistics.ids.VoiceChannelStatisticID;

@Repository
public interface VoiceChannelStatisticsRepository extends JpaRepositoryImplementation<VoiceChannelStatistic, VoiceChannelStatisticID> {
	
	@Modifying
	@Query("delete voicechannel_statistic s where id.date <= ?1")
	public void deleteOldStatistics(LocalDate olderDate);

	public List<VoiceChannelStatistic> findFirst3ByVoiceChannelGuildAndIdDateOrderByMinutesDesc(BotGuild guild, LocalDate now);

}
