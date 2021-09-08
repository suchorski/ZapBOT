package com.suchorski.zapbot.repositories.commands.social;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.suchorski.zapbot.models.commands.social.UserBirthday;

@Repository
public interface BirthdayRepository extends JpaRepositoryImplementation<UserBirthday, Long> {
	
	@Query("select b from user_birthday b inner join b.user u inner join u.members m inner join m.guild g where g.id = ?1 and day(b.date) = ?2 and month(b.date) = ?3")
	public List<UserBirthday> findBirthdays(long guildId, int dayOfMonth, int monthValue);

}
