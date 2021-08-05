package com.suchorski.zapbot.events.commands.owner;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.components.Log;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.models.bot.BotUser;
import com.suchorski.zapbot.services.bot.UserService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

@Component
public class LevelMultiplierCommand extends BotCommand {

	@Autowired private Log log;
	@Autowired private UserService userService;

	@PostConstruct
	public void init() {
		this.name = "multiplicador";
		this.help = "Altera o multiplicador de XP";
		this.aliases = new String[] { "multiplier", "multi" };
		this.arguments = "<multiplicador> <@menção do usuário>";
		this.ownerCommand = true;
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			StringBuffer multiplierArg = new StringBuffer();
			StringBuffer userArg = new StringBuffer();
			CommandUtils.splitArgs(Utils.clear(event.getArgs()), multiplierArg, userArg);
			float multiplier = Float.parseFloat(multiplierArg.toString().replace(',', '.'));
			event.getMessage().getMentionedUsers().stream().findFirst().ifPresentOrElse(u -> {				
				BotUser user = userService.findOrCreate(u.getIdLong());
				userService.setMultiplier(user, multiplier);
				log.infof("%s agora tem um multiplicador de %.2f", u.getAsMention(), multiplier);
				CommandUtils.success(event, String.format("multiplicador agora é %.2f", multiplier));
			}, () -> {
				CommandUtils.warning(event, "você precisa mecionar o usuário que receberá as moedas");				
			});
		} catch (NumberFormatException e) {
			CommandUtils.warning(event, "número inválido");
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());				
		}
	}

}
