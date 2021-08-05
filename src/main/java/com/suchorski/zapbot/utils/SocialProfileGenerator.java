package com.suchorski.zapbot.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Map;

import javax.imageio.ImageIO;

import com.suchorski.zapbot.models.bot.BotMember;
import com.suchorski.zapbot.models.bot.BotUser;

import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import net.dv8tion.jda.api.OnlineStatus;

public class SocialProfileGenerator {
	
	private static final int IMG_WIDTH = 600;
	private static final int IMG_HEIGHT = 300;
	private static final int HEADER_MIN_HEIGHT = 60;
	private static final int HEADER_MAX_HEIGHT = 110;
	private static final int HEADER_X_FIRST = IMG_WIDTH / 3;
	private static final int HEADER_X_SECOND = (int) ((float) IMG_WIDTH / 2.5f);
	private static final int FOOTER_HEIGHT = 80;
	private static final int WIDTH = IMG_WIDTH;
	private static final int HEIGHT = IMG_HEIGHT + HEADER_MIN_HEIGHT + FOOTER_HEIGHT;
	
	private static final Font FONT_L = new Font("fixedsys", 0, 35);
	private static final Font FONT_M = new Font("fixedsys", 0, 20);
	private static final Font FONT_X = new Font("fixedsys", 0, 18);
	
