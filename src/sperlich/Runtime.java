package sperlich;

import com.sun.glass.events.KeyEvent;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.xml.internal.fastinfoset.Encoder;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.ImageIcon;
import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;

public class Runtime extends Application {
	public static String appVersion = "v1.8.3";
	public static String download = "https://www.sperlich.at/storage/content/DBD%20Plus.jar";
	public static String downloadPath = System.getProperty("user.dir") + "/DBD Plus.jar";
	public static String killerSteamURL;
	public static String lightTheme;
	public static String darkTheme;
	public static Thread keyboardThread;
	public static Thread mouseThread;
	public static Thread gameReader;
	public static Thread updater;
	public static Text killerName;
	public static Text matchStatusTime;
	public static Text totalTotems;
	public static Text mapName;
	public static Text mapTitle;
	public static Text offeringTitle;
	public static Text destroyedPallets;
	public static Text totalVaults;
	public static Text killerPlayerName;
	public static Text appVersionText;
	public static Text startLoadText;
	public static Text survivorsTitle;
	public static Text hatchTitle;
	public static Text totalBloodpoints;
	public static Text[] survivorNames = new Text[4];
	public static Text[] totalHooks = new Text[4];
	public static Text[] survivorAction = new Text[4];
	public static Text[] obsessionStatesText = new Text[4];
	public static ImageView vaultIcon;
	public static ImageView bodyImage;
	public static ImageView killerPic;
	public static ImageView totemPic;
	public static ImageView palletIcon;
	public static ImageView killerBackground;
	public static ImageView killerOffering;
	public static ImageView loadImage;
	public static ImageView hatchImage;
	public static ImageView[] killerPerks = new ImageView[4];
	public static ImageView[] killerPerkFrames = new ImageView[4];
	public static ImageView[] deadSymbols = new ImageView[4];
	public static ImageView[] actionIcons = new ImageView[4];
	public static ProgressBar[] survivorBars = new ProgressBar[4];
	public static ProgressBar[] dstrikeBars = new ProgressBar[4];
	public static ProgressBar startUpLoadBar;
	public static ProgressBar downloadProgress;
	public static HBox totemsBox;
	public static HBox palletsBox;
	public static HBox vaultsBox;
	public static HBox bloodpointsBox;
	public static VBox mapBox;
	public static CheckMenuItem skillCheckMenuItem;
	public static Configuration config = new Configuration();
	public static boolean uiInitialized;
	public static boolean mouseButton1Hold;
	public static Node startScreen;
	public static AnchorPane body;
	public static Scene scene;
	public static WinUser.INPUT input;
	public static Font font;
	public static Parent root;
	public static TextArea feedback;
	public static Stage commentWindow;
	public static Stage disclaimerWindow;
	public static Stage setKeyWindow;
	public static Stage overlayWindow;
	public static Stage stage;
	public static char skillCheckLetter = 'P';
	public static FXMLLoader loader;
	public static DropShadow oldShadow;
	public static CheckBox policyCheck;
	public static Label networkCheck;
	public static boolean overlayMode;
	public static Overlay overlay;
	public static boolean toggleMode;
	public static Slider opacitySlider;

	public static void main(String[] args) {
		Log.out("Application started.");
		Application.launch(args);
	}
	
	@Override
	public void init() {
		//keyboardThread = new Input();
		//keyboardThread.start();
		//mouseThread = new MouseInput();
		//mouseThread.start();
		initializeUserInput();
	}
	
