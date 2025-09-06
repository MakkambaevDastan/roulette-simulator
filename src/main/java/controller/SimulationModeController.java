package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.RouletteContext;
import cell.SimulationModeStrategyCell;
import enums.Spot;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.stage.WindowEvent;
import model.Bet;
import strategy.BaseStrategy;
import utils.LogHelper;
import utils.StrategyHelper;
import view.SpotHeatmapView;

public class SimulationModeController extends BaseController {

	private RouletteContext rouletteContext;

	private ObservableList<BaseStrategy> strategyList;

	@FXML
	private Label currentLoopCountLabel;

	@FXML
	private Label redRateLabel;

	@FXML
	private Label greenRateLabel;

	@FXML
	private Label blackRateLabel;

	@FXML
	private ListView<BaseStrategy> strategyListView;

	@FXML
	private ListView<Spot> spotHistoryListView;

	@FXML
	private ScrollPane heatmapScrollPane;

	private SpotHeatmapView spotHeatmapView;

	private ScheduledService<Boolean> mainLoopService;

	@FXML
	private Button pauseButton;

	@FXML
	private Button resumeButton;

	@FXML
	private Button stopButton;

	@FXML
	private Slider simulationSpeedSliderInMode;

	@FXML
	private Label simulationSpeedLabelInMode;

	@Override
	public void setOnCloseRequest(WindowEvent event) {
		if (mainLoopService != null) {
			mainLoopService.cancel();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater(() -> {
			strategyList = StrategyHelper.createStrategyList(StrategyHelper.getEnableStrategyClassSet(),
					rouletteContext);

			resumeButton.setDisable(true);

			simulationSpeedSliderInMode.setValue(rouletteContext.simulationSpeed);
			simulationSpeedLabelInMode.setText(rouletteContext.simulationSpeed + "ms");

			simulationSpeedSliderInMode.valueProperty().addListener((observable, oldValue, newValue) -> {
				int speed = newValue.intValue();
				rouletteContext.simulationSpeed = speed;
				simulationSpeedLabelInMode.setText(speed + "ms");
			});

			spotHeatmapView = new SpotHeatmapView(rouletteContext);
			heatmapScrollPane.setContent(spotHeatmapView);

			strategyListView.setCellFactory(listView -> new SimulationModeStrategyCell(rouletteContext));
			strategyListView.setItems(strategyList);

			ObservableList<Spot> spotHistoryObservableList = FXCollections.observableArrayList();

			spotHistoryListView.setCellFactory(listView -> new ListCell<Spot>() {
				@Override
				protected void updateItem(Spot spot, boolean empty) {
					super.updateItem(spot, empty);
					if (empty || spot == null) {
						setText(null);
						setStyle("");
					} else {
						setText(String.valueOf(spot.getNumber()));
						if (spot.isRed()) {
							setStyle("-fx-background-color: #ffcccc;");
						} else if (spot.isBlack()) {
							setStyle("-fx-background-color: #cccccc;");
						} else if (spot.isGreen()) {
							setStyle("-fx-background-color: #ccffcc;");
						}
					}
				}
			});
			spotHistoryListView.setItems(spotHistoryObservableList);

			mainLoopService = new ScheduledService<Boolean>() {

				@Override
				protected Task<Boolean> createTask() {
					return new Task<Boolean>() {

						@Override
						protected Boolean call() throws Exception {
							if (rouletteContext.simulationSpeed > 0) {
								Thread.sleep(rouletteContext.simulationSpeed);
							}

							Spot nextSpot = Spot.getRandomNextSpot(rouletteContext);

							for (BaseStrategy strategy : strategyList) {
								List<Bet> betList = strategy.getNextBetList(rouletteContext);

								strategy.updateStrategyParameter(betList, nextSpot);
							}

							rouletteContext.currentLoopCount++;

							rouletteContext.addSpotHistory(nextSpot);

							Platform.runLater(() -> {
								strategyListView.getItems().sort(BaseStrategy.getBalanceComparator());

								strategyListView.refresh();

								spotHistoryObservableList.setAll(rouletteContext.spotHistoryList);
								if (!spotHistoryObservableList.isEmpty()) {
									spotHistoryListView.scrollTo(spotHistoryObservableList.size() - 1);
								}

								currentLoopCountLabel.setText(String.valueOf(rouletteContext.currentLoopCount));

								redRateLabel.setText(String.format("%.2f%%", rouletteContext.getRedRate() * 100));
								greenRateLabel.setText(String.format("%.2f%%", rouletteContext.getGreenRate() * 100));
								blackRateLabel.setText(String.format("%.2f%%", rouletteContext.getBlackRate() * 100));

								if (spotHeatmapView != null) {
									spotHeatmapView.updateHeatmap();
								}
							});

							LogHelper.info(
									String.format("試行回数=%d, 出目=%s", rouletteContext.currentLoopCount, nextSpot.name()));

							if (strategyList.stream().allMatch(strategy -> !strategy.isLive())) {
								System.out.println("全ての戦略が無効です");
								// TODO
							}

							return true;
						}
					};
				}
			};

			mainLoopService.start();
		});
	}

	public void setRouletteContext(RouletteContext rouletteContext) {
		this.rouletteContext = rouletteContext;
	}

	public SpotHeatmapView getSpotHeatmapView() {
		return spotHeatmapView;
	}

	@FXML
	private void onPauseButtonClick() {
		if (mainLoopService != null && mainLoopService.isRunning()) {
			mainLoopService.cancel();
			pauseButton.setDisable(true);
			resumeButton.setDisable(false);
			LogHelper.info("シミュレーションを一時停止しました");
		}
	}

	@FXML
	private void onResumeButtonClick() {
		if (mainLoopService != null) {
			mainLoopService.restart();
			pauseButton.setDisable(false);
			resumeButton.setDisable(true);
			LogHelper.info("シミュレーションを再開しました");
		}
	}

	@FXML
	private void onStopButtonClick() {
		if (mainLoopService != null) {
			mainLoopService.cancel();
		}
		getThisStage().close();
		LogHelper.info("シミュレーションを終了しました");
	}
}