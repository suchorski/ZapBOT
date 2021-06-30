package com.suchorski.zapbot.services.bot;

import java.time.Instant;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.exceptions.BannedException;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.bot.BotMember;
import com.suchorski.zapbot.models.bot.ids.BotMemberID;
import com.suchorski.zapbot.repositories.bot.MemberRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;

@Service
@Transactional
public class MemberService extends AbstractService<MemberRepository, BotMember, BotMemberID> {
	
	@Autowired private MemberRepository repository;
	@Autowired private UserService userService;
	@Autowired private GuildService guildService;
	
	@PostConstruct
	private void init() {
		setRepository(repository);
		setNothingFoundMessage("membro não encontrado");
	}
	
	public BotMember findOrCreate(long userId, long guildId) {
		try {
			return findById(new BotMemberID(userId, guildId));
		} catch (NothingFoundException e) {
			BotMember member = new BotMember(userService.findOrCreate(userId), guildService.findOrCreate(guildId));
			return create(member);
		}
	}

	public void addMessageXp(BotMember member, int points) {
		member.addMessageXp(points);
		member.setLastMessage(Instant.now());
		update(member);
	}

	public List<BotMember> top3() {
		return repository.findFirst3ByOrderByLevelDescXpDescMessagesDesc();
	}

	public void checkBanned(BotMember member) throws BannedException {
		if (member.getBanned()) {
			throw new BannedException("você não pode usar os comandos do BOT nesta guilda");
		}
	}

	public void ban(BotMember member) {
		member.setBanned(true);
		update(member);
	}
	
	public void unban(BotMember member) {
		member.setBanned(false);
		update(member);
	}

}
