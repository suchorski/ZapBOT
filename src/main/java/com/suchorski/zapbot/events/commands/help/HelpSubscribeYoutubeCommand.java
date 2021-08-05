package com.suchorski.zapbot.events.commands.help;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suchorski.zapbot.events.abstracts.GenericHelpListSubCommand;
import com.suchorski.zapbot.events.commands.announce.YoutubeSubscribeCommand;

@Component
public class HelpSubscribeYoutubeCommand extends GenericHelpListSubCommand<YoutubeSubscribeCommand> {
	
	@Autowired private YoutubeSubscribeCommand mainCommand;
	
	@PostConstruct
	public void init() {
		super.initialize(mainCommand);
	}

}
