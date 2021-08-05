package com.suchorski.zapbot.models.commands.social;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.suchorski.zapbot.models.bot.BotUser;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "profile_background")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class UserProfileBackground implements Serializable {
	
	private static final long serialVersionUID = 2197242499311560230L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 64, updatable = false, nullable = false, unique = true)
	private String name;
	
	@Column(nullable = false)
	@ColumnDefault("b'0'")
	private Boolean visible = false;
	
	@Column(nullable = false)
	private Integer price;
	
	@Column(nullable = false)
	@ColumnDefault("current_timestamp")
	private LocalDate creationDate = LocalDate.now();
	
	@Lob
	@Column(insertable = true, updatable = false, nullable = false)
	private byte[] image;
	
	@ManyToMany(mappedBy = "backgrounds", cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<BotUser> users = new HashSet<>();
	
	public UserProfileBackground(String name, byte[] data, int price) {
		this.name = name;
		this.image = data;
		this.price = price;
	}

	public UserProfileBackground(String name, byte[] data) {
		this(name, data, 0);
	}

	public UserProfileBackground(String name, byte[] data, int price, boolean visible) {
		this(name, data, price);
		this.visible = visible;
	}
	
}
