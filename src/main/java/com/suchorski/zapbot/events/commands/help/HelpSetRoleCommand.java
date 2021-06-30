package com.suchorski.zapbot.events.commands.help;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suchorski.zapbot.events.abstracts.GenericHelpListSubCommand;
import com.suchorski.zapbot.events.commands.administration.SetRoleCommand;

@Component
public class HelpSetRoleCommand extends GenericHelpListSubCommand<SetRoleCommand> {
	
	@Autowired private SetRoleCommand mainCommand;
	
	@PostConstruct
	public void init() {
		super.initialize(mainCommand);
	}

}
