package com.suchorski.zapbot.events.commands.social;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.models.bot.BotMember;
import com.suchorski.zapbot.services.bot.MemberService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.SocialProfileGenerator;

import kong.unirest.UnirestException;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

@Component
public class ProfileCommand extends BotCommand {

	@Autowired private MemberService memberService;
	@Autowired private ProfileMessageCommand profileMessageCommand;
	@Autowired private ProfileColorCommand profileColorCommand;
	@Autowired private ProfileTopCommand profileTopCommand;
	@Autowired private ProfileGlobalCommand profileGlobalCommand;

	@PostConstruct
	public void init() {
		this.name = "perfil";
		this.help = "Mostra o nível do usuário";
		this.aliases = new String[] { "profile", "rank" };
		this.cooldown = Constants.COOLDOWNS.FAST;
		this.cooldownScope = CooldownScope.USER;
		this.arguments = "[@usuario]";
		this.children = new Command[] { profileColorCommand, profileMessageCommand, profileTopCommand, profileGlobalCommand };
		addPermissions(Permission.MESSAGE_ATTACH_FILES);
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			List<Member> members = event.getMessage().getMentionedMembers();
			Member member = members.isEmpty() ? event.getMember() : members.get(0);
			if (!member.getUser().isBot()) {
				BotMember botMember = memberService.findOrCreate(member.getIdLong(), event.getGuild().getIdLong());
				event.getChannel().sendFile(SocialProfileGenerator.generate(botMember, member.getEffectiveName(), member.getUser().getName(), member.getUser().getDiscriminator(), member.getUser().getEffectiveAvatarUrl(), member.getOnlineStatus(), member.getColor()), String.format("%s.png", member.getUser().getName())).queue(m -> {
					CommandUtils.success(event.getMessage());
				}, m -> {
					CommandUtils.error(event.getMessage());
				});
			} else {
				CommandUtils.warning(event, "usuário é um bot e não possui perfil");
			}
		} catch (IOException | UnirestException e) {
			CommandUtils.error(event, e.getLocalizedMessage());
		}
	}

}
