package com.suchorski.zapbot.events.commands.social;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.social.UserProfile;
import com.suchorski.zapbot.services.commands.social.ProfileService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

@Component
public class ProfileMessageCommand extends BotCommand {

	@Autowired private ProfileService profileService;

	@PostConstruct
	public void init() {
		this.name = "mensagem";
		this.help = "Define a frase do perfil";
		this.aliases = new String[] { "message", "frase" };
		this.arguments = "<frase>";
		this.childOnly = true;
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			StringBuffer message = new StringBuffer();
			CommandUtils.splitArgs(Utils.clear(event.getArgs()), 0, message);
			UserProfile profile = profileService.findById(event.getAuthor().getIdLong());
			profileService.changeMessage(profile, message.toString());
			CommandUtils.success(event, "mensagem alterada com sucesso");
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		} catch (NothingFoundException e) {
			CommandUtils.error(event, e.getLocalizedMessage());
		}
	}

}
