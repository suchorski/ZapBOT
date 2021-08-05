package com.suchorski.zapbot.events.futures;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.suchorski.zapbot.models.commands.lucky.ids.TextChannelDrawID;
import com.suchorski.zapbot.services.commands.lucky.DrawService;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;

public class DrawFuture implements Runnable {
	
	private long textChannelId;
	private long messageId;
	private String emoji;
	private int quantity;
	private String prize;
	private JDA jda;
	private DrawService drawService;
	
	public DrawFuture(long textChannelId, long messageId, String emoji, int quantity, String prize, JDA jda, DrawService drawService) {
		this.textChannelId = textChannelId;
		this.messageId = messageId;
		this.emoji = emoji;
		this.quantity = quantity;
		this.prize = prize;
		this.jda = jda;
		this.drawService = drawService;
	}

	@Override
	public void run() {
		jda.getTextChannelById(textChannelId).retrieveMessageById(messageId).queue(m -> {
			m.retrieveReactionUsers(emoji).queue(l -> {
				List<User> users = l.stream().filter(u -> u.getIdLong() != jda.getSelfUser().getIdLong()).collect(Collectors.toList());
				if (users.size() >= quantity) {
					Random random = new Random();
					Set<User> sorted = new HashSet<User>(quantity);
					while (sorted.size() != quantity) {
						sorted.add(users.get(random.nextInt(users.size())));
					}
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("E o %s do prêmio (%s) %s:", quantity == 1 ? "vencedor" : "vencedores", prize, quantity == 1 ? "foi" : "foram"));
					sorted.forEach(s -> sb.append(String.format(" %s", s.getAsMention())));
					m.reply(sb.toString()).queue();
				} else {
					jda.getTextChannelById(textChannelId).sendMessage("Sorteio cancelado! Quantidade de inscritos é menor que o número de sorteados.").queue();
				}
			}, e -> {
				// Draw canceled for message deleted
			});
		}, e -> {			
			// Draw canceled for message deleted
		});
		drawService.deleteById(new TextChannelDrawID(messageId, textChannelId));
	}

}
