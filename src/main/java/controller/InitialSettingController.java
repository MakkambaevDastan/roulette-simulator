package controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import application.RouletteContext;
import constants.Configurations;
import enums.HeatmapLayoutType;
import enums.RouletteType;
import enums.SpotGenerateType;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.LogHelper;

public class InitialSettingController extends BaseController {

	@FXML
	private MenuItem aboutMenuItem;

	@FXML
	private RadioButton rouletteTypeRadioButton1;

	@FXML
	private RadioButton rouletteTypeRadioButton2;

	@FXML
	private RadioButton rouletteTypeRadioButton3;

	@FXML
	private TextField initialBalanceTextField;

	@FXML
	private TextField minimumBetTextField;

	@FXML
	private TextField maximumBetTextField;

	@FXML
	private RadioButton runModeRadioButton1;

	@FXML
	private RadioButton runModeRadioButton2;

	@FXML
	private RadioButton heatmapLayoutRadioButton1;

	@FXML
	private RadioButton heatmapLayoutRadioButton2;

	@FXML
	private ComboBox<String> spotGenerateTypeComboBox;

	@FXML
	private Button selectStrategyButton;

	@FXML
	private Button startButton;

	@FXML
	private Slider simulationSpeedSlider;

	@FXML
	private Label simulationSpeedLabel;

	@Override
	public void setOnCloseRequest(WindowEvent event) {
		// TODO
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initialBalanceTextField.setText(String.valueOf(Configurations.DEFAULT_INITIAL_BALANCE));
		minimumBetTextField.setText(String.valueOf(Configurations.DEFAULT_MINIMUM_BET));
		maximumBetTextField.setText(String.valueOf(Configurations.DEFAULT_MAXIMUM_BET));

		spotGenerateTypeComboBox.getItems().addAll(
			"ランダム",
			"数字の順番", 
			"盤上での順番",
			"ランダム(赤のみ)",
			"ランダム(黒のみ)",
			"ランダム(1以外)"
		);
		spotGenerateTypeComboBox.getSelectionModel().select(" ");

		aboutMenuItem.setOnAction(event -> {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Title " + Main.class.getPackage().getImplementationVersion());
			alert.setHeaderText("Roulette Simulator");
			alert.setContentText("Copyright (c) 2018 cyrus");
			alert.show();
		});

		selectStrategyButton.setOnMouseClicked(event -> {
			Stage newStage = new Stage();
			newStage.initModality(Modality.APPLICATION_MODAL);
			newStage.initOwner(getThisStage());

			openSelectStrategyList(newStage, createRouletteContext());
		});

		startButton.setOnMouseClicked(event -> {
			Stage newStage = new Stage();
			newStage.initModality(Modality.APPLICATION_MODAL);
			newStage.initOwner(getThisStage());

			openSimulationMode(newStage, createRouletteContext());
		});

		simulationSpeedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			int speed = newValue.intValue();
			simulationSpeedLabel.setText(speed + "ms");
		});
	}

	private RouletteContext createRouletteContext() {
		RouletteType rouletteType = null;
		if (rouletteTypeRadioButton1.isSelected()) {
			rouletteType = RouletteType.ONE_TO_36;
		} else if (rouletteTypeRadioButton2.isSelected()) {
			rouletteType = RouletteType.EUROPEAN_STYLE;
		} else if (rouletteTypeRadioButton3.isSelected()) {
			rouletteType = RouletteType.AMERICAN_STYLE;
		}
		
		HeatmapLayoutType heatmapLayoutType = null;
		if (heatmapLayoutRadioButton1.isSelected()) {
			heatmapLayoutType = HeatmapLayoutType.CIRCULAR;
		} else if (heatmapLayoutRadioButton2.isSelected()) {
			heatmapLayoutType = HeatmapLayoutType.RECTANGULAR;
		}
		
		String selectedSpotGenerateType = spotGenerateTypeComboBox.getSelectionModel().getSelectedItem();
		SpotGenerateType spotGenerateType = null;
		switch (selectedSpotGenerateType) {
			case "ランダム":
				spotGenerateType = SpotGenerateType.RANDOM;
				break;
			case "数字の順番":
				spotGenerateType = SpotGenerateType.ROTATION_NUMBER;
				break;
			case "盤上での順番":
				spotGenerateType = SpotGenerateType.ROTATION_WHEEL;
				break;
			case "ランダム(赤のみ)":
				spotGenerateType = SpotGenerateType.RANDOM_RED_ONLY;
				break;
			case "ランダム(黒のみ)":
				spotGenerateType = SpotGenerateType.RANDOM_BLACK_ONLY;
				break;
			case "ランダム(1以外)":
				spotGenerateType = SpotGenerateType.RANDOM_EXCEPT_ONE;
				break;
			default:
				spotGenerateType = SpotGenerateType.RANDOM;
				break;
		}
		
		long initialBalance = Long.parseLong(initialBalanceTextField.getText());
		long minimumBet = Long.parseLong(minimumBetTextField.getText());
		long maximumBet = Long.parseLong(maximumBetTextField.getText());

		LogHelper.info("ルーレットのタイプ=" + rouletteType.name());
		LogHelper.info("ヒートマップレイアウト=" + heatmapLayoutType.name());
		LogHelper.info("出目の生成方法=" + spotGenerateType.name());
		LogHelper.info("初期所持金=" + initialBalance);
		LogHelper.info("最小ベット額=" + minimumBet);
		LogHelper.info("最大ベット額=" + maximumBet);
		if (runModeRadioButton1.isSelected()) {
			LogHelper.info("実行モード=" + runModeRadioButton1.getText());
		} else if (runModeRadioButton2.isSelected()) {
			LogHelper.info("実行モード=" + runModeRadioButton2.getText());
		}

		RouletteContext context = new RouletteContext(rouletteType, heatmapLayoutType, spotGenerateType, initialBalance, minimumBet, maximumBet);
		context.simulationSpeed = (long) simulationSpeedSlider.getValue();
		return context;
	}
}