	@Override
	public void start(Stage stage) {
		loader = new FXMLLoader(getClass().getResource("window.fxml"));
		try {
			root = loader.load();
		} catch (IOException e1) {
			Log.out("Failed to load FXMLLoader");
		}
		Runtime.scene = new Scene(root, 1000, 650);
		stage.setTitle("DBD+");
		stage.setMinHeight(600);
		stage.setMinWidth(1000);
		stage.getIcons().add(getImage("dbd_icon.png"));
		stage.setScene(scene);
		stage.setResizable(true);
		stage.setOnCloseRequest(event -> {
			try {
				Runtime.config.mainWindowPosX = (int) stage.getX();
				Runtime.config.mainWindowPosY = (int) stage.getY();
				Runtime.config.save();
			} catch (Exception e) {
				exitApplication();
			}
			exitApplication();
		});
		URL css = Runtime.class.getResource("style.css");
		Runtime.lightTheme = getClass().getResource("light_theme.css").toExternalForm();
		Runtime.darkTheme = getClass().getResource("dark_theme.css").toExternalForm();
		File saveFile = new File("/DBDPlus/dbdplus.config");
		if (!saveFile.exists()) {
			Runtime.config = new Configuration();
			setDarkTheme();
		}
		MenuItem skillChecker = (MenuItem) loader.getNamespace().get("skillChecker");
		skillChecker.setVisible(false);
		opacitySlider = (Slider) loader.getNamespace().get("opacitySlider");
		body = (AnchorPane) root.lookup("#body");
		killerName = (Text) root.lookup("#killerId");
		hatchTitle = (Text) root.lookup("#hatchTitle");
		hatchImage = (ImageView) root.lookup("#hatch");
		killerPlayerName = (Text) root.lookup("#killerName");
		matchStatusTime = (Text) root.lookup("#gameStatusTime");
		killerPic = (ImageView) root.lookup("#killerPic");
		mapName = (Text) root.lookup("#mapName");
		mapTitle = (Text) root.lookup("#mapTitle");
		offeringTitle = (Text) root.lookup("#offeringTitle");
		killerOffering = (ImageView) root.lookup("#killerOffering");
		destroyedPallets = (Text) root.lookup("#destroyedPallets");
		totalVaults = (Text) root.lookup("#totalVaults");
		appVersionText = (Text) root.lookup("#appVersion");
		totemsBox = (HBox) root.lookup("#totemsBox");
		palletsBox = (HBox) root.lookup("#palletsBox");
		vaultsBox = (HBox) root.lookup("#vaultsBox");
		bloodpointsBox = (HBox) root.lookup("#bloodpointsBox");
		mapBox = (VBox) root.lookup("#mapContainer");
		survivorsTitle = (Text) root.lookup("#survivorsTitle");
		totalBloodpoints = (Text) root.lookup("#totalBloodpoints");
		startUpLoadBar = (ProgressBar) root.lookup("#startUpLoadingBar");
		startLoadText = (Text) root.lookup("#startLoadText");
		startScreen = root.lookup("#startScreen");
		palletIcon = (ImageView) root.lookup("#palletIcon");
		vaultIcon = (ImageView) root.lookup("#vaultIcon");
		totemPic = (ImageView) root.lookup("#totemPic");
		loadImage = (ImageView) root.lookup("#loadImage");
		loadImage.fitWidthProperty().bind(stage.widthProperty());
		loadImage.fitHeightProperty().bind(stage.heightProperty());
		loadImage.setVisible(true);
		totemPic.setImage(getImage("totem.png"));
		palletIcon.setImage(getImage("pallet.png"));
		vaultIcon.setImage(getImage("vault.png"));
		startScreen.setVisible(true);
		startUpLoadBar.setProgress(0);
		downloadProgress = (ProgressBar) root.lookup("#downloadBar");
		downloadProgress.setVisible(false);
		//totalTotems.setText("0/0");
		matchStatusTime.setText("LOBBY");
		killerOffering.setImage(null);
		destroyedPallets.setText("0/0");
		totalVaults.setText("0/0");
		appVersionText.setOnMouseClicked(e -> downloadNewVersion());
		for (int i = 0; i < 4; i++) {
			killerPerks[i] = (ImageView) root.lookup("#perk_"+i);
			killerPerkFrames[i] = (ImageView) root.lookup("#perk_frame"+i);
			if (killerPerks[i] != null && killerPerkFrames[i] != null) {
				String perkIcon = "perk_default.png";
				String perk_frame = "perk_frame.png";
				killerPerks[i].setImage(getImage(perkIcon));
				killerPerkFrames[i].setImage(getImage(perk_frame));
				killerPerks[i].setVisible(false);
				killerPerkFrames[i].setVisible(false);
			}
			actionIcons[i] = (ImageView) root.lookup("#actionIcon"+i);
			actionIcons[i].setVisible(false);
			actionIcons[i].setManaged(false);
			survivorNames[i] = (Text)root.lookup("#surv"+i);
			survivorNames[i].setText("");
			totalHooks[i] = (Text)root.lookup("#totalHooks"+i);
			survivorAction[i] = (Text)root.lookup("#survAction"+i);
			survivorBars[i] = (ProgressBar)root.lookup("#survBar"+i);
			dstrikeBars[i] = (ProgressBar)root.lookup("#dstrikeBar"+i);
			obsessionStatesText[i] = (Text)root.lookup("#obsessionStateText"+i);
			deadSymbols[i] = (ImageView) root.lookup("#survDead"+i);
			setImage(deadSymbols[i], "dead_symbol.png");
			deadSymbols[i].setManaged(false);
			deadSymbols[i].setVisible(false);
			survivorBars[i].setVisible(false);
			dstrikeBars[i].setVisible(false);
			obsessionStatesText[i].setVisible(false);
		}
		killerPic.setImage(getImage("unknown.png"));
		mapName.setVisible(false);
		mapTitle.setVisible(false);
		killerOffering.setVisible(false);
		offeringTitle.setVisible(false);
		totemsBox.setVisible(false);
		palletsBox.setVisible(false);
		vaultsBox.setVisible(false);
		survivorsTitle.setVisible(false);
		mapBox.setVisible(false);
		hatchTitle.setVisible(false);
		hatchImage.setVisible(false);
		uiInitialized = true;
		final Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (!GameReader.loadingFinished) {
                    updateProgress(GameReader.loadingProgress, GameReader.totalLines);
                    Thread.sleep(10);
                }
                return null;
            }
        };
        Runtime.startUpLoadBar.progressProperty().bind(task.progressProperty());
        Runtime.config.load();
        toggleMode = false;
        toggleScreenOverlay();
        toggleMode= true;
		gameReader = new Thread(new GameReader() {
			@Override
			public void run() {
				try { initialize(); } catch (InterruptedException e) { Log.out("Couldn't start GameReader thread!"); }
			}
		});
		gameReader.start();
		Log.out("UI successfully rendered.");
		Runtime.scene.getStylesheets().add(css.toString());
		Runtime.config.load();
		skillCheckLetter = config.skillCheckLetter;
		setLoadTheme();
		loadPerformSkillChecks();
		checkForUpdate();
		config.performSkillchecks = false;
		scene.setOnMouseClicked(e -> {
			Runtime.config.mainWindowPosX = (int) stage.getX();
			Runtime.config.mainWindowPosY = (int) stage.getY();
			Runtime.config.save();
		});
		stage.setX(Runtime.config.mainWindowPosX);
		stage.setY(Runtime.config.mainWindowPosY);
		stage.show();
		if (!config.policy) {
			openDisclaimerPopup();
		}
	}
	
	public void toggleScreenOverlay() {
		boolean switched = false;
		if (toggleMode) {
			if (Runtime.config.overlay) {
				Runtime.config.overlay = false;
				Runtime.config.save();
				switched = true;
			} else {
				Runtime.config.overlay = true;
				Runtime.config.save();
				switched = true;
			}
		}
		if (Runtime.config.overlay) {
			Log.out("SCREEN OVERLAY: ON");
			drawScreenOverlay();
		} else {
			if (overlay != null) {
				Log.out("SCREEN OVERLAY: OFF");
				overlay.stage.close();
			}
		}
		CheckMenuItem overlayMenu = (CheckMenuItem) loader.getNamespace().get("screenOverlay");
		overlayMenu.setSelected(config.overlay);
		//MenuBar menuBar = (MenuBar) loader.getNamespace().get("menuBar");
		//Menu toggleMenu = (Menu) loader.getNamespace().get("toggleMenu");
		if (!switched) {
			toggleTime();
			//toggleTotems();
			//togglePallets();
			//toggleVaults();
			//toggleBloodpoints();
			toggleMap();
			opacitySlider.setValue(Runtime.config.overlayOpacity*100);
		}
	}
	
	public void drawScreenOverlay() {
		overlay = new Overlay();
		overlay.update();
	}
	
	public static void updateOverlay() {
		if (overlay != null) {
			overlay.update();
		}
	}
	
	public void toggleTime() {
		if (toggleMode) {
			if (Runtime.config.time) {
				Runtime.config.time = false;
				Runtime.config.save();
			} else {
				Runtime.config.time = true;
				Runtime.config.save();
			}
		}
		if (Runtime.config.time) {
			Log.out("DISPLAY TIME: ON");
			toggleNode(Runtime.matchStatusTime, true);
			if (overlay != null) {
				toggleNode(overlay.time, true);
			}
		} else {
			Log.out("DISPLAY TIME: OFF");
			toggleNode(Runtime.matchStatusTime, false);
			if (overlay != null) {
				toggleNode(overlay.time, false);
			}
		}
		CheckMenuItem toggler = (CheckMenuItem) loader.getNamespace().get("toggleTime");
		toggler.setSelected(config.time);
	}
	
	public void toggleTotems() {
		if (toggleMode) {
			if (Runtime.config.totems) {
				Runtime.config.totems = false;
				Runtime.config.save();
			} else {
				Runtime.config.totems = true;
				Runtime.config.save();
			}
		}
		Runtime.config.totems = false;
		Runtime.config.save();
		if (Runtime.config.totems) {
			Log.out("DISPLAY TOTEMS: ON");
			toggleNode(Runtime.totemsBox, true);
			if (overlay != null) {
				toggleNode(overlay.totemHB, true);
			}
		} else {
			Log.out("DISPLAY TOTEMS: OFF");
			toggleNode(Runtime.totemsBox, false);
			if (overlay != null) {
				toggleNode(overlay.totemHB, false);
			}
		}
		CheckMenuItem toggler = (CheckMenuItem) loader.getNamespace().get("toggleTotems");
		toggler.setSelected(config.totems);
	}
	
	public void togglePallets() {
		if (toggleMode) {
			if (Runtime.config.pallets) {
				Runtime.config.pallets = false;
				Runtime.config.save();
			} else {
				Runtime.config.pallets = true;
				Runtime.config.save();
			}
		}
		Runtime.config.pallets = false;
		Runtime.config.save();
		if (Runtime.config.pallets) {
			Log.out("DISPLAY PALLETS: ON");
			toggleNode(Runtime.palletsBox, true);
			if (overlay != null) {
				toggleNode(overlay.palletHB, true);
			}
		} else {
			Log.out("DISPLAY PALLETS: OFF");
			toggleNode(Runtime.palletsBox, false);
			if (overlay != null) {
				toggleNode(overlay.palletHB, false);
			}
		}
		CheckMenuItem toggler = (CheckMenuItem) loader.getNamespace().get("togglePallets");
		toggler.setSelected(config.pallets);
	}
	
	public void toggleVaults() {
		if (toggleMode) {
			if (Runtime.config.vaults) {
				Runtime.config.vaults = false;
				Runtime.config.save();
			} else {
				Runtime.config.vaults = true;
				Runtime.config.save();
			}
		}
		Runtime.config.vaults = false;
		Runtime.config.save();
		if (Runtime.config.vaults) {
			Log.out("DISPLAY VAULTS: ON");
			toggleNode(Runtime.vaultsBox, true);
			if (overlay != null) {
				toggleNode(overlay.vaultHB, true);
			}
		} else {
			Log.out("DISPLAY TIME: OFF");
			toggleNode(Runtime.vaultsBox, false);
			if (overlay != null) {
				toggleNode(overlay.vaultHB, false);
			}
		}
		CheckMenuItem toggler = (CheckMenuItem) loader.getNamespace().get("toggleVaults");
		toggler.setSelected(config.vaults);
	}
	
	public void toggleBloodpoints() {
		if (toggleMode) {
			if (Runtime.config.bloodpoints) {
				Runtime.config.bloodpoints = false;
				Runtime.config.save();
			} else {
				Runtime.config.bloodpoints = true;
				Runtime.config.save();
			}
		}
		if (Runtime.config.bloodpoints) {
			Log.out("DISPLAY BLOODPOINTS: ON");
			toggleNode(Runtime.bloodpointsBox, true);
		} else {
			Log.out("DISPLAY BLOODPOINTS: OFF");
			toggleNode(Runtime.bloodpointsBox, false);
		}
		CheckMenuItem toggler = (CheckMenuItem) loader.getNamespace().get("toggleBloodpoints");
		toggler.setSelected(config.bloodpoints);
	}
	
	public void toggleMap() {
		if (toggleMode) {
			if (Runtime.config.map) {
				Runtime.config.map = false;
				Runtime.config.save();
			} else {
				Runtime.config.map = true;
				Runtime.config.save();
			}
		}
		if (Runtime.config.map) {
			Log.out("DISPLAY MAP: ON");
			toggleNode(Runtime.mapBox, true);
			if (overlay != null) {
				toggleNode(overlay.mapName, true);
			}
		} else {
			Log.out("DISPLAY MAP: OFF");
			toggleNode(Runtime.mapBox, false);
			if (overlay != null) {
				toggleNode(overlay.mapName, false);
			}
		}
		CheckMenuItem toggler = (CheckMenuItem) loader.getNamespace().get("toggleMap");
		toggler.setSelected(Runtime.config.map);
	}
	
	public void updateOverlayOpacity() {
		if (overlay != null) {
			overlay.stage.setOpacity(opacitySlider.getValue()/100);
			Runtime.config.overlayOpacity = opacitySlider.getValue()/100;
			Runtime.config.save();
		}
	}
	
	public void editOverlay() {
		if (overlay != null) {
			Log.out("Enter Overlay Edit Mode");
			overlay.enterOverlayEditMode();
		}
	}
	
	public static Image getImage(String url) {
		URL image = Runtime.class.getResource(url);
		return new Image(image.toString());
	}
	
	public static void deleteStartScreen() {
		body.getChildren().remove(startScreen);
	}
	
	public void openFeedbackWindow() {
		commentWindow = new Stage();
		BorderPane div = new BorderPane();
		feedback = new TextArea();
		feedback.setWrapText(true);
		feedback.setPromptText("Write your feedback here:");
		div.setStyle("-fx-background-color: #111");
		Label title = new Label("Write your feedback here: \n "
				+ "\n You may include: "
				+ "\n - Suggestions"
				+ "\n - Improvements "
				+ "\n - New Features "
				+ "\n - Bugs "
				+ "\n - Anything else \n "
				+ "\n You can also write me an email to: dbdplus.official@gmail.com");
		title.setTextFill(new Color(1, 1, 1, 1));
		title.setPadding(new Insets(25, 25, 0, 25));
		HBox bottom = new HBox();
		Button send = new Button("send");
		Button close = new Button("close");
		send.setPrefSize(65, 30);
		close.setPrefSize(65, 30);
		HBox.setMargin(send, new Insets(10,10,10,10));
		HBox.setMargin(close, new Insets(10,10,10,10));
		bottom.setAlignment(Pos.CENTER_RIGHT);
		send.setOnMouseClicked(e -> sendFeedback());
		close.setOnMouseClicked(e -> closeFeedback());
		bottom.getChildren().addAll(close, send);
		BorderPane.setMargin(feedback, new Insets(25, 25, 25, 25));
		div.setBottom(bottom);
		div.setCenter(feedback);
		div.setTop(title);
		
		commentWindow.getIcons().add(getImage("dbd_icon.png"));
		commentWindow.setTitle("Feedback");
		commentWindow.setScene(new Scene(div, 550, 400));
		commentWindow.show();
	}
	
	public void sendFeedback() {
		Log.out("Sending Feedback...");
		 URL link;
		try {
			String text = URLEncoder.encode(feedback.getText(), Encoder.UTF_8);
			Log.out("Trying to send: '" + text + "'");
			link = new URL("https://www.sperlich.at/dbdplus.php?fromuser=true&username="+getPcname()+"&feedback=true&comment=" + text);
			BufferedReader site = new BufferedReader(new InputStreamReader(link.openStream()));
	        site.close();
	        Log.out("Feedback has been successfully send!");
	        Alert al = new Alert(Alert.AlertType.INFORMATION, "Thank you for your feedback. \n I received your feedback and will see what I can do.");
			al.setTitle("Feedback send");
			al.showAndWait();
	        
		} catch (MalformedURLException e) {
			Log.out("IO Exception to check for new app version to download.");
			Alert al = new Alert(Alert.AlertType.ERROR, "An error occurred. It may work at the second try.");
			al.setTitle("Unknown Error occured");
			al.showAndWait();
		} catch (IOException e) {
			Log.out("IO Exception for comment feedback occurred.");
			Alert al = new Alert(Alert.AlertType.ERROR, "An error occurred. It may work at the second try.");
			al.setTitle("Unknown Error occured");
			al.showAndWait();
		}
		closeFeedback();
	}
	
	public void closeFeedback() {
		commentWindow.close();
	}
	
	public void openSteamProfile() {
		Log.out("Trying to open URL: " + killerSteamURL);
		openWebpage(killerSteamURL);
	}
	
	public void openTwitter() {
		Log.out("Trying to open URL: https://twitter.com/DbdPlus");
		openWebpage("https://twitter.com/DbdPlus");
	}
	
	public void mouseKillerPicEnter() {
		oldShadow = (DropShadow) killerPic.getEffect();
		DropShadow ds = new DropShadow();
		ds.setColor(Color.rgb(50, 50, 255));
		setShadow(killerPic, ds);
	}
	
	public void mouseKillerPicExit() {
		setShadow(killerPic, oldShadow);
	}
	
	public void showSkillCheckKey() {
		setKeyWindow = new Stage();
		BorderPane div = new BorderPane();
		Text desc = new Text("Type your preferred action key into the field below: ");
		desc.setFill(Color.rgb(255, 255, 255));
		TextField inputField = new TextField();
		inputField.setOnKeyReleased(e -> setMaxInputLength(inputField));
		inputField.setMaxSize(50, 25);
		inputField.setAlignment(Pos.CENTER);
		inputField.setPromptText("KEY");
		div.setStyle("-fx-background-color: #111");
		HBox bottom = new HBox();
		Button send = new Button("ok");
		Button close = new Button("close");
		bottom.setAlignment(Pos.CENTER_RIGHT);
		send.setOnMouseClicked(e -> setSkillCheckKey(inputField.getText()));
		close.setOnMouseClicked(e -> setKeyWindow.close());
		send.setPrefSize(65, 30);
		close.setPrefSize(65, 30);
		HBox.setMargin(send, new Insets(10,10,10,10));
		HBox.setMargin(close, new Insets(10,10,10,10));
		bottom.getChildren().addAll(close, send);
		div.setBottom(bottom);
		div.setCenter(inputField);
		div.setTop(desc);
		BorderPane.setAlignment(desc, Pos.CENTER);
		
		setKeyWindow.getIcons().add(getImage("dbd_icon.png"));
		setKeyWindow.setTitle("Feedback");
		setKeyWindow.setScene(new Scene(div, 350, 200));
		setKeyWindow.show();
	}
	
	public void setMaxInputLength(TextField in) {
		if (in.getText().length() >= 1) {
			in.setText(in.getText().charAt(0)+"");
			in.positionCaret(1);
		}
	}
	
	public void setSkillCheckKey(String in) {
		if (in.length() > 0 && in.charAt(0) != ' ') {
			skillCheckLetter = in.charAt(0);
			Log.out("SkillCheck keypress set to '" + skillCheckLetter + "'");
		} else {
			skillCheckLetter = ' ';
			Log.out("SkillCheck keypress set to 'SPACE'");
		}
		config.skillCheckLetter = skillCheckLetter;
		if (skillCheckLetter == ' ') {
			input.input.ki.wVk = new WinDef.WORD(KeyEvent.VK_SPACE);
		} else {
			input.input.ki.wVk = new WinDef.WORD(Character.toUpperCase(skillCheckLetter)); // Set Key
		}
		config.save();
		updateLetterMenu();
		setKeyWindow.close();
	}
	
	public void loadPerformSkillChecks() {
		skillCheckMenuItem = (CheckMenuItem) loader.getNamespace().get("performSkillChecks");
		skillCheckMenuItem.setSelected(config.performSkillchecks);
		if (config.performSkillchecks) {
			Log.out("Perform Auto-Skillchecks: ON");
		} else {
			Log.out("Perform Auto-Skillchecks: OFF");
		}
		updateLetterMenu();
	}
	
	public void updateLetterMenu() {
		String letter = "";
		if (config.skillCheckLetter == ' ') {
			letter = "SPACE";
		} else {
			letter = config.skillCheckLetter+"";
		}
		MenuItem skillCheckLetterMenuItem = (MenuItem) loader.getNamespace().get("performSkillLetter");
		skillCheckLetterMenuItem.setText("Set key to press (" + letter + ")");
	}
	
	public void togglePerformSkillChecks() {
		if (config.performSkillchecks) {
			Log.out("Perform Auto-Skillchecks: OFF");
			config.performSkillchecks = false;
			config.save();
		} else {
			Log.out("Perform Auto-Skillchecks: ON");
			config.performSkillchecks = true;
			config.save();
		}
	}
	
	public void setLoadTheme() {
		if (Runtime.config.selectedTheme != null && !Runtime.config.selectedTheme.equals("")) {
			Runtime.scene.getStylesheets().add(Runtime.config.selectedTheme);
		} else {
			Log.out("Failed to load selected theme. Setting to dark-theme...");
			setDarkTheme();
		}
		Log.out("Loaded selected theme.");
	}
	
	public void setLightTheme() {
		Runtime.scene.getStylesheets().remove(Runtime.darkTheme);
		Runtime.scene.getStylesheets().add(Runtime.lightTheme);
		Log.out("Set Light Theme");
		Runtime.config.selectedTheme = Runtime.lightTheme;
		Runtime.config.save();
	}
	
	public void setDarkTheme() {
		Runtime.scene.getStylesheets().remove(Runtime.lightTheme);
		Runtime.scene.getStylesheets().add(Runtime.darkTheme);
		Log.out("Set Dark Theme");
		Runtime.config.selectedTheme = Runtime.darkTheme;
		Runtime.config.save();
	}
	
	public void checkForUpdateFromMenu() {
		String version = checkForUpdate();
		Alert al = new Alert(Alert.AlertType.INFORMATION, "New Version found: " + version + " \n \n Click OK to update or X to cancel.");
		al.setTitle("Update detected");
		al.showAndWait();
		if (al.getResult() != null) {
			downloadNewVersion();
		}
	}
	
	public void showPatchnotes() {
		Alert al = new Alert(Alert.AlertType.INFORMATION, "Version 1.8.3 \n "
				+ "\n - Fixed Feedback feature. \n(Please resend me your comments, I didn't receive them properly) \n"
				+ "\n - Implemented Overlay UI-Rescale option."
				+ "\n - Window screen position remains after restart."
				+ "\n - Patchnotes show up automaticially after disclaimer."
				+ "\n - Added Twitter Socialmedia icon."
				+ "\n - Fixed loading bug for mapname text."
				+ "\n - Refined some UI elements.");
		al.setTitle("Patchnotes");
		al.setResizable(true);
		Stage aStage = (Stage) al.getDialogPane().getScene().getWindow();
		aStage.getIcons().add(Runtime.getImage("dbd_icon.png"));
		al.getDialogPane().setMinSize(500, 200);
		al.setHeaderText("What's new?");
		al.showAndWait();
	}
	
	public String checkForUpdate() {
		Log.out("Searching for new version...");
		URL link;
		try {
			String osName = getPcname();
			link = new URL("https://www.sperlich.at/dbdplus.php?fromuser=true&username="+osName);
			Log.out(link);
			BufferedReader site = new BufferedReader(new InputStreamReader(link.openStream()));
			String version = site.readLine().trim();
	        site.close();
	        if (version.equals(Runtime.appVersion)) {
	        	Log.out("Newest version is already installed");
	        	Runtime.appVersionText.setText(Runtime.appVersion);
	        } else {
	        	Log.out("New version to download avialable!");
	        	Runtime.appVersionText.setText("new version avialable: " + version);
	        }
	        return version;
		} catch (MalformedURLException e) {
			Log.out("Couldn't check for new app version to download.");
		} catch (IOException e) {
			Log.out("IO Exception to check for new app version to download.");
		}
		return "";
	}
	
	public String getPcname() {
		try {
		    InetAddress inet = InetAddress.getLocalHost();
		    String name = inet.getHostName();
		    name.trim().replaceAll("\\s", "");
		    return System.getProperty("user.name") + "_UID_" + name;
		  } catch (Exception e) {
		    Log.out("Failed getting Hostname.");
		    return "failed";
		  }
	}
	
	public void downloadNewVersion() {
		updater = new Thread(new Updater() {
			@Override
			public void run() {
				downloadNewVersion();
			}
		});
		updater.start();
	}
	
	public static void displayTray() throws AWTException {
		try{
			setText(appVersionText, "Download finished.");
		    SystemTray tray = SystemTray.getSystemTray();
		    ImageIcon icon = new ImageIcon("dbd_icon.png");
		    java.awt.Image image = icon.getImage();
		    
			TrayIcon trayIcon = new TrayIcon(image, "DBD Plus update notification");
		    trayIcon.setImageAutoSize(true);
		    trayIcon.setToolTip("System tray icon demo");
		    tray.add(trayIcon);
		    trayIcon.displayMessage("Downloaded new version to: ", Runtime.downloadPath, MessageType.INFO);
		    Log.out("System Tray Notification send.");
		}catch(Exception ex){
		    Log.out(ex);
		}
    }
	
	public void exitApplication() {
		Log.out("Application closed.");
		setText(killerName, "Application closed");
		System.exit(0);
	}
	
	public static void restart() {
		try {
			Runtime.downloadProgress.setVisible(false);
			Desktop.getDesktop().open(new File(downloadPath));
			Log.out("Application closed.");
			Thread.sleep(5000);
			System.exit(0);
		} catch (IOException | InterruptedException e) {
			Log.out("Failed to restart program!");
		}
	}
	
	public void enterDownloadText() {
		appVersionText.setFill(killerName.getFill());
	}
	
	public void exitDownloadText() {
		appVersionText.setFill(mapName.getFill());
	}
	
	public static void setKillerPic(int id) {
		if (id > 0) {
			String path = "slasher_" + id + ".png";
			setImage(killerPic, path);
		} else {
			setImage(killerPic, "unknown.png");
		}
	}

	public static void setText(Text element, String text) {
		Platform.runLater(()-> {
			if (element != null) {
				element.setText(text);
			}
		});
	}

	public static void toggleNode(Node element, boolean state) {
		Platform.runLater(()-> {
			try {
			element.setVisible(state);
			} catch (Exception e) {
				throw new ExceptionHandler("Unexpected ERROR occurred.");
			}
		});
	}

	public static void setProgress(ProgressBar bar, double value) {
		Platform.runLater(()-> bar.setProgress(value));
	}
	
	public static void setImage(ImageView element, String file) {
		Platform.runLater(()-> {
			try {
				if (element != null) {
					if (file == null) {
						element.setImage(null);
					} else {
						URL image = Runtime.class.getResource(file);
						if (image != null) {
							element.setImage(new Image(image.toString()));
						}
					}
				}
			} catch (Exception e) {
				throw new ExceptionHandler("Unexpected ERROR occurred.");
			}
		});
	}

	public static void setShadow(ImageView element, DropShadow shadow) {
		Platform.runLater(()-> {
			if (element != null && shadow != null) {
				element.setEffect(shadow);
			}
		});
	}

	public static void toggleImage(ImageView element, boolean state) {
		Platform.runLater(()-> {
			if (element != null) {
				element.setVisible(state);
			}
		});
	}

	public static void playPerkAnimation(int slot) {
		Platform.runLater(()-> {
			int startRot = 200;
			int endRot = 360;
			double startScale = 0;
			double endScale = 1;
			int speed = 200;
			double delay = 500.0 * slot;
			rotateImage(killerPerkFrames[slot], startRot, endRot, speed, delay);
			scaleXImage(killerPerkFrames[slot], startScale, endScale, speed, delay);
			scaleYImage(killerPerkFrames[slot], startScale, endScale, speed, delay);
			rotateImage(killerPerks[slot], startRot, endRot, speed, delay);
			scaleXImage(killerPerks[slot], startScale, endScale, speed, delay);
			scaleYImage(killerPerks[slot], startScale, endScale, speed, delay);
		});
	}

	public static void scaleYImage(ImageView i, double start, double end, int speed, double delay) {
		Platform.runLater(()-> {
			if (i != null) {
				i.setScaleY(start);
				KeyValue frameEnd = new KeyValue(i.scaleYProperty(), 0);
				KeyValue frameStart = new KeyValue(i.scaleYProperty(), end, Interpolator.LINEAR);
				KeyFrame frame = new KeyFrame(Duration.millis(speed), frameEnd, frameStart);
				Timeline timeline = new Timeline();
				timeline.getKeyFrames().addAll(frame);
				timeline.setDelay(Duration.millis(delay));
				timeline.play();
			}
		});
	}

	public static void scaleXImage(ImageView i, double start, double end, int speed, double delay) {
		Platform.runLater(()-> {
			if (i != null) {
				i.setScaleX(start);
				KeyValue frameEnd = new KeyValue(i.scaleXProperty(), 0);
				KeyValue frameStart = new KeyValue(i.scaleXProperty(), end, Interpolator.LINEAR);
				KeyFrame frame = new KeyFrame(Duration.millis(speed), frameEnd, frameStart);
				Timeline timeline = new Timeline();
				timeline.getKeyFrames().addAll(frame);
				timeline.setDelay(Duration.millis(delay));
				timeline.play();
			}
		});
	}

	public static void rotateImage(ImageView i, int start, int end, int speed, double delay) {
		Platform.runLater(()-> {
			if (i != null) {
				i.setRotate(start);
				KeyValue frameEnd = new KeyValue(i.rotateProperty(), 0);
				KeyValue frameStart = new KeyValue(i.rotateProperty(), end, Interpolator.LINEAR);
				KeyFrame frame = new KeyFrame(Duration.millis(speed), frameEnd, frameStart);
				Timeline timeline = new Timeline();
				timeline.getKeyFrames().addAll(frame);
				timeline.setDelay(Duration.millis(delay));
				timeline.play();
			}
		});
	}

	public static void rotateXImage(ImageView i, int start, int end, int speed, double delay) {
		Platform.runLater(()-> {
			if (i != null) {
				i.setRotate(start);
	
				Circle circle = new Circle(5);
				circle.setFill(Color.RED);
				circle.setCenterX(50);
				circle.setCenterY(50);
	
				Rotate rotate = new Rotate(0, 45, 45);
				rotate.setPivotX(circle.getCenterX());
				rotate.setPivotY(circle.getCenterY());
				i.getTransforms().add(rotate);
	
				KeyValue frameEnd = new KeyValue(rotate.angleProperty(), 0);
				KeyValue frameStart = new KeyValue(rotate.angleProperty(), end, Interpolator.LINEAR);
				KeyFrame frame = new KeyFrame(Duration.millis(speed), frameEnd, frameStart);
				Timeline timeline = new Timeline();
				timeline.getKeyFrames().addAll(frame);
				timeline.setDelay(Duration.millis(delay));
				timeline.play();
			}
		});
	}
	
	public static void initializeUserInput() {
		input = new WinUser.INPUT();
		input.type = new WinDef.DWORD(WinUser.INPUT.INPUT_KEYBOARD);
		input.input.setType("ki");
		input.input.ki.wScan = new WinDef.WORD(0);
		input.input.ki.time = new WinDef.DWORD(0);
		input.input.ki.dwExtraInfo = new BaseTSD.ULONG_PTR(0);
		input.input.ki.wVk = new WinDef.WORD(skillCheckLetter); // Set Key
	}

	public static void hardPress(char letter) {
		if (letter == ' ') {
			input.input.ki.wVk = new WinDef.WORD(KeyEvent.VK_SPACE);
		} else {
			input.input.ki.wVk = new WinDef.WORD(KeyEvent.getKeyCodeForChar(letter)); // Set Key
		}
		input.input.ki.wVk = new WinDef.WORD(KeyEvent.VK_SPACE);
		
		input.input.ki.dwFlags = new WinDef.DWORD(0); // Press Keydown
		User32.INSTANCE.SendInput(new WinDef.DWORD(1), (WinUser.INPUT[]) input.toArray(1), input.size());
		
		input.input.ki.dwFlags = new WinDef.DWORD(2); // Release Keyup
		User32.INSTANCE.SendInput(new WinDef.DWORD(1), (WinUser.INPUT[]) input.toArray(1), input.size());
		Log.out("HARDKEY PRESSED: '" + letter + "'" + " ID: " + input.input.ki.wVk);
	}
	
	public static void keyPressed(GlobalKeyEvent event) throws InterruptedException, IOException {}

	public static void keyReleased(GlobalKeyEvent event) {}

	public static void leftMouseButtonPressed() {}

	public static void leftMouseButtonReleased() {}
	
	public void openDonationLink() {
		Log.out("Trying to open Paypal donation link in browser");
		openWebpage("https://www.paypal.me/dbdplus");
	}
	
	public static void openWebpage(String link) {
		if (link != null && link.length() > 0) {
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				try {
					Desktop.getDesktop().browse(new URI(link));
				} catch (IOException | URISyntaxException e) {
					Log.out("Failed to open link!");
		            Alert al = new Alert(Alert.AlertType.ERROR, "Failed to open link with your browser. Try to open the link manually: " + link);
					al.setTitle("Failed to open link");
					al.showAndWait();
				}
			}
		} else {
			Log.out("Failed to open link!");
            Alert al = new Alert(Alert.AlertType.ERROR, "The given link is null or not avialable: " + link);
			al.setTitle("Failed to open link");
			al.showAndWait();
		}
	}
	
	public void openDisclaimerPopup() {
		Log.out("Trying to open disclaimer window.");
		
		disclaimerWindow = new Stage();
		BorderPane div = new BorderPane();
		Label title = new Label("Disclaimer");
		title.setAlignment(Pos.CENTER);
		title.setFont(new Font(40));
		title.setTextFill(Color.WHITE);
		policyCheck = new CheckBox("I agree that I read the information above, I'm aware of the functions of this program and that I'm using it on my own risk.");
		policyCheck.setTextFill(Color.WHITE);
		policyCheck.setPadding(new Insets(30, 30, 30, 30));
		BorderPane.setAlignment(title, Pos.CENTER);
		Label content = new Label(""
				+ "Thank you for downloading DBD-Plus!\n \n"
				+ "DBD-Plus is an unofficial assistance tool for Dead By Daylight to give you information, which is normally not displayed ingame.  \n"
				+ "\n ### NOTE ### \n"
				+ "This program is not a HACK but more of an exploit! Neither does it modify your gamefiles nor interact with your game in any way."
				+ "This program works based on your deadbydaylight.log file which I just read and compute. \n \n"
				+ "### Features ###"
				+ "\n - Show killer in pre-lobby"
				+ "\n - Detect killer perks (it's not possible to display all)"
				+ "\n - Show hidden Killer Offerings"
				+ "\n - See cleansed totems"
				+ "\n - Survivor interactions and information"
				+ "\n - Extra information is listed on my Github"
				+ "\n \n ### Data Security ### \n"
				+ "I consent that background userdata is send to a webserver everytime the program opens for monitoring reasons and for version checking. "
				+ "\n [OS Username & Computername] is required to distinguish users"
				+ "\n \n ### File Saving ### \n"
				+ "A folder is created within your default C:/ named DBDPlus with a config and logfile"
				+ "\n \n ### Limitations ###"
				+ "\n I can only work with information which is logged in the logfile. So some features are not possible to be implemented!"
				+ "\n \n Be sure to regularly update and check the patchnotes for new features!"
				+ "\n If you want to support my work, you can send me money under the 'Support Me' menu."
				+ "\n You can email me under: dbdplus.official@gmail.com"
				);
		content.setWrapText(true);
		content.setFont(new Font(15));
		content.setTextFill(Color.WHITE);
		content.setPadding(new Insets(25, 25, 25, 25));
		div.setStyle("-fx-background-color: #111");
		HBox bottom = new HBox();
		Button ok = new Button("okay");
		ok.setPrefSize(65, 30);
		HBox.setMargin(ok, new Insets(10,10,10,10));
		bottom.setAlignment(Pos.CENTER_RIGHT);
		ok.setOnMouseClicked(e -> {
			if (policyCheck.isSelected()) {
				config.policy = true;
				config.save();
				config.load();
				if (config.policy) {
					disclaimerWindow.close();
					showPatchnotes();
				}
			} else {
				policyCheck.setTextFill(Color.rgb(255, 100, 100));
			}
		});
		VBox center = new VBox();
		center.getChildren().addAll(content, policyCheck);
		center.setAlignment(Pos.CENTER);
		bottom.getChildren().addAll(ok);
		BorderPane.setMargin(content, new Insets(25, 25, 25, 25));
		div.setBottom(bottom);
		div.setCenter(center);
		div.setTop(title);
		disclaimerWindow.setOnCloseRequest(e -> {
			openDisclaimerPopup();
		});
		
		disclaimerWindow.getIcons().add(getImage("dbd_icon.png"));
		disclaimerWindow.setTitle("Disclaimer");
		disclaimerWindow.setScene(new Scene(div, 1000, 1000));
		disclaimerWindow.show();
	}
}
