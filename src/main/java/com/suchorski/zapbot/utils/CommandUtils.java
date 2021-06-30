package com.suchorski.zapbot.utils;

import java.util.Arrays;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.vdurmont.emoji.EmojiParser;

import net.dv8tion.jda.api.entities.Message;

public class CommandUtils {
	
	public static void addReation(Message message, String unicode) {
		message.addReaction(unicode).queue();
	}
	
	public static void delReation(Message message, String unicode) {
		message.removeReaction(unicode).queue();
	}
	
	public static void success(Message message) {
		addReation(message, Constants.EMOJIS.SUCCESS);
	}
	
	public static void warning(Message message) {
		addReation(message, Constants.EMOJIS.WARNING);
	}
	
	public static void error(Message message) {
		addReation(message, Constants.EMOJIS.ERROR);
	}
	
	public static void success(CommandEvent event, String message) {
		event.reply(String.format("%s, %s!", event.getAuthor().getAsMention(), message), m -> {
			success(event.getMessage());
		}, m -> {			
			error(event.getMessage());
		});
	}
	
	public static void warning(CommandEvent event, String message) {
		event.reply(String.format("%s, %s!", event.getAuthor().getAsMention(), message), m -> {
			warning(event.getMessage());
		}, m -> {			
			error(event.getMessage());
		});
	}

	public static void error(CommandEvent event, String message) {
		event.reply(String.format("%s, %s!", event.getAuthor().getAsMention(), message), m -> {
			error(event.getMessage());
		}, m -> {			
			error(event.getMessage());
		});
	}
	
	public static void checkNumArgs(String args, int quantity) throws CommandUtilsException {
		int size = args.isBlank() ? 0 : args.replaceAll("\\S", "").length() + 1;
		if (size != quantity) {
			throw new CommandUtilsException("número de argumentos diferente do número de parâmetros");
		}
	}

	public static void checkMinNumArgs(String args, int quantity) throws CommandUtilsException {
		int size = args.isBlank() ? 0 : args.replaceAll("\\S", "").length() + 1;
		if (size < quantity) {
			throw new CommandUtilsException("número de argumentos é menor que o número de parâmetros");
		}
	}
	
	public static void checkNumArgsBetween(String[] args, int min, int max) throws CommandUtilsException {
		if (args.length < min || args.length > max) {
			throw new CommandUtilsException("número de argumentos está fora do intervalo");
		}
	}
	
	public static void splitArgs(String args, StringBuffer... params) throws CommandUtilsException {
		checkNumArgs(args, params.length);
		String[] split = args.trim().split("\\s");
		for (int i = 0; i < split.length; ++i) {
			params[i].insert(0, split[i]);
		}
	}

	public static void splitArgs(String args, int lastIndex, StringBuffer... params) throws CommandUtilsException {
		checkMinNumArgs(args, lastIndex + 1);
		String[] split = args.trim().split("\\s");
		for (int i = 0; i < params.length - 1; ++i) {
			params[i].insert(0, split[i]);
		}
		params[params.length - 1].insert(0, String.join(" ", Arrays.copyOfRange(split, lastIndex, split.length)).trim());
	}
	
	public static void stringMinLength(String string, int size) throws CommandUtilsException {
		if (string.length() < size) {
			throw new CommandUtilsException(String.format("tamanho do texto é menor que %d", size));
		}
	}
	
	public static void stringMaxLength(String string, int size) throws CommandUtilsException {
		if (string.length() > size) {
			throw new CommandUtilsException(String.format("tamanho do texto é maior que %d", size));
		}
	}
	
	public static void isOneEmoji(String emoji) throws CommandUtilsException {
		if (EmojiParser.extractEmojis(emoji.toString()).size() != 1) {
			throw new CommandUtilsException("precis ser um emoji único padrão");
		}
	}

}
