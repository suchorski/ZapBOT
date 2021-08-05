package com.suchorski.zapbot.memory.listeners;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
class Pass {
	
	private long channelId;
	private long userId;
	
}