package com.suchorski.zapbot.resources.commands.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BotCommandsDTO {
	
	private String preffix;
	private List<BotCommandDTO> commands;
	
}
