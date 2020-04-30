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
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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
import java.awt.MediaTracker;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;


public class Main extends Application {
	public static String appVersion = "v1.2";
	public static String download = "https://www.sperlich.at/storage/content/DBD%20Plus.jar";
	public static String downloadPath = System.getProperty("user.dir") + "/DBD Plus.jar";
	public static App app;
	public static boolean uiInitialized;
	public static GameReader game;
	public static boolean mouseButton1Hold;
	public static Thread keyboardThread;
	public static Thread mouseThread;
	public static Thread gameReader;
	public static Thread updater;
	// public static Window window;
	public Text killerName;
	public Text matchStatusTime;
	//public Text cleanseTotemStatus;
	public Text totalTotems;
	public Text mapName;
	public Text mapTitle;
	public Text offeringTitle;
	public Text destroyedPallets;
	public Text totalVaults;
	public Text killerPlayerName;
	public static Text appVersionText;
	public ImageView vaultIcon;
	public ImageView killerPic;
	public ImageView totemPic;
	public ImageView[] killerPerks;
	public ImageView[] killerPerkFrames;
	public ImageView killerOffering;
	public Text[] survivorNames = new Text[4];
	public Text[] totalHooks = new Text[4];
	public Text[] survivorAction = new Text[4];
	public ProgressBar[] survivorBars = new ProgressBar[4];
	public static ProgressBar startUpLoadBar;
	public static ProgressBar downloadProgress;
	public ImageView palletIcon;
	public ImageView killerBackground;
	public static WinUser.INPUT input;
	public boolean blockKillerSet;
	public Font font;
	public Parent root;
	public HBox totemsBox;
	public HBox palletsBox;
	public HBox vaultsBox;
	public VBox mapBox;
	public Text survivorsTitle;
	public static Scene scene;
	public static String lightTheme;
	public static String darkTheme;
	public static Configuration config = new Configuration();
	public static Text startLoadText;
	public static Node startScreen;
	public static AnchorPane body;
	public ImageView loadImage;
	public static ImageView bodyImage;

