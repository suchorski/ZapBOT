package com.suchorski.zapbot.events.commands.social;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.models.bot.BotMember;
import com.suchorski.zapbot.services.bot.MemberService;
import com.suchorski.zapbot.utils.CommandUtils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

@Component
public class ProfileTopCommand extends BotCommand {

	@Autowired private MemberService memberService;

	@PostConstruct
	public void init() {
		this.name = "top";
		this.help = "Mostra o top rank dos usuários";
		this.aliases = new String[] { "best" };
		this.cooldown = Constants.COOLDOWNS.FAST;
		this.cooldownScope = CooldownScope.CHANNEL;
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		List<BotMember> top = memberService.top3();
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Constants.COLORS.DEFAULT);
		builder.setAuthor("TOP 3!");
		int i = 0;
		for (BotMember m : top) {
			User user = event.getJDA().retrieveUserById(m.getId().getUserId()).complete(); 
			builder.addField(String.format("Usuário %s", Constants.PLACES[i++]), user.getAsMention(), true);
			builder.addField("Nível", String.format("%d", m.getLevel()), true);
			builder.addField("XP", String.format("%d / %d", m.getXp(), m.getMaxXp()), true);
		}
		event.reply(builder.build(), m -> {
			CommandUtils.success(event.getMessage());
		}, m -> {
			CommandUtils.error(event.getMessage());
		});
	}

}