	public static InputStream generate(BotMember member, String nickname, String name, String number, String url, OnlineStatus status, Color guildColor) throws IOException, UnirestException {
		BufferedImage avatar = ImageIO.read(new ByteArrayInputStream(Unirest.get(url).asBytes().getBody()));
		try (ByteArrayInputStream bis = new ByteArrayInputStream(member.getUser().getProfile().getBackground().getImage())) {
			BufferedImage background = ImageIO.read(bis);
			BufferedImage bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
			Color statusColor;
			switch (status) {
			case ONLINE:
				statusColor = Color.GREEN;
				break;
			case IDLE:
				statusColor = Color.ORANGE;
				break;
			case DO_NOT_DISTURB:
				statusColor = Color.RED;
				break;
			default:
				statusColor = Color.GRAY;
			}
			draw(bi, nickname, name, number, avatar, background, member.getUser(), member, guildColor, statusColor);
			try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
				ImageIO.write(bi, "png", output);
				return new ByteArrayInputStream(output.toByteArray());
			}
		}
	}
	
	private static void draw(BufferedImage bi, String nickname, String name, String number, BufferedImage avatar, BufferedImage background, BotUser user, BotMember member, Color guildColor, Color statusColor) {
		Color constrastingUserColor = getContrastingColors(user.getProfile().getColor());
		Graphics2D g = bi.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		drawBackground(g, background);
		drawFrame(g, user.getProfile().getColor());
		drawAvatar(g, avatar, user.getProfile().getColor(), guildColor, statusColor);
		drawNicks(g, nickname, name, number, constrastingUserColor);
		drawInfos(g, user, member, constrastingUserColor);
		drawTextInRectJustified(g, user.getProfile().getMessage(), constrastingUserColor, WIDTH - 10, 5, HEIGHT - FOOTER_HEIGHT, FONT_M);
	}
	
	private static void drawBackground(Graphics2D g, BufferedImage background) {
		g.drawImage(background, 0, HEADER_MIN_HEIGHT, IMG_WIDTH, IMG_HEIGHT, null);
	}

	private static void drawFrame(Graphics2D g, Color color) {
		g.setColor(color);
		g.fillPolygon(new int[] {0, WIDTH, WIDTH, HEADER_X_SECOND, HEADER_X_FIRST, 0}, new int[] {0, 0, HEADER_MAX_HEIGHT, HEADER_MAX_HEIGHT, HEADER_MIN_HEIGHT, HEADER_MIN_HEIGHT}, 6);
		g.fillRect(0, HEIGHT - FOOTER_HEIGHT, WIDTH, FOOTER_HEIGHT);
	}
	
	private static void drawAvatar(Graphics2D g, BufferedImage avatar, Color userColor, Color guildColor, Color statusColor) {
		Shape clip = g.getClip();
		g.setColor(guildColor);
		g.fillOval(7, 7, 106, 106);
		g.setClip(new Ellipse2D.Float(10, 10, 100, 100));
		g.drawImage(avatar, 10, 10, 100, 100, null);
		g.setClip(clip);
		g.setColor(statusColor);
		g.fillOval(85, 10, 20, 20);
		g.setColor(userColor);
		g.drawOval(85, 10, 20, 20);
	}
	
	private static void drawNicks(Graphics2D g, String nickname, String name, String number, Color color) {
		drawText(g, nickname, color, 120, 0, false, true, FONT_M);
		drawText(g, String.format("%s#%s", name, number), color, 120, 50, false, false, FONT_X);
	}
	
	private static void drawInfos(Graphics2D g, BotUser user, BotMember member, Color color) {
		int height = g.getFontMetrics(FONT_X).getHeight();
		drawText(g, String.format("Nível: %d", member.getLevel()), color, WIDTH - 5, 0, true, true, FONT_X);
		drawText(g, String.format("XP: %d / %d", member.getXp(), member.getMaxXp()), color, WIDTH - 5, 1 + height, true, true, FONT_X);
		drawText(g, String.format("Moedas: %d", user.getCoins().getQuantity()), color, WIDTH - 5, 2 + height * 2, true, true, FONT_X);
		drawText(g, String.format("Rifas: %d", user.getRaffles().getQuantity()), color, WIDTH - 5, 3 + height * 3, true, true, FONT_X);
		FontMetrics metrics = g.getFontMetrics(FONT_L);
		int yReps = (HEADER_MAX_HEIGHT - HEADER_MIN_HEIGHT - metrics.getHeight()) / 2 + metrics.getDescent() + metrics.getLeading();
		drawText(g, String.format("+%d reps", user.getProfile().getRecommendations()), color, HEADER_X_SECOND + 5, HEADER_MAX_HEIGHT - yReps, false, false, FONT_L);
	}
	
	private static Color getContrastingColors(Color color) {
		final double a = 1 - (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255;
		return a < 0.5 ? Color.BLACK : Color.WHITE;
	}
	
	private static void drawText(Graphics2D g, String text, Color color, int x, int y, boolean rightAligned, boolean fromUpper, Font font) {
		g.setFont(font);
		g.setColor(color);
		int sumY = fromUpper ? g.getFontMetrics().getHeight() : 0;
		if (rightAligned) {
			g.drawString(text, x - g.getFontMetrics().stringWidth(text), y + sumY);
		} else {
			g.drawString(text, x, y + sumY);			
		}
	}
	
	private static void drawTextInRectJustified(Graphics2D g, String text, Color color, int width, int x, int y, Font font) {
		AttributedString as = new AttributedString(text, Map.of(TextAttribute.SIZE, font.getSize2D()));
		g.setFont(font);
		g.setColor(color);
		FontRenderContext frc = g.getFontRenderContext();
		AttributedCharacterIterator paragraph = as.getIterator();
		int paragraphStart = paragraph.getBeginIndex();
		int paragraphEnd = paragraph.getEndIndex();
	    LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);
	    float drawPosY = y;
	    lineMeasurer.setPosition(paragraphStart);
	    while (lineMeasurer.getPosition() < paragraphEnd) {
	        TextLayout layout = lineMeasurer.nextLayout(width);
	        if (lineMeasurer.getPosition() != paragraphEnd) {
	        	layout = layout.getJustifiedLayout(width);
	        }
	        drawPosY += layout.getAscent();
	        layout.draw(g, x, drawPosY);
	        drawPosY += layout.getDescent() + layout.getLeading();
	    }
	}
	
}
