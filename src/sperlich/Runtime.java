package sperlich;

import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.ImageIcon;
import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;


public class Runtime extends Application {
	public static App app;
	public static GameReader game;
	public static String appVersion = "v1.2.5";
	public static String download = "https://www.sperlich.at/storage/content/DBD%20Plus.jar";
	public static String downloadPath = System.getProperty("user.dir") + "/DBD Plus.jar";
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
	public static Text[] survivorNames = new Text[4];
	public static Text[] totalHooks = new Text[4];
	public static Text[] survivorAction = new Text[4];
	public static ImageView vaultIcon;
	public static ImageView bodyImage;
	public static ImageView killerPic;
	public static ImageView totemPic;
	public static ImageView palletIcon;
	public static ImageView killerBackground;
	public static ImageView killerOffering;
	public static ImageView loadImage;
	public static ImageView[] killerPerks = new ImageView[4];
	public static ImageView[] killerPerkFrames = new ImageView[4];
	public static ImageView[] deadSymbols = new ImageView[4];
	public static ImageView[] actionIcons = new ImageView[4];
	public static ProgressBar[] survivorBars = new ProgressBar[4];
	public static ProgressBar startUpLoadBar;
	public static ProgressBar downloadProgress;
	public static HBox totemsBox;
	public static HBox palletsBox;
	public static HBox vaultsBox;
	public static VBox mapBox;
	public static Configuration config = new Configuration();
	public static boolean uiInitialized;
	public static boolean mouseButton1Hold;
	public static Node startScreen;
	public static AnchorPane body;
	public static Scene scene;
	public static WinUser.INPUT input;
	public static Font font;
	public static Parent root;
	

	public static void main(String[] args) {
		Log.out("Application started.");
		try {
			Application.launch(args);
		} catch (Exception e) {
			throw new ExceptionHandler("Unexpected ERROR occurred.");
		}
	}
	
	@Override
	public void init() {
		keyboardThread = new Input();
		keyboardThread.start();
		mouseThread = new MouseInput();
		mouseThread.start();
		initializeUserInput();
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		root = FXMLLoader.load(getClass().getResource("window.fxml"));
		Runtime.scene = new Scene(root, 1000, 600);
		stage.setTitle("DBD Plus");
		stage.setMinHeight(600);
		stage.setMinWidth(1000);
		stage.getIcons().add(getImage("dbd_icon.png"));
		stage.setScene(scene);
		stage.setResizable(true);
		stage.setOnCloseRequest(event -> {
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
		body = (AnchorPane) root.lookup("#body");
		killerName = (Text) root.lookup("#killerId");
		killerPlayerName = (Text) root.lookup("#killerName");
		matchStatusTime = (Text) root.lookup("#gameStatusTime");
		killerPic = (ImageView) root.lookup("#killerPic");
		totalTotems = (Text) root.lookup("#totalTotems");
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
		mapBox = (VBox) root.lookup("#mapContainer");
		survivorsTitle = (Text) root.lookup("#survivorsTitle");
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
		totalTotems.setText("0/0");
		matchStatusTime.setText("LOBBY");
		killerOffering.setImage(null);
		destroyedPallets.setText("0/0");
		totalVaults.setText("0/0");
		appVersionText.setOnMouseClicked(e -> {
            downloadNewVersion();
        });
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
			deadSymbols[i] = (ImageView) root.lookup("#survDead"+i);
			setImage(deadSymbols[i], "dead_symbol.png");
			deadSymbols[i].setManaged(false);
			deadSymbols[i].setVisible(false);
			survivorBars[i].setVisible(false);
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
		gameReader = new Thread(new GameReader() {
			@Override
			public void run() {
				try { initialize(); } catch (InterruptedException e) {}
			}
		});
		gameReader.start();
		Log.out("UI successfully rendered.");
		Runtime.scene.getStylesheets().add(css.toString());
		Runtime.config.load();
		setLoadTheme();
		checkForUpdate();
		stage.show();
	}

	public Image getImage(String url) {
		URL image = Runtime.class.getResource(url);
		return new Image(image.toString());
	}
	
	public static void deleteStartScreen() {
		body.getChildren().remove(startScreen);
	}
	
	public void setLoadTheme() {
		Runtime.scene.getStylesheets().add(Runtime.config.selectedTheme);
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
	
	public void checkForUpdate() {
		Log.out("Searching for new version...");
		 URL link;
		try {
			link = new URL("https://www.sperlich.at/dbdplus.php?fromuser=true");
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
		} catch (MalformedURLException e) {
			Log.out("Couldn't check for new app version to download.");
		} catch (IOException e) {
			Log.out("IO Exception to check for new app version to download.");
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
		} catch (IOException | InterruptedException e) {};
	}
	
	public void enterDownloadText() {
		appVersionText.setFill(killerName.getFill());
	}
	
	public void exitDownloadText() {
		appVersionText.setFill(mapName.getFill());
	}
	
	public static void setKillerPic(int id) throws ExceptionHandler {
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
		Platform.runLater(()-> {
			bar.setProgress(value);
		});
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

	public static void playPerkAnimation(String perk, int slot) {
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
		input.input.ki.wVk = new WinDef.WORD(' '); // 0x41
	}

	public static void hardPress(char letter) {
		input.input.ki.dwFlags = new WinDef.DWORD(0); // keydown
		User32.INSTANCE.SendInput(new WinDef.DWORD(1), (WinUser.INPUT[]) input.toArray(1), input.size());
		Log.out("HARDKEY PRESSED " + letter);
	}
	
	public static void keyPressed(GlobalKeyEvent event) throws InterruptedException, IOException {}

	public static void keyReleased(GlobalKeyEvent event) {}

	public static void leftMouseButtonPressed() {}

	public static void leftMouseButtonReleased() {}
}
