package sperlich;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.text.*;

public class Overlay {

	public Stage stage;
	public BorderPane root;
	public GridPane grid, killerPerksGrid;
	public VBox centerVB, rightVB, leftVB, rightCenterVB, leftBotVB, killerPerkVB;
	public HBox totemHB, palletHB, vaultHB, killerPerksHB, survivorsHB;
	public Text killerName, time, mapName, totemText, palletText, vaultText, killerPlayerName;
	public ImageView offering, totemPic, palletPic, vaultPic;
	public ImageView[] killerPerksB = new ImageView[4];
	public ImageView[] killerPerksIcon = new ImageView[4];
	public ProgressBar[] borrowBars = new ProgressBar[4];
	public ProgressBar[] dstrikeBars = new ProgressBar[4];
	public Text[] survivorNames = new Text[4];
	public ImageView[] actionIcons = new ImageView[4];
	public ImageView[] deathIcons = new ImageView[4];
	public Text[] actionTexts = new Text[4];
	public Text[] hookTexts = new Text[4];
	public StackPane[] survivors = new StackPane[4];
	public Button close;
	public Scene scene;
	public int fontSize = 35;
	public int width;
	public int height;
	
	public Overlay() {
		try {
			init();
		} catch (Exception e) {
			Log.out("An unexpected error occurred!");
			StackTraceElement[] elements = e.getStackTrace();
            for (int iterator=1; iterator<=elements.length; iterator++)  {
               Log.out("Class Name:"+elements[iterator-1].getClassName()+" Method Name:"+elements[iterator-1].getMethodName()+" Line Number:"+elements[iterator-1].getLineNumber());
        	}
		}
	}
	
	public void init() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		width = gd.getDisplayMode().getWidth()-25;
		height = gd.getDisplayMode().getHeight()-25;
		
		Log.out("Screen Resolution: " + width + "x" + height);
		
		stage = new Stage();
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setAlwaysOnTop(true);
		stage.getIcons().add(Runtime.getImage("dbd_icon.png"));
		stage.setTitle("overlay");
		stage.setResizable(false);
		stage.setMaxHeight(height);
		stage.setMaxWidth(width);
		grid = grid(3, 3);
		//grid.setGridLinesVisible(true);
		root = new BorderPane();
		root.setBackground(null);
		Log.out("CURRENT OVERLAY OPACITY: " + Runtime.config.overlayOpacity);
		stage.setOpacity(Runtime.config.overlayOpacity);
        
