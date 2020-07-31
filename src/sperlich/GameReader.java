package sperlich;

import javafx.application.Platform;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class GameReader extends Thread {
	
	String lastInteractionEnterLine;
	int totalRepairedGens;
	int camperPickedUpLine;
	int camperDropLine;
	int camperEnterHookLine;
	String[] killerPerks = new String[4];
	static String mapTheme = "";
	static String mapName = "";
	public static boolean playerIsIngame;
	public static boolean noedActive;
	public static boolean ruinActive;
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
	public int totalBloodPoints;
	public String valueLine;
	public double currentValue;
	public static double loadingProgress;
	public int updateDelta;
	public boolean stopSleep;
	public Date logOpenDateTime = null;
	public Date logServerTime = null;
	public Date matchStartTime;
	public Date currentTime;
	public long timeDifference;
	public int matchTime;
	public Timer timer;
	public int totalPalletPullDowns;
	public boolean spiritFuryDetected;
	public int thrownPallets;
	public int spiritActive;
	public int totalPalletStuns;
	public int totalDestroyedPallets;
	public int normalDestroyedPallets;
	public int maxSpawnedPallets;
	public int maxSpawnedWindows;
	public boolean palletStunActive;
	public boolean hudActive = false;
	public boolean deactiveSpiritFuryDetection;
	public boolean gameIsClosed = false;
	public boolean skillCheckActive = false;
	public static boolean loadingFinished;
	public boolean matchHasStarted;
	public ArrayList<Player> players = new ArrayList<>();
	public Player lastPlayerInteracting;
	public int lastEndLine;
	public static boolean skip;
	public int errorDebug;
	public int currentMatchDuration;
	public int moriedSurvivors;
	public boolean hatchSpawned;
	public Thread skillCheck;
	public boolean gameCrashed;
	public String lastSteamId;
	public int customFound;
	public static String killerSteamId;
	String path = System.getProperty("user.home").replaceAll("\\\\", "/")+"/AppData/Local/DeadByDaylight/Saved/Logs/DeadByDaylight.log";
	ArrayList<String> obsessionStates = new ArrayList<>();
	public int refreshRate;

	public void initialize() throws InterruptedException {
		Log.out("GameReader successfully started.");
		sleep(1000);
		Log.out("Searching Logfile At: " + path);
        try {
        	BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
	    	fullScan(reader);
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
			readLog(reader);
		} catch (Exception e) {
			Log.out("An unexpected error occurred!");
			StackTraceElement[] elements = e.getStackTrace();
            for (int iterator=1; iterator<=elements.length; iterator++)  {
               Log.out("Class Name:"+elements[iterator-1].getClassName()+" Method Name:"+elements[iterator-1].getMethodName()+" Line Number:"+elements[iterator-1].getLineNumber());
        	}
			timer.stop();
			gameCrashed = true;
			Runtime.setText(Runtime.killerName, "Unexpected ERROR occurred");
			if (Runtime.overlay != null) {
				Runtime.setText(Runtime.overlay.killerName, "Unexpected ERROR occurred");
			}
			Runtime.setImage(Runtime.killerPic, "unknown.png");
			resetGame();
			oldKillerId = -1;
			initialize();
        }
	}

	public void fullScan(BufferedReader reader) throws IOException {
		Log.out("Starting fullscan...");
		String line = "";
		int lineCount = 0;
		int matchStarts = 0;
		int matchEnds = 0;
		int lastLine = 0;
		gameIsClosed = false;
		while (line != null) {
			line = reader.readLine();
			if (line != null) {
				line = line.replaceAll("[^\\u0000-\\uFFFF]", "");
				if (Search.contains(line, Search.gameStarted)) {
					matchStarts++;
					lastLine = lineCount;
				}
				if (Search.contains(line, Search.gameLeft)) {
					matchEnds++;
					lastLine = lineCount;
				}
				if (Search.contains(line, Search.gameClosed)) {
					gameIsClosed = true;
					lastLine = lineCount;
				}
				if (logServerTime == null && Search.contains(line, "LogConfig: Applying CVar settings from Section")) {
					logServerTime = GameReader.getDateFromTimestamp(getTimestamp(line));
					if (logServerTime != null) {
						timeDifference = (logOpenDateTime.getTime() - logServerTime.getTime())/1000;
						Log.out("ServerTime: " + logServerTime);
						Log.out("Calculated Time Difference: " + timeDifference);
					}
				}
				if (logOpenDateTime == null && Search.contains(line, "Log file open,")) {
					SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
					//yyyy.MM.dd-HH.mm.ss:SSS3
					try {
						logOpenDateTime = formatter.parse(line.substring(Search.getIndex(line, ",")+1, line.length()).trim());
					} catch (ParseException e) { Log.out("Couldn't parse date"); }
					Log.out("UserTime: " + logOpenDateTime);
				}
				searchSteamId(line);
				searchKiller(line);
				searchBloodPoints(line);
			}
			lineCount++;
		}
		startLineNumber = lastLine;
		if (matchStarts > matchEnds) {
			startGame(startLineNumber);
		} else {
			endGame();
		}
		totalLines = lineCount;
		setKiller(currentKillerId);
		reader.close();
		customFound = 0;
		lastSteamId = null;
		Log.out("Successfully scanned " + lineCount + " lines.");
	}

	public void readLog(BufferedReader reader) throws IOException, InterruptedException {
		Log.out("Current match status: " + playerIsIngame);
		String line = "";
		int lineCount = 0;
		Runnable task = () -> updateTime();
		timer = new Timer(500, task);
		
		while (true) {
			line = reader.readLine();
			if (line != null) {
				loadingProgress++;
				if (lineCount >= startLineNumber) {
					line = line.replaceAll("[^\\u0000-\\uFFFF]", "");
					scanLine(line, lineCount, reader);
					if (lineCount >= totalLines-25) {
						if (!loadingFinished) {
							Log.out("Finished Loading");
							Platform.runLater(()-> {
								Runtime.startLoadText.setText("Finished");
								Runtime.startScreen.setVisible(false);
								Runtime.deleteStartScreen();
							});
						}
						loadingFinished = true;
					}
				}
				lineCount++;
			} else {
				if (!skillCheckActive) {
					Thread.sleep(1);
				}
			}
		}
	}

	public void scanLine(String line, int lineCount, BufferedReader reader) {
		checkIfIngame(line, lineCount);
		checkMatchStartTime(line);
		searchKiller(line);
		setKiller(currentKillerId);
		searchSteamId(line);
		if (playerIsIngame) {
			try {
				searchMap(line);
				//searchSurvivorStates(line, lineCount);
				//searchTotems(line);
				//searchKillerPerks(line, lineCount);
				//searchSurvivorPerks(line);
				searchOfferings(line);
				//searchPallets(line);
				//searchPlayers(line, lineCount);
				//checkSurvivorHookPhases(line, lineCount);
				//searchSurvivorActions(line);
				//searchPlayerPositions(line);
				//searchOthers(line);
				searchBloodPoints(line);
				//searchSkillCheck(line, lineCount, reader);
			} catch (Exception e) {
				Log.out("An unexpected error occurred on this line! Printing error...");
				StackTraceElement[] elements = e.getStackTrace();
	            for (int iterator=1; iterator<=elements.length; iterator++)  {
	               Log.out("Class Name:"+elements[iterator-1].getClassName()+" Method Name:"+elements[iterator-1].getMethodName()+" Line Number:"+elements[iterator-1].getLineNumber());
	        	}
			}
			if (gameCrashed) {
				
				gameCrashed = false;
			}
		}
		if (gameIsClosed) {
			Log.out("Game is closed");
			currentKillerId = -1;
			setKiller(-1);
			Runtime.setText(Runtime.killerName, "Game closed");
			if (Runtime.overlay != null) {
				Runtime.setText(Runtime.overlay.killerName, "Game closed");
			}
		}
	}
	
	public void searchSteamId(String line) {
		if (Search.contains(line, Search.searchSteamId)) {
			lastSteamId = line.substring(Search.getIndex(line, "PlayerId:")+9, line.length()).trim();
		}
		if (Search.contains(line, "LogCustomization: ------")) {
			customFound++;
			if (customFound > 1) {
				lastSteamId = null;
				customFound = 0;
			}
		}
		if (lastSteamId != null && Search.contains(line, "LogCustomization: -->")) {
			int id = getKillerByCosmetic(line.substring(Search.getIndex(line, ">")+1, Search.getIndex(line, "_")+1).trim().toLowerCase());
			if (id > 0) {
				Log.out("Killer " + getKillerById(id) + " Steam64-Id found: " + lastSteamId);
				killerSteamId = lastSteamId.substring(Search.getIndex(lastSteamId, "|")+1, lastSteamId.length());
				Runtime.killerSteamURL = "https://steamcommunity.com/profiles/" + killerSteamId;
				Tooltip t = new Tooltip("Opem Steam-Profile: " + Runtime.killerSteamURL);
				Tooltip.install(Runtime.killerPic, t);
				lastSteamId = null;
			}
		}
	}
	
	public int getKillerByCosmetic(String line) {
		// Trapper
		if (Search.contains(line, "TR_")) {
			return 1;
		}
		// Wraith
		if (Search.contains(line, "Wraith_") || Search.contains(line, "TW")) {
			return 2;
		}
		// Hillbilly
		if (Search.contains(line, "Hillbilly_") || Search.contains(line, "TC")) {
			return 3;
		}
		// Nurse
		if (Search.contains(line, "Nurse_") || Search.contains(line, "TN_")) {
			return 4;
		}
		// Hag
		if (Search.contains(line, "HA_")) {
			return 5;
		}
		// Michael Myers
		if (Search.contains(line, "MM_")) {
			return 6;
		}
		// Doctor
		if (Search.contains(line, "DO_")) {
			return 7;
		}
		// Huntress
		if (Search.contains(line, "BE_")) {
			return 8;
		}
		// Leatherface
		if (Search.contains(line, "CA_")) {
			return 9;
		}
		// Freddy
		if (Search.contains(line, "SD_")) {
			return 10;
		}
		// Pig
		if (Search.contains(line, "FK_")) {
			return 11;
		}
		// Clown
		if (Search.contains(line, "GK_")) {
			return 12;
		}
		// Spirit
		if (Search.contains(line, "HK_")) {
			return 13;
		}
		// Legion
		if (Search.contains(line, "KK_")) {
			return 14;
		}
		// Plague
		if (Search.contains(line, "MK_")) {
			return 15;
		}
		// Ghostface
		if (Search.contains(line, "OK_")) {
			return 16;
		}
		// Demogorgon
		if (Search.contains(line, "QK_")) {
			return 17;
		}
		// Oni
		if (Search.contains(line, "Swedenkiller_") || Search.contains(line, "SW_")) {
			return 18;
		}
		// Deathslinger
		if (Search.contains(line, "UK_") || Search.contains(line, "UkraineKiller_")) {
			return 19;
		}
		// Executioner
		if (Search.contains(line, "K20_")) {
			return 20;
		}
		return -1;
	}
	
	public void setKiller(int id) {
		if (oldKillerId != currentKillerId && currentKillerId > 0) {
			Log.out("Killer detected: " + getKillerById(id) + " id: " + currentKillerId);
			Runtime.setKillerPic(id);
			Runtime.setText(Runtime.killerName, getKillerById(id));
			if (Runtime.overlay != null) {
				Runtime.setText(Runtime.overlay.killerName, getKillerById(id));
			}
			oldKillerId = currentKillerId;
		}
	}

	public String getKillerById(int id) {
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
		case 20:
			return "Executioner";
		default:
			return "Unknown";
		}
	}

	public void checkIfIngame(String line, int currentLine) {
		if (Search.contains(line, Search.gameStarted)) {
			startGame(currentLine);
		}
		if (Search.contains(line, Search.gameLeft)) {
			lastEndLine = currentLine;
			endGame();
		}
	}

	public void startGame(int lineCount) {
		Log.out("Match start detected.");
		resetGame();
		//Runtime.totemsBox.setVisible(true);
		//Runtime.palletsBox.setVisible(true);
		//Runtime.vaultsBox.setVisible(true);
		//Runtime.survivorsTitle.setVisible(true);
		if (Runtime.config.map) {
			Runtime.mapBox.setVisible(true);
		}
		playerIsIngame = true;
		startLineNumber = lineCount;
		Runtime.setText(Runtime.matchStatusTime, "Loading");
		if (Runtime.overlay != null) {
			Runtime.setText(Runtime.overlay.time, Runtime.matchStatusTime.getText());
		}
		DropShadow shadow = new DropShadow();
		shadow.setColor(Color.rgb(150, 255, 10));
		Runtime.setShadow(Runtime.killerPic, shadow);
		if (timer != null) {
			//timer.start();
		}
	}

	public void searchPlayerPositions(String line) {
		if (Search.contains(line, Search.searchPlayerPosition)) {
			//String position = line.substring(Search.getIndex(line, searchPlayerPosition) + 29, line.length() - 1);
			//Log.out("Player " + lastPlayerInteracting.name + ": " + position);
		}
	}
	
	public void resetGame() {
		Log.out("Resetting Match...");
		playerIsIngame = false;
		spiritFuryDetected = false;
		cleanseTotemStartCount = 0;
		cleanseTotemEndCount = 0;
		totalCleansedTotems = 0;
		totalDestroyedPallets = 0;
		maxSpawnedPallets = 0;
		maxSpawnedWindows = 0;
		thrownPallets = 0;
		totalRepairedGens = 0;
		currentMatchDuration = 0;
		hatchSpawned = false;
		matchHasStarted = false;
		lastSteamId = null;
		Runtime.toggleNode(Runtime.hatchTitle, false);
		Runtime.toggleImage(Runtime.hatchImage, false);
		//Runtime.setText(Runtime.killerPlayerName, "Detected Perks: ");
		obsessionStates = new ArrayList<>();
		killerPerks = new String[4];
		for (int i = 0; i < Runtime.survivorNames.length; i++) {
			//Runtime.setText(Runtime.survivorNames[i], "");
			//Runtime.setText(Runtime.totalHooks[i], "Hookstate: 0");
			Runtime.toggleNode(Runtime.survivorAction[i], false);
			Runtime.toggleNode(Runtime.totalHooks[i], false);
			Runtime.toggleNode(Runtime.survivorBars[i], false);
			//Runtime.setProgress(Runtime.survivorBars[i], 0);
			//Runtime.deadSymbols[i].setVisible(false);
			//Runtime.setText(Runtime.survivorAction[i], "");
			//Runtime.setImage(Runtime.actionIcons[i], null);
		}
		players = new ArrayList<>();
		matchTime = 0;
		normalDestroyedPallets = 0;
		Runtime.setText(Runtime.matchStatusTime, "Lobby");
		//Runtime.setText(Runtime.destroyedPallets, totalDestroyedPallets + "/0");
		//Runtime.setText(Runtime.totalTotems, "0/5");
		//Runtime.setText(Runtime.totalVaults, "0/0");
		Runtime.setImage(Runtime.killerOffering, null);
		Runtime.toggleImage(Runtime.killerOffering, false);
		Runtime.toggleNode(Runtime.offeringTitle, false);
		Runtime.toggleNode(Runtime.mapName, false);
		Runtime.toggleNode(Runtime.mapTitle, false);
		Runtime.toggleImage(Runtime.hatchImage, false);
		//searchTotems("");
		for (int i = 0; i < Runtime.killerPerks.length; i++) {
			if (Runtime.killerPerks[i] != null && Runtime.killerPerkFrames[i] != null) {
				//String icon = "perk_default.png";
				//Runtime.setImage(Runtime.killerPerks[i], icon);
				//Runtime.toggleImage(Runtime.killerPerks[i], false);
				//Runtime.toggleImage(Runtime.killerPerkFrames[i], false);
			}
		}
		Runtime.updateOverlay();
		if (timer != null) {
			//timer.stop();
		}
	}

	public void endGame() {
		Runtime.totemsBox.setVisible(false);
		Runtime.palletsBox.setVisible(false);
		Runtime.vaultsBox.setVisible(false);
		Runtime.survivorsTitle.setVisible(false);
		Runtime.mapBox.setVisible(false);
		Log.out("Match end detected.");
		resetGame();
		DropShadow shadow = new DropShadow();
		shadow.setColor(Color.rgb(0, 0, 0));
		Runtime.setShadow(Runtime.killerPic, shadow);
		playerIsIngame = false;
	}

	public void searchOthers(String line) {
		if (Search.contains(line, Search.searchGenComplete) && currentMatchDuration >= 20) {
			totalRepairedGens++;
			Log.out("Repaired Generator: " + totalRepairedGens);
		}
		if (getSurvivorCount() >= 4 && !hatchSpawned && totalRepairedGens > getSurvivorsInMap()) {
			hatchSpawned = true;
			Runtime.toggleNode(Runtime.hatchTitle, true);
			Runtime.toggleImage(Runtime.hatchImage, true);
			Runtime.updateOverlay();
			Log.out("HATCH SPAWNED");
		}
		
	}
	
	public int getSurvivorsAlive() {
		int total = 0;
		for (Player p : players) {
			if (p.isSurvivor && !p.isDead) {
				total++;
			}
		}
		return total;
	}
	
	public int getSurvivorsInMap() {
		int total = 0;
		for (Player p : players) {
			if (p.isSurvivor && !p.isDead && !p.hasEscaped) {
				total++;
			}
		}
		return total;
	}
	
	public void searchSurvivorActions(String line) {
		applyObsessionState();
		//boolean updateStatus = false;
		// Survivor started any interaction
		if (Search.contains(line, Search.interactEnter)) {
			Player p = findPlayer(line);
			if (p != null) {
				p.isInteracting = true;
				lastPlayerInteracting = p;
				if (p.isSurvivor) {
					//updateStatus = true;
				}
			}
		}
		// Survivor stopped any interaction
		if (Search.contains(line, Search.interactExit)) {
			Player p = findPlayer(line);
			if (p != null) {
				p.isInteracting = false;
				//Runtime.toggleImage(Runtime.actionIcons[p.survivorId], false);
				//Runtime.setText(Runtime.survivorAction[p.survivorId], "");
				if (p.isSurvivor) {
					//updateStatus = true;
				}
			}
		}
		// Borrowed Time
		if (Search.contains(line, Search.borrowedTime) && Search.contains(line, Search.borrowedTimeInit)) {
			Player p = findPlayer(line);
			if (p != null && p.borrowedTime == 0) {
				p.borrowedTime = 15;
			} else if (p != null) {
				p.borrowedTime = 0;
			}
		}
		if (Search.contains(line, Search.playerEscaped) && Search.contains(line, Search.interactEnter)) {
			Player p = findPlayer(line);
			if (p != null && p.hooks < 3) {
				p.hasEscaped = true;
				Platform.runLater(()-> {
					Runtime.setImage(Runtime.deadSymbols[p.survivorId], "escaped.png");
					Runtime.deadSymbols[p.survivorId].setVisible(true);
					Runtime.deadSymbols[p.survivorId].setManaged(true);
				});
				Log.out("Player " + p.name + "_" + p.survivorId + " has escaped!");
				Runtime.updateOverlay();
			}
		}
		// Hiding in Closet
		if ((Search.contains(line, Search.closetEnter) || Search.contains(line, Search.closetEnterRushed)) && Search.contains(line, Search.interactEnter)) {
			Player p = findPlayer(line);
			if (p != null) {
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
				Runtime.setImage(Runtime.actionIcons[p.survivorId], "action_locker.png");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], true);
				Log.out(p.name + " is entering locker");
				Runtime.updateOverlay();
			}
		}
		if ((Search.contains(line, Search.closetExit) || Search.contains(line, Search.closetExitRushed)) && Search.contains(line, Search.interactExit)) {
			Player p = findPlayer(line);
			if (p != null) {
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], false);
				Log.out(p.name + " is exiting locker");
				Runtime.updateOverlay();
			}
		}
		// Repairing
		if (Search.contains(line, Search.searchSurvivorRepair) && Search.contains(line, Search.interactEnter)) {
			Player p = findPlayer(line);
			if (p != null) {
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
				Runtime.setImage(Runtime.actionIcons[p.survivorId], "action_repair.png");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], true);
				Log.out(p.name + " started repairing");
				Runtime.updateOverlay();
			}
		}
		if (Search.contains(line, Search.searchSurvivorRepair) && Search.contains(line, Search.interactExit)) {
			Player p = findPlayer(line);
			if (p != null) {
				Log.out(p.name + " stopped repairing");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], false);
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
				Runtime.updateOverlay();
			}
		}
		// Heal other
		if (Search.contains(line, Search.searchSurvivorHealOther) && Search.contains(line, Search.interactEnter)) {
			Player p = findPlayer(line);
			if (p != null) {
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
				Runtime.setImage(Runtime.actionIcons[p.survivorId], "action_healother.png");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], true);
				Log.out(p.name + " started healing companion");
				Runtime.updateOverlay();
			}
		}
		if (Search.contains(line, Search.searchSurvivorHealOther) && Search.contains(line, Search.interactExit)) {
			Player p = findPlayer(line);
			if (p != null) {
				Log.out(p.name + " stop healing companion");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], false);
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
				Runtime.updateOverlay();
			}
		}
		// Self healing with medkit
		if (Search.contains(line, Search.searchSuvivorSelfhealMedkit) && Search.contains(line, Search.interactEnter)) {
			Player p = findPlayer(line);
			if (p != null) {
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
				Runtime.setImage(Runtime.actionIcons[p.survivorId], "action_medkit.png");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], true);
				Log.out(p.name + " started healing with Medkit");
				Runtime.updateOverlay();
			}
		}
		if (Search.contains(line, Search.searchSuvivorSelfhealMedkit) && Search.contains(line, Search.interactExit)) {
			Player p = findPlayer(line);
			if (p != null) {
				Log.out(p.name + " stopped healing with Medkit");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], false);
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
				Runtime.updateOverlay();
			}
		}
		// Searching Chest
		if (Search.contains(line, Search.searchSurvivorLooting) && Search.contains(line, Search.interactEnter)) {
			Player p = findPlayer(line);
			if (p != null) {
				Runtime.setImage(Runtime.actionIcons[p.survivorId], "action_loot.png");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], true);
				Log.out(p.name + " started searching chest");
				Runtime.updateOverlay();
			}
		}
		if (Search.contains(line, Search.searchSurvivorLooting) && Search.contains(line, Search.interactExit)) {
			Player p = findPlayer(line);
			if (p != null) {
				Log.out(p.name + " stopped searching chest");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], false);
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
				Runtime.updateOverlay();
			}
		}
		// Wiggling
		if (Search.contains(line, Search.searchSurvivorWiggle) && Search.contains(line, Search.interactEnter)) {
			Player p = findPlayer(line);
			if (p != null) {
				Runtime.setText(Runtime.survivorAction[p.survivorId], "Action: wiggle");
				Log.out(p.name + " started wiggling");
				Runtime.updateOverlay();
			}
		}
		if (Search.contains(line, Search.searchSurvivorWiggle) && Search.contains(line, Search.interactExit)) {
			Player p = findPlayer(line);
			if (p != null) {
				Log.out(p.name + " stopped wiggling");
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
				Runtime.updateOverlay();
			}
		}
		// Recovering
		if (Search.contains(line, Search.searchSurvivorRecover) && Search.contains(line, Search.interactEnter)) {
			Player p = findPlayer(line);
			if (p != null) {
				Runtime.setText(Runtime.survivorAction[p.survivorId], "recover");
				Log.out(p.name + " started recovering");
				Runtime.updateOverlay();
			}
		}
		if (Search.contains(line, Search.searchSurvivorRecover) && Search.contains(line, Search.interactExit)) {
			Player p = findPlayer(line);
			if (p != null) {
				Log.out(p.name + " stopped recovering");
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
				Runtime.updateOverlay();
			}
		}
		// Selfcare
		if (Search.contains(line, Search.searchSurvivorSelfcare) && Search.contains(line, Search.interactEnter)) {
			Player p = findPlayer(line);
			if (p != null) {
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
				Runtime.setImage(Runtime.actionIcons[p.survivorId], "action_selfcare.png");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], true);
				Log.out(p.name + " started selfcaring");
				Runtime.updateOverlay();
			}
		}
		if (Search.contains(line, Search.searchSurvivorSelfcare) && Search.contains(line, Search.interactExit)) {
			Player p = findPlayer(line);
			if (p != null) {
				Log.out(p.name + " stopped selfcaring");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], false);
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
				Runtime.updateOverlay();
			}
		}
		// Chasing
		if (Search.contains(line, "is in chase.")) {
			Player p = findSurvivor(line);
			if (p != null) {
				Log.out(p.name + " started chasing.");
				Runtime.setImage(Runtime.actionIcons[p.survivorId], "action_chase.gif");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], true);
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
				try {
					//Runtime.toggleNode(Runtime.overlay.actionIcons[p.survivorId], true);
					//Runtime.setImage(Runtime.overlay.actionIcons[p.survivorId], "action_chase.png");
				} catch (Exception e) {
					Log.out("Failed to set chase icon in overlay");
					StackTraceElement[] elements = e.getStackTrace();
		            for (int iterator=1; iterator<=elements.length; iterator++)  {
		               Log.out("Class Name:"+elements[iterator-1].getClassName()+" Method Name:"+elements[iterator-1].getMethodName()+" Line Number:"+elements[iterator-1].getLineNumber());
		        	}
				}
			}
		}
		
		if (Search.contains(line, "is not in chase anymore.")) {
			Player p = findSurvivor(line);
			if (p != null) {
				Log.out(p.name + " stopped chasing.");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], false);
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
				Runtime.updateOverlay();
			}
		}
		// Opening Exit Gate
		if ((Search.contains(line, Search.searchSurvivorExitEscape)) && Search.contains(line, Search.interactEnter)) {
			Player p = findPlayer(line);
			if (p != null) {
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
				Runtime.setImage(Runtime.actionIcons[p.survivorId], "action_exitgate.png");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], true);
				Log.out(p.name + " started opening exit gate");
				Runtime.updateOverlay();
			}
		}
		if ((Search.contains(line, Search.searchSurvivorExitEscape)) && Search.contains(line, Search.interactExit)) {
			Player p = findPlayer(line);
			if (p != null) {
				Log.out(p.name + " stopped opening exit gate");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], false);
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
				Runtime.updateOverlay();
			}
		}
		// Cleansing Totem
		if ((Search.contains(line, "[CleanseTotem]")) && Search.contains(line, Search.interactEnter)) {
			Player p = findPlayer(line);
			if (p != null) {
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
				Runtime.setImage(Runtime.actionIcons[p.survivorId], "totem.png");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], true);
				Log.out(p.name + " started cleansing a totem");
				Runtime.updateOverlay();
			}
		}
		if ((Search.contains(line, "[CleanseTotem]")) && Search.contains(line, Search.interactExit)) {
			Player p = findPlayer(line);
			if (p != null) {
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], false);
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
				Log.out(p.name + " stopped cleansing a totem");
				Runtime.updateOverlay();
			}
		}
	}
	
	public void searchPlayers(String line, int lineCount) {

		if (Search.contains(line, Search.interactEnter)) {
			if (findPlayer(line).name.equals("error") && Search.contains(line, Search.interactEnter) && Search.contains(line, "[BP_Camper") && Search.contains(line, "character_c")) {
				String name = "";
				String sname = "";
				String kind = slaveOrMaster(line);
				name = line.substring(0, Search.getIndex(line, kind)-1).trim();
				name = name.substring(Search.getLastIndex(name, "-")+1, name.length()).trim();
				Log.out(line);
				sname = line.substring(Search.getIndex(line, "[BP_Camper"), line.length());
				sname = sname.substring(4, Search.getIndex(sname, "-"));
				if (!playerExists(name) && getSurvivorCount() < 4) {
					Player surv = new Player(name, false);
					surv.isSurvivor = true;
					surv.survivorName = sname.trim();
					players.add(surv);
					surv.survivorId = getSurvivorCount()-1;
					Runtime.toggleNode(Runtime.survivorAction[surv.survivorId], true);
					Runtime.toggleNode(Runtime.totalHooks[surv.survivorId], true);
					Runtime.setText(Runtime.survivorNames[surv.survivorId], name);
					Log.out(lineCount + ": Player Survivor-Role " + (surv.survivorId) + " detected: " + surv.name + " / " + sname);
					Runtime.updateOverlay();
				}
			}
		} else if (Search.contains(line, Search.searchSurvivorNamePart1) && Search.contains(line, Search.searchSurvivorNamePart2) || Search.contains(line, Search.searchPlayerName)) {
			/*String kind = slaveOrMaster(line);
			if (kind != "" || Search.contains(line, Search.searchPlayerName)) {
				String name = "";
				String sname = "";
				if (findPlayer(line).name.equals("")) {
					return;
				}
				if (Search.contains(line, Search.searchPlayerName)) {
					name = line.substring(Search.getIndex(line, searchPlayerName)+12, Search.getIndex(line, "isLocalPlayer")).trim();
				} else {
					name = line.substring(Search.getIndex(line, Search.searchSurvivorNamePart1), Search.getIndex(line, kind));
					name = name.substring(Search.getIndex(name, "-") + 2, name.length() - 1).trim();
				}
				String detect = "InteractionHandler: Verbose: [";
				sname = line.substring(Search.getIndex(line, detect)+detect.length(), line.length());
				sname = sname.substring(0, Search.getIndex(sname, "-"));
				if (playerExists(name) == false && name != null && getSurvivorCount() < 4) {
					Player surv = new Player(name, false);
					surv.isSurvivor = true;
					surv.survivorName = sname.trim();
					players.add(surv);
					surv.survivorId = getSurvivorCount()-1;
					Runtime.toggleNode(Runtime.survivorAction[surv.survivorId], true);
					Runtime.toggleNode(Runtime.totalHooks[surv.survivorId], true);
					if (surv.survivorId >= 0) {
						if (name.length() > 15) {
							Runtime.setText(Runtime.survivorNames[surv.survivorId], name.substring(0, 15));
						} else {
							Runtime.setText(Runtime.survivorNames[surv.survivorId], name);
						}
					}
					Log.out(lineCount + ": Player Survivor-Role " + (getSurvivorCount()-1) + " detected: " + surv.name);
				}
			}*/
		}	else if (Search.contains(line, Search.searchKillerName)) {
			try {
				String kind = slaveOrMaster(line);
				String name = line.substring(Search.getIndex(line, Search.searchKillerName), Search.getIndex(line, kind));
				name = name.substring(Search.getIndex(name, "-") + 2, name.length() - 1).trim();
				if (!playerExists(name)) {
					Player surv = new Player(name, true);
					players.add(surv);
					Runtime.setText(Runtime.killerPlayerName, name + "'s  Perks: ");
					Log.out("Player Killer-Role detected: " + surv.name);
					Runtime.updateOverlay();
				}
			} catch (StringIndexOutOfBoundsException e) {
				Log.out("Couldn't get playername!");
				StackTraceElement[] elements = e.getStackTrace();
	            for (int iterator=1; iterator<=elements.length; iterator++)  {
	               Log.out("Class Name:"+elements[iterator-1].getClassName()+" Method Name:"+elements[iterator-1].getMethodName()+" Line Number:"+elements[iterator-1].getLineNumber());
	        	}
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
			if (p.name.equals(name)) {
				return p;
			}
		}
		return null;
	}
	
	public int getSurvivorCount() {
		int count = 0;
		for (int i=0; i < players.size(); i++) {
			if (players.get(i).isSurvivor) {
				count++;
			}
		}
		return count;
	}

	public String slaveOrMaster(String line) {
		String kind = "";
		if (Search.contains(line, "[CLIENT MASTER]")) {
			kind = "[CLIENT MASTER]";
		}
		if (Search.contains(line, "[CLIENT SLAVE]")) {
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
		if (Search.contains(line, Search.bloodPoints)) {
			try {
				String _pointsIndex = "BloodPoints:";
				String _points = line.substring(Search.getIndex(line, _pointsIndex) + _pointsIndex.length(), Search.getIndex(line, "FearTokens")).trim();
				totalBloodPoints += Integer.parseInt(_points);
				Runtime.totalBloodpoints.setText(totalBloodPoints+"");
				Log.out("Total earned bloodpoints: " + totalBloodPoints);
			} catch (NumberFormatException e) {
				Log.out("Couldn't parse bloodpoints!");
			}
		}
	}

	public static String getTimestamp(String line) {
		return line.substring(Search.getIndex(line,"[")+1, Search.getIndex(line, "]"));
	}
	
	public static Date getDateFromTimestamp(String time) {
		try {
			return new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss:SSS").parse(time);
		} catch (ParseException e) {
			Log.out("Couldn't parse date");
		}
		return null;
	}
	
	public void searchPallets(String line) {
		if ((Search.contains(line, Search.palletPullDownR) || Search.contains(line, Search.palletPullDownL)
				|| Search.contains(line, Search.palletPullDownRunningR) || Search.contains(line, Search.palletPullDownRunningL))
				&& Search.contains(line, Search.interactExit)) {
			totalPalletPullDowns++;
			//thrownPallets++;++
			Log.out("Pallet thrown by " + findPlayer(line).name);
		}
		if (Search.contains(line, Search.palletKillerStun) && Search.contains(line, Search.interactEnter)) {
			totalPalletStuns++;
			palletStunActive = true;
			Log.out("Killer stunned by Survivors: " + totalPalletStuns);
		}
		if (Search.contains(line, Search.vaultGeneration)) {
			maxSpawnedWindows++;
			Runtime.setText(Runtime.totalVaults, maxSpawnedWindows + "");
			if (Runtime.overlay != null) {
				Runtime.setText(Runtime.overlay.vaultText, Runtime.totalVaults.getText());
			}
			Log.out("Window Vault_" + maxSpawnedWindows + " spawn detected. ");
		}
		if (palletStunActive && Search.contains(line, Search.palletPullDownShowUI)) {
			hudActive = true;
		}
		if (palletStunActive && Search.contains(line, Search.palletKillerStun) && Search.contains(line, Search.interactExit)) {
			palletStunActive = false;
		}
		if (Search.contains(line, Search.palletDestroyed) || Search.contains(line, Search.palletPowerDestroyed)) {
			totalDestroyedPallets++;
			normalDestroyedPallets++;
			if (totalDestroyedPallets % 2 == 0 && totalDestroyedPallets > 0) {
				spiritActive++;
			}
			// When Spirit Fury doesnt work correctly
			if (totalDestroyedPallets > 0 && totalDestroyedPallets > maxSpawnedPallets + 1) {
				totalDestroyedPallets = normalDestroyedPallets;
			}
			Runtime.setText(Runtime.destroyedPallets, totalDestroyedPallets + "/" + (maxSpawnedPallets + 1));
			if (Runtime.overlay != null) {
				Runtime.setText(Runtime.overlay.palletText, Runtime.destroyedPallets.getText());
			}
			Log.out("Pallet destroyed by Killer. Total destroyed: " + totalDestroyedPallets);
			Log.out("Destroyed Pallets without Spirit Fury: " + normalDestroyedPallets);
			Log.out("Current used pallets left: " + (thrownPallets-1));
		}
		if (Search.contains(line, Search.palletGeneration)) {
			maxSpawnedPallets = Integer
					.parseInt(line.substring(Search.getIndex(line, Search.palletGeneration) - 2, Search.getIndex(line, Search.palletGeneration))
							.replace("_", "").trim());
			Runtime.setText(Runtime.destroyedPallets, totalDestroyedPallets + "/" + (maxSpawnedPallets + 1));
			if (Runtime.overlay != null) {
				Runtime.setText(Runtime.overlay.palletText, Runtime.destroyedPallets.getText());
			}
			Log.out("Pallet_" + maxSpawnedWindows + " spawned detected. ");
		}
	}

	public Player findPlayer(String line) {
		for (Player p : players) {
			if (Search.contains(line, p.name)) {
				return p;
			}
		}
		return new Player("error");
	}
	
	public Player findSurvivor(String line) {
		for (Player p : players) {
			if (p.isSurvivor && Search.contains(line, p.survivorName)) {
				return p;
			}
		}
		return new Player("error");
	}
	
	public void searchKiller(String line) {
		/*if (Search.contains(line, Search.killer)) {
			// System.out.println(line);
			int index = Search.getIndex(line, Search.killer);
			line = line.substring(index, line.length());
			line = line.replace(Search.killer, "");
			currentKillerId = Integer.parseInt(line.substring(0, 2));
		}*/
		if (Search.contains(line, "LogCustomization: -->")) {
			int id = getKillerByCosmetic(line);
			if (id > 0) {
				currentKillerId = id;
			}
		}
	}

	public void searchOfferings(String line) {
		boolean offeringDetected = false;
		if (Search.contains(line, Search.ebonyMori)) {
			Log.out("Killer Offering detected: Ebony Mori");
			Runtime.setImage(Runtime.killerOffering, "ebony_mori.png");
			offeringDetected = true;
		}
		if (Search.contains(line, Search.ivoryMori)) {
			Log.out("Killer Offering detected: Ivory Mori");
			Runtime.setImage(Runtime.killerOffering, "ivory_mori.png");
			offeringDetected = true;
		}
		if (Search.contains(line, Search.cypressMori)) {
			Log.out("Killer Offering detected: Cypress Mori");
			Runtime.setImage(Runtime.killerOffering, "cypress_mori.png");
			offeringDetected = true;
		}
		if (Search.contains(line, Search.shroudSeperation)) {
			Log.out("Killer Offering detected: Shroud Of Seperation");
			Runtime.setImage(Runtime.killerOffering, "shroud_of_separation.png");
			offeringDetected = true;
		}
		if (offeringDetected) {
			Runtime.toggleImage(Runtime.killerOffering, true);
			Runtime.toggleNode(Runtime.offeringTitle, true);
			// Main.rotateXImage(Main.killerOffering, 90, 180, 2000, 100);
		}
	}

	public void searchSurvivorStates(String line, int numb) {
		if (line != null) {
			if (Search.contains(line, Search.camperPickUp)) {
				camperPickedUpLine = numb;
			}
			if (Search.contains(line, Search.camperDrop)) {
				camperDropLine = numb;
			}
			if (Search.contains(line, Search.camperStunDrop)) {
				camperDropLine = numb;
			}
			if (Search.contains(line, Search.camperEnterHook)) {
				camperEnterHookLine = numb;
			}
		}
	}

	public void searchSkillCheck(String line, int numb, BufferedReader reader) {
		if (Search.contains(line, Search.startSkillCheck)) {
			startSkillCheckCount++;
			skillCheckStartLine = numb;
			skillCheckActive = true;
			if (numb >= totalLines && Runtime.config.performSkillchecks) {
				skillCheck = new Thread(new SkillCheck(reader, line) {
					@Override
					public void run() {}
				});
				skillCheck.start();
			}
		}
		if (Search.contains(line, Search.endSkillCheck)) {
			skillCheckActive = false;
			hideSkillCheckCount++;
			if (skillCheck != null) {
				skillCheck.interrupt();
				skillCheck = null;
			}
		}
	}

	public void searchKillerPerks(String line, int lineCount) {
		if (line != null) {
			if (Search.contains(line, Search.interactEnter)) {
				lastInteractionEnterLine = line;
			}
			if (Search.contains(line, Search.interactEnter)) {
				lastInteractionEnterLine = line;
			}
			if (Search.contains(line, Search.ruin)) {
				setPerk("ruin");
			}
			if (Search.contains(line, Search.makeYourChoice)) {
				setPerk("makeYourChoice");
			}
			if (Search.contains(line, Search.noed)) {
				setPerk("noOneEscapesDeath");
			}
			if (Search.contains(line, Search.overwhelmingPresence)) {
				setPerk("overwhelmingPresence");
			}
			if (Search.contains(line, Search.lullaby)) {
				setPerk("HuntressLullaby");
			}
			if (Search.contains(line, Search.retribution)) {
				setPerk("hexRetribution");
			}
			if (Search.contains(line, Search.hauntedGrounds)) {
				setPerk("hauntedGround");
			}
			if (Search.contains(line, Search.devourHope)) {
				setPerk("devourHope");
			}
			if (Search.contains(line, Search.thirdSeal)) {
				setPerk("theThirdSeal");
			}
			if (Search.contains(line, Search.nurseCalling)) {
				setPerk("nursesCalling");
			}
			if (Search.contains(line, Search.bitterMurmur)) {
				setPerk("bitterMurmur");
			}
			if (Search.contains(line, Search.thrillOfTheHunt)) {
				setPerk("thrillOfTheHunt");
			}
			if (Search.contains(line, Search.bloodWarden)) {
				setPerk("bloodWarden");
			}
			if (Search.contains(line, Search.darkDevotion)) {
				setPerk("darkDevotion");
			}
			if (Search.contains(line, Search.coulrophobia)) {
				setPerk("coulrophobia");
			}
			if (Search.contains(line, Search.dyingLight)) {
				setPerk("dyingLight");
			}
			if (Search.contains(line, Search.franklinsDemise)) {
				// setPerk("franklinsLoss");
			}
			if (Search.contains(line, Search.deadManSwitch)) {
				setPerk("deadManSwitch");
			}
			if (Search.contains(line, Search.ironMaiden)) {
				setPerk("ironMaiden");
			}
			if (Search.contains(line, Search.bbqAndChilli)) {
				setPerk("BBQAndChili");
			}
			if (Search.contains(line, Search.mindBreaker)) {
				setPerk("mindBreaker");
			}
			if (Search.contains(line, Search.rancor)) {
				setPerk("hatred");
			}
			if (Search.contains(line, Search.rememberMe)) {
				setPerk("rememberMe");
			}
			if (Search.contains(line, Search.thanatophobia)) {
				setPerk("thatanophobia");
			}
			if (Search.contains(line, Search.territorialImperative)) {
				setPerk("territorialImperative");
			}
			if (Search.contains(line, Search.tinkerer)) {
				setPerk("tinkerer");
			}
			if (Search.contains(line, Search.beastOfPrey)) {
				setPerk("BeastOfPrey");
			}
			if (Search.contains(line, Search.hangMan)) {
				setPerk("hangmansTrick");
			}
			if (Search.contains(line, Search.allEars)) {
				setPerk("imAllEars");
			}
			if (Search.contains(line, Search.knockOut)) {
				setPerk("knockOut");
			}
			if (Search.contains(line, Search.fireUpVaultSpeed) || Search.contains(line, Search.fireUpBreakPalletSpeed)
					|| Search.contains(line, Search.fireUpDamageGenSpeed)) {
				// setPerk("fireUp"); NOT WORKING
			}
			if (Search.contains(line, Search.bamboozleVaultSpeed)) {
				setPerk("bamboozle");
			}
			if (Search.contains(line, Search.stridor)) {
				setPerk("stridor");
			}
			if (Search.contains(line, Search.sloppyButcher)) {
				setPerk("sloppyButcher");
			}
			if (Search.contains(line, Search.unnvervingPresence)) {
				setPerk("unnervingPresence");
			}
			/*if (Search.contains(line, Search.enduringPalletStunSpeed) && Search.contains(lastInteractionEnterLine, Search.palletStun)) {
				setPerk("enduring");
			}*/
			if (Search.contains(line, Search.gameStartCamAnimationEnd)) {
				camFadeOutLine = lineCount;
			}
			if (Search.contains(line, Search.genBlocked)) {
				// Get Line for last blocked gen
				lastGenBlockLine = lineCount;
			}
			if (Search.contains(line, Search.brutalStrengthDamageGenSpeed) || Search.contains(line, Search.brutalStrengthPalletSpeed)) {
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

	public void searchSurvivorPerks(String line) {
		if (Search.contains(line, Search.obsessionState)) {
			obsessionStates.add(line);
		}
	}
	
	public void applyObsessionState() {
		if (!obsessionStates.isEmpty()) {
			for (String s : obsessionStates) {
				for (int i=0; i < players.size(); i++) {
					if (Search.contains(s, players.get(i).name)) {
						try {
							players.get(i).obessionState = Integer.parseInt(s.substring(Search.getIndex(s, Search.obsessionState)+Search.obsessionState.length(), Search.getIndex(s, Search.obsessionState)+Search.obsessionState.length()+1));
							Log.out("Obsession State of " + players.get(i).name + " : " + players.get(i).obessionState);
							//Runtime.setText(Runtime.obsessionStatesText[i], "Dstrike Chance: " + players.get(i).obessionState);
							//Runtime.toggleNode(Runtime.obsessionStatesText[i], true);
							obsessionStates.remove(s);
							return;
						} catch (Exception e) {
							Log.out("Failed to apply obsession state.");
						}
					}
				}
			}
		}
	}
	
	public void checkMatchStartTime(String line) {
		if (!matchHasStarted && Search.contains(line, Search.gameStartCamAnimationEnd) && line.length() > 24) {
			try {
				matchStartTime = GameReader.getDateFromTimestamp(getTimestamp(line));
				matchHasStarted = true;
			} catch (Exception e) {
				Log.out("Couldn't parse match start time!");
			}
		}
	}

	public void checkSurvivorHookPhases(String line, int lineCount) {
		if (Search.contains(line, Search.dStrike)) {
			Player p = findPlayer(line);
			if (p != null) {
				Log.out(p.name + " has Decisive Strike!");
			}
		}
		if (Search.contains(line, "equested to Play: HookCamper") && Search.contains(line, "AnimTag_Carry") && Search.contains(line, "AnimLeader: BP_Camper")) {
			Player p = findSurvivor(line);
			if (p != null) {
				p.hooks++;
				Runtime.setText(Runtime.totalHooks[p.survivorId], "Hookstate: " + p.hooks);
				if (Runtime.overlay != null) {
					Runtime.setText(Runtime.overlay.hookTexts[p.survivorId], Runtime.totalHooks[p.survivorId].getText());
				}
				Log.out(lineCount + ": Survivor " + p.name + " hooks.");
				p.isHooked = true;
				p.dstrikeTime = 0;
				Runtime.toggleNode(Runtime.dstrikeBars[p.survivorId], false);
				Runtime.setImage(Runtime.actionIcons[p.survivorId], "hook.png");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], true);
				Runtime.toggleNode(Runtime.survivorAction[p.survivorId], false);
				if (p.hooks >= 3) {
					p.hooks = 3;
					p.isDead = true;
					Runtime.updateOverlay();
					Platform.runLater(()-> {
						Runtime.setImage(Runtime.deadSymbols[p.survivorId], "dead_symbol.png");
						Runtime.deadSymbols[p.survivorId].setVisible(true);
						Runtime.deadSymbols[p.survivorId].setManaged(true);
						Log.out("Player " + p.name + "_" + p.survivorId + " has been sacrificed");
					});
				}
			}
		}
		if (Search.contains(line, "Interaction: Verbose: [SacrificeSurvivor]") && Search.contains(line, Search.interactExit)) {
			Player p = findSurvivor(line);
			if (p != null) {
				p.hooks = 3;
				p.isDead = true;
				Runtime.updateOverlay();
				Platform.runLater(()-> {
					Runtime.setImage(Runtime.deadSymbols[p.survivorId], "dead_symbol.png");
					Runtime.deadSymbols[p.survivorId].setVisible(true);
					Runtime.deadSymbols[p.survivorId].setManaged(true);
					Log.out("Player " + p.name + "_" + p.survivorId + " has been sacrificed");
				});
			}
		}
		if (Search.contains(line, Search.camperExitHook)) {
			Player p = findSurvivor(line);
			if (!p.name.equals("error")) {
				Log.out("Player " + p.name + "_" + p.survivorId + " has been unhooked");
				p.dstrikeTime = 60;
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], false);
				Runtime.updateOverlay();
			}
		}
		
	}
	
	public void updateTime() {
		if (playerIsIngame && matchHasStarted && matchStartTime != null) {
			currentTime = new Date();
			currentMatchDuration = (int)((currentTime.getTime() - matchStartTime.getTime())/1000-timeDifference);
			String time = secondsToTime(currentMatchDuration);
			Runtime.setText(Runtime.matchStatusTime, time);
			if (Runtime.overlay != null) {
				Runtime.setText(Runtime.overlay.time, Runtime.matchStatusTime.getText());
			}
			for (Player p : players) {
				if (p.borrowedTime > 0) {
					p.borrowedTime -= 0.5;
					Runtime.setProgress(Runtime.survivorBars[p.survivorId], p.borrowedTime/15.0);
					Runtime.toggleNode(Runtime.survivorBars[p.survivorId], true);
					if (Runtime.overlay != null) {
						Runtime.setProgress(Runtime.overlay.borrowBars[p.survivorId], Runtime.survivorBars[p.survivorId].getProgress());
						Runtime.toggleNode(Runtime.overlay.borrowBars[p.survivorId], true);
					}
					if (p.borrowedTime <= 0) {
						Runtime.toggleNode(Runtime.survivorBars[p.survivorId], false);
						if (Runtime.overlay != null) {
							Runtime.toggleNode(Runtime.overlay.borrowBars[p.survivorId], false);
						}
					}
				}
				if (p.dstrikeTime > 0) {
					p.dstrikeTime -= 0.5;
					Runtime.setProgress(Runtime.dstrikeBars[p.survivorId], p.dstrikeTime/60);
					Runtime.toggleNode(Runtime.dstrikeBars[p.survivorId], true);
					if (Runtime.overlay != null) {
						Runtime.toggleNode(Runtime.overlay.dstrikeBars[p.survivorId], true);
						Runtime.setProgress(Runtime.overlay.dstrikeBars[p.survivorId], Runtime.dstrikeBars[p.survivorId].getProgress());
					}
					if (p.dstrikeTime <= 0) {
						Runtime.toggleNode(Runtime.dstrikeBars[p.survivorId], false);
						if (Runtime.overlay != null) {
							Runtime.toggleNode(Runtime.overlay.dstrikeBars[p.survivorId], false);
						}
					}
				}
			}
		} else if (playerIsIngame && !matchHasStarted) {
			Runtime.setText(Runtime.matchStatusTime, Runtime.matchStatusTime.getText()+".");
			if (Runtime.matchStatusTime.getText().length() > 10) {
				Runtime.setText(Runtime.matchStatusTime, "Loading");
			}
			if (Runtime.overlay != null) {
				Runtime.setText(Runtime.overlay.time, Runtime.matchStatusTime.getText());
			}
		} else {
			Runtime.setText(Runtime.matchStatusTime, "Lobby");
			if (Runtime.overlay != null) {
				Runtime.setText(Runtime.overlay.time, Runtime.matchStatusTime.getText());
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
		if (line != null && Search.contains(line, Search.mapTheme)) {
			String[] strings = line.split(":");
			mapTheme = strings[4].replace("Map", "").trim();
			mapName = strings[5].replace("Generation Seed", "");
			mapName = mapName.substring(Search.getIndex(mapName, "_") + 1, mapName.length());
			mapName = getMapName(mapName);
			// System.out.println(mapName + " : " + mapTheme);
			Runtime.setText(Runtime.mapName, mapName + " (" + mapTheme + ")");
			Runtime.toggleNode(Runtime.mapTitle, true);
			Runtime.toggleNode(Runtime.mapName, true);
			Runtime.updateOverlay();
			Log.out("Currently playing on: " + "(" + mapName + ") " + "(" + mapTheme + ")");
		}
	}

	public void searchTotems(String line) {
		if (Search.contains(line, Search.totem) && Search.contains(line, Search.interactEnter)) {
			cleanseTotemStartCount++;
		} else if (Search.contains(line, Search.totem) && Search.contains(line, Search.interactExit)) {
			cleanseTotemEndCount++;
		} else if (Search.contains(line, Search.interactFinished) && Search.contains(line, Search.totem)) {
			totalCleansedTotems++;
			Runtime.updateOverlay();
		}
		DropShadow shadow = new DropShadow();
		shadow.setWidth(0);
		shadow.setHeight(0);
		shadow.setBlurType(BlurType.ONE_PASS_BOX);
		if (cleanseTotemStartCount > cleanseTotemEndCount) {
			shadow.setColor(Color.rgb(255, 0, 255));
			// Main.setShadow(Main.totemPic, shadow);
		} else {
			shadow.setColor(Color.rgb(100, 100, 100));
			// Main.setShadow(Main.totemPic, shadow);
		}
		Runtime.setText(Runtime.totalTotems, totalCleansedTotems + "/5");
	}

	public void setPerk(String perk) {
		for (int i = 0; i < killerPerks.length; i++) {
			if (killerPerks[i] == perk) {
				return;
			}
		}
		for (int i = 0; i < killerPerks.length; i++) {
			if (killerPerks[i].equals("") || killerPerks[i] == null) {
				try {
					killerPerks[i] = perk;
					String image = "iconPerks_" + perk + ".png";
					Runtime.setImage(Runtime.killerPerks[i], image);
					Runtime.toggleImage(Runtime.killerPerks[i], true);
					Runtime.toggleImage(Runtime.killerPerkFrames[i], true);
					Runtime.playPerkAnimation(i);
					Runtime.updateOverlay();
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