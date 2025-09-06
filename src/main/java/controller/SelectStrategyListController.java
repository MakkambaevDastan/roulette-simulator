package controller;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import application.RouletteContext;
import cell.SelectStrategyListStrategyCell;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.stage.WindowEvent;
import strategy.BaseStrategy;
import utils.StrategyHelper;

public class SelectStrategyListController extends BaseController {

	private RouletteContext rouletteContext;

	@FXML
	private ListView<BaseStrategy> strategyListView;

	@FXML
	private CheckBox selectAllCheckBox;

	@FXML
	private Button okButton;

	@FXML
	private Button cancelButton;

	private Map<String, SimpleBooleanProperty> onMap = new HashMap<>();

	@Override
	public void setOnCloseRequest(WindowEvent event) {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater(() -> {
			Set<Class<? extends BaseStrategy>> allStrategySet = StrategyHelper.getAllStrategyClassSet();

			Set<Class<? extends BaseStrategy>> enableStrategySet = StrategyHelper.getEnableStrategyClassSet();

			for (Class<? extends BaseStrategy> strategyClass : allStrategySet) {
				onMap.put(strategyClass.getName(),
						new SimpleBooleanProperty(enableStrategySet.contains(strategyClass)));
			}

			strategyListView.setCellFactory(SelectStrategyListStrategyCell
					.forListView((BaseStrategy strategy) -> onMap.get(strategy.getClass().getName())));
			strategyListView.setItems(StrategyHelper.createStrategyList(allStrategySet, rouletteContext));

			strategyListView.getItems().sort(BaseStrategy.getStrategyNameComparator());
			
			updateSelectAllCheckBox();
			
			selectAllCheckBox.setOnAction(event -> {
				boolean selectAll = selectAllCheckBox.isSelected();
				for (BaseStrategy strategy : strategyListView.getItems()) {
					SimpleBooleanProperty property = onMap.get(strategy.getClass().getName());
					if (property != null) {
						property.setValue(selectAll);
					}
				}
			});
			
			for (SimpleBooleanProperty property : onMap.values()) {
				property.addListener((observable, oldValue, newValue) -> {
					updateSelectAllCheckBox();
				});
			}
		});

		okButton.setOnMouseClicked(event -> {
			Set<Class<? extends BaseStrategy>> enableStrategySet = new HashSet<>();
			for (BaseStrategy strategy : strategyListView.getItems()) {
				try {
					if (onMap.get(strategy.getClass().getName()).getValue()) {
						enableStrategySet.add(strategy.getClass());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			StrategyHelper.saveEnableStrategyClassSet(enableStrategySet);

			getThisStage().close();
		});

		cancelButton.setOnMouseClicked(event -> {
			getThisStage().close();
		});
	}

	public void setRouletteContext(RouletteContext rouletteContext) {
		this.rouletteContext = rouletteContext;
	}

	private void updateSelectAllCheckBox() {
		if (strategyListView.getItems() == null || strategyListView.getItems().isEmpty()) {
			selectAllCheckBox.setSelected(false);
			return;
		}
		
		boolean allSelected = true;
		for (BaseStrategy strategy : strategyListView.getItems()) {
			SimpleBooleanProperty property = onMap.get(strategy.getClass().getName());
			if (property == null || !property.getValue()) {
				allSelected = false;
				break;
			}
		}
		
		selectAllCheckBox.setSelected(allSelected);
	}
}