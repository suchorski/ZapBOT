package com.suchorski.zapbot.events.abstracts;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

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
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;

@Component
public abstract class BotCommand extends CommandTranslation implements Ordered {
	
	@Value("${bot.owner.id}") private String OWNER_ID;
	
	@Autowired private UserService userService;
	@Autowired private MemberService memberService;

	@Getter @Setter protected boolean childOnly = false;
	
	public BotCommand() {
		this.botPermissions = new Permission[] { Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_EXT_EMOJI };
	}

	@Override
	public String getArguments() {
		return this.arguments == null ? "" : this.arguments; 
	}
	
	public void addPermissions(Permission... permissions) {
		Set<Permission> perms = new HashSet<Permission>(this.botPermissions.length + permissions.length);
		perms.addAll(Arrays.asList(this.botPermissions));
		perms.addAll(Arrays.asList(permissions));
		this.botPermissions = perms.toArray(Permission[]::new);
	}

	protected void checkRole(CommandEvent event, String requiredRole) throws NoRoleRequiredException {
		if (event.getAuthor().getId().equals(OWNER_ID) || event.getGuild().getOwnerIdLong() == event.getAuthor().getIdLong()) {
			return;
		}
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
