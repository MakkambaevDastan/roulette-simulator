package view;

import java.util.Map;

import application.RouletteContext;
import enums.HeatmapLayoutType;
import enums.RouletteType;
import enums.Spot;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SpotHeatmapView extends GridPane {

	private RouletteContext rouletteContext;
	private Label[][] spotLabels;
	private static final String BASE_SPOT_LABEL_STYLE = "-fx-alignment: center; -fx-border-color: gray; -fx-border-width: 1;";

	public SpotHeatmapView(RouletteContext rouletteContext) {
		this.rouletteContext = rouletteContext;
		initializeHeatmap();
	}

	private void initializeHeatmap() {
		Spot[] wheelLayout = Spot.getWheelLayout(rouletteContext.rouletteType);

		if (rouletteContext.heatmapLayoutType == HeatmapLayoutType.CIRCULAR) {
			if (tryCircularLayout(wheelLayout)) {
				return;
			}
		}

		initializeRectangularLayout(wheelLayout);
	}

	private boolean tryCircularLayout(Spot[] wheelLayout) {
		try {
			int numSpots = wheelLayout.length;

			double radius = Math.max(4, numSpots / (2 * Math.PI));
			int gridSize = (int) (radius * 2 + 4);

			spotLabels = new Label[gridSize][gridSize];

			setHgap(2);
			setVgap(2);
			setStyle("-fx-padding: 5;");

			double centerX = gridSize / 2.0;
			double centerY = gridSize / 2.0;

			for (int i = 0; i < numSpots; i++) {
				Spot spot = wheelLayout[i];
				double angle = 2 * Math.PI * i / numSpots - Math.PI / 2;

				int x = (int) Math.round(centerX + radius * Math.cos(angle));
				int y = (int) Math.round(centerY + radius * Math.sin(angle));

				if (rouletteContext.rouletteType == RouletteType.EUROPEAN_STYLE && spot == Spot.SPOT_0) {
					// FIXME
					Label label = createSpotLabel(wheelLayout[i]);
					spotLabels[x][y + 1] = label;
					add(label, x, y + 1);
				} else {
					if (x >= 0 && x < gridSize && y >= 0 && y < gridSize) {
						Label label = createSpotLabel(wheelLayout[i]);
						spotLabels[y][x] = label;
						add(label, x, y);
					} else {
						return false;
					}
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void initializeRectangularLayout(Spot[] wheelLayout) {
		int numSpots = wheelLayout.length;
		int cols = calculateCompactColumns(numSpots);
		int rows = (int) Math.ceil((double) numSpots / cols);

		spotLabels = new Label[rows][cols];

		setHgap(2);
		setVgap(2);
		setStyle("-fx-padding: 5;");

		int row = 0;
		int col = 0;

		for (Spot spot : wheelLayout) {
			Label label = createSpotLabel(spot);
			spotLabels[row][col] = label;
			add(label, col, row);

			col++;
			if (col >= cols) {
				col = 0;
				row++;
			}
		}
	}

	private void initializeGridLayout(Spot[] wheelLayout) {

		int numSpots = wheelLayout.length;
		int cols = calculateOptimalColumns(numSpots);
		int rows = (int) Math.ceil((double) numSpots / cols);

		spotLabels = new Label[rows][cols];

		setHgap(2);
		setVgap(2);
		setStyle("-fx-padding: 5;");

		int row = 0;
		int col = 0;

		for (Spot spot : wheelLayout) {
			Label label = createSpotLabel(spot);
			spotLabels[row][col] = label;
			add(label, col, row);

			col++;
			if (col >= cols) {
				col = 0;
				row++;
			}
		}
	}

	public void updateHeatmap() {
		if (rouletteContext.spotHistoryList.isEmpty()) {
			return;
		}

		Map<Spot, Integer> frequencyMap = rouletteContext.getSpotFrequency();

		int maxCount = frequencyMap.values().stream().mapToInt(Integer::intValue).max().orElse(1);

		for (int row = 0; row < spotLabels.length; row++) {
			for (int col = 0; col < spotLabels[row].length; col++) {
				Label label = spotLabels[row][col];
				if (label != null) {
					Spot spot = getSpotFromLabel(label);
					int count = frequencyMap.getOrDefault(spot, 0);
					updateSpotLabelColor(label, spot, count == 0 ? 0 : (double) count / maxCount);
				}
			}
		}
	}

	private static String getSpotDisplayText(Spot spot) {
		if (spot == Spot.SPOT_00) {
			return "00";
		} else if (spot == Spot.SPOT_0) {
			return "0";
		} else {
			return String.valueOf(spot.getNumber());
		}
	}

	private static Spot getSpotFromLabel(Label label) {
		String text = label.getText();
		if ("00".equals(text)) {
			return Spot.SPOT_00;
		} else if ("0".equals(text)) {
			return Spot.SPOT_0;
		} else {
			try {
				int number = Integer.parseInt(text);
				return Spot.getByNumber(number);
			} catch (NumberFormatException e) {
				return Spot.SPOT_0; // デフォルト
			}
		}
	}

	private static int calculateCompactColumns(int numSpots) {
		int cols = (int) Math.ceil(Math.sqrt(numSpots));

		if (numSpots <= 36) {
			return Math.min(cols, 8);
		} else if (numSpots <= 37) {
			return Math.min(cols, 8);
		} else if (numSpots <= 38) {
			return Math.min(cols, 8);
		} else {
			return Math.min(cols, 10);
		}
	}

	private static int calculateOptimalColumns(int numSpots) {
		if (numSpots <= 36) {
			return 6;
		} else if (numSpots <= 42) {
			return 7;
		} else {
			return 8;
		}
	}

	private static Label createSpotLabel(Spot spot) {
		Label label = new Label(getSpotDisplayText(spot));
		label.setMinSize(25, 25);
		label.setMaxSize(25, 25);
		label.setStyle(BASE_SPOT_LABEL_STYLE);
		label.setFont(Font.font("System", FontWeight.BOLD, 9));

		updateSpotLabelColor(label, spot, 0);

		return label;
	}

	private static void updateSpotLabelColor(Label label, Spot spot, double intensity) {
		if (intensity == 0) {
			String textColor = spot.isRed() ? "red" : (spot.isBlack() ? "black" : "green");
			label.setStyle(BASE_SPOT_LABEL_STYLE + "; -fx-background-color: white; -fx-text-fill: " + textColor + ";");
		} else {
			String backgroundColor;
			String textColor;

			if (spot.isRed()) {
				double alpha = 0.3 + (intensity * 0.7);
				backgroundColor = String.format("rgba(255,0,0,%.2f)", alpha);
				textColor = intensity > 0.5 ? "white" : "red";
			} else if (spot.isBlack()) {
				double alpha = 0.3 + (intensity * 0.7);
				backgroundColor = String.format("rgba(0,0,0,%.2f)", alpha);
				textColor = intensity > 0.3 ? "white" : "black";
			} else {
				double alpha = 0.3 + (intensity * 0.7);
				backgroundColor = String.format("rgba(0,128,0,%.2f)", alpha);
				textColor = intensity > 0.5 ? "white" : "green";
			}

			label.setStyle(BASE_SPOT_LABEL_STYLE + "; -fx-background-color: " + backgroundColor +
					"; -fx-text-fill: " + textColor + ";");
		}
	}
}