		// Buttons
		close = new Button("X");
		close.setOnAction(e -> stage.close());
		close.setPrefSize(45, 35);
		close.setStyle("-fx-background-color: #F66");
		close.setTextFill(Color.rgb(255, 255, 255));
		// Text
		killerName = text("Unknown", fontSize+10);
		time = text("0m 0s", fontSize);
		mapName = text("", fontSize);
		totemText = text("", fontSize);
		palletText = text("", fontSize);
		vaultText = text("", fontSize);
		killerPlayerName = text("", fontSize);
		// ImageViews
		offering = new ImageView();
		totemPic = new ImageView(Runtime.getImage("totem.png"));
		palletPic = new ImageView(Runtime.getImage("pallet.png"));
		vaultPic = new ImageView(Runtime.getImage("vault.png"));
		// Killer Perks
		killerPerksGrid = grid(1, 4);
		killerPerksGrid.setPrefSize(150, 100);
		int perkSize = 100;
		for (int i=0; i < 4; i++) {
			ImageView perkB = new ImageView(Runtime.getImage("perk_frame.png"));
			ImageView perkIcon = new ImageView(Runtime.getImage("perk_default.png"));
			perkB.setPreserveRatio(false);
			perkB.setFitHeight(perkSize);
			perkB.setFitWidth(perkSize);
			perkIcon.setPreserveRatio(false);
			perkIcon.setFitHeight(perkSize);
			perkIcon.setFitWidth(perkSize);
			killerPerksGrid.add(perkB, i, 0);
			killerPerksGrid.add(perkIcon, i, 0);
			killerPerksB[i] = perkB;
			killerPerksIcon[i] = perkIcon;
		}
		// Survivor
		int iconSize = 50;
		for (int i=0; i < 4; i++) {
			StackPane pStack = new StackPane();
			VBox vb = new VBox();
			Text name = text("survivor_" + i, fontSize-15);
			Text action = text("Action: ", fontSize-15);
			Text hooks = text("Hooks: ", fontSize-15);
			ImageView actionIcon = new ImageView(Runtime.getImage(""));
			ImageView deathIcon = new ImageView(Runtime.getImage(""));
			actionIcon.setFitWidth(iconSize);
			actionIcon.setFitHeight(iconSize);
			deathIcon.setFitWidth(iconSize*2);
			deathIcon.setFitHeight(iconSize*2);
			StackPane stack = new StackPane();
			ProgressBar borrowBar = new ProgressBar();
			ProgressBar dstrikeBar = new ProgressBar();
			borrowBar.setStyle("-fx-accent: #F60");
			dstrikeBar.setStyle("-fx-accent: #38F");
			borrowBar.setPrefSize(125, 12);
			dstrikeBar.setPrefSize(125, 12);
			stack.setAlignment(Pos.CENTER_LEFT);
			borrowBars[i] = borrowBar;
			dstrikeBars[i] = dstrikeBar;
			survivorNames[i] = name;
			actionIcons[i] = actionIcon;
			actionTexts[i] = action;
			hookTexts[i] = hooks;
			deathIcons[i] = deathIcon;
			vb.getChildren().addAll(name, hooks, stack, borrowBar, dstrikeBar);
			pStack.getChildren().addAll(vb, deathIcon);
			vb.setAlignment(Pos.BOTTOM_LEFT);
			stack.getChildren().addAll(action, actionIcon);
			survivors[i] = pStack;
			pStack.setAlignment(Pos.BOTTOM_LEFT);
			switch (i) {
				case 0:
					name.setFill(Color.rgb(255, 34, 34));
					break;
				case 1:
					name.setFill(Color.rgb(255, 170, 0));
					break;
				case 2:
					name.setFill(Color.rgb(68, 187, 0));
					break;
				case 3:
					name.setFill(Color.rgb(68, 68, 221));
					break;
			}
		}
		// HBoxes
		totemHB = new HBox();
		totemHB.setAlignment(Pos.CENTER_RIGHT);
		totemHB.getChildren().addAll(totemText, totemPic);
		palletHB = new HBox();
		palletHB.setAlignment(Pos.CENTER_RIGHT);
		palletHB.getChildren().addAll(palletText, palletPic);
		vaultHB = new HBox();
		vaultHB.setAlignment(Pos.CENTER_RIGHT);
		vaultHB.getChildren().addAll(vaultText, vaultPic);
		killerPerksHB = new HBox();
		killerPerksHB.setAlignment(Pos.CENTER_LEFT);
		killerPerksHB.getChildren().addAll(killerPlayerName);
		survivorsHB = new HBox();
		survivorsHB.setAlignment(Pos.BOTTOM_LEFT);
		survivorsHB.setSpacing(50);
		survivorsHB.setPadding(new Insets(50, 50, 50, 50));
		survivorsHB.getChildren().addAll(survivors);
		 // VBoxes
		centerVB = new VBox();
		rightVB = new VBox();
		leftVB = new VBox();
		leftBotVB = new VBox();
		rightCenterVB = new VBox();
		killerPerkVB = new VBox();
		rightVB.setPadding(new Insets(50, 50, 50, 50));
		rightVB.setAlignment(Pos.TOP_RIGHT);
		rightVB.getChildren().addAll(/*close*/);
		centerVB.setSpacing(25);
		centerVB.setAlignment(Pos.TOP_CENTER);
		centerVB.setPadding(new Insets(50, 50, 50, 50));
		leftVB.setPadding(new Insets(50, 50, 50, 50));
		leftVB.getChildren().addAll(mapName, offering);
		rightCenterVB.setSpacing(50);
		rightCenterVB.setAlignment(Pos.CENTER_RIGHT);
		rightCenterVB.getChildren().addAll(totemHB, palletHB, vaultHB);
		killerPerkVB.setPadding(new Insets(50, 50, 50, 50));
		killerPerkVB.setAlignment(Pos.CENTER_LEFT);
		killerPerkVB.getChildren().addAll(killerPerksHB, killerPerksGrid);
		leftBotVB.setPadding(new Insets(15, 15, 15, 15));
		leftBotVB.setAlignment(Pos.TOP_LEFT);
		leftBotVB.getChildren().addAll(killerPerkVB);
		centerVB.getChildren().addAll(killerName, time);
		// Grid
		grid.setMaxSize(width, height);
		grid.setMinSize(5, 5);
        grid.add(centerVB, 1, 0);
		grid.add(rightVB, 2, 0);
		grid.add(rightCenterVB, 2, 1);
		grid.add(leftBotVB, 0, 2);
		grid.add(leftVB, 0, 0);
		grid.add(survivorsHB, 1, 2);
		root.setCenter(grid);
		root.setMaxSize(width, height);
		root.setPrefSize(width, height);
		
