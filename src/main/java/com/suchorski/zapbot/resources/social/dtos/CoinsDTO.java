package com.suchorski.zapbot.resources.social.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoinsDTO {
	
	private boolean status;
	private String message;
	private long coins;
	private boolean forceLogout;

}