	public static void main(String[] args) throws AWTException, InterruptedException, IOException {
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
		// initializeUserInput();
		//if (app.holdMouseButton.isSelected()) { mouseButton1Hold = true; }
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		killerPerks = new ImageView[4];
		killerPerkFrames = new ImageView[4];
		root = FXMLLoader.load(getClass().getResource("window.fxml"));
		scene = new Scene(root, 1000, 600);
		Main.scene = scene;
		stage.setTitle("DBD Plus");
		URL css = Main.class.getResource("style.css");
		//font = Font.loadFont(getClass().getResourceAsStream("horror.ttf"), 30);
		URL imageUrl = Main.class.getResource("dbd_skillcheck.png");
		// Fetch UI Elements
		Main.lightTheme = getClass().getResource("light_theme.css").toExternalForm();
		Main.darkTheme = getClass().getResource("dark_theme.css").toExternalForm();
		File saveFile = new File("/DBDPlus/dbdplus.config");
		if (saveFile.exists() == false) {
			Main.config = new Configuration();
			Main.config.selectedTheme = lightTheme;
			Main.config.save();
		}
		body = (AnchorPane) root.lookup("#body");
		//bodyImage = (ImageView) root.lookup("#bodyBackground");
		stage.setMinHeight(600);
		stage.setMinWidth(1000);
		killerName = (Text) root.lookup("#killerId");
		killerPlayerName = (Text) root.lookup("#killerName");
		matchStatusTime = (Text) root.lookup("#gameStatusTime");
		imageUrl = Main.class.getResource("dbd_skillcheck.png");
		killerPic = (ImageView) root.lookup("#killerPic");
		//skillCheckText = (Text) root.lookup("#skillCheckText");
		//skillCheckBar = (ProgressBar) root.lookup("#skillCheckBar");
		//cleanseTotemStatus = (Text) root.lookup("#cleanseTotemStatus");
		totalTotems = (Text) root.lookup("#totalTotems");
		totemPic = (ImageView) root.lookup("#totemPic");
		mapName = (Text) root.lookup("#mapName");
		mapTitle = (Text) root.lookup("#mapTitle");
		offeringTitle = (Text) root.lookup("#offeringTitle");
		killerOffering = (ImageView) root.lookup("#killerOffering");
		destroyedPallets = (Text) root.lookup("#destroyedPallets");
		palletIcon = (ImageView) root.lookup("#palletIcon");
		totalVaults = (Text) root.lookup("#totalVaults");
		vaultIcon = (ImageView) root.lookup("#vaultIcon");
		appVersionText = (Text) root.lookup("#appVersion");
		totemsBox = (HBox) root.lookup("#totemsBox");
		palletsBox = (HBox) root.lookup("#palletsBox");
		vaultsBox = (HBox) root.lookup("#vaultsBox");
		mapBox = (VBox) root.lookup("#mapContainer");
		survivorsTitle = (Text) root.lookup("#survivorsTitle");
		startUpLoadBar = (ProgressBar) root.lookup("#startUpLoadingBar");
		startLoadText = (Text) root.lookup("#startLoadText");
		startScreen = root.lookup("#startScreen");
		loadImage = (ImageView) root.lookup("#loadImage");
		loadImage.fitWidthProperty().bind(stage.widthProperty());
		loadImage.fitHeightProperty().bind(stage.heightProperty());
		//bodyImage.fitWidthProperty().bind(body.widthProperty());
		//bodyImage.fitHeightProperty().bind(body.heightProperty());
		loadImage.setVisible(true);
		//bodyImage.setVisible(false);
		startScreen.setVisible(true);
		startUpLoadBar.setProgress(0);
		appVersionText.setOnMouseClicked(e -> {
            downloadNewVersion();
        });
		downloadProgress = (ProgressBar) root.lookup("#downloadBar");
		downloadProgress.setVisible(false);
		/*
		 * killerBackground = (ImageView)root.lookup("#killerBackground");
		 * setImage(killerBackground, "killerBackground.gif");
		 * toggleImage(killerBackground, false);
		 */
		totalTotems.setText("0/0");
		matchStatusTime.setText("LOBBY");
		killerOffering.setImage(null);
		destroyedPallets.setText("0/0");
		totalVaults.setText("0/0");
		for (int i = 0; i < killerPerks.length; i++) {
			killerPerks[i] = (ImageView) root.lookup("#perk_" + (i + 1));
			killerPerkFrames[i] = (ImageView) root.lookup("#perk_frame" + (i + 1));
			if (killerPerks[i] != null && killerPerkFrames[i] != null) {
				String perkIcon = "perk_default.png";
				String perk_frame = "perk_frame.png";
				killerPerks[i].setImage(getImage(perkIcon));
				killerPerkFrames[i].setImage(getImage(perk_frame));
				killerPerks[i].setVisible(false);
				killerPerkFrames[i].setVisible(false);
			}
		}
		for (int i = 0; i < 4; i++) {
			survivorNames[i] = (Text)root.lookup("#surv"+(i+1));
			survivorNames[i].setText("");
			totalHooks[i] = (Text)root.lookup("#totalHooks"+(i+1));
			survivorAction[i] = (Text)root.lookup("#survAction"+(i+1));
			survivorBars[i] = (ProgressBar)root.lookup("#survBar"+(i+1));
			survivorBars[i].setVisible(false);
		}
		killerPic.setImage(getImage("unknown.png"));
		totemPic.setImage(getImage("totem.png"));
		palletIcon.setImage(getImage("pallet.png"));
		vaultIcon.setImage(getImage("vault.png"));
		imageUrl = Main.class.getResource("dbd_icon.png");
		stage.getIcons().add(new Image(imageUrl.toString()));
		stage.setOnCloseRequest(event -> {
			exitApplication();
		});
		//toggleText(skillCheckText, false);
		//skillCheckBar.setVisible(false);
		//toggleText(cleanseTotemStatus, false);
		mapName.setVisible(false);
		mapTitle.setVisible(false);
		killerOffering.setVisible(false);
		offeringTitle.setVisible(false);
		totemsBox.setVisible(false);
		palletsBox.setVisible(false);
		vaultsBox.setVisible(false);
		survivorsTitle.setVisible(false);
		mapBox.setVisible(false);

		stage.setScene(scene);
		stage.setResizable(true);
		//stage.setMinHeight(800);
		//stage.setMinWidth(1000);
		uiInitialized = true;
		Main m = this;
		
		final Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (!GameReader_2.loadingFinished) {
                    updateProgress(GameReader_2.loadingProgress, GameReader_2.totalLines);
                    Thread.sleep(10);
                }
                return null;
            }
        };
        Main.startUpLoadBar.progressProperty().bind(task.progressProperty());
		
		gameReader = new Thread(new GameReader_2() {
			@Override
			public void run() {
				initialize(m);
			}
		});
		try {
			gameReader.start();
		} catch (NullPointerException e) {
			throw new ExceptionHandler("ERROR: Launching program");
		}
		Log.out("UI successfully rendered.");
		Main.scene.getStylesheets().add(css.toString());
		Main.config.load();
		setLoadTheme();
		//Log.out("CURRENT THEME: " + Configuration.selectedTheme);
		checkForUpdate();
		stage.show();
	}

	public Image getImage(String url) {
		URL image = Main.class.getResource(url);
		return new Image(image.toString());
	}
	
	public static void deleteStartScreen() {
		body.getChildren().remove(startScreen);
		//bodyImage.setVisible(true);
	}
	
	public void setLoadTheme() {
		Main.scene.getStylesheets().add(Main.config.selectedTheme);
		Log.out("Loaded selected theme.");
	}
	
	public void setLightTheme() {
		Main.scene.getStylesheets().remove(Main.darkTheme);
		Main.scene.getStylesheets().add(Main.lightTheme);
		Log.out("Set Light Theme");
		Main.config.selectedTheme = Main.lightTheme;
		Main.config.save();
	}
	
	public void setDarkTheme() {
		Main.scene.getStylesheets().remove(Main.lightTheme);
		Main.scene.getStylesheets().add(Main.darkTheme);
		Log.out("Set Dark Theme");
		Main.config.selectedTheme = Main.darkTheme;
		Main.config.save();
	}
	
	public void checkForUpdate() {
		Log.out("Searching for new version...");
		 URL link;
		try {
			link = new URL("https://www.sperlich.at/dbdplus.html");
			BufferedReader site = new BufferedReader(new InputStreamReader(link.openStream()));
			String version = site.readLine();
	        site.close();
	        if (version.equals(Main.appVersion)) {
	        	Log.out("Newest version is already installed");
	        	Main.appVersionText.setText(Main.appVersion);
	        } else {
	        	Log.out("New version to download avialable!");
	        	Main.appVersionText.setText("new version avialable: " + version);
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
		    trayIcon.displayMessage("Downloaded new version to: ", Main.downloadPath, MessageType.INFO);
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
			Main.downloadProgress.setVisible(false);
			Desktop.getDesktop().open(new File(downloadPath));
			Log.out("Application closed.");
			Thread.sleep(5000);
			System.exit(0);
		} catch (IOException e) {
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	
	public void enterDownloadText() {
		
		appVersionText.setFill(killerName.getFill());
	}
	
	public void exitDownloadText() {
		appVersionText.setFill(mapName.getFill());
	}
	
	public void setKillerPic(int id) throws ExceptionHandler {
		if (id > 0) {
			String path = "slasher_" + id + ".png";
			try {
				setImage(killerPic, path);
			} catch (NullPointerException e) {
				throw new ExceptionHandler("Killer not found at " + path);
			}
		} else {
			setImage(killerPic, "unknown.png");
		}
	}

	public static void keyPressed(GlobalKeyEvent event) throws InterruptedException, IOException {
		// System.out.println("Key Pressed: " + event.getKeyChar());
		/*
		 * if (event.getKeyChar() == 'f') { if (app.programStarted) { app.stop(); } else
		 * { app.start(); } } if (event.getKeyChar() == 'q') { if (app.autoStruggle) {
		 * app.autoStruggle = false; } else { app.autoStruggle = true; } }
		 */
	}

	public static void keyReleased(GlobalKeyEvent event) {

	}

	public static void leftMouseButtonPressed() {
		/*
		 * if (app.holdMouseButton.isSelected()) { mouseButton1Hold = true;
		 * System.out.println("m1 pressed"); }
		 */
	}

	public static void leftMouseButtonReleased() {
		/*
		 * if (app.holdMouseButton.isSelected()) { mouseButton1Hold = false;
		 * System.out.println("m1 released"); }
		 */
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

	public void hardPress(char letter) {
		// Pressing
		input.input.ki.dwFlags = new WinDef.DWORD(0); // keydown
		User32.INSTANCE.SendInput(new WinDef.DWORD(1), (WinUser.INPUT[]) input.toArray(1), input.size());
		Log.out("HARDKEY PRESSED " + letter);
	}

	public static void setText(Text element, String text) {
		Platform.runLater(()-> {
			try {
				if (element != null) {
					try {
						element.setText(text);
					} catch (NullPointerException e) {
						throw new ExceptionHandler("ERROR: setting text");
					}
				}
			} catch (Exception e) {
				throw new ExceptionHandler("Unexpected ERROR occurred.");
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

	public void setProgress(ProgressBar bar, double value) {
		Platform.runLater(()-> {
			bar.setProgress(value);
		});
	}
	
	public void setImage(ImageView element, String file) {
		Platform.runLater(()-> {
			try {
				if (element != null) {
					if (file == null) {
						element.setImage(null);
					} else {
						URL image = Main.class.getResource(file);
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

	public void setShadow(ImageView element, DropShadow shadow) {
		Platform.runLater(()-> {
			if (element != null && shadow != null) {
				element.setEffect(shadow);
			}
		});
	}

	public void toggleImage(ImageView element, boolean state) {
		Platform.runLater(()-> {
			if (element != null) {
				element.setVisible(state);
			}
		});
	}

	public void playPerkAnimation(String perk, int slot) {
		Platform.runLater(()-> {
			int startRot = 200;
			int endRot = 360;
			double startScale = 0;
			double endScale = 1;
			int speed = 200;
			double delay = 500 * slot;
			// Animate Perk Frames
			try {
				rotateImage(killerPerkFrames[slot], startRot, endRot, speed, delay);
				scaleXImage(killerPerkFrames[slot], startScale, endScale, speed, delay);
				scaleYImage(killerPerkFrames[slot], startScale, endScale, speed, delay);
				// Animate Perk Icons
				rotateImage(killerPerks[slot], startRot, endRot, speed, delay);
				scaleXImage(killerPerks[slot], startScale, endScale, speed, delay);
				scaleYImage(killerPerks[slot], startScale, endScale, speed, delay);
			} catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
				throw new ExceptionHandler("ERROR: Perk Imageview not found");
			}
		});
	}

	public void scaleYImage(ImageView i, double start, double end, int speed, double delay) {
		Platform.runLater(()-> {
			if (i != null) {
				i.setScaleY(start);
				KeyValue frameEnd = new KeyValue(i.scaleYProperty(), 0);
				KeyValue frameStart = new KeyValue(i.scaleYProperty(), end, Interpolator.LINEAR);
				KeyFrame frame = new KeyFrame(Duration.millis(speed), frameEnd, frameStart);
				Timeline timeline = new Timeline();
				timeline.getKeyFrames().addAll(frame);
				timeline.setDelay(Duration.millis(delay));
				try {
					timeline.play();
				} catch (IllegalStateException e) {
					throw new ExceptionHandler("ERROR: Rotating ImageView");
				}
			}
		});
	}

	public void scaleXImage(ImageView i, double start, double end, int speed, double delay) {
		Platform.runLater(()-> {
			if (i != null) {
				i.setScaleX(start);
				KeyValue frameEnd = new KeyValue(i.scaleXProperty(), 0);
				KeyValue frameStart = new KeyValue(i.scaleXProperty(), end, Interpolator.LINEAR);
				KeyFrame frame = new KeyFrame(Duration.millis(speed), frameEnd, frameStart);
				Timeline timeline = new Timeline();
				timeline.getKeyFrames().addAll(frame);
				timeline.setDelay(Duration.millis(delay));
				try {
					timeline.play();
				} catch (IllegalStateException e) {
					throw new ExceptionHandler("ERROR: Rotating ImageView");
				}
			}
		});
	}

	public void rotateImage(ImageView i, int start, int end, int speed, double delay) {
		Platform.runLater(()-> {
			if (i != null) {
				i.setRotate(start);
				KeyValue frameEnd = new KeyValue(i.rotateProperty(), 0);
				KeyValue frameStart = new KeyValue(i.rotateProperty(), end, Interpolator.LINEAR);
				KeyFrame frame = new KeyFrame(Duration.millis(speed), frameEnd, frameStart);
				Timeline timeline = new Timeline();
				timeline.getKeyFrames().addAll(frame);
				timeline.setDelay(Duration.millis(delay));
				try {
					timeline.play();
				} catch (IllegalStateException e) {
					throw new ExceptionHandler("ERROR: Rotating ImageView");
				}
			}
		});
	}

	public void rotateXImage(ImageView i, int start, int end, int speed, double delay) {
		Platform.runLater(()-> {
			if (i != null) {
				// i.setRotate(start);
	
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
				try {
					// timeline.play();
				} catch (IllegalStateException e) {
					throw new ExceptionHandler("ERROR: Rotating ImageView");
				}
			}
		});
	}
}
