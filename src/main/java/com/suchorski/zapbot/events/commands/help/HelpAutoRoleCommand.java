package com.suchorski.zapbot.events.commands.help;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suchorski.zapbot.events.abstracts.GenericHelpListSubCommand;
import com.suchorski.zapbot.events.commands.autorole.AutoRoleCommand;

@Component
public class HelpAutoRoleCommand extends GenericHelpListSubCommand<AutoRoleCommand> {
	
	@Autowired private AutoRoleCommand mainCommand;
	
	@PostConstruct
	public void init() {
		super.initialize(mainCommand);
	}

}
