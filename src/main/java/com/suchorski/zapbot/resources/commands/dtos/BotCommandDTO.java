package com.suchorski.zapbot.resources.commands.dtos;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BotCommandDTO {
	
	private String command;
	private String help;
	private String args;
	private List<String> aliases;
	private List<BotCommandDTO> subCommands;

	public BotCommandDTO(String command, String help, String args, List<String> aliases) {
		this(command, help, args, aliases, new ArrayList<BotCommandDTO>(0));
	}

}
