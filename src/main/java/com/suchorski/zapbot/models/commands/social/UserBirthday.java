package com.suchorski.zapbot.models.commands.social;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.suchorski.zapbot.models.bot.BotUser;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "user_birthday")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class UserBirthday implements Serializable {
	
	private static final long serialVersionUID = -8635469289643230242L;

	@Id
	@Column(name = "user_id", updatable = false, nullable = false)
	private Long id;
	
	@Column(insertable = false, nullable = true)
	@ColumnDefault("NULL")
	private LocalDate date = null;
	
	@Column(insertable = false, nullable = false)
	@ColumnDefault("'1000-01-01 00:00:00'")
	private Instant lastSet = Instant.ofEpochMilli(0);
	
	@OneToOne(optional = false)
	@JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_userbirthday_user"))
	@OnDelete(action = OnDeleteAction.CASCADE)
	@MapsId
	private BotUser user;
	
	public UserBirthday(BotUser user) {
		this.id = user.getId();
		user.setBirthday(this);
	}
	
}
