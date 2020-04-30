package sperlich;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.awt.AWTException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.nio.charset.StandardCharsets;
import java.io.FileReader;

import javax.swing.Timer;

import com.sun.jmx.snmp.Timestamp;

public class GameReader_2 extends Thread {

	public static boolean playerIsIngame;
	public static boolean skillCheckActive;
	public static boolean skillCheckActionPerformed;
	public static boolean noedActive;
	public static boolean ruinActive;
	String searchKiller = "GameFlow: (BP_Menu_Slasher";
	String searchGameStarted = "UDBDGameInstance::StartOfferingSequence";
	String searchGameEnded = "LogAsyncLoad: Unloading library Game";
	String searchMapTheme = "ProceduralLevelGeneration: InitLevel:";
	String searchSpawnPallet = ".PalletPullUpChargeable Enabled?: Enabled";
	String searchWindowVaults = "ProceduralLevel:PersistentLevel.WindowStandard_C_";
	String showSkillCheck = "Investigation: HudView::ShowSkillCheck";
	String hideSkillCheck = "Investigation: HudView::HideSkillCheck";
	String skillCheckProgressInfo = "HudView::UpdateSkillCheckProgress";
	String searchTotem = "[CleanseTotem][BP_TotemBase_C_";
	String interactionEnter = "[==> Interaction Enter]";
	String interactionExit = "[<== Interaction Exit]";
	String interactionFinished = "[<!> Charge Complete Received]";
	String interactionGenerator = "";
	String cleansedHexTotem = "Originating Effect: Hex";
	String searchEbonyMori = "Ebony Memento Mori";
	String searchIvoryMori = "Ivory Memento Mori";
	String searchCypressMori = "Cypress Memento Mori";
	String searchShroudSeperation = "Shroud of Separation";
	String shroudSeperationSurvivor = "Shroud of Binding";
	String gameClosed = "LogExit: Exiting.";
	String searchBloodPoints = "Bloodpoints Value: ";
	// PERKS
	String noed = "Originating Effect: No_One_Escapes_Death";
	String ruin = "Originating Effect: Hex_Ruin";
	String overwhelmingPresence = "Originating Effect: OverwhelmingPresence";
	String lullaby = "Originating Effect: Hex_HuntressLullaby";
	String retribution = "Originating Effect: HexRetribution";
	String hauntedGrounds = "Originating Effect: Hex_HauntedGround";
	String devourHope = "Originating Effect: Hex_Devour_Hope";
	String thirdSeal = "Originating Effect: Hex_The_Third_Seal";
	String nurseCalling = "Originating Effect: NurseCalling";
	String bitterMurmur = "Originating Effect: Bitter_Murmur";
	String thrillOfTheHunt = "Originating Effect: Hex_Thrill_Of_The_Hunt";
	String bloodWarden = "Originating Effect: BloodWarden";
	String coulrophobia = "Originating Effect: Coulrophobia";
	String darkDevotion = "Originating Effect: DarkDevotion";
	String dyingLight = "Originating Effect: Dying_Light";
	String deadManSwitch = "Originating Effect: DeadMansSwitch";
	String franklinsDemise = "GameFlow: OnDropped"; // Unsicher
	String ironMaiden = "Originating Effect: Ironmaiden";
	String bbqAndChilli = "Originating Effect: BBQAndChilli";
	String mindBreaker = "Originating Effect: MindBreaker";
	String rancor = "Originating Effect: Rancor";
	String rememberMe = "Id: RememberMe_Effect";
	String thanatophobia = "Id: Thanatophobia_Effect_";
	String territorialImperative = "Id: TerritorialImperativeEffect";
	String tinkerer = "Originating Effect: Tinkerer";
	String beastOfPrey = "Originating Effect: BeastOfPrey";
	String hangMan = "Originating Effect: HangmansTrick";
	String allEars = "Originating Effect: ImAllEars";
	String knockOut = "Originating Effect: InTheDark";
	String stridor = "Stridor_Effect_";
	String sloppyButcher = "Originating Effect: Sloppy_Butcher";
	String unnvervingPresence = "Originating Effect: Unnerving_Presence";
	String makeYourChoice = "Originating Effect: MakeYourChoice";
	String mapTheme = "";
	String mapName = "";
	String gameStartCamAnimationEnd = "HudView::OnShowHUDComplete called";
	String genBlocked = "Requested to Play: EntityGeneratorBlock None.";
	String camperPickUp = "Requested to Play: SurvivorPickup_";
	String camperDrop = "Requested to Play: SurvivorDrop";
	String camperStunDrop = "Requested to Play: SurvivorStunDrop";
	String camperEnterHook = "<Entering state Hooked>";
	String defaultVaultSpeed = "Interaction time: 1.70";
	String bamboozleVaultSpeed = "Interaction time: 1.478";
	String myersVaultSpeed = "Interaction time: 1.275";
	String fireUpVaultSpeed = "Interaction time: 1.63";
	String defaultBreakPalletSpeed = "Interaction time: 2.60";
	String fireUpBreakPalletSpeed = "Interaction time: 2.50";
	String fireUpDamageGenSpeed = "Interaction time: 1.92";
	String brutalStrengthPalletSpeed = "Interaction time: 2.16";
	String defaultDamageGenSpeed = "Interaction time: 2.00";
	String brutalStrengthDamageGenSpeed = "Interaction time: 1.66";
	String enduringPalletStunSpeed = "Interaction time: 1.00";
	String palletStun = "[PalletStun]";
	String palletDestroyed = "Requested to Play: PlankDestroy";
	String palletPowerDestroyed = "Requested to Play: DestroyWithPowerAttack";
	String palletPullDownShowUI = "HudScreen: HudView::AddInputPrompts";
	String palletPullDownL = "[PulldownDefinitionLeftStationary]";
	String palletPullDownR = "[PulldownDefinitionRightStationary]";
	String palletPullDownRunningR = "[PulldownDefinitionRight]";
	String palletPullDownRunningL = "[PulldownDefinitionLeft]";
	String palletPullDownExit = "AuthoritativeMovement set to False";
	String palletKillerStun = "[PalletStun][SlasherInteractable_C_";
	String survivorSecondHook = "Verbose: [Struggle][BP_SmallMeatLocker";
	String survivorFirstHook = "Id: Camper Was Recently Unhooked";
	String lastInteractionEnterLine = "";
	int camperPickedUpLine = 0;
	int camperDropLine = 0;
	int camperEnterHookLine = 0;
	String[] killerPerks = new String[4];
	public int foundGameStarts;
	public int foundGameEnds;
	public int startLineNumber;
	public int endLineNumber;
	public static int totalLines;
	public int skillCheckStartLine;
	public int startSkillCheckCount;
	public int hideSkillCheckCount;
	public int cleanseTotemStartCount;
	public int cleanseTotemEndCount;
	public int totalCleansedTotems;
	public int camFadeOutLine;
	public int lastGenBlockLine;
	public int currentKillerId;
	public int oldKillerId;
	public double totalBloodPoints;
	public String valueLine;
	public double currentValue;
	public double skillAreaLength;
	public double skillAreaStart;
	public double skillBonusAreaLength;
	public static double loadingProgress;
	public String skillCheckKeyHit;
	public int updateDelta;
	public boolean stopSleep;
	public int matchStartTime;
	public int matchTime;
	public Timer timer;
	public Timer retryTimer;
	public Main main;
	public int totalPalletPullDowns;
	public boolean spiritFuryDetected;
	public int thrownPallets;
	public int spiritActive;
	public int totalPalletStuns;
	public int totalDestroyedPallets;
	public int normalDestroyedPallets;
	public int maxSpawnedPallets;
	public int maxSpawnedWindows;
	public int sleepSpeed;
	public boolean palletStunActive;
	public boolean hudActive = false;
	public boolean deactiveSpiritFuryDetection;
	public boolean gameIsClosed = false;
	public static boolean loadingFinished;
	public ArrayList<Player> players = new ArrayList<Player>();
	public int lastEndLine;
	String searchKillerName = "InteractionHandler: Verbose: [BP_Slasher_Character_";
	String searchSurvivorNamePart1 = "Verbose: [BP_Camper";
	String searchSurvivorNamePart2 = "_Character_C_";
	String searchSurvivorRepair = "Interaction: Verbose: [GeneratorRepair";
	String searchSurvivorRecover = "Interaction: Verbose: [Recover]";
	String searchSurvivorWiggle = "Interaction: Verbose: [Wiggle]";
	String searchSuvivorSelfhealMedkit = "Interaction: Verbose: [SelfHealWithMedkit]";
	String searchSurvivorLooting = "Interaction: Verbose: [OpenSearchable]";
	String searchSurvivorHealOther = "Interaction: Verbose: [HealOther";
	String searchSurvivorSelfcare = "Interaction: Verbose: [SelfHealNoMedkit]";
	String borrowedTime = " Originating Effect: BorrowedTime";
	String dStrike = "Originating Effect: DecisiveStrike";
	String borrowedTimeInit = "StatusEffect::Multicast_InitializeStatusEffect";
	public boolean isRunning;

