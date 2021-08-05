package com.suchorski.zapbot.events.commands.fun;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.utils.CommandUtils;

import kong.unirest.Callback;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.json.JSONObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

@Component
public class DogCommand extends BotCommand {

	@PostConstruct
	public void init() {
		this.name = "cachorro";
		this.help = "Mostra a imagem de um cachorro";
		this.aliases = new String[] { "dog", "cao" };
		this.cooldown = Constants.COOLDOWNS.SLOWER;
		this.cooldownScope = CooldownScope.GUILD;
		addPermissions(Permission.MESSAGE_EMBED_LINKS);
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		Unirest.get("https://dog.ceo/api/breeds/image/random").asJsonAsync(new Callback<JsonNode>(){
			@Override
			public void completed(HttpResponse<JsonNode> hr) {
				JSONObject json = hr.getBody().getObject();
				if ("success".equalsIgnoreCase(json.getString("status"))) {
					EmbedBuilder builder = new EmbedBuilder();
					builder.setColor(Constants.COLORS.DEFAULT);
					builder.setImage(json.getString("message"));
					event.reply(builder.build(), m -> {
						CommandUtils.success(event.getMessage());					
					}, m -> {
						CommandUtils.error(event.getMessage());
					});
				} else {					
					CommandUtils.error(event.getMessage());
				}
			}

			@Override
			public void failed(UnirestException e) {
				CommandUtils.error(event.getMessage());
			}

			@Override
			public void cancelled() {
				CommandUtils.error(event.getMessage());
			}
		});
	}

}
