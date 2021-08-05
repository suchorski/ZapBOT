package com.suchorski.zapbot.events.commands.social;

import java.awt.Color;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.exceptions.NotEnoughCoinsException;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.social.UserProfile;
import com.suchorski.zapbot.services.commands.social.ProfileService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

@Component
public class ProfileColorCommand extends BotCommand {

	@Autowired private ProfileService profileService;

	@PostConstruct
	public void init() {
		this.name = "cor";
		this.help = String.format("Define a cor do perfil (Moedas: %d)", Constants.COSTS.COLOR);
		this.aliases = new String[] { "color" };
		this.arguments = "<vermelho verde azul | #RRGGBB>";
		this.childOnly = true;
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			int r, g, b;
			String args = event.getArgs().trim().toLowerCase();
			if (args.matches("#[a-fA-F0-9]{6}")) {
				r = Integer.parseInt(args.substring(1, 3), 16);
				g = Integer.parseInt(args.substring(3, 5), 16);
				b = Integer.parseInt(args.substring(5, 7), 16);
			} else {
				StringBuffer rArg = new StringBuffer();
				StringBuffer gArg = new StringBuffer();
				StringBuffer bArg = new StringBuffer();
				CommandUtils.splitArgs(Utils.clear(args), rArg, gArg, bArg);
				r = Integer.parseInt(rArg.toString());
				g = Integer.parseInt(gArg.toString());
				b = Integer.parseInt(bArg.toString());
			}
			UserProfile profile = profileService.findById(event.getAuthor().getIdLong());
			profileService.changeProfileColor(profile, new Color(r, g, b));
			CommandUtils.success(event, "cor alterada com sucesso");
		} catch (NotEnoughCoinsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		} catch (IllegalArgumentException e) {
			CommandUtils.warning(event, "números incorretos");
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		} catch (NothingFoundException e) {
			CommandUtils.error(event, e.getLocalizedMessage());
		}
	}

}
