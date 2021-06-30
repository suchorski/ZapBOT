package com.suchorski.zapbot.services.commands.lucky;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.lucky.TextChannelDraw;
import com.suchorski.zapbot.models.commands.lucky.ids.TextChannelDrawID;
import com.suchorski.zapbot.repositories.commands.lucky.DrawRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;

@Service
@Transactional
public class DrawService extends AbstractService<DrawRepository, TextChannelDraw, TextChannelDrawID> {
	
	@Autowired private DrawRepository repository;
	
	@PostConstruct
	private void init() {
		setRepository(repository);
		setNothingFoundMessage("sorteio não encontrado");
	}

	public TextChannelDraw forceCreate(TextChannelDraw draw) {
		try {
			TextChannelDraw d = findById(draw.getId());
			deleteById(d.getId());
			return create(draw);
		} catch (NothingFoundException e) {
			return create(draw);
		}
	}
	
}
