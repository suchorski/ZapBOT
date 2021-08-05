package com.suchorski.zapbot.events.commands.social;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.components.Log;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.exceptions.NotEnoughCoinsException;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.social.UserCoins;
import com.suchorski.zapbot.models.commands.social.UserProfile;
import com.suchorski.zapbot.services.commands.social.CoinsService;
import com.suchorski.zapbot.services.commands.social.ProfileService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

import net.dv8tion.jda.api.entities.User;

@Component
public class RecommendationCommand extends BotCommand {

	@Autowired private Log log;
	@Autowired private CoinsService coinsService;
	@Autowired private ProfileService profileService;

	@PostConstruct
	public void init() {
		this.name = "recomendar";
		this.help = String.format("Dá uma reputação para alguém (Moedas: %d)", Constants.COSTS.RECOMMENDATION);
		this.aliases = new String[] { "recommend" };
		this.cooldown = Constants.COOLDOWNS.SLOWER;
		this.cooldownScope = CooldownScope.USER;
		this.arguments = "<@menção ao usuário recomendado>";
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			CommandUtils.checkNumArgs(Utils.clear(event.getArgs()), 1);
			List<User> users = event.getMessage().getMentionedUsers();
			if (users.size() == 1) {
				User u = users.get(0);
				if (event.getAuthor().getIdLong() != u.getIdLong() && !u.isBot()) {
					UserCoins userCoins = coinsService.findById(event.getAuthor().getIdLong());
					UserProfile recommendedProfile = profileService.findById(u.getIdLong());
					profileService.recommend(recommendedProfile, userCoins);
					log.infof("%s recomendou %s", event.getAuthor().getAsMention(), u.getAsMention());
					CommandUtils.success(event, "recomendação adicionada com sucesso");
				} else {					
					CommandUtils.warning(event, "você não pode recomendar você mesmo ou um BOT");
				}
			} else {
				CommandUtils.warning(event, "você só pode recomendar uma pessoa por vez");
			}
		} catch (NotEnoughCoinsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage().toLowerCase());
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage().toLowerCase());
		} catch (NothingFoundException e) {
			CommandUtils.error(event, e.getLocalizedMessage().toLowerCase());
		}
	}

}
