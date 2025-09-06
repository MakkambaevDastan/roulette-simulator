package cell;

import java.io.IOException;
import java.text.NumberFormat;

import application.RouletteContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import strategy.BaseStrategy;

public class SimulationModeStrategyCell extends ListCell<BaseStrategy> {

	private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance();

	private RouletteContext rouletteContext;

	@FXML
	private Node cellContainer;

	@FXML
	private Label strategyNameLabel;

	@FXML
	private Label currentBalanceLabel;

	@FXML
	private Label differenceWithInitialBalanceLabel;

	@FXML
	private Label maximumBalanceLabel;

	@FXML
	private Label minimumBalanceLabel;

	@FXML
	private Label lastTotalBetValueLabel;

	@FXML
	private Label maximumTotalBetValueLabel;

	@FXML
	private Label wholeTotalBetValueLabel;

	@FXML
	private Label averageTotalBetValueLabel;

	@FXML
	private Label wholeTotalPayoutLabel;

	@FXML
	private Label averageTotalPayoutLabel;

	@FXML
	private Label winningAverageLabel;

	@FXML
	private LineChart<Number, Number> balanceHistoryChart;

	@FXML
	private NumberAxis chartXAxis;

	@FXML
	private NumberAxis chartYAxis;

	public SimulationModeStrategyCell(RouletteContext rouletteContext) {
		this.rouletteContext = rouletteContext;

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/cell/SimulationModeStrategyCell.fxml"));
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void updateItem(BaseStrategy strategy, boolean empty) {
		super.updateItem(strategy, empty);
		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			strategyNameLabel.setText(strategy.getStrategyName());
			currentBalanceLabel.setText(NUMBER_FORMAT.format(strategy.currentBalance));
			long differenceWithInitialBalance = (strategy.currentBalance - rouletteContext.initialBalance);
			if (0 <= differenceWithInitialBalance) {
				differenceWithInitialBalanceLabel.setText("+" + NUMBER_FORMAT.format(differenceWithInitialBalance));
				differenceWithInitialBalanceLabel.setTextFill(Color.BLUE);
			} else {
				differenceWithInitialBalanceLabel.setText(NUMBER_FORMAT.format(differenceWithInitialBalance));
				differenceWithInitialBalanceLabel.setTextFill(Color.RED);
			}
			maximumBalanceLabel.setText(NUMBER_FORMAT.format(strategy.maximumBalance));
			minimumBalanceLabel.setText(NUMBER_FORMAT.format(strategy.minimumBalance));
			if (0 <= strategy.minimumBalance) {
				minimumBalanceLabel.setTextFill(Color.BLACK);
			} else {
				minimumBalanceLabel.setTextFill(Color.RED);
			}
			lastTotalBetValueLabel.setText(NUMBER_FORMAT.format(strategy.getLastTotalBetValue()));
			maximumTotalBetValueLabel.setText(NUMBER_FORMAT.format(strategy.maximumTotalBetValue));
			wholeTotalBetValueLabel.setText(NUMBER_FORMAT.format(strategy.wholeTotalBetValue));
			averageTotalBetValueLabel.setText(NUMBER_FORMAT.format(strategy.getAverageTotalBetValue()));
			wholeTotalPayoutLabel.setText(NUMBER_FORMAT.format(strategy.wholeTotalPayoutValue));
			averageTotalPayoutLabel.setText(NUMBER_FORMAT.format(strategy.getAverageTotalPayoutValue()));
			winningAverageLabel.setText(String.format("%.2f%%", strategy.getWinningAverage() * 100));
			try {
				updateBalanceHistoryChart(strategy);
			} catch (Exception e) {
			}
			if (strategy.isLive()) {
				cellContainer.setStyle("");
			} else {
				cellContainer.setStyle("-fx-background-color: lightgray;");
			}
			setGraphic(cellContainer);
		}
	}

	private void updateBalanceHistoryChart(BaseStrategy strategy) {
		balanceHistoryChart.getData().clear();
		if (strategy.balanceHistoryList.isEmpty()) {
			return;
		}
		XYChart.Series<Number, Number> series = new XYChart.Series<>();
		int index = 0;
		for (Long balance : strategy.balanceHistoryList) {
			series.getData().add(new XYChart.Data<>(index, balance));
			index++;
		}
		balanceHistoryChart.getData().add(series);
		if (!strategy.balanceHistoryList.isEmpty()) {
			long minBalance = strategy.balanceHistoryList.stream().mapToLong(Long::longValue).min().orElse(0);
			long maxBalance = strategy.balanceHistoryList.stream().mapToLong(Long::longValue).max().orElse(0);
			long margin = Math.max((maxBalance - minBalance) / 10, 1000);
			chartYAxis.setLowerBound(minBalance - margin);
			chartYAxis.setUpperBound(maxBalance + margin);
		}
		chartXAxis.setLowerBound(0);
		chartXAxis.setUpperBound(Math.max(strategy.balanceHistoryList.size() - 1, 1));
	}
}