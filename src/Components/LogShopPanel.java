package Components;

import Controller.UserProfileController;
import Util.ColorPalette;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LogShopPanel extends JPanel {

    private final int borderRadius = 25;

    private RoundedPanel statusTopPanel;

    private RoundedButton backButton;
    private RoundedButton searchButton;

    private CategoryChart categoryChartDaily;
    private CategoryChart categoryChartMonthly;
    private PieChart pieChart;

    private FormTextFiledPanel dateFromPanel;
    private FormTextFiledPanel dateToPanel;

    private RoundedInputText dateFromInputPanel;
    private RoundedInputText dateToInputPanel;

    private LiveJLabelNumber revenueLabel;
    private LiveJLabelNumber profitLabel;
    private LiveJLabelNumber orderLabel;
    private LiveJLabelNumber customersLabel;
    private LiveJLabelNumber itemsSoldLabel;
    private LiveJLabelNumber averageOrderLabel;
    private LiveJLabelNumber primeUserLabel;
    private LiveJLabelNumber tempLiveLabel;

    private List<String> dateForDaily;
    private List<String> dateForMonthly;
    private List<Double> revenueForDaily;
    private List<Double> revenueForMonthly;
    private List<Double> profitForMonthly;
    private Map<String, Integer> productSalesMap;

    private JLabel lastUpdateLabel;
    private JLabel tempLabel;

    public static final String DATE_FROM_PROP = "dateFrom";
    public static final String DATE_TO_PROP = "dateTo";
    public static final String SEARCH_BTN_PROP = "search";

    private UserProfileController controller;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void setController(UserProfileController controller) {
        this.controller = controller;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public LogShopPanel() {
        setupUI();
        createComponents();
        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            removeAll();
            setupUI();
            revalidate();
            repaint();
        });
    }

    private void setupUI() {
        setBackground(ColorPalette.getInstance().getBgMain());
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(30, 30, 30, 30));
    }

    private void createComponents() {
        JLabel title = new JLabel("Shop Analytics Dashboard");
        title.setOpaque(false);
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setForeground(ColorPalette.getInstance().getTextPrimary());
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

        // Back btn
        backButton = new RoundedButton("Back", 30);
        backButton.setBackground(ColorPalette.getInstance().getAccentDanger());
        backButton.setHoverColor(ColorPalette.getInstance().getAccentDanger());
        backButton.setMaximumSize(new Dimension(140, backButton.getHeight()));
        backButton.setForeground(ColorPalette.getInstance().getTextPrimary());
        backButton.setAlignmentX(CENTER_ALIGNMENT);
        backButton.addActionListener(e -> controller.showMainPage());
        this.add(backButton);

        this.add(Box.createVerticalGlue());
    }

    private RoundedPanel createTopPanel() {
        RoundedPanel panel = new RoundedPanel(borderRadius, ColorPalette.getInstance().getBgSecondary(),
                ColorPalette.getInstance().getBorder());
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 15, 10, 15);

        gbc.gridx = 0;
        gbc.weightx = 1;
        panel.add(createLiveStatCard("Revenue", "$"), gbc);
        revenueLabel = tempLiveLabel;

        gbc.gridx = 1;
        panel.add(createLiveStatCard("Profit", "$"), gbc);
        profitLabel = tempLiveLabel;

        gbc.gridx = 2;
        panel.add(createLiveStatCard("Customers", null), gbc);
        customersLabel = tempLiveLabel;

        gbc.gridx = 3;
        panel.add(createLiveStatCard("Orders", null), gbc);
        orderLabel = tempLiveLabel;

        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(createLiveStatCard("Items Sold", null), gbc);
        itemsSoldLabel = tempLiveLabel;

        gbc.gridx = 1;
        panel.add(createLiveStatCard("Average Order", "$"), gbc);
        averageOrderLabel = tempLiveLabel;

        gbc.gridx = 2;
        panel.add(createLiveStatCard("Prime User", null), gbc);
        primeUserLabel = tempLiveLabel;

        gbc.gridx = 3;
        panel.add(createStatCard("Last Update"), gbc);
        lastUpdateLabel = tempLabel;

        return panel;
    }

    private RoundedPanel createLiveStatCard(String label, String defaultString) {
        RoundedPanel card = new RoundedPanel(borderRadius, ColorPalette.getInstance().getBgSecondary(),
                ColorPalette.getInstance().getSelectionBg());
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 25, 10, 25));

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Arial", Font.PLAIN, 12));
        labelComp.setForeground(ColorPalette.getInstance().getTextMuted());
        labelComp.setAlignmentX(CENTER_ALIGNMENT);

        LiveJLabelNumber valueComp = new LiveJLabelNumber(50);
        if (defaultString != null)
            valueComp.setDefaultString(defaultString);
        valueComp.setFont(new Font("Arial", Font.BOLD, 22));
        valueComp.setForeground(ColorPalette.getInstance().getTextPrimary());
        valueComp.setAlignmentX(CENTER_ALIGNMENT);

        card.add(labelComp);
        card.add(Box.createVerticalStrut(15));
        card.add(valueComp);
        card.add(Box.createVerticalStrut(5));

        this.tempLiveLabel = valueComp;
        return card;
    }

    private RoundedPanel createStatCard(String label) {
        RoundedPanel card = new RoundedPanel(borderRadius, ColorPalette.getInstance().getBgSecondary(),
                ColorPalette.getInstance().getSelectionBg());
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 25, 10, 25));

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Arial", Font.PLAIN, 12));
        labelComp.setForeground(ColorPalette.getInstance().getTextMuted());
        labelComp.setAlignmentX(CENTER_ALIGNMENT);

        JLabel valueComp = new JLabel("temp value");
        valueComp.setFont(new Font("Arial", Font.BOLD, 22));
        valueComp.setForeground(ColorPalette.getInstance().getTextPrimary());
        valueComp.setAlignmentX(CENTER_ALIGNMENT);

        card.add(labelComp);
        card.add(Box.createVerticalStrut(15));
        card.add(valueComp);
        card.add(Box.createVerticalStrut(5));

        this.tempLabel = valueComp;
        return card;
    }

    private JPanel createChartsRow() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.insets = new Insets(0, 0, 0, 10);

        gbc.gridx = 0;
        gbc.weightx = 1;
        panel.add(createRevenueDailyChart(), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 10, 0, 0);
        panel.add(createMonthlyBarChart(), gbc);

        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE,
                panel.getPreferredSize().height));

        return panel;
    }

    private JPanel createProductPieChartPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0));
        panel.setOpaque(false);

        panel.add(createProductPieChart());

        return panel;
    }

    private JPanel createRevenueDailyChart() {
        RoundedPanel panel = new RoundedPanel(borderRadius, ColorPalette.getInstance().getBgSecondary(),
                ColorPalette.getInstance().getBorder());
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(450, 470));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // TODO: Replace with real data from monthlyAnalytics
        List<String> months = Arrays.asList("");
        List<Double> revenue = Arrays.asList(0.0);

        categoryChartDaily = new CategoryChartBuilder()
                .width(400)
                .height(340)
                .title("Daily Revenue")
                .xAxisTitle("Date")
                .yAxisTitle("Revenue ($)")
                .build();

        // custom style
        categoryChartDaily.getStyler().setChartBackgroundColor(ColorPalette.getInstance().getBgSecondary());
        categoryChartDaily.getStyler().setPlotBackgroundColor(ColorPalette.getInstance().getBgSecondary());
        categoryChartDaily.getStyler().setAnnotationTextFontColor(ColorPalette.getInstance().getTextPrimary());
        categoryChartDaily.getStyler().setChartTitleFontColor(ColorPalette.getInstance().getTextPrimary());
        categoryChartDaily.getStyler().setChartFontColor(ColorPalette.getInstance().getTextPrimary());
        categoryChartDaily.getStyler().setXAxisTitleColor(ColorPalette.getInstance().getTextPrimary());
        categoryChartDaily.getStyler().setYAxisTitleColor(ColorPalette.getInstance().getTextPrimary());
        categoryChartDaily.getStyler().setLegendBackgroundColor(ColorPalette.getInstance().getBgSecondary());
        categoryChartDaily.getStyler().setAxisTickLabelsColor(ColorPalette.getInstance().getTextPrimary());
        categoryChartDaily.getStyler().setSeriesColors(new Color[] { ColorPalette.getInstance().getAccentPrimary() });
        categoryChartDaily.getStyler().setDefaultSeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Line);

        categoryChartDaily.addSeries("Revenue", months, revenue);
        categoryChartDaily.getSeries("Revenue").setFillColor(ColorPalette.getInstance().getBgSecondary());
        categoryChartDaily.getSeries("Revenue").setMarker(null);
        categoryChartDaily.getSeries("Revenue").setLineColor(ColorPalette.getInstance().getAccentPrimary());

        XChartPanel<CategoryChart> chartPanel = new XChartPanel<>(categoryChartDaily);
        panel.add(chartPanel, BorderLayout.CENTER);

        // search panel
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        datePanel.setOpaque(false);
        datePanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        dateFromInputPanel = new RoundedInputText("YYYY/MM/DD", 4);
        dateFromInputPanel.setPreferredSize(new Dimension(50 * 3, 50));
        dateFromInputPanel.setMaximumSize(new Dimension(50 * 3, 50));
        dateFromPanel = new FormTextFiledPanel("From", dateFromInputPanel, DATE_FROM_PROP);
        dateFromPanel.setPreferredSize(new Dimension(50 * 3, 75));
        dateFromInputPanel.addActionListener(e -> {
            var result = controller.validationDate(dateFromInputPanel.getText());
            if (!result.isValid())
                dateFromPanel.setError(result.getErrorMessage());
            else
                dateFromPanel.clearError();

            support.firePropertyChange(DATE_FROM_PROP, null, dateFromInputPanel.getText());
        });
        datePanel.add(dateFromPanel);

        dateToInputPanel = new RoundedInputText("YYYY/MM/DD", 4);
        dateToPanel = new FormTextFiledPanel("To", dateToInputPanel, DATE_TO_PROP);
        dateToInputPanel.setPreferredSize(new Dimension(50 * 3, 50));
        dateToInputPanel.setMaximumSize(new Dimension(50 * 3, 50));
        dateToPanel.setPreferredSize(new Dimension(50 * 3, 75));
        dateToInputPanel.addActionListener(e -> {
            var result = controller.validationDate(dateToInputPanel.getText());
            if (!result.isValid())
                dateToPanel.setError(result.getErrorMessage());
            else
                dateToPanel.clearError();

            support.firePropertyChange(DATE_TO_PROP, null, dateToInputPanel.getText());
        });
        datePanel.add(dateToPanel);

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        btnPanel.setLayout(new BorderLayout());

        searchButton = new RoundedButton("Search", 25);
        searchButton.setBorder(new EmptyBorder(10, 0, 10, 0));
        searchButton.setPreferredSize(new Dimension(83, 30));
        searchButton.addActionListener(e -> {
            var resultFrom = controller.validationDate(dateFromInputPanel.getText());
            if (!resultFrom.isValid())
                dateFromPanel.setError(resultFrom.getErrorMessage());
            else
                dateFromPanel.clearError();

            var resultTo = controller.validationDate(dateToInputPanel.getText());
            if (!resultTo.isValid())
                dateToPanel.setError(resultTo.getErrorMessage());
            else
                dateToPanel.clearError();

            if (resultFrom.isValid() && resultTo.isValid()) {
                LocalDate from = LocalDate.parse(dateFromInputPanel.getText(),
                        DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                LocalDate to = LocalDate.parse(dateToInputPanel.getText(),
                        DateTimeFormatter.ofPattern("yyyy/MM/dd"));

                if (from.isAfter(to))
                    dateToPanel.setError("From date must be before \"To date\"");
                else {
                    dateToPanel.clearError();
                    controller.handleDailyChart(dateFromInputPanel.getText(), dateToInputPanel.getText());
                    support.firePropertyChange(DATE_FROM_PROP, null, null);
                }
            }

        });
        btnPanel.add(searchButton, BorderLayout.CENTER);

        datePanel.add(btnPanel);

        panel.add(datePanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMonthlyBarChart() {
        RoundedPanel panel = new RoundedPanel(borderRadius, ColorPalette.getInstance().getBgSecondary(),
                ColorPalette.getInstance().getBorder());
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setPreferredSize(new Dimension(450, 470));

        List<String> months = Arrays.asList("Jul 2026");
        List<Double> revenue = Arrays.asList(22.2);

        categoryChartMonthly = new CategoryChartBuilder()
                .width(400)
                .height(340)
                .title("Monthly Revenue")
                .xAxisTitle("Month")
                .yAxisTitle("Revenue ($)")
                .build();

        // custom style
        categoryChartMonthly.getStyler().setChartBackgroundColor(ColorPalette.getInstance().getBgSecondary());
        categoryChartMonthly.getStyler().setPlotBackgroundColor(ColorPalette.getInstance().getBgSecondary());
        categoryChartMonthly.getStyler().setAnnotationTextFontColor(ColorPalette.getInstance().getTextPrimary());
        categoryChartMonthly.getStyler().setChartTitleFontColor(ColorPalette.getInstance().getTextPrimary());
        categoryChartMonthly.getStyler().setChartFontColor(ColorPalette.getInstance().getTextPrimary());
        categoryChartMonthly.getStyler().setXAxisTitleColor(ColorPalette.getInstance().getTextPrimary());
        categoryChartMonthly.getStyler().setYAxisTitleColor(ColorPalette.getInstance().getTextPrimary());
        categoryChartMonthly.getStyler().setLegendBackgroundColor(ColorPalette.getInstance().getBgSecondary());
        categoryChartMonthly.getStyler().setAxisTickLabelsColor(ColorPalette.getInstance().getTextPrimary());
        categoryChartMonthly.getStyler().setDecimalPattern("#,##0.00");
        categoryChartMonthly.getStyler().setSeriesColors(new Color[] { ColorPalette.getInstance().getAccentPrimary(),
                ColorPalette.getInstance().getAccentWarning() });
        categoryChartMonthly.getStyler().setDefaultSeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Bar);

        categoryChartMonthly.addSeries("Revenue", months, revenue);
        categoryChartMonthly.addSeries("Profit", months, revenue);

        XChartPanel<CategoryChart> chartPanel = new XChartPanel<>(categoryChartMonthly);
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProductPieChart() {
        RoundedPanel panel = new RoundedPanel(borderRadius, ColorPalette.getInstance().getBgSecondary(),
                ColorPalette.getInstance().getBorder());
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        Map<String, Integer> productSales = new LinkedHashMap<>();
        productSales.put("Product A", 120);

        pieChart = new PieChartBuilder()
                .width(400)
                .height(300)
                .title("Product Sales Distribution")
                .build();

        // Custom styling
        pieChart.getStyler().setChartBackgroundColor(ColorPalette.getInstance().getBgSecondary());
        pieChart.getStyler().setPlotBackgroundColor(ColorPalette.getInstance().getBgSecondary());
        pieChart.getStyler().setAnnotationTextFontColor(ColorPalette.getInstance().getTextPrimary());
        pieChart.getStyler().setChartTitleFontColor(ColorPalette.getInstance().getTextPrimary());
        pieChart.getStyler().setLabelsFontColor(ColorPalette.getInstance().getTextPrimary());
        pieChart.getStyler().setLegendBackgroundColor(ColorPalette.getInstance().getBgSecondary());
        pieChart.getStyler().setChartFontColor(ColorPalette.getInstance().getTextPrimary());
        pieChart.getStyler().setPlotBorderVisible(false);

        for (Map.Entry<String, Integer> entry : productSales.entrySet()) {
            pieChart.addSeries(entry.getKey(), entry.getValue());
        }

        XChartPanel<PieChart> chartPanel = new XChartPanel<>(pieChart);
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }

    public void setDateForDaily(List<String> dateForDaily) {
        this.dateForDaily = dateForDaily;
    }

    public void setDateForMonthly(List<String> dateForMonthly) {
        this.dateForMonthly = dateForMonthly;
    }

    public void setRevenueForDaily(List<Double> revenueForDaily) {
        this.revenueForDaily = revenueForDaily;
    }

    public void setRevenueForMonthly(List<Double> revenueForMonthly) {
        this.revenueForMonthly = revenueForMonthly;
    }

    public void refreshViewChartDaily() {
        categoryChartDaily.updateCategorySeries("Revenue", dateForDaily, revenueForDaily, null);
        repaint();
        revalidate();
    }

    public void refreshViewChartMonthly() {
        categoryChartMonthly.updateCategorySeries("Revenue", dateForMonthly, revenueForMonthly, null);
        categoryChartMonthly.updateCategorySeries("Profit", dateForMonthly, profitForMonthly, null);
        repaint();
        revalidate();
    }

    public void refreshViewChartProduct() {
        if (pieChart == null || productSalesMap == null || pieChart.getSeries("Product A") == null)
            return;

        pieChart.removeSeries("Product A");
        for (Map.Entry<String, Integer> entry : productSalesMap.entrySet()) {
            pieChart.addSeries(entry.getKey(), entry.getValue());
        }
        repaint();
        revalidate();
    }

    public void refreshFullView() {
        revenueLabel.setTarget(controller.getRevenue());
        profitLabel.setTarget(controller.getProfit());
        customersLabel.setTarget(controller.getCustomer());
        orderLabel.setTarget(controller.getOrders());
        itemsSoldLabel.setTarget(controller.getItemSold());
        averageOrderLabel.setTarget(controller.getAverageOrder());
        primeUserLabel.setTarget(controller.getCountPrimeUser());
        lastUpdateLabel.setText(controller.getLastUpdate());

        dateFromInputPanel.setActivePlaceHolder(true);
        dateToInputPanel.setActivePlaceHolder(true);
        dateFromPanel.clearError();
        dateToPanel.clearError();
        List<String> temp = new ArrayList<>();
        List<Double> temp2 = new ArrayList<>();
        categoryChartDaily.updateCategorySeries("Revenue", temp, temp2, null);
        controller.handleMonthlyChart();
    }

    public void setProfitForMonthly(List<Double> profitForMonthly) {
        this.profitForMonthly = profitForMonthly;
    }

    public void setProductSalesMap(Map<String, Integer> productSalesMap) {
        this.productSalesMap = productSalesMap;
    }
}
