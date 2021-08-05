package com.suchorski.zapbot.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class YoutubeUtils {

	public static Map<String, Object> generateSubscribeMap(String channelId, String youtubeSubscribeSecret, String url) {
		return generateMap(channelId, youtubeSubscribeSecret, url, "subscribe");
	}

	public static Map<String, Object> generateUnsubscribeMap(String channelId, String youtubeSubscribeSecret, String url) {		
		return generateMap(channelId, youtubeSubscribeSecret, url, "unsubscribe");
	}
	
	private static Map<String, Object> generateMap(String channelId, String youtubeSubscribeSecret, String url, String mode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("hub.callback", String.format("%s/youtube/subscribe", url));
		params.put("hub.topic", String.format("https://www.youtube.com/xml/feeds/videos.xml?channel_id=%s", channelId));
		params.put("hub.verify", "sync");
		params.put("hub.mode", mode);
		params.put("hub.secret", youtubeSubscribeSecret);
		return params;
	}

	public static String getChannelId(String arg) throws IOException {
		if (arg.startsWith("https://")) {
			Document document = Jsoup.connect(arg).get();
			Element link = document.selectFirst("link[type='application/rss+xml']");
			if (link != null) {
				String url = link.absUrl("href");
				return url.substring(url.length() - 24);
			}
		}
		return arg;
	}

}
