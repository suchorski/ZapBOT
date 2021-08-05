package com.suchorski.zapbot.models.commands.social;

import java.io.Serializable;
import java.time.Instant;

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

import com.suchorski.zapbot.exceptions.NotEnoughCoinsException;
import com.suchorski.zapbot.models.bot.BotUser;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "user_coins")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class UserCoins implements Serializable {
	
	private static final long serialVersionUID = -8868025498531068848L;

	@Id
	@Column(name = "user_id", updatable = false, nullable = false)
	private Long id;
	
	@Column(insertable = false, nullable = false)
	@ColumnDefault("0")
	private Long quantity = 0L;
	
	@Column(name = "last_get_coins", insertable = false, nullable = false)
	@ColumnDefault("'1000-01-01 00:00:00'")
	private Instant last = Instant.ofEpochMilli(0);
	
	@OneToOne(optional = false)
	@JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_usercoins_user"))
	@OnDelete(action = OnDeleteAction.CASCADE)
	@MapsId
	private BotUser user;
	
	public UserCoins(BotUser user) {
		this.id = user.getId();
		user.setCoins(this);
	}
	
	public void addCoins(long coins) {
		this.quantity += coins;
	}

	public void subCoins(long coins) throws NotEnoughCoinsException {
		if (coins > this.quantity) {
			throw new NotEnoughCoinsException("moedas insuficientes");
		}
		this.quantity -= coins;
	}
	
}