		scene = new Scene(root, width, height);
		stage.setMaxHeight(height);
		stage.setMaxWidth(width);
		scene.setFill(null);
		stage.setScene(scene);
		stage.show();
	}
	
	public Text text(String text, int size) {
		Text t = new Text(text);
		t.setFont(new Font(size));
		t.setFill(Color.WHITE);
		return t;
	}
	
	public GridPane grid(int numRows, int numCols) {
		GridPane g = new GridPane();
		for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / numCols);
            colConst.setMaxWidth(width);
            g.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / numRows);
            rowConst.setMaxHeight(height);
            g.getRowConstraints().add(rowConst);
        }
        return g;
	}
	
	public void update() {
		Platform.runLater(()-> {
			killerName.setText(Runtime.killerName.getText());
			time.setText(Runtime.matchStatusTime.getText());
			mapName.setText(Runtime.mapName.getText());
			offering.setImage(Runtime.killerOffering.getImage());
			totemText.setText(Runtime.totalTotems.getText());
			palletText.setText(Runtime.destroyedPallets.getText());
			vaultText.setText(Runtime.totalVaults.getText());
			killerPlayerName.setText(Runtime.killerPlayerName.getText());
			for (int i=0; i < 4; i++) {
				ImageView perkB = killerPerksB[i];
				ImageView perkIcon = killerPerksIcon[i];
				perkB.setVisible(Runtime.killerPerkFrames[i].isVisible());
				perkIcon.setVisible(Runtime.killerPerks[i].isVisible());
				if (perkIcon.isVisible()) {
					perkIcon.setImage(Runtime.killerPerks[i].getImage());
				}
				borrowBars[i].setVisible(Runtime.survivorBars[i].isVisible());
				borrowBars[i].setProgress(Runtime.survivorBars[i].getProgress());
				dstrikeBars[i].setVisible(Runtime.dstrikeBars[i].isVisible());
				dstrikeBars[i].setProgress(Runtime.dstrikeBars[i].getProgress());
				survivorNames[i].setText(Runtime.survivorNames[i].getText());
				actionIcons[i].setVisible(Runtime.actionIcons[i].isVisible());
				actionIcons[i].setImage(Runtime.actionIcons[i].getImage());
				actionTexts[i].setVisible(Runtime.survivorAction[i].isVisible());
				actionTexts[i].setText(Runtime.survivorAction[i].getText());
				deathIcons[i].setVisible(Runtime.deadSymbols[i].isVisible());
				deathIcons[i].setImage(Runtime.deadSymbols[i].getImage());
				hookTexts[i].setText(Runtime.totalHooks[i].getText());
				hookTexts[i].setVisible(Runtime.totalHooks[i].isVisible());
			}
		});
	}
}
