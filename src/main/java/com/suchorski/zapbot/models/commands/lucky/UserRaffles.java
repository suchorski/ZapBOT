package com.suchorski.zapbot.models.commands.lucky;

import java.io.Serializable;

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

@Entity(name = "user_raffles")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class UserRaffles implements Serializable {
	
	private static final long serialVersionUID = -322536953824652812L;

	@Id
	@Column(name = "user_id", updatable = false, nullable = false)
	private Long id;
	
	@Column(insertable = false, nullable = false)
	@ColumnDefault("0")
	private Integer quantity = 0;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_userraflles_user"))
	@MapsId
	@OnDelete(action = OnDeleteAction.CASCADE)
	private BotUser user;
	
	public UserRaffles(BotUser user) {
		this.id = user.getId();
		user.setRaffles(this);
	}

	public void addRaflles(int quantity) {
		this.quantity += this.quantity;
	}
	
}
