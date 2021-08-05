package com.suchorski.zapbot.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.constants.Constants.COLORS;
import com.suchorski.zapbot.models.interfaces.statistics.StatisticGraph;
import com.suchorski.zapbot.models.interfaces.statistics.StatisticGraph.Statistic;


public class GraphGenerator {

	public static Color COLOR_TEXT = COLORS.INFO;
	public static Color COLOR_SUBTEXT = COLORS.DISCORD.TEXT_CHAT;
	public static Color COLOR_LINE_AXIS = COLORS.OWNER;
	public static Color COLOR_LINE_BORDER = COLORS.DISCORD.DARK_GRAY;
	public static Color COLOR_BACKGROUND_DISCORD = COLORS.DISCORD.LIGHT_GRAY;
	public static Color COLOR_BACKGROUND_GRAPH = COLORS.DISCORD.GRAY;
	public static Color[] COLOR_LINES = { COLORS.SUCCESS, COLORS.DANGER, COLORS.OWNER, COLORS.WARNING };

	public static InputStream generateGraph(String title, List<? extends StatisticGraph> items, boolean filter) throws IOException {
		Collections.sort(items);
		if (filter) {
			items = items.stream().filter(i -> ChronoUnit.DAYS.between(i.getDate().getDate(), LocalDate.now()) <= Constants.PREMIUM.MAX.STATISTISTICS[0]).collect(Collectors.toList());
		}
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (StatisticGraph sg : items) {
			for (Statistic s : sg.getStatistics()) {
				dataset.addValue(s.getValue(), s.getCategory(), sg.getDate());
			}
		}
		JFreeChart chart = ChartFactory.createLineChart(title, "Datas", "Quantidades", dataset, PlotOrientation.VERTICAL, true, false, false);
		LineAndShapeRenderer renderer = (LineAndShapeRenderer) chart.getCategoryPlot().getRenderer();
		renderer.setDefaultShapesVisible(true);
		/* Chart */
		chart.getTitle().setPaint(COLOR_TEXT); // title color
		chart.setBackgroundPaint(COLOR_BACKGROUND_DISCORD); // background color
		/* X Axis */
		chart.getCategoryPlot().getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_90); // rotate 90 degree
		chart.getCategoryPlot().getDomainAxis().setAxisLinePaint(COLOR_LINE_AXIS); // line color
		chart.getCategoryPlot().getDomainAxis().setLabelPaint(COLOR_TEXT); // label color
		chart.getCategoryPlot().getDomainAxis().setTickLabelPaint(COLOR_SUBTEXT); // tick color
		/* Y Axis */
		chart.getCategoryPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits()); // integer values
		chart.getCategoryPlot().getRangeAxis().setAxisLinePaint(COLOR_LINE_AXIS); // line color
		chart.getCategoryPlot().getRangeAxis().setLabelPaint(COLOR_TEXT); // label color
		chart.getCategoryPlot().getRangeAxis().setTickLabelPaint(COLOR_SUBTEXT); // tick color
		/* Legend */
		chart.getLegend().setBackgroundPaint(COLOR_BACKGROUND_GRAPH); // background color
		chart.getLegend().setItemPaint(COLOR_TEXT); // text color
		chart.getLegend().setPadding(4f, 4f, 4f, 4f); // padding size
		chart.getLegend().setBorder(1f, 1f, 1f, 1f); // border width
		/* Graph */
		chart.getCategoryPlot().setBackgroundPaint(COLOR_BACKGROUND_GRAPH); // background color
		chart.getCategoryPlot().setOutlinePaint(COLOR_LINE_BORDER); // border color
		chart.getCategoryPlot().setOutlineStroke(new BasicStroke(2f)); // border width
		chart.getCategoryPlot().setRangeGridlinePaint(COLOR_LINE_BORDER); // horizontal line color
		chart.getCategoryPlot().setRangeGridlineStroke(new BasicStroke(2f)); // horizontal line width
		chart.getCategoryPlot().setNoDataMessagePaint(COLOR_TEXT); // no data color
		chart.getCategoryPlot().setNoDataMessage("Nenhuma estatística para mostrar ainda"); // no data message
		chart.getCategoryPlot().setNoDataMessageFont(new Font(Font.SANS_SERIF, 0, 32)); // no data font
		for (int i = 0; i < items.size(); ++i) {
			chart.getCategoryPlot().getRenderer().setSeriesPaint(i, COLOR_LINES[i % 4], false); // line color
			chart.getCategoryPlot().getRenderer().setSeriesStroke(i, new BasicStroke(3f), false); // line width
			chart.getCategoryPlot().getRenderer().setSeriesShape(i, new Ellipse2D.Double(-5d, -5d, 10d, 10d), false); // shape
		}
		BufferedImage bi = chart.createBufferedImage(800, 600);
		try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
			ImageIO.write(bi, "png", output);
			return new ByteArrayInputStream(output.toByteArray());
		}
	}

}
