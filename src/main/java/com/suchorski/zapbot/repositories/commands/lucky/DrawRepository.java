package com.suchorski.zapbot.repositories.commands.lucky;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.suchorski.zapbot.models.commands.lucky.TextChannelDraw;
import com.suchorski.zapbot.models.commands.lucky.ids.TextChannelDrawID;

@Repository
public interface DrawRepository extends JpaRepositoryImplementation<TextChannelDraw, TextChannelDrawID> {

}