	public void initialize(Main m) {
		try {
			sleep(1000);
		} catch (InterruptedException e1) {
			
		}
		Log.out("GameReader successfully started.");
		retryTimer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				initialize(main);
			}
		});
		sleepSpeed = 0;
		players = new ArrayList<Player>();
		retryTimer.setInitialDelay(1000);
		retryTimer.start();
		loadingProgress = 0;
		String path = System.getProperty("user.home").replaceAll("\\\\", "/");
		path += "/AppData/Local/DeadByDaylight/Saved/Logs/DeadByDaylight.log";
		Log.out("Searching Logfile At: " + path);
		main = m;
		BufferedReader reader;
		killerPerks = new String[4];
		try {
			retryTimer.stop();
			
			File fileDir = new File("PATH_TO_FILE");

	        reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
			
			//reader = new BufferedReader(new FileReader(path));
	        try {
		    	fullScan(reader);
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
				readLog(reader);
			} catch (Exception e) {
				Log.out("An unexpected error occurred!");
				main.setText(main.killerName, "Unexpected ERROR occurred");
				resetGame();
				main.setImage(main.killerPic, "unknown.png");
				retryTimer.stop();
				main.setText(main.matchStatusTime, "Try restarting the program");
				main.killerName.setFill(Color.rgb(255, 75, 75));
	        	throw new ExceptionHandler("Unexpected Error occurred!");
	        }
		} catch (ExceptionHandler e) {
			throw new ExceptionHandler("ERROR: " + e);
		} catch (IOException e) {
			main.setText(main.killerName, "Gamefile missing");
			retryTimer.start();
			throw new ExceptionHandler("ERROR: Game not found");
		}
	}

	public void fullScan(BufferedReader reader) throws IOException, InterruptedException {
		Log.out("Starting fullscan...");
		playerIsIngame = false;
		String line = "";
		int lineCount = 0;
		int matchStarts = 0;
		int matchEnds = 0;
		int lastLine = 0;
		main.blockKillerSet = true;
		gameIsClosed = false;
		while (line != null) {
			line = reader.readLine();
			if (line != null) {
				// checkIfIngame(line, lineCount);
				if (line.indexOf(searchGameStarted) >= 0) {
					matchStarts++;
					// Log.out(lineCount);
					lastLine = lineCount;
				}
				if (line.indexOf(searchGameEnded) >= 0) {
					matchEnds++;
					lastLine = lineCount;
				}
				searchKiller(line);
				// searchPlayers(line, lineCount);
				if (line.indexOf(gameClosed) >= 0) {
					gameIsClosed = true;
					lastLine = lineCount;
				}
			}
			lineCount++;
		}
		startLineNumber = lastLine;
		if (matchStarts > matchEnds) {
			startGame(startLineNumber);
		} else {
			Log.out("Skip to: " + startLineNumber);
			endGame();
		}
		totalLines = lineCount;
		setKiller(currentKillerId);
		// System.out.println("RESULT: " + matchStarts + " : " + matchEnds);
		main.blockKillerSet = false;
		Log.out("Successfully scanned " + lineCount + " lines.");
		reader.close();
	}

	public void readLog(BufferedReader reader) throws ExceptionHandler, IOException, InterruptedException {
		Log.out("Current match status: " + playerIsIngame);
		String line = "";
		int lineCount = 0;
		main.setKillerPic(currentKillerId);
		main.setText(main.killerName, GetKillerById(currentKillerId));
		main.blockKillerSet = false;
		isRunning = true;
		timer = new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				updateTime();
			}
		});
		timer.setInitialDelay(0);
		while (true) {
			line = reader.readLine();
			if (line != null) {
				loadingProgress++;
				if (lineCount >= startLineNumber) {
					scanLine(line, lineCount);
					if (lineCount >= totalLines-10) {
						Thread.sleep(1);
						loadingFinished = true;
						Platform.runLater(()-> {
							Main.startLoadText.setText("Finished");
							Main.startScreen.setVisible(false);
							Main.deleteStartScreen();
						});
					}
					if (lineCount%15 == 0 && loadingFinished == false) {
						Thread.sleep(1);
					}
				}
				lineCount++;
			}
		}
	}

	public void scanLine(String line, int lineCount) throws IOException {
		checkIfIngame(line, lineCount);
		matchTime(line, lineCount);
		searchKiller(line);
		setKiller(currentKillerId);
		if (playerIsIngame) {
			// System.out.println("Line: " + lineCount + " - " + line);
			searchMap(line);
			searchSurvivorStates(line, lineCount);
			searchTotems(line);
			searchKillerPerks(line, lineCount);
			// processSkillCheck(line);
			searchOfferings(line);
			checkIfIngame(line, lineCount);
			searchPallets(line, lineCount);
			searchBloodPoints(line);
			searchPlayers(line, lineCount);
			checkSurvivorHookPhases(line);
			searchSurvivorActions(line);
		}
		if (gameIsClosed) {
			Log.out("Game is closed");
			currentKillerId = -1;
			setKiller(-1);
			main.setText(main.killerName, "Game closed");
		}
	}

	public void setKiller(int id) {
		if (oldKillerId != currentKillerId) {
			Log.out("Killer detected: " + GetKillerById(id));
			main.setKillerPic(id);
			main.setText(main.killerName, GetKillerById(id));
			oldKillerId = currentKillerId;
		}
	}

	public String GetKillerById(int id) {
		switch (id) {
		case 1:
			return "Trapper";
		case 2:
			return "Wraith";
		case 3:
			return "Hillbilly";
		case 4:
			return "Nurse";
		case 5:
			return "The Hag";
		case 6:
			return "The Shape";
		case 7:
			return "Doctor";
		case 8:
			return "Huntress";
		case 9:
			return "Leatherface";
		case 10:
			return "Freddy Krueger";
		case 11:
			return "The Pig";
		case 12:
			return "Clown";
		case 13:
			return "Spirit";
		case 14:
			return "Legion";
		case 15:
			return "The Plague";
		case 16:
			return "Ghostface";
		case 17:
			return "Demogorgon";
		case 18:
			return "Oni";
		case 19:
			return "Deathslinger";
		default:
			return "Unknown";
		}
	}

	public void checkIfIngame(String line, int currentLine) {
		if (line.indexOf(searchGameStarted) >= 0) {
			startGame(currentLine);
		}
		if (line.indexOf(searchGameEnded) >= 0) {
			lastEndLine = currentLine;
			endGame();
		}
	}

	public void startGame(int lineCount) {
		Log.out("Match start detected.");
		resetGame();
		main.totemsBox.setVisible(true);
		main.palletsBox.setVisible(true);
		main.vaultsBox.setVisible(true);
		main.survivorsTitle.setVisible(true);
		main.mapBox.setVisible(true);
		playerIsIngame = true;
		startLineNumber = lineCount;
		main.setText(main.matchStatusTime, "Loading");
		DropShadow shadow = new DropShadow();
		shadow.setColor(Color.rgb(150, 255, 10));
		main.setShadow(main.killerPic, shadow);
		if (timer != null) {
			timer.start();
		}
	}

	public void resetGame() {
		System.out.println("Resetting Match...");
		playerIsIngame = false;
		spiritFuryDetected = false;
		cleanseTotemStartCount = 0;
		cleanseTotemEndCount = 0;
		totalCleansedTotems = 0;
		totalDestroyedPallets = 0;
		maxSpawnedPallets = 0;
		maxSpawnedWindows = 0;
		thrownPallets = 0;
		main.setText(main.killerPlayerName, "Detected Perks: ");
		killerPerks = new String[4];
		for (int i = 0; i < main.survivorNames.length; i++) {
			main.setText(main.survivorNames[i], "");
			main.setText(main.totalHooks[i], "Hookstate: 0");
			main.toggleNode(main.survivorAction[i], false);
			main.toggleNode(main.totalHooks[i], false);
			main.toggleNode(main.survivorBars[i], false);
			main.setProgress(main.survivorBars[i], 0);
		}
		players = new ArrayList<Player>();
		matchTime = 0;
		matchStartTime = 0;
		normalDestroyedPallets = 0;
		main.setText(main.matchStatusTime, "LOBBY");
		main.setText(main.destroyedPallets, totalDestroyedPallets + "/0");
		main.setText(main.totalTotems, "0/5");
		main.setText(main.totalVaults, "0/0");
		main.setImage(main.killerOffering, null);
		main.toggleImage(main.killerOffering, false);
		main.toggleNode(main.offeringTitle, false);
		main.toggleNode(main.mapName, false);
		main.toggleNode(main.mapTitle, false);
		searchTotems("");
		for (int i = 0; i < main.killerPerks.length; i++) {
			if (main.killerPerks[i] != null && main.killerPerkFrames[i] != null) {
				String icon = "perk_default.png";
				main.setImage(main.killerPerks[i], icon);
				main.toggleImage(main.killerPerks[i], false);
				main.toggleImage(main.killerPerkFrames[i], false);
			}
		}
		if (timer != null) {
			timer.stop();
		}
	}

	public void endGame() {
		main.totemsBox.setVisible(false);
		main.palletsBox.setVisible(false);
		main.vaultsBox.setVisible(false);
		main.survivorsTitle.setVisible(false);
		main.mapBox.setVisible(false);
		System.out.println("Match end detected.");
		resetGame();
		DropShadow shadow = new DropShadow();
		shadow.setColor(Color.rgb(0, 0, 0));
		main.setShadow(main.killerPic, shadow);
	}

	public void searchSurvivorActions(String line) {
		if (line.indexOf(borrowedTime) >= 0 && line.indexOf(borrowedTimeInit) >= 0) {
			Player p = findPlayer(line);
			if (p != null && p.borrowedTime == 0) {
				p.borrowedTime = 15;
			} else if (p != null) {
				p.borrowedTime = 0;
				main.setText(main.survivorAction[p.survivorId], "Action: ");
			}
		}
		// Repairing
		if (line.indexOf(searchSurvivorRepair) >= 0 && line.indexOf(interactionEnter) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				p.isRepairing = true;
				main.setText(main.survivorAction[p.survivorId], "Action: repair");
				Log.out(p.name + " started repairing");
			}
			
		}
		if (line.indexOf(searchSurvivorRepair) >= 0 && line.indexOf(interactionExit) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				p.isRepairing = false;
				Log.out(p.name + " stopped repairing");
				main.setText(main.survivorAction[p.survivorId], "Action: ");
			}
		}
		// Heal other
		if (line.indexOf(searchSurvivorHealOther) >= 0 && line.indexOf(interactionEnter) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				main.setText(main.survivorAction[p.survivorId], "Action: healing");
				Log.out(p.name + " started healing companion");
			}
		}
		if (line.indexOf(searchSurvivorHealOther) >= 0 && line.indexOf(interactionExit) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				Log.out(p.name + " stop healing companion");
				main.setText(main.survivorAction[p.survivorId], "Action: ");
			}
		}
		// Self healing with medkit
		if (line.indexOf(searchSuvivorSelfhealMedkit) >= 0 && line.indexOf(interactionEnter) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				main.setText(main.survivorAction[p.survivorId], "Action: self heal");
				Log.out(p.name + " started healing with Medkit");
			}
		}
		if (line.indexOf(searchSuvivorSelfhealMedkit) >= 0 && line.indexOf(interactionExit) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				Log.out(p.name + " stopped healing with Medkit");
				main.setText(main.survivorAction[p.survivorId], "Action: ");
			}
		}
		// Searching Chest
		if (line.indexOf(searchSurvivorLooting) >= 0 && line.indexOf(interactionEnter) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				main.setText(main.survivorAction[p.survivorId], "Action: looting");
				Log.out(p.name + " started searching chest");
			}
		}
		if (line.indexOf(searchSurvivorLooting) >= 0 && line.indexOf(interactionExit) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				Log.out(p.name + " stopped searching chest");
				main.setText(main.survivorAction[p.survivorId], "Action: ");
			}
		}
		// Wiggling
		if (line.indexOf(searchSurvivorWiggle) >= 0 && line.indexOf(interactionEnter) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				main.setText(main.survivorAction[p.survivorId], "Action: wiggle");
				Log.out(p.name + " started wiggling");
			}
		}
		if (line.indexOf(searchSurvivorWiggle) >= 0 && line.indexOf(interactionExit) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				Log.out(p.name + " stopped wiggling");
				main.setText(main.survivorAction[p.survivorId], "Action: ");
			}
		}
		// Recovering
		if (line.indexOf(searchSurvivorRecover) >= 0 && line.indexOf(interactionEnter) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				main.setText(main.survivorAction[p.survivorId], "Action: recover");
				Log.out(p.name + " started recovering");
			}
		}
		if (line.indexOf(searchSurvivorRecover) >= 0 && line.indexOf(interactionExit) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				Log.out(p.name + " stopped recovering");
				main.setText(main.survivorAction[p.survivorId], "Action: ");
			}
		}
		// Selfcare
		if (line.indexOf(searchSurvivorSelfcare) >= 0 && line.indexOf(interactionEnter) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				main.setText(main.survivorAction[p.survivorId], "Action: selfcare");
				Log.out(p.name + " started selfcaring");
			}
		}
		if (line.indexOf(searchSurvivorSelfcare) >= 0 && line.indexOf(interactionExit) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				Log.out(p.name + " stopped selfcaring");
				main.setText(main.survivorAction[p.survivorId], "Action: ");
			}
		}

	}
	
	public void searchPlayers(String line, int lineCount) {

		if (line.indexOf(searchSurvivorNamePart1) >= 0 && line.indexOf(searchSurvivorNamePart2) >= 0) {
			try {
				String kind = slaveOrMaster(line);
				if (kind != "") {
					String name = line.substring(line.indexOf(searchSurvivorNamePart1), line.indexOf(kind));
					// String number = line.substring(line.indexOf("_Character_C_")+13,
					// line.indexOf("_Character_C_")+14);
					name = name.substring(name.indexOf("-") + 2, name.length() - 1).trim();
					if (playerExists(name) == false) {
						if (name == null || name == "" || name == " ") {
							name = "survivor";
						}
						Player surv = new Player(name, false);
						surv.survivorId = getSurvivorCount();
						players.add(surv);
						if (surv.survivorId >= 0) {
							if (name.length() > 15) {
								main.setText(main.survivorNames[surv.survivorId], name.substring(0, 15));
							} else {
								main.setText(main.survivorNames[surv.survivorId], name);
							}
						}
						main.toggleNode(main.survivorAction[surv.survivorId], true);
						main.toggleNode(main.totalHooks[surv.survivorId], true);
						Log.out("Player Survivor-Role " + getSurvivorCount() + " detected: " + surv.name);
					}
				}
			} catch (StringIndexOutOfBoundsException e) {

			}
		} else if (line.indexOf(searchKillerName) >= 0) {
			try {
				String kind = slaveOrMaster(line);
				if (kind != "") {
					String name = line.substring(line.indexOf(searchKillerName), line.indexOf(kind));
					name = name.substring(name.indexOf("-") + 2, name.length() - 1).trim();
					if (playerExists(name) == false) {
						Player surv = new Player(name, true);
						players.add(surv);
						if (name.length() > 10) {
							main.setText(main.killerPlayerName, name.substring(0, 15) + "'s  Perks: ");
						} else {
							main.setText(main.killerPlayerName, name + "'s  Perks: ");
						}
						Log.out("Player Killer-Role detected: " + surv.name);
					}
				}
			} catch (StringIndexOutOfBoundsException e) {

			}
		}

	}

	public Player getSurvivorById(int id) {
		for (Player p : players) {
			if (p.survivorId == id) {
				return p;
			}
		}
		return null;
	}
	
	public Player getSurvivorByName(String name) {
		for (Player p : players) {
			if (p.name == name) {
				return p;
			}
		}
		return null;
	}
	
	public int getSurvivorCount() {
		int count = 0;
		for (Player p : players) {
			if (p.isSurvivor) {
				count++;
			}
		}
		return count;
	}

	public String slaveOrMaster(String line) {
		String kind = "";
		if (line.indexOf("[CLIENT MASTER]") >= 0) {
			kind = "[CLIENT MASTER]";
		}
		if (line.indexOf("[CLIENT SLAVE]") >= 0) {
			kind = "[CLIENT SLAVE]";
		}
		return kind;
	}

	public boolean playerExists(String name) {
		for (Player suv : players) {
			if (suv.name.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public void searchBloodPoints(String line) {
		if (line.indexOf(searchBloodPoints) >= 0) {
			try {
				double points = Double.parseDouble(line.substring(line.lastIndexOf(":") + 1, line.length()).trim());
				totalBloodPoints += points;
				// System.out.println(totalBloodPoints);
			} catch (NumberFormatException e) {

			}
		}
	}

	public void searchPallets(String line, int lineCount) {
		if ((line.indexOf(palletPullDownR) >= 0 || line.indexOf(palletPullDownL) >= 0
				|| line.indexOf(palletPullDownRunningR) >= 0 || line.indexOf(palletPullDownRunningL) >= 0)
				&& line.indexOf(interactionExit) >= 0) {
			totalPalletPullDowns++;
			thrownPallets++;
			Log.out("Pallet thrown by " + findPlayer(line).name);
		}
		if (line.indexOf(palletKillerStun) >= 0 && line.indexOf(interactionEnter) >= 0) {
			totalPalletStuns++;
			palletStunActive = true;
			Log.out("Killer stunned by Survivors: " + totalPalletStuns);
		}
		if (line.indexOf(searchWindowVaults) >= 0) {
			maxSpawnedWindows++;
			main.setText(main.totalVaults, maxSpawnedWindows + "");
			Log.out("Window Vault_" + maxSpawnedWindows + " spawn detected. ");
		}
		if (palletStunActive) {
			if (line.indexOf(palletPullDownShowUI) >= 0) {
				hudActive = true;
			}
		}
		if (palletStunActive && line.indexOf(palletKillerStun) >= 0 && line.indexOf(interactionExit) >= 0) {
			palletStunActive = false;
			if (hudActive == false && false) {
				if (!spiritFuryDetected) {
					setPerk("spiritFury");
					spiritFuryDetected = true;
				}
				totalDestroyedPallets++;
				main.setText(main.destroyedPallets, totalDestroyedPallets + "/" + (maxSpawnedPallets + 1));
			}
			hudActive = false;
		}
		if (line.indexOf(palletDestroyed) >= 0 || line.indexOf(palletPowerDestroyed) >= 0) {
			totalDestroyedPallets++;
			normalDestroyedPallets++;
			thrownPallets--;
			if (totalDestroyedPallets % 2 == 0 && totalDestroyedPallets > 0) {
				spiritActive++;
			}
			// When Spirit Fury doesnt work correctly
			if (totalDestroyedPallets > 0 && totalDestroyedPallets > maxSpawnedPallets + 1) {
				totalDestroyedPallets = normalDestroyedPallets;
			}
			main.setText(main.destroyedPallets, totalDestroyedPallets + "/" + (maxSpawnedPallets + 1));
			Log.out("Pallet destroyed by Killer. Total destroyed: " + totalDestroyedPallets);
			Log.out("Destroyed Pallets without Spirit Fury: " + normalDestroyedPallets);
			Log.out("Current used pallets left: " + (thrownPallets-1));
		}
		if (line.indexOf(searchSpawnPallet) >= 0) {
			maxSpawnedPallets = Integer
					.parseInt(line.substring(line.indexOf(searchSpawnPallet) - 2, line.indexOf(searchSpawnPallet))
							.replace("_", "").trim());
			main.setText(main.destroyedPallets, totalDestroyedPallets + "/" + (maxSpawnedPallets + 1));
			Log.out("Pallet_" + maxSpawnedWindows + " spawned detected. ");
		}
	}

	public Player findPlayer(String line) {
		for (Player p : players) {
			if (line.indexOf(p.name) >= 0) {
				return p;
			}
		}
		return null;
	}
	
	public void searchKiller(String line) {
		if (line.indexOf(searchKiller) >= 0) {
			// System.out.println(line);
			int index = line.indexOf(searchKiller);
			line = line.substring(index, line.length());
			line = line.replace(searchKiller, "");
			currentKillerId = Integer.parseInt(line.substring(0, 2));
		}
	}

	public void searchOfferings(String line) {
		boolean offeringDetected = false;
		if (line.indexOf(searchEbonyMori) >= 0) {
			Log.out("Killer Offering detected: Ebony Mori");
			main.setImage(main.killerOffering, "ebony_mori.png");
			offeringDetected = true;
		}
		if (line.indexOf(searchIvoryMori) >= 0) {
			Log.out("Killer Offering detected: Ivory Mori");
			main.setImage(main.killerOffering, "ivory_mori.png");
			offeringDetected = true;
		}
		if (line.indexOf(searchCypressMori) >= 0) {
			Log.out("Killer Offering detected: Cypress Mori");
			main.setImage(main.killerOffering, "cypress_mori.png");
			offeringDetected = true;
		}
		if (line.indexOf(searchShroudSeperation) >= 0) {
			Log.out("Killer Offering detected: Shroud Of Seperation");
			main.setImage(main.killerOffering, "shroud_of_separation.png");
			offeringDetected = true;
		}
		if (offeringDetected) {
			main.toggleImage(main.killerOffering, true);
			main.toggleNode(main.offeringTitle, true);
			// main.rotateXImage(main.killerOffering, 90, 180, 2000, 100);
		}
	}

	public void searchSurvivorStates(String line, int numb) {
		if (line != null) {
			if (line.indexOf(camperPickUp) >= 0) {
				camperPickedUpLine = numb;
			}
			if (line.indexOf(camperDrop) >= 0) {
				camperDropLine = numb;
			}
			if (line.indexOf(camperStunDrop) >= 0) {
				camperDropLine = numb;
			}
			if (line.indexOf(camperEnterHook) >= 0) {
				camperEnterHookLine = numb;
			}
		}
	}

	public void searchSkillCheck(String line, int numb) {
		if (line != null) {
			if (line.indexOf(showSkillCheck) >= 0) {
				startSkillCheckCount++;
				skillCheckStartLine = numb;
			}
			if (line.indexOf(hideSkillCheck) >= 0) {
				hideSkillCheckCount++;
			}
		}
	}

	public void startSkillCheck(String line) {
		String[] strings = line.split(",");
		skillCheckKeyHit = strings[1].replace("keyText=", "").trim();
		skillAreaStart = Math.round(Double.parseDouble(strings[2].replace("hitAreaStart=", "").trim()) * 10000.0)
				/ 10000.0;
		skillAreaLength = Math.round(Double.parseDouble(strings[3].replace("hitAreaLength=", "").trim()) * 10000.0)
				/ 10000.0;
		skillBonusAreaLength = Math
				.round(Double.parseDouble(strings[4].replace("bonusAreaLength=", "").trim()) * 10000.0) / 10000.0;
		Log.out("Skillcheck detected: " + skillCheckKeyHit + ", HitAreaStart: " + skillAreaStart
				+ ", HitAreaLength: " + skillAreaLength + ", HitAreaLength: " + skillAreaLength + ", bonusAreaLength: "
				+ skillBonusAreaLength);
		skillCheckActive = true;
		skillCheckActionPerformed = false;
		stopSleep = true;
		//main.skillCheckBar.setVisible(true);
		//main.toggleText(main.skillCheckText, true);
	}

	public void endSkillCheck() {
		skillAreaStart = 0;
		skillAreaLength = 0;
		skillBonusAreaLength = 0;
		skillCheckActive = false;
		//main.skillCheckBar.setVisible(false);
		//main.toggleText(main.skillCheckText, false);
		stopSleep = false;
		// System.out.println("End Of Skillcheck.");
	}

	public void processSkillCheck(String line) throws ExceptionHandler {
		// System.out.print("Starts: " + startSkillCheckCount + " end: " +
		// hideSkillCheckCount);
		valueLine = line;
		if (line.indexOf(showSkillCheck) >= 0) {
			startSkillCheck(line);
		} else if (line.indexOf(hideSkillCheck) >= 0) {
			endSkillCheck();
		}
		if (skillCheckActive) {
			if (valueLine.indexOf(skillCheckProgressInfo) >= 0) {
				valueLine = (valueLine.substring(valueLine.indexOf("value="), valueLine.length()));
				valueLine = valueLine.substring(6, valueLine.length());
				try {
					currentValue = Double.parseDouble(valueLine.substring(0, valueLine.indexOf(',')));
				} catch (NumberFormatException e) {
					throw new ExceptionHandler("Couldn't parse skillcheck value");
				}
			}
			// System.out.println(currentValue);
			if (currentValue >= skillAreaStart) {
				// Main.hardPress(' ');
				endSkillCheck();
			}
			//main.skillCheckBar.setProgress(currentValue);
		}
	}

	public void searchKillerPerks(String line, int lineCount) {
		if (line != null) {
			if (line.indexOf(interactionEnter) >= 0) {
				lastInteractionEnterLine = line;
			}
			if (line.indexOf(interactionEnter) >= 0) {
				lastInteractionEnterLine = line;
			}
			if (line.indexOf(ruin) >= 0) {
				setPerk("ruin");
			}
			if (line.indexOf(makeYourChoice) >= 0) {
				setPerk("makeYourChoice");
			}
			if (line.indexOf(noed) >= 0) {
				setPerk("noOneEscapesDeath");
			}
			if (line.indexOf(overwhelmingPresence) >= 0) {
				setPerk("overwhelmingPresence");
			}
			if (line.indexOf(lullaby) >= 0) {
				setPerk("HuntressLullaby");
			}
			if (line.indexOf(retribution) >= 0) {
				setPerk("hexRetribution");
			}
			if (line.indexOf(hauntedGrounds) >= 0) {
				setPerk("hauntedGround");
			}
			if (line.indexOf(devourHope) >= 0) {
				setPerk("devourHope");
			}
			if (line.indexOf(thirdSeal) >= 0) {
				setPerk("theThirdSeal");
			}
			if (line.indexOf(nurseCalling) >= 0) {
				setPerk("nursesCalling");
			}
			if (line.indexOf(bitterMurmur) >= 0) {
				setPerk("bitterMurmur");
			}
			if (line.indexOf(thrillOfTheHunt) >= 0) {
				setPerk("thrillOfTheHunt");
			}
			if (line.indexOf(bloodWarden) >= 0) {
				setPerk("bloodWarden");
			}
			if (line.indexOf(darkDevotion) >= 0) {
				setPerk("darkDevotion");
			}
			if (line.indexOf(coulrophobia) >= 0) {
				setPerk("coulrophobia");
			}
			if (line.indexOf(dyingLight) >= 0) {
				setPerk("dyingLight");
			}
			if (line.indexOf(franklinsDemise) >= 0) {
				// setPerk("franklinsLoss");
			}
			if (line.indexOf(deadManSwitch) >= 0) {
				setPerk("deadManSwitch");
			}
			if (line.indexOf(ironMaiden) >= 0) {
				setPerk("ironMaiden");
			}
			if (line.indexOf(bbqAndChilli) >= 0) {
				setPerk("BBQAndChili");
			}
			if (line.indexOf(mindBreaker) >= 0) {
				setPerk("mindBreaker");
			}
			if (line.indexOf(rancor) >= 0) {
				setPerk("hatred");
			}
			if (line.indexOf(rememberMe) >= 0) {
				setPerk("rememberMe");
			}
			if (line.indexOf(thanatophobia) >= 0) {
				setPerk("thatanophobia");
			}
			if (line.indexOf(territorialImperative) >= 0) {
				setPerk("territorialImperative");
			}
			if (line.indexOf(tinkerer) >= 0) {
				setPerk("tinkerer");
			}
			if (line.indexOf(beastOfPrey) >= 0) {
				setPerk("BeastOfPrey");
			}
			if (line.indexOf(hangMan) >= 0) {
				setPerk("hangmansTrick");
			}
			if (line.indexOf(allEars) >= 0) {
				setPerk("imAllEars");
			}
			if (line.indexOf(knockOut) >= 0) {
				setPerk("knockOut");
			}
			if (line.indexOf(fireUpVaultSpeed) >= 0 || line.indexOf(fireUpBreakPalletSpeed) >= 0
					|| line.indexOf(fireUpDamageGenSpeed) >= 0) {
				// setPerk("fireUp"); NOT WORKING
			}
			if (line.indexOf(bamboozleVaultSpeed) >= 0) {
				setPerk("bamboozle");
			}
			if (line.indexOf(stridor) >= 0) {
				setPerk("stridor");
			}
			if (line.indexOf(sloppyButcher) >= 0) {
				setPerk("sloppyButcher");
			}
			if (line.indexOf(unnvervingPresence) >= 0) {
				setPerk("unnervingPresence");
			}
			if (line.indexOf(enduringPalletStunSpeed) >= 0 && lastInteractionEnterLine.indexOf(palletStun) >= 0) {
				setPerk("enduring");
			}
			if (line.indexOf(gameStartCamAnimationEnd) >= 0) {
				camFadeOutLine = lineCount;
			}
			if (line.indexOf(genBlocked) >= 0) {
				// Get Line for last blocked gen
				lastGenBlockLine = lineCount;
			}
			if (line.indexOf(brutalStrengthDamageGenSpeed) >= 0 || line.indexOf(brutalStrengthPalletSpeed) >= 0) {
				setPerk("brutalStrength");
			}
			if (lastGenBlockLine > 0) {
				if (lastGenBlockLine < camFadeOutLine && lastGenBlockLine > startLineNumber) {
					setPerk("corruptIntervention");
				} else if (lastGenBlockLine > camperPickedUpLine
						&& (lastGenBlockLine < camperDropLine || lastGenBlockLine < camperEnterHookLine)) {
					setPerk("thrillingTremors");
				}
			}
			// Missing Perks
			/*
			 * Agitation BloodHound Bloodecho DeerStalker Discordance -- Multiplayer
			 * Lightborn Brutal Strength Bamboozle Curel Limits Distressing Franklings
			 * Demise --- Not confirmed Furtive Chase Gearhead Infectious Fright Insidious
			 * IronGrasp Madgrid MonitorAbuse MonstrousShrine PopGoesTheWeasel Nemesis
			 * PlayWithYourFood Predator OverCharge SaveTheBastForLast -- Multiplayer
			 * ShadowBorn SpiesFromTheShadows SpiritFury Surge Surveillance Unrelenting
			 * Whispers Zanshin Tactics
			 */
		}
	}

	public void matchTime(String line, int lineCount) {
		if (line.indexOf(gameStartCamAnimationEnd) >= 0 && line.length() >= 24) {
			try {
				Calendar calendar = Calendar.getInstance();

				int days = Integer.parseInt(line.substring(9, 11));
				int hours = Integer.parseInt(line.substring(12, 14));
				if (hours >= 24)
					hours = hours - 24;
				int minutes = Integer.parseInt(line.substring(15, 17));
				int seconds = Integer.parseInt(line.substring(18, 20));
				int duration = (days * 24 * 60 * 60) + (hours * 60 * 60) + (minutes * 60) + seconds + (60 * 60) * 2;
				// System.out.println("DBD: " + days + " : " + hours + " : " + minutes);

				int cdays = calendar.get(Calendar.DAY_OF_MONTH);
				int chours = calendar.get(Calendar.HOUR_OF_DAY);
				int cminutes = calendar.get(Calendar.MINUTE);
				int cseconds = calendar.get(Calendar.SECOND);
				int current = (cdays * 24 * 60 * 60) + (chours * 60 * 60) + (cminutes * 60) + cseconds;
				// System.out.println("YOURS: " + cdays + " : " + chours + " : " + cminutes);
				matchStartTime = duration - 2;
				// Main.matchTime.setText(secondsToTime(current - matchStartTime));
			} catch (NumberFormatException e) {

			}
		}
	}

	public void checkSurvivorHookPhases(String line) {
		if (line.indexOf(dStrike) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				Log.out(p.name + " has Decisive Strike!");
			}
		}
		if (line.indexOf(survivorFirstHook) >= 0) {
			Player p = findPlayer(line);
			if (p != null && !p.hookPhase2) {
				p.hookPhase1 = true;
				main.setText(main.totalHooks[p.survivorId], "Hookstate: 1");
				Log.out("Survivor " + p.name + " first hook.");
			}
		} else if (line.indexOf(survivorSecondHook) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				p.hookPhase2 = true;
				main.setText(main.totalHooks[p.survivorId], "Hookstate: 2");
				Log.out("Survivor " + p.name + " on second hook.");
			}
		}
	}
	
	public void updateTime() {
		if (matchStartTime > 0) {
			Calendar calendar = Calendar.getInstance();
			int cdays = calendar.get(Calendar.DAY_OF_MONTH);
			int chours = calendar.get(Calendar.HOUR_OF_DAY);
			int cminutes = calendar.get(Calendar.MINUTE);
			int cseconds = calendar.get(Calendar.SECOND);
			int current = (cdays * 24 * 60 * 60) + (chours * 60 * 60) + (cminutes * 60) + cseconds;
			String time = secondsToTime(current - matchStartTime);
			main.setText(main.matchStatusTime, time);
			
			for (Player p : players) {
				if (p.borrowedTime > 0) {
					p.borrowedTime -= 0.5;
					main.setText(main.survivorAction[p.survivorId], "time: " + p.borrowedTime);
					main.setProgress(main.survivorBars[p.survivorId], p.borrowedTime/15.0);
					main.toggleNode(main.survivorBars[p.survivorId], true);
					if (p.borrowedTime == 0) {
						main.setText(main.survivorAction[p.survivorId], "Action: ");
						main.toggleNode(main.survivorBars[p.survivorId], false);
					}
				}
			}
		} else {
			main.setText(main.matchStatusTime, main.matchStatusTime.getText()+".");
			if (main.matchStatusTime.getText().length() > 10) {
				main.setText(main.matchStatusTime, "Loading");
			}
		}
	}

	public String secondsToTime(int s) {
		int minutes = s / 60;
		int seconds = s - (minutes * 60);
		return minutes + "m" + " " + seconds + "s";
	}

	public String getMapName(String theme) {
		theme = theme.toLowerCase().trim();
		//Log.out("MAP: " + theme);
		switch (theme) {
		case "saloon":
			return "Dead Dawg Saloon";
		case "lab":
			return "Underground Complex";
		case "cottage":
			return "Mount Ormond Resort";
		case "shrine":
			return "Sanctum Of Wrath";
		case "manor":
			return "Family Residence";
		case "hideout":
			return "The Game";
		case "street_01":
			return "Badham Preschool 1";
		case "street_02":
			return "Badham Preschool 2";
		case "street_03":
			return "Badham Preschool 3";
		case "street_04":
			return "Badham Preschool 4";
		case "street_05":
			return "Badham Preschool 5";
		case "grimpantry":
			return "Grim Pantry";
		case "palerose":
			return "The Pale Rose";
		case "treatment":
			return "The Treatment";
		case "mahouse":
			return "Mother's Dwelling";
		case "temple":
			return "Temple Of Purgation";
		case "street":
			return "Lampkin Lane";
		case "chapel":
			return "Fathers Campbells Chapel";
		case "asylum":
			return "Disturbed Ward";
		case "scrapyard":
			return "Wreckers Yard";
		case "lodge":
			return "Blood Lodge";
		case "office":
			return "Azarovs Resting Place";
		case "garage":
			return "Wretched Shop";
		case "gasstation":
			return "Gas Station";
		case "barn":
			return "Fractured Cowshed";
		case "cornfield":
			return "Rotten Fields";
		case "silo":
			return "Torment Creek";
		case "farmhouse":
			return "The Thompson House";
		case "slaughterhouse":
			return "Rancid Abattoir";
		case "storehouse":
			return "Groaning Storehouse";
		case "mine":
			return "Suffocation Pit";
		case "foundry":
			return "Ironworks Of Misery";
		case "forest":
			return "Shelter Woods";
		case "coaltower":
			return "Coal Tower";
		default:
			return "unknown";
		}
	}

	public void searchMap(String line) {
		if (line != null) {
			if (line.indexOf(searchMapTheme) >= 0) {
				String[] strings = line.split(":");
				mapTheme = strings[4].replace("Map", "").trim();
				mapName = strings[5].replace("Generation Seed", "");
				mapName = mapName.substring(mapName.indexOf("_") + 1, mapName.length());
				mapName = getMapName(mapName);
				// System.out.println(mapName + " : " + mapTheme);
				main.setText(main.mapName, mapName + " (" + mapTheme + ")");
				main.toggleNode(main.mapTitle, true);
				main.toggleNode(main.mapName, true);
				Log.out("Currently playing on: " + "(" + mapName + ") " + "(" + mapTheme + ")");
			}
		}
	}

	public void searchTotems(String line) {
		if (line.indexOf(searchTotem) >= 0 && line.indexOf(interactionEnter) >= 0) {
			cleanseTotemStartCount++;
		} else if (line.indexOf(searchTotem) >= 0 && line.indexOf(interactionExit) >= 0) {
			cleanseTotemEndCount++;
		} else if (line.indexOf(interactionFinished) >= 0 && line.indexOf(searchTotem) >= 0) {
			totalCleansedTotems++;
		}
		DropShadow shadow = new DropShadow();
		shadow.setWidth(0);
		shadow.setHeight(0);
		shadow.setBlurType(BlurType.ONE_PASS_BOX);
		if (cleanseTotemStartCount > cleanseTotemEndCount) {
			shadow.setColor(Color.rgb(255, 0, 255));
			// main.setShadow(main.totemPic, shadow);
		} else {
			shadow.setColor(Color.rgb(100, 100, 100));
			// main.setShadow(main.totemPic, shadow);
		}
		main.setText(main.totalTotems, totalCleansedTotems + "/5");
	}

	public void setPerk(String perk) {
		for (int i = 0; i < killerPerks.length; i++) {
			if (killerPerks[i] == perk) {
				return;
			}
		}
		for (int i = 0; i < killerPerks.length; i++) {
			if (killerPerks[i] == "" || killerPerks[i] == null) {
				try {
					killerPerks[i] = perk;
					String image = "iconPerks_" + perk + ".png";
					main.setImage(main.killerPerks[i], image);
					main.toggleImage(main.killerPerks[i], true);
					main.toggleImage(main.killerPerkFrames[i], true);
					main.playPerkAnimation(perk, i);
					Log.out("Killer Perk detected: " + perk);
					break;
				} catch (ArrayIndexOutOfBoundsException e) {
					throw new ExceptionHandler("ERROR: Perk Imageview out of bounds");
				} catch (NullPointerException e) {
					throw new ExceptionHandler("ERROR: Imageview OutOfBoundsException");
				}
			}
		}
	}
}