package com.suchorski.zapbot.models.commands.lucky;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.models.bot.BotTextChannel;
import com.suchorski.zapbot.models.commands.lucky.ids.TextChannelDrawID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "textchannel_draw")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class TextChannelDraw implements Serializable {
	
	private static final long serialVersionUID = -2063353883000495954L;

	@EmbeddedId
	private TextChannelDrawID id;
	
	@Column(updatable = false, nullable = false)
	private Instant scheduledDate;
	
	@Column(length = 16, updatable = false, nullable = false)
	@ColumnDefault("'" + Constants.EMOJIS.MARK + "'")
	private String emoji = Constants.EMOJIS.MARK;
	
	@Column(updatable = false, nullable = false)
	private Integer quantity;
	
	@Column(length = 1024, updatable = false, nullable = false)
	private String prize;
	
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_textchanneldraw_textchannel"))
	@MapsId("textChannelId")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private BotTextChannel textChannel;
	
	public TextChannelDraw(long messageId, BotTextChannel textChannel, Instant scheduledDate, int quantity, String prize) {
		this.id = new TextChannelDrawID(messageId, textChannel.getId());
		this.scheduledDate = scheduledDate;
		this.quantity = quantity;
		this.prize = prize;
		textChannel.addDraw(this);
	}
	
}
