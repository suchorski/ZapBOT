package com.suchorski.zapbot.services.commands.reply;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.models.commands.reply.GuildReply;
import com.suchorski.zapbot.models.commands.reply.ids.GuildReplyID;
import com.suchorski.zapbot.repositories.commands.reply.ReplyRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;

@Service
@Transactional
public class ReplyService extends AbstractService<ReplyRepository, GuildReply, GuildReplyID> {
	
	@Autowired private ReplyRepository repository;
	
	@PostConstruct
	private void init() {
		setRepository(repository);
		setNothingFoundMessage("resposta não encontrada");
	}

	public void addReply(GuildReply reply) {
		update(reply);
	}

}
