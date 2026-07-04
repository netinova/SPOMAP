package Components;

import Controller.UserProfileController;
import Util.ColorPalette;
import org.knowm.xchart.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LogShopPanel extends JPanel {

    private final int borderRadius = 25;

    private RoundedPanel statusTopPanel;
    private RoundedPanel chartPanel;

    private CategoryChart categoryChartDaily;
    private CategoryChart categoryChartMonthly;
    private PieChart pieChart;

    private LiveJLabelNumber revenueLabel;
    private LiveJLabelNumber profitLabel;
    private LiveJLabelNumber orderLabel;
    private LiveJLabelNumber customersLabel;
    private LiveJLabelNumber itemsSoldLabel;
    private LiveJLabelNumber averageOrderLabel;
    private LiveJLabelNumber tempLiveLabel;

    private JLabel lastUpdateLabel;
    private JLabel tempLabel;

    private UserProfileController controller;

    public void setController(UserProfileController controller) {
        this.controller = controller;
    }

    public LogShopPanel() {
        setupUI();
        createComponents();
    }

    private void setupUI() {
        setBackground(ColorPalette.BG_MAIN);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(30, 30, 30, 30));
    }

    private void createComponents() {
        JLabel title = new JLabel("Shop Analytics Dashboard");
        title.setOpaque(false);
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setForeground(ColorPalette.TEXT_PRIMARY);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        this.add(title);
        this.add(Box.createVerticalStrut(35));

        // Top Summary Cards
        statusTopPanel = createTopPanel();
        statusTopPanel.setAlignmentX(CENTER_ALIGNMENT);
        this.add(statusTopPanel);
        this.add(Box.createVerticalStrut(30));

        // === CHARTS SECTION (XChart) ===
        JPanel chartsRow = createChartsRow();
        chartsRow.setAlignmentX(CENTER_ALIGNMENT);
        this.add(chartsRow);
        this.add(Box.createVerticalStrut(25));

        JPanel productPieChart = createProductPieChartPanel();
        productPieChart.setAlignmentX(CENTER_ALIGNMENT);
        this.add(productPieChart);
        this.add(Box.createVerticalStrut(25));
        RoundedButton btn = new RoundedButton("asd",30);
        btn.addActionListener(e -> {
            btn.setText(String.valueOf(productPieChart.getWidth()));
        });
        this.add(btn);

        //TODO: more status
        this.add(Box.createVerticalGlue());
    }

    private RoundedPanel createTopPanel() {
        RoundedPanel panel = new RoundedPanel(borderRadius, ColorPalette.BG_SECONDARY, ColorPalette.BORDER);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.gridy=0;
        gbc.insets = new Insets(10, 15, 10, 15);

        gbc.gridx = 0; gbc.weightx = 1;
        panel.add(createLiveStatCard("Revenue", "$"), gbc);
        revenueLabel = tempLiveLabel;

        gbc.gridx = 1;
        panel.add(createLiveStatCard("Profit", "$"), gbc);
        profitLabel = tempLiveLabel;

        gbc.gridx = 2;
        panel.add(createLiveStatCard("Orders", null), gbc);
        orderLabel = tempLiveLabel;

        gbc.gridx = 3;
        panel.add(createLiveStatCard("Customers", null), gbc);
        customersLabel = tempLiveLabel;

        gbc.gridy=1;
        gbc.gridx=0;
        panel.add(createLiveStatCard("Items Sold",null),gbc);
        itemsSoldLabel = tempLiveLabel;

        gbc.gridx=1;
        panel.add(createLiveStatCard("Average Order","$"),gbc);
        averageOrderLabel = tempLiveLabel;

        gbc.gridx=2;
        panel.add(createStatCard("Last Update"),gbc);
        lastUpdateLabel = tempLabel;

        return panel;
    }

    private RoundedPanel createLiveStatCard(String label, String defaultString) {
        RoundedPanel card = new RoundedPanel(borderRadius, ColorPalette.BG_SECONDARY, ColorPalette.SELECTION_BG);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 25, 10, 25));

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Arial", Font.PLAIN, 12));
        labelComp.setForeground(ColorPalette.TEXT_MUTED);
        labelComp.setAlignmentX(CENTER_ALIGNMENT);

        LiveJLabelNumber valueComp = new LiveJLabelNumber(50);
        if (defaultString != null) valueComp.setDefaultString(defaultString);
        valueComp.setFont(new Font("Arial", Font.BOLD, 22));
        valueComp.setForeground(ColorPalette.TEXT_PRIMARY);
        valueComp.setAlignmentX(CENTER_ALIGNMENT);

        card.add(labelComp);
        card.add(Box.createVerticalStrut(15));
        card.add(valueComp);
        card.add(Box.createVerticalStrut(5));

        this.tempLiveLabel = valueComp;
        return card;
    }

    private RoundedPanel createStatCard(String label) {
        RoundedPanel card = new RoundedPanel(borderRadius, ColorPalette.BG_SECONDARY, ColorPalette.SELECTION_BG);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 25, 10, 25));

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Arial", Font.PLAIN, 12));
        labelComp.setForeground(ColorPalette.TEXT_MUTED);
        labelComp.setAlignmentX(CENTER_ALIGNMENT);

        JLabel valueComp = new JLabel("temp value");
        valueComp.setFont(new Font("Arial", Font.BOLD, 22));
        valueComp.setForeground(ColorPalette.TEXT_PRIMARY);
        valueComp.setAlignmentX(CENTER_ALIGNMENT);

        card.add(labelComp);
        card.add(Box.createVerticalStrut(15));
        card.add(valueComp);
        card.add(Box.createVerticalStrut(5));

        this.tempLabel = valueComp;
        return card;
    }
    private JPanel createChartsRow() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));

        panel.add(createRevenueDailyChart());
        panel.add(createMonthlyBarChart());

        return panel;
    }

    private JPanel createProductPieChartPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0));
        panel.setOpaque(false);

        panel.add(createProductPieChart());

        return panel;
    }
    private JPanel createRevenueDailyChart() {
        RoundedPanel wrapper = new RoundedPanel(borderRadius, ColorPalette.BG_SECONDARY, ColorPalette.BORDER);
        wrapper.setLayout(new BorderLayout());
        wrapper.setBorder(new EmptyBorder(10, 10, 10, 10));

        // TODO: Replace with real data from monthlyAnalytics
        List<String> months = Arrays.asList("Jul 2026");
        List<Double> revenue = Arrays.asList(113.36);

        categoryChartDaily = new CategoryChartBuilder()
                .width(400)
                .height(340)
                .title("Daily Revenue")
                .xAxisTitle("Date")
                .yAxisTitle("Revenue ($)")
                .build();

        //custom style
        categoryChartDaily.getStyler().setChartBackgroundColor(ColorPalette.BG_SECONDARY);
        categoryChartDaily.getStyler().setPlotBackgroundColor(ColorPalette.BG_SECONDARY);
        categoryChartDaily.getStyler().setAnnotationTextFontColor(ColorPalette.TEXT_PRIMARY);
        categoryChartDaily.getStyler().setChartTitleFontColor(ColorPalette.TEXT_PRIMARY);
        categoryChartDaily.getStyler().setChartFontColor(ColorPalette.TEXT_PRIMARY);
        categoryChartDaily.getStyler().setXAxisTitleColor(ColorPalette.TEXT_PRIMARY);
        categoryChartDaily.getStyler().setYAxisTitleColor(ColorPalette.TEXT_PRIMARY);
        categoryChartDaily.getStyler().setLegendBackgroundColor(ColorPalette.BG_SECONDARY);
        categoryChartDaily.getStyler().setAxisTickLabelsColor(ColorPalette.TEXT_PRIMARY);
        categoryChartDaily.getStyler().setSeriesColors(new Color[]{ColorPalette.ACCENT_PRIMARY});
        categoryChartDaily.getStyler().setDefaultSeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Line);

        categoryChartDaily.addSeries("Revenue", months, revenue);
        categoryChartDaily.getSeries("Revenue").setFillColor(ColorPalette.BG_SECONDARY);
        categoryChartDaily.getSeries("Revenue").setMarker(null);
        categoryChartDaily.getSeries("Revenue").setLineColor(ColorPalette.ACCENT_PRIMARY);

        XChartPanel<CategoryChart> chartPanel = new XChartPanel<>(categoryChartDaily);
        wrapper.add(chartPanel, BorderLayout.CENTER);

        return wrapper;
    }

    // Orders by Month - Bar Chart (already good)
    private JPanel createMonthlyBarChart() {
        RoundedPanel wrapper = new RoundedPanel(borderRadius, ColorPalette.BG_SECONDARY, ColorPalette.BORDER);
        wrapper.setLayout(new BorderLayout());
        wrapper.setBorder(new EmptyBorder(10, 10, 10, 10));

        List<String> months = Arrays.asList("Jul 2026");
        List<Double> revenue = Arrays.asList(22.2);

        categoryChartMonthly = new CategoryChartBuilder()
                .width(400)
                .height(340)
                .title("Monthly Revenue")
                .xAxisTitle("Month")
                .yAxisTitle("Revenue ($)")
                .build();

        //custom style
        categoryChartMonthly.getStyler().setChartBackgroundColor(ColorPalette.BG_SECONDARY);
        categoryChartMonthly.getStyler().setPlotBackgroundColor(ColorPalette.BG_SECONDARY);
        categoryChartMonthly.getStyler().setAnnotationTextFontColor(ColorPalette.TEXT_PRIMARY);
        categoryChartMonthly.getStyler().setChartTitleFontColor(ColorPalette.TEXT_PRIMARY);
        categoryChartMonthly.getStyler().setChartFontColor(ColorPalette.TEXT_PRIMARY);
        categoryChartMonthly.getStyler().setXAxisTitleColor(ColorPalette.TEXT_PRIMARY);
        categoryChartMonthly.getStyler().setYAxisTitleColor(ColorPalette.TEXT_PRIMARY);
        categoryChartMonthly.getStyler().setLegendBackgroundColor(ColorPalette.BG_SECONDARY);
        categoryChartMonthly.getStyler().setAxisTickLabelsColor(ColorPalette.TEXT_PRIMARY);
        categoryChartMonthly.getStyler().setSeriesColors(new Color[]{ColorPalette.ACCENT_PRIMARY});
        categoryChartMonthly.getStyler().setDefaultSeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Bar);

        categoryChartMonthly.addSeries("Revenue", months, revenue);

        XChartPanel<CategoryChart> chartPanel = new XChartPanel<>(categoryChartMonthly);
        wrapper.add(chartPanel, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel createProductPieChart() {
        RoundedPanel wrapper = new RoundedPanel(borderRadius, ColorPalette.BG_SECONDARY, ColorPalette.BORDER);
        wrapper.setLayout(new BorderLayout());
        wrapper.setBorder(new EmptyBorder(10, 10, 10, 10));

        Map<String, Integer> productSales = new LinkedHashMap<>();
        productSales.put("Product A", 120);

        pieChart = new PieChartBuilder()
                .width(400)
                .height(300)
                .title("Product Sales Distribution")
                .build();

        // Custom styling
        pieChart.getStyler().setChartBackgroundColor(ColorPalette.BG_SECONDARY);
        pieChart.getStyler().setPlotBackgroundColor(ColorPalette.BG_SECONDARY);
        pieChart.getStyler().setAnnotationTextFontColor(ColorPalette.TEXT_PRIMARY);
        pieChart.getStyler().setChartTitleFontColor(ColorPalette.TEXT_PRIMARY);
        pieChart.getStyler().setLabelsFontColor(ColorPalette.TEXT_PRIMARY);
        pieChart.getStyler().setLegendBackgroundColor(ColorPalette.BG_SECONDARY);
        pieChart.getStyler().setChartFontColor(ColorPalette.TEXT_PRIMARY);
        pieChart.getStyler().setPlotBorderVisible(false);

        for (Map.Entry<String, Integer> entry : productSales.entrySet()) {
            pieChart.addSeries(entry.getKey(), entry.getValue());
        }

        XChartPanel<PieChart> chartPanel = new XChartPanel<>(pieChart);
        wrapper.add(chartPanel, BorderLayout.CENTER);

        return wrapper;
    }

    public void loadView() {
        // TODO: Load real data from controller and update labels + charts
    }
}