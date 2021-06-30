package com.suchorski.zapbot.repositories.commands.reply;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.suchorski.zapbot.models.commands.reply.GuildReply;
import com.suchorski.zapbot.models.commands.reply.ids.GuildReplyID;

@Repository
public interface ReplyRepository extends JpaRepositoryImplementation<GuildReply, GuildReplyID> {

}
