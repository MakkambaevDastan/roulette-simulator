package controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.RouletteContext;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public abstract class BaseController implements Initializable {

	private Stage thisStage;

	public void setThisStage(Stage stage) {
		thisStage = stage;
	}

	public Stage getThisStage() {
		return thisStage;
	}

	public abstract void initialize(URL location, ResourceBundle resources);

	public abstract void setOnCloseRequest(WindowEvent event);

	public static void openInitSetting(Stage stage) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(ClassLoader.class.getResource("/fxml/InitialSetting.fxml"));
			Parent root = fxmlLoader.load();
			BaseController controller = fxmlLoader.getController();
			controller.setThisStage(stage);
			stage.setTitle("ルーレットシミュレーター");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void openSelectStrategyList(Stage stage, RouletteContext rouletteContext) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(ClassLoader.class.getResource("/fxml/SelectStrategyList.fxml"));
			Parent root = fxmlLoader.load();

			SelectStrategyListController controller = fxmlLoader.getController();
			initializeController(controller, stage);
			controller.setRouletteContext(rouletteContext);

			stage.setTitle("Title");

			Scene scene = new Scene(root);

			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void openSimulationMode(Stage stage, RouletteContext rouletteContext) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(ClassLoader.class.getResource("/fxml/SimulationMode.fxml"));
			Parent root = fxmlLoader.load();

			SimulationModeController controller = fxmlLoader.getController();
			initializeController(controller, stage);
			controller.setRouletteContext(rouletteContext);

			stage.setTitle("Title");

			Scene scene = new Scene(root);

			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void initializeController(BaseController controller, Stage stage) {
		controller.setThisStage(stage);
		stage.setOnCloseRequest(event -> {
			controller.setOnCloseRequest(event);
		});
	}
}