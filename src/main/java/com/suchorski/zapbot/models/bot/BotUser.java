package com.suchorski.zapbot.models.bot;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.lucky.UserRaffles;
import com.suchorski.zapbot.models.commands.social.UserAutoReact;
import com.suchorski.zapbot.models.commands.social.UserBirthday;
import com.suchorski.zapbot.models.commands.social.UserCoins;
import com.suchorski.zapbot.models.commands.social.UserProfile;
import com.suchorski.zapbot.models.commands.social.UserProfileBackground;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity(name = "user")
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class BotUser implements Serializable {

	private static final long serialVersionUID = 1050081941603823137L;

	@NonNull
	@Id
	private Long id;

	@Column(insertable = false, nullable = false)
	@ColumnDefault("b'0'")
	private Boolean banned = false;

	@Column(insertable = false, updatable = false, nullable = false)
	@ColumnDefault("current_timestamp")
	private Instant creationDate = Instant.now();

	@Column(insertable = false, nullable = false)
	@ColumnDefault("1")
	private Float xpMultiplier = 1f;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private UserAutoReact autoReact;
	
	@Column(insertable = false, nullable = false)
	@ColumnDefault("0")
	private Integer premiumCoins = 0;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "user_has_background",
			joinColumns = { @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_user")) },
			inverseJoinColumns = { @JoinColumn(name = "background_id", foreignKey = @ForeignKey(name = "fk_user_background")) }
			)
	@OrderBy("creation_date asc")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<UserProfileBackground> backgrounds = new LinkedHashSet<>();

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private UserBirthday birthday;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private UserCoins coins;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private UserProfile profile;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private UserRaffles raffles;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<BotMember> members = new HashSet<>();
	
	public void setAutoReact(UserAutoReact autoReact) {
		autoReact.setUser(this);
		this.autoReact = autoReact;
	}

	public void addPremiumCoins() {
		++this.premiumCoins;
	}

	public void addBackground(UserProfileBackground background) {
		background.getUsers().add(this);
		this.backgrounds.add(background);
	}

	public void delBackground(UserProfileBackground background) {
		background.getUsers().remove(this);
		this.backgrounds.remove(background);
	}

	public void setBirthday(UserBirthday birthday) {
		birthday.setUser(this);
		this.birthday = birthday;
	}

	public void setCoins(UserCoins coins) {
		coins.setUser(this);
		this.coins = coins;
	}

	public void setProfile(UserProfile profile) {
		profile.setUser(this);
		this.profile = profile;
	}

	public void setRaffles(UserRaffles raffles) {
		raffles.setUser(this);
		this.raffles = raffles;
	}

	public void addMember(BotMember member) {
		member.setUser(this);
		this.members.add(member);
	}

	public void delMember(BotMember member) throws NothingFoundException {
		if (this.members.remove(member)) {
			member.setUser(null);
		} else {
			throw new NothingFoundException("membro não encontrado");
		}
	}
	
	@Transient
	public boolean isPremium() {
		return premiumCoins >= Constants.PREMIUM.USER_COINS;
	}

}
