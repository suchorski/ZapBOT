package com.suchorski.zapbot.events.commands.administration;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.models.bot.BotMember;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.bot.MemberService;
import com.suchorski.zapbot.utils.CommandUtils;

import net.dv8tion.jda.api.entities.User;

@Component
public class UnblockCommand extends BotCommand {

	@Autowired private GuildService guildService;
	@Autowired private MemberService memberService;

	@PostConstruct
	public void init() {
		this.name = "desbloquear";
		this.help = "Desbane usuários do BOT na guilda";
		this.aliases = new String[] { "unblock" };
		this.arguments = "<menção dos usuários>";
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getOperator();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		List<User> users = event.getMessage().getMentionedUsers();
		if (!users.isEmpty()) {
			for (User u : users) {
				BotMember member = memberService.findOrCreate(u.getIdLong(), event.getGuild().getIdLong());
				memberService.unban(member);
			}
			CommandUtils.success(event, "membros desbanidos do BOT na guilda");
		} else {
			CommandUtils.warning(event, "você precisa mencionar os usuários a serem desbanidos");
		}
	}

}
