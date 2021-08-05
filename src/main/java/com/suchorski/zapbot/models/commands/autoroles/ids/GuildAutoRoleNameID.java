package com.suchorski.zapbot.models.commands.autoroles.ids;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class GuildAutoRoleNameID implements Serializable {
	
	private static final long serialVersionUID = 6322475479741097800L;

	@Column(length = 32, updatable = false, nullable = false)
	private String name;

	@Column(updatable = false, nullable = false)
	private Long guildId;

}
