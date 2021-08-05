package com.suchorski.zapbot.repositories.commands.social;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.suchorski.zapbot.models.commands.social.UserProfile;

@Repository
public interface ProfileRepository extends JpaRepositoryImplementation<UserProfile, Long> {

}
