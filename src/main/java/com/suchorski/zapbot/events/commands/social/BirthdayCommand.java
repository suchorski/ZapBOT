package com.suchorski.zapbot.events.commands.social;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.events.commands.announce.BirthdayDisableCommand;
import com.suchorski.zapbot.events.commands.announce.BirthdaySetCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.social.UserBirthday;
import com.suchorski.zapbot.services.commands.social.BirthdayService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

@Component
public class BirthdayCommand extends BotCommand {

	@Autowired private BirthdayService birthdayService;
	@Autowired private BirthdaySetCommand birthdaySetCommand;
	@Autowired private BirthdayDisableCommand birthdayDisableCommand;

	@PostConstruct
	public void init() {
		this.name = "aniversario";
		this.help = "Define a data de aniversário";
		this.aliases = new String[] { "birthday", "aniversário" };
		this.arguments = "<data de aniversário>";
		this.children = new Command[] { birthdaySetCommand, birthdayDisableCommand };
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			StringBuffer date = new StringBuffer();
			CommandUtils.splitArgs(Utils.clear(event.getArgs()), date);
			LocalDate localDate = LocalDate.parse(date.toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			UserBirthday birthday = birthdayService.findById(event.getAuthor().getIdLong());
			birthdayService.setBirthday(birthday, localDate);
			CommandUtils.success(event, "data de aniversário definida com sucesso");
		} catch (DateTimeParseException e) {
			CommandUtils.warning(event, "data inválida");
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		} catch (NothingFoundException e) {
			CommandUtils.error(event, e.getLocalizedMessage());
		} catch (Exception e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		}
	}

}
