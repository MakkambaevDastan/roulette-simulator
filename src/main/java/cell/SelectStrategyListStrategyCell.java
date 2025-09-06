package cell;

import java.io.IOException;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import strategy.BaseStrategy;

public class SelectStrategyListStrategyCell<T> extends ListCell<BaseStrategy> {

	@FXML
	private Node cellContainer;

	@FXML
	private CheckBox enableCheckBox;

	@FXML
	private Label strategyClassNameLabel;

	private ObservableValue<Boolean> booleanProperty;

	private ObjectProperty<Callback<BaseStrategy, ObservableValue<Boolean>>> selectedStateCallback = new SimpleObjectProperty<Callback<BaseStrategy, ObservableValue<Boolean>>>(
			this, "selectedStateCallback");

	public SelectStrategyListStrategyCell(Callback<BaseStrategy, ObservableValue<Boolean>> getSelectedProperty) {
		setSelectedStateCallback(getSelectedProperty);

		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("/fxml/cell/SelectStrategyListStrategyCell.fxml"));
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Callback<ListView<BaseStrategy>, ListCell<BaseStrategy>> forListView(
			Callback<BaseStrategy, ObservableValue<Boolean>> getSelectedProperty) {
		return list -> new SelectStrategyListStrategyCell<>(getSelectedProperty);
	}

	@Override
	public void updateItem(BaseStrategy strategy, boolean empty) {
		super.updateItem(strategy, empty);
		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			setText(null);
			enableCheckBox.setText(strategy.getStrategyName());
			strategyClassNameLabel.setText(strategy.getClass().getName());

			Callback<BaseStrategy, ObservableValue<Boolean>> callback = getSelectedStateCallback();
			if (callback == null) {
				throw new NullPointerException(
						"The CheckBoxListCell selectedStateCallbackProperty can not be null");
			}
			if (booleanProperty != null) {
				enableCheckBox.selectedProperty().unbindBidirectional((BooleanProperty) booleanProperty);
			}
			booleanProperty = callback.call(strategy);
			if (booleanProperty != null) {
				enableCheckBox.selectedProperty().bindBidirectional((BooleanProperty) booleanProperty);
			}

			setGraphic(cellContainer);
		}
	}

	public final ObjectProperty<Callback<BaseStrategy, ObservableValue<Boolean>>> selectedStateCallbackProperty() {
		return selectedStateCallback;
	}

	public final void setSelectedStateCallback(Callback<BaseStrategy, ObservableValue<Boolean>> value) {
		selectedStateCallbackProperty().set(value);
	}

	public final Callback<BaseStrategy, ObservableValue<Boolean>> getSelectedStateCallback() {
		return selectedStateCallbackProperty().get();
	}
}