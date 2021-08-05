package com.suchorski.zapbot.events.commands.social;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.exceptions.NotEnoughCoinsException;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.exceptions.SomethingFoundException;
import com.suchorski.zapbot.services.bot.UserService;
import com.suchorski.zapbot.services.commands.social.ProfileBackgroundService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

@Component
public class BackgroundBuyCommand extends BotCommand {

	@Autowired private UserService userService;
	@Autowired private ProfileBackgroundService profileBackgroundService;

	@PostConstruct
	public void init() {
		this.name = "comprar";
		this.help = "compra o fundo do perfil";
		this.aliases = new String[] { "buy" };
		this.arguments = "<código>";
		this.childOnly = true;
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			StringBuffer codeArg = new StringBuffer();
			CommandUtils.splitArgs(Utils.clear(event.getArgs()), codeArg);
			int code = Integer.parseInt(codeArg.toString());
			userService.buyProfileBackground(userService.findOrCreate(event.getAuthor().getIdLong()), profileBackgroundService.findById(code));
			CommandUtils.success(event, "fundo de perfil comprado com sucesso");
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		} catch (NumberFormatException e) {
			CommandUtils.warning(event, "número incorreto");			
		} catch (NothingFoundException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		} catch (SomethingFoundException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());			
		} catch (NotEnoughCoinsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());			
		}
	}

}
