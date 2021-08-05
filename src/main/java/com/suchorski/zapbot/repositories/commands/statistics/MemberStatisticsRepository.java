package com.suchorski.zapbot.repositories.commands.statistics;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.suchorski.zapbot.models.bot.BotGuild;
import com.suchorski.zapbot.models.commands.statistics.MemberStatistic;
import com.suchorski.zapbot.models.commands.statistics.ids.MemberStatisticID;

@Repository
public interface MemberStatisticsRepository extends JpaRepositoryImplementation<MemberStatistic, MemberStatisticID> {
	
	@Modifying
	@Query("delete member_statistic s where id.date <= ?1")
	public void deleteOldStatistics(LocalDate olderDate);

	public List<MemberStatistic> findFirst3ByMemberGuildAndIdDateOrderByMessagesDesc(BotGuild guild, LocalDate now);

}
