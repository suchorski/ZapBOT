package com.suchorski.zapbot.resources.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jagrosh.jdautilities.command.Command;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.resources.commands.dtos.BotCommandDTO;
import com.suchorski.zapbot.resources.commands.dtos.BotCommandsDTO;

@RestController
@RequestMapping("/commands")
public class CommandsResource {

	@Autowired private List<BotCommand> commands;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BotCommandsDTO> commands() {
		List<BotCommandDTO> commandsDTO = new ArrayList<BotCommandDTO>(commands.size());
		for (BotCommand command : commands.stream().filter(c -> !c.isChildOnly()).collect(Collectors.toList())) {
			commandsDTO.add(createDTO(command));
		}
		return ResponseEntity.ok(new BotCommandsDTO(Constants.OPTIONS.PREFFIX, commandsDTO));
	}
	
	private BotCommandDTO createDTO(Command command) {
		if (command.getChildren().length == 0) {
			return new BotCommandDTO(command.getName(), command.getHelp(), command.getArguments(), Arrays.asList(command.getAliases()));
		}
		List<BotCommandDTO> scDtos = new ArrayList<BotCommandDTO>(command.getChildren().length);
		for (Command sc : command.getChildren()) {
			scDtos.add(createDTO(sc));
		}
		return new BotCommandDTO(command.getName(), command.getHelp(), command.getArguments(), Arrays.asList(command.getAliases()), scDtos);
	}

}
