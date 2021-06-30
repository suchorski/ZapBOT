package com.suchorski.zapbot.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class YoutubeUtils {

	public static Map<String, Object> generateMap(String channelId, String youtubeSubscribeSecret, String url) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("hub.callback", String.format("%s/youtube/subscribe", url));
		params.put("hub.topic", String.format("https://www.youtube.com/xml/feeds/videos.xml?channel_id=%s", channelId));
		params.put("hub.verify", "sync");
		params.put("hub.mode", "subscribe");
		params.put("hub.secret", youtubeSubscribeSecret);
		return params;
	}

	public static String getChannelId(StringBuffer arg) throws IOException {
		if (arg.toString().startsWith("https://")) {
			Document document = Jsoup.connect(arg.toString()).get();
			Element link = document.selectFirst("link[type='application/rss+xml']");
			if (link != null) {
				String url = link.absUrl("href");
				return url.substring(url.length() - 24);
			}
		}
		return arg.toString();
	}

}
