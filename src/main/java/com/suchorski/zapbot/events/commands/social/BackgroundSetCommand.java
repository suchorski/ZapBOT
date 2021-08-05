package com.suchorski.zapbot.events.commands.social;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.social.UserProfile;
import com.suchorski.zapbot.services.commands.social.ProfileBackgroundService;
import com.suchorski.zapbot.services.commands.social.ProfileService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

@Component
public class BackgroundSetCommand extends BotCommand {

	@Autowired private ProfileService profileService;
	@Autowired private ProfileBackgroundService profileBackgroundService;

	@PostConstruct
	public void init() {
		this.name = "definir";
		this.help = "Define o fundo do perfil";
		this.aliases = new String[] { "define", "set" };
		this.arguments = "<código>";
		this.childOnly = true;
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			StringBuffer codeArg = new StringBuffer();
			CommandUtils.splitArgs(Utils.clear(event.getArgs()), codeArg);
			int code = Integer.parseInt(codeArg.toString());
			UserProfile profile = profileService.findById(event.getAuthor().getIdLong());
			profileService.set(profile, profileBackgroundService.findById(code));
			CommandUtils.success(event, "fundo de perfil definido com sucesso");
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		} catch (NumberFormatException e) {
			CommandUtils.warning(event, "número incorreto");			
		} catch (NothingFoundException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		}
	}

}
