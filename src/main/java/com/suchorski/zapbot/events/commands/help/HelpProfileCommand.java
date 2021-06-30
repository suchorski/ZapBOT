package com.suchorski.zapbot.events.commands.help;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suchorski.zapbot.events.abstracts.GenericHelpListSubCommand;
import com.suchorski.zapbot.events.commands.social.ProfileCommand;

@Component
public class HelpProfileCommand extends GenericHelpListSubCommand<ProfileCommand> {
	
	@Autowired private ProfileCommand mainCommand;
	
	@PostConstruct
	public void init() {
		super.initialize(mainCommand);
	}

}
