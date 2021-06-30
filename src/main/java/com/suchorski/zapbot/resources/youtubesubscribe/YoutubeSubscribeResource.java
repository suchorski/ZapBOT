package com.suchorski.zapbot.resources.youtubesubscribe;

import java.util.List;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.suchorski.zapbot.components.Bot;
import com.suchorski.zapbot.components.Log;
import com.suchorski.zapbot.models.commands.youtubesubscribe.TextChannelYoutubeSubscribe;
import com.suchorski.zapbot.resources.youtubesubscribe.dtos.YoutubeDTO;
import com.suchorski.zapbot.services.commands.youtubesubscribe.YoutubeSubscribeService;

@RestController
@RequestMapping("/youtube")
public class YoutubeSubscribeResource {

	@Value("${bot.youtube.subscribe.secret}") private String youtubeSubscribeSecret;

	@Autowired private Log log;
	@Autowired private Bot bot;
	@Autowired private YoutubeSubscribeService youtubeSubscribeService;

	@GetMapping("/subscribe")
	public ResponseEntity<String> verify(@RequestParam("hub.challenge") String challenge) {
		return ResponseEntity.ok(challenge);
	}

	@PostMapping(value = "/subscribe", consumes = MediaType.APPLICATION_ATOM_XML_VALUE)
	public ResponseEntity<Void> updated(@RequestBody String body, @RequestHeader("X-Hub-Signature") String signature) {
		try {
			String hash = new HmacUtils(HmacAlgorithms.HMAC_SHA_1, youtubeSubscribeSecret).hmacHex(body.getBytes());
			if (hash.equals(signature.replaceAll("[^=]+=", ""))) {
				XmlMapper mapper = new XmlMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				YoutubeDTO youtubeDTO = mapper.readValue(body, YoutubeDTO.class);
				List<TextChannelYoutubeSubscribe> youtubeSubscribes = youtubeSubscribeService.findAllFromYoutubeChannelId(youtubeDTO.getEntry().getChannelId());
				String message = String.format("Saiu video novo no canal @everyone\n%s", youtubeDTO.getEntry().getUrl());
				for (TextChannelYoutubeSubscribe ys : youtubeSubscribes) {
					bot.getJda().getTextChannelById(ys.getId().getTextChannelId()).sendMessage(message).queue();
				}
			}
		} catch (JsonProcessingException e) {
			log.dangerf(e.getLocalizedMessage());
		}
		return ResponseEntity.noContent().build();
	}

}
