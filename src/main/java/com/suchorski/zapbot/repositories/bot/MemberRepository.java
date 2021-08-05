package com.suchorski.zapbot.repositories.bot;

import java.util.List;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.suchorski.zapbot.models.bot.BotMember;
import com.suchorski.zapbot.models.bot.ids.BotMemberID;

@Repository
public interface MemberRepository extends JpaRepositoryImplementation<BotMember, BotMemberID> {

	public List<BotMember> findFirst3ByOrderByLevelDescXpDescMessagesDesc();

}
