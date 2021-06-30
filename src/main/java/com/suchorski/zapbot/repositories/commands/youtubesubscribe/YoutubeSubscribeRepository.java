package com.suchorski.zapbot.repositories.commands.youtubesubscribe;

import java.util.List;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.suchorski.zapbot.models.commands.youtubesubscribe.TextChannelYoutubeSubscribe;
import com.suchorski.zapbot.models.commands.youtubesubscribe.ids.TextChannelYoutubeSubscribeID;

@Repository
public interface YoutubeSubscribeRepository extends JpaRepositoryImplementation<TextChannelYoutubeSubscribe, TextChannelYoutubeSubscribeID> {

	public List<TextChannelYoutubeSubscribe> findAllByIdYoutubeChannelId(String youtubeChannelId);

	public List<TextChannelYoutubeSubscribe> findAllByTextChannelGuildId(long guildId);

}
