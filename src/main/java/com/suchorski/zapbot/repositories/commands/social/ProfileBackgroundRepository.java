package com.suchorski.zapbot.repositories.commands.social;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.suchorski.zapbot.models.commands.social.UserProfileBackground;

@Repository
public interface ProfileBackgroundRepository extends JpaRepositoryImplementation<UserProfileBackground, Integer> {

	public Optional<UserProfileBackground> findByName(String name);

	public List<UserProfileBackground> findAllByVisibleTrue();

}
