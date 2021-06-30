package com.suchorski.zapbot.events.abstracts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.exceptions.BannedException;
import com.suchorski.zapbot.exceptions.NoRoleRequiredException;
import com.suchorski.zapbot.models.bot.BotMember;
import com.suchorski.zapbot.models.bot.BotUser;
import com.suchorski.zapbot.services.bot.MemberService;
import com.suchorski.zapbot.services.bot.UserService;
import com.suchorski.zapbot.utils.CommandUtils;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.ChannelType;

@Component
public abstract class BotCommand extends Command implements Ordered {
	
	@Autowired private UserService userService;
	@Autowired private MemberService memberService;

	@Getter @Setter protected boolean childOnly = false;

	@Override
	public String getArguments() {
		return this.arguments == null ? "" : this.arguments; 
	}

	@Override
	public String getCooldownError(CommandEvent event, int remaining) {
		return remaining <= 0 ? null : String.format("%s Comando bloqueado por mais %d %s!", event.getClient().getWarning(), remaining, remaining != 1 ? "segundos" : "segundo");
	}

	protected void checkRole(CommandEvent event, String requiredRole) throws NoRoleRequiredException {
		if (requiredRole != null && (!event.isFromType(ChannelType.TEXT) || event.getMember().getRoles().stream().noneMatch(r -> r.getName().equalsIgnoreCase(requiredRole)))) {
			throw new NoRoleRequiredException(String.format("você precisa ter o cargo **%s**", requiredRole));
		}
	}

	protected String getZapRole(CommandEvent event) {
		return null;
	}
	
	protected abstract void zapExecute(CommandEvent event);

	@Override
	protected void execute(CommandEvent event) {
		try {
			BotUser user = userService.findOrCreate(event.getAuthor().getIdLong());
			userService.checkBanned(user);
			BotMember member = memberService.findOrCreate(event.getAuthor().getIdLong(), event.getGuild().getIdLong());
			memberService.checkBanned(member);
			event.getChannel().sendTyping().queue();
			checkRole(event, getZapRole(event));
			zapExecute(event);
		} catch (NoRoleRequiredException e) {
			CommandUtils.error(event, e.getLocalizedMessage());
		} catch (BannedException e) {
			CommandUtils.error(event, e.getLocalizedMessage());
		}
	}
	
	@Override
	public int getOrder() {
		int order = 0;
		int calc = 1;
		for (int i = name.length() - 1; i >= 0; --i) {
			int n = name.charAt(i) - 'a';
			order += n * calc;
			calc *= 10;
		}
		return order;
	}

}
