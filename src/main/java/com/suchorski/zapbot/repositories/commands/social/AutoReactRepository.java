package com.suchorski.zapbot.repositories.commands.social;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.suchorski.zapbot.models.commands.social.UserAutoReact;

@Repository
public interface AutoReactRepository extends JpaRepositoryImplementation<UserAutoReact, Long> {

}
