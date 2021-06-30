package com.suchorski.zapbot.resources.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suchorski.zapbot.models.bot.BotUser;
import com.suchorski.zapbot.resources.social.dtos.CoinsDTO;
import com.suchorski.zapbot.services.bot.UserService;
import com.suchorski.zapbot.services.commands.social.CoinsService;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

@RestController
@RequestMapping("/coins")
public class CoinsResource {
	
	@Value("${bot.captcha.secret}") private String captchaSecret;

	@Autowired private UserService userService;
	@Autowired private CoinsService coinsService;

	@GetMapping(value = "/{discord_token}/{captcha_token}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CoinsDTO> getCoins(@PathVariable("discord_token") String discordToken, @PathVariable("captcha_token") String captchaToken) {
		HttpResponse<JsonNode> captchaResponse = Unirest.post("https://hcaptcha.com/siteverify").field("response", captchaToken).field("secret", captchaSecret).asJson();
		if (!captchaResponse.getBody().getObject().getBoolean("success")) {
			return ResponseEntity.ok(new CoinsDTO(false, "Falha ao resolver o captcha", 0L, false));
		}
		HttpResponse<JsonNode> discordResponse = Unirest.get("https://discord.com/api/oauth2/@me").header("Authorization", String.format("Bearer %s", discordToken)).asJson();
		JSONObject discordUser = discordResponse.getBody().getObject().getJSONObject("user");
		if (discordUser != null) {
			long id = discordUser.getLong("id");
			BotUser user = userService.findOrCreate(id);
			try {
				long coins = coinsService.getCoins(user.getCoins());
				return ResponseEntity.ok(new CoinsDTO(true, "", coins, false));
			} catch (Exception e) {
				return ResponseEntity.ok(new CoinsDTO(false, e.getLocalizedMessage(), 0L, false));
			}
		} else {
			return ResponseEntity.ok(new CoinsDTO(false, "Falha ao processar usuário do Discord", 0L, true));
		}
	}

}
