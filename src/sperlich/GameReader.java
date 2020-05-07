package sperlich;

import javafx.application.Platform;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.nio.charset.StandardCharsets;
import javax.swing.Timer;

public class GameReader extends Thread {
	String searchKiller = "GameFlow: (BP_Menu_Slasher";
	String searchGameStarted = "UDBDGameInstance::StartOfferingSequence";
	String searchGameLeft = "LogAIModule: Creating AISystem for world OfflineLobby";
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
	String survivorDead = "[CleanInteractionArray AFTER]";
	String closetEnter = "Interaction: Verbose: [ClosetHideEnterSneak]";
	String closetExit  = "Interaction: Verbose: [ClosetHideExitSneak]";
	String closetEnterRushed = "Interaction: Verbose: [ClosetHideEnterRushed]";
	String closetExitRushed = "Interaction: Verbose: [ClosetHideExitRushed]";
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
	String searchPlayerName = " playerName:";
	String playerEscaped = "[EscapeMap]";
	String lastInteractionEnterLine;
	int camperPickedUpLine;
	int camperDropLine;
	int camperEnterHookLine;
	String[] killerPerks = new String[4];
	public static boolean playerIsIngame;
	public static boolean skillCheckActive;
	public static boolean skillCheckActionPerformed;
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
	public Timer skipTimer;
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
	public static boolean loadingFinished;
	public ArrayList<Player> players = new ArrayList<Player>();
	public int lastEndLine;
	public static boolean skip;
			
	String path = System.getProperty("user.home").replaceAll("\\\\", "/")+"/AppData/Local/DeadByDaylight/Saved/Logs/DeadByDaylight.log";

	public void initialize() throws InterruptedException {
		Log.out("GameReader successfully started.");
		sleep(2000);
		retryTimer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					initialize();
				} catch (InterruptedException e) {
				}
			}
		});
		retryTimer.setInitialDelay(1000);
		retryTimer.start();
		retryTimer.stop();
		Log.out("Searching Logfile At: " + path);
        try {
        	BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
	    	fullScan(reader);
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
			readLog(reader);
		} catch (Exception e) {
			Log.out("An unexpected error occurred!");
			Runtime.setText(Runtime.killerName, "Unexpected ERROR occurred");
			Runtime.setImage(Runtime.killerPic, "unknown.png");
			resetGame();
			retryTimer.start();
			currentKillerId = -1;
			StackTraceElement[] elements = e.getStackTrace();  
            for (int iterator=1; iterator<=elements.length; iterator++)  {
               System.out.println("Class Name:"+elements[iterator-1].getClassName()+" Method Name:"+elements[iterator-1].getMethodName()+" Line Number:"+elements[iterator-1].getLineNumber());
        	}
            throw new ExceptionHandler("Unexpected error occurred");
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
				if (line.indexOf(searchGameStarted) >= 0) {
					matchStarts++;
					lastLine = lineCount;
				}
				if (line.indexOf(searchGameLeft) >= 0) {
					matchEnds++;
					lastLine = lineCount;
				}
				if (line.indexOf(gameClosed) >= 0) {
					gameIsClosed = true;
					lastLine = lineCount;
				}
				searchKiller(line);
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
		Log.out("Successfully scanned " + lineCount + " lines.");
	}

	public void readLog(BufferedReader reader) throws IOException, InterruptedException {
		Log.out("Current match status: " + playerIsIngame);
		String line = "";
		int lineCount = 0;
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
					line = line.replaceAll("[^\\u0000-\\uFFFF]", "");
					scanLine(line, lineCount);
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
				Thread.sleep(1);
			}
		}
	}

	public void scanLine(String line, int lineCount) throws IOException {
		checkIfIngame(line, lineCount);
		matchTime(line, lineCount);
		searchKiller(line);
		setKiller(currentKillerId);
		if (playerIsIngame) {
			searchMap(line);
			searchSurvivorStates(line, lineCount);
			searchTotems(line);
			searchKillerPerks(line, lineCount);
			searchOfferings(line);
			searchPallets(line, lineCount);
			searchBloodPoints(line);
			searchPlayers(line, lineCount);
			checkSurvivorHookPhases(line, lineCount);
			searchSurvivorActions(line);
		}
		if (gameIsClosed) {
			Log.out("Game is closed");
			currentKillerId = -1;
			setKiller(-1);
			Runtime.setText(Runtime.killerName, "Game closed");
		}
	}

	public void setKiller(int id) {
		if (oldKillerId != currentKillerId) {
			Log.out("Killer detected: " + GetKillerById(id));
			Runtime.setKillerPic(id);
			Runtime.setText(Runtime.killerName, GetKillerById(id));
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
		if (line.indexOf(searchGameLeft) >= 0) {
			lastEndLine = currentLine;
			endGame();
		}
		if (line.indexOf(searchGameEnded) >= 0) {
			timer.stop();
		}
	}

	public void startGame(int lineCount) {
		Log.out("Match start detected.");
		resetGame();
		Runtime.totemsBox.setVisible(true);
		Runtime.palletsBox.setVisible(true);
		Runtime.vaultsBox.setVisible(true);
		Runtime.survivorsTitle.setVisible(true);
		Runtime.mapBox.setVisible(true);
		playerIsIngame = true;
		startLineNumber = lineCount;
		Runtime.setText(Runtime.matchStatusTime, "Loading");
		DropShadow shadow = new DropShadow();
		shadow.setColor(Color.rgb(150, 255, 10));
		Runtime.setShadow(Runtime.killerPic, shadow);
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
		Runtime.setText(Runtime.killerPlayerName, "Detected Perks: ");
		killerPerks = new String[4];
		for (int i = 0; i < Runtime.survivorNames.length; i++) {
			Runtime.setText(Runtime.survivorNames[i], "");
			Runtime.setText(Runtime.totalHooks[i], "Hookstate: 0");
			Runtime.toggleNode(Runtime.survivorAction[i], false);
			Runtime.toggleNode(Runtime.totalHooks[i], false);
			Runtime.toggleNode(Runtime.survivorBars[i], false);
			Runtime.setProgress(Runtime.survivorBars[i], 0);
			Runtime.deadSymbols[i].setVisible(false);
			Runtime.setText(Runtime.survivorAction[i], "");
			Runtime.setImage(Runtime.actionIcons[i], null);
		}
		players = new ArrayList<>();
		matchTime = 0;
		matchStartTime = 0;
		normalDestroyedPallets = 0;
		Runtime.setText(Runtime.matchStatusTime, "LOBBY");
		Runtime.setText(Runtime.destroyedPallets, totalDestroyedPallets + "/0");
		Runtime.setText(Runtime.totalTotems, "0/5");
		Runtime.setText(Runtime.totalVaults, "0/0");
		Runtime.setImage(Runtime.killerOffering, null);
		Runtime.toggleImage(Runtime.killerOffering, false);
		Runtime.toggleNode(Runtime.offeringTitle, false);
		Runtime.toggleNode(Runtime.mapName, false);
		Runtime.toggleNode(Runtime.mapTitle, false);
		searchTotems("");
		for (int i = 0; i < Runtime.killerPerks.length; i++) {
			if (Runtime.killerPerks[i] != null && Runtime.killerPerkFrames[i] != null) {
				String icon = "perk_default.png";
				Runtime.setImage(Runtime.killerPerks[i], icon);
				Runtime.toggleImage(Runtime.killerPerks[i], false);
				Runtime.toggleImage(Runtime.killerPerkFrames[i], false);
			}
		}
		if (timer != null) {
			timer.stop();
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
	}

	public void searchSurvivorActions(String line) {
		if (line.indexOf(borrowedTime) >= 0 && line.indexOf(borrowedTimeInit) >= 0) {
			Player p = findPlayer(line);
			if (p != null && p.borrowedTime == 0) {
				p.borrowedTime = 15;
			} else if (p != null) {
				p.borrowedTime = 0;
			}
		}
		if (line.indexOf(playerEscaped) >= 0 && line.indexOf(interactionEnter) >= 0) {
			Player p = findPlayer(line);
			if (p != null && p.hooks < 3) {
				p.isEscaped = true;
				Platform.runLater(()-> {
					Runtime.setImage(Runtime.deadSymbols[p.survivorId], "escaped.png");
					Runtime.deadSymbols[p.survivorId].setVisible(true);
					Runtime.deadSymbols[p.survivorId].setManaged(true);
				});
				Log.out("Player " + p.name + "_" + p.survivorId + " has escaped!");
			}
		}
		// Hiding in Closet
		if ((line.indexOf(closetEnter) >= 0 || line.indexOf(closetEnterRushed) >= 0) && line.indexOf(interactionEnter) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
				Runtime.setImage(Runtime.actionIcons[p.survivorId], "action_locker.png");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], true);
				Log.out(p.name + " is entering locker");
			}
		}
		if ((line.indexOf(closetExit) >= 0 || line.indexOf(closetExitRushed) >= 0) && line.indexOf(interactionExit) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], false);
				Log.out(p.name + " is exiting locker");
			}
		}
		// Repairing
		if (line.indexOf(searchSurvivorRepair) >= 0 && line.indexOf(interactionEnter) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				p.isRepairing = true;
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
				Runtime.setImage(Runtime.actionIcons[p.survivorId], "action_repair.png");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], true);
				Log.out(p.name + " started repairing");
			}
		}
		if (line.indexOf(searchSurvivorRepair) >= 0 && line.indexOf(interactionExit) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				p.isRepairing = false;
				Log.out(p.name + " stopped repairing");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], false);
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
			}
		}
		// Heal other
		if (line.indexOf(searchSurvivorHealOther) >= 0 && line.indexOf(interactionEnter) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
				Runtime.setImage(Runtime.actionIcons[p.survivorId], "action_healother.png");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], true);
				Log.out(p.name + " started healing companion");
			}
		}
		if (line.indexOf(searchSurvivorHealOther) >= 0 && line.indexOf(interactionExit) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				Log.out(p.name + " stop healing companion");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], false);
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
			}
		}
		// Self healing with medkit
		if (line.indexOf(searchSuvivorSelfhealMedkit) >= 0 && line.indexOf(interactionEnter) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
				Runtime.setImage(Runtime.actionIcons[p.survivorId], "action_medkit.png");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], true);
				Log.out(p.name + " started healing with Medkit");
			}
		}
		if (line.indexOf(searchSuvivorSelfhealMedkit) >= 0 && line.indexOf(interactionExit) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				Log.out(p.name + " stopped healing with Medkit");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], false);
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
			}
		}
		// Searching Chest
		if (line.indexOf(searchSurvivorLooting) >= 0 && line.indexOf(interactionEnter) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				Runtime.setImage(Runtime.actionIcons[p.survivorId], "action_loot.png");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], true);
				Log.out(p.name + " started searching chest");
			}
		}
		if (line.indexOf(searchSurvivorLooting) >= 0 && line.indexOf(interactionExit) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				Log.out(p.name + " stopped searching chest");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], false);
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
			}
		}
		// Wiggling
		if (line.indexOf(searchSurvivorWiggle) >= 0 && line.indexOf(interactionEnter) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				Runtime.setText(Runtime.survivorAction[p.survivorId], "Action: wiggle");
				Log.out(p.name + " started wiggling");
			}
		}
		if (line.indexOf(searchSurvivorWiggle) >= 0 && line.indexOf(interactionExit) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				Log.out(p.name + " stopped wiggling");
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
			}
		}
		// Recovering
		if (line.indexOf(searchSurvivorRecover) >= 0 && line.indexOf(interactionEnter) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				Runtime.setText(Runtime.survivorAction[p.survivorId], "recover");
				Log.out(p.name + " started recovering");
			}
		}
		if (line.indexOf(searchSurvivorRecover) >= 0 && line.indexOf(interactionExit) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				Log.out(p.name + " stopped recovering");
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
			}
		}
		// Selfcare
		if (line.indexOf(searchSurvivorSelfcare) >= 0 && line.indexOf(interactionEnter) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
				Runtime.setImage(Runtime.actionIcons[p.survivorId], "action_selfcare.png");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], true);
				Log.out(p.name + " started selfcaring");
			}
		}
		if (line.indexOf(searchSurvivorSelfcare) >= 0 && line.indexOf(interactionExit) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				Log.out(p.name + " stopped selfcaring");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], false);
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
			}
		}
		// Chasing
		if (line.indexOf("is in chase.") >= 0) {
			Player p = findSurvivor(line);
			if (p != null) {
				Log.out(p.name + " started chasing.");
				Runtime.setImage(Runtime.actionIcons[p.survivorId], "action_chase.gif");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], true);
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
			}
		}
		
		if (line.indexOf("is not in chase anymore.") >= 0) {
			Player p = findSurvivor(line);
			if (p != null) {
				Log.out(p.name + " stopped chasing.");
				Runtime.toggleImage(Runtime.actionIcons[p.survivorId], false);
				Runtime.setText(Runtime.survivorAction[p.survivorId], "");
			}
		}
	}
	
	public void searchPlayers(String line, int lineCount) {

		if (line.indexOf(interactionEnter) >= 0) {
			if (findPlayer(line).name.equals("error") && findSurvivor(line).name.equals("error")) {
				if (line.indexOf(interactionEnter) >= 0 && findPlayer(line).name != "") {
					String name = "";
					String sname = "";
					String kind = slaveOrMaster(line);
					name = line.substring(0, line.indexOf(kind)-1).trim();
					name = name.substring(name.lastIndexOf("-")+1, name.length()).trim();
					sname = line.substring(line.indexOf("[BP_Camper"), line.length());
					sname = sname.substring(4, sname.indexOf("-"));
					
					if (!playerExists(name) && getSurvivorCount() < 4) {
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
						Log.out(lineCount + ": Player Survivor-Role " + (surv.survivorId) + " detected: " + surv.name + " / " + sname);
					}
				}
			}
		} else if (line.indexOf(searchSurvivorNamePart1) >= 0 && line.indexOf(searchSurvivorNamePart2) >= 0 || line.indexOf(searchPlayerName) >= 0) {
			/*String kind = slaveOrMaster(line);
			if (kind != "" || line.indexOf(searchPlayerName) >= 0) {
				String name = "";
				String sname = "";
				if (findPlayer(line).name.equals("")) {
					return;
				}
				if (line.indexOf(searchPlayerName) >= 0) {
					name = line.substring(line.indexOf(searchPlayerName)+12, line.indexOf("isLocalPlayer")).trim();
				} else {
					name = line.substring(line.indexOf(searchSurvivorNamePart1), line.indexOf(kind));
					name = name.substring(name.indexOf("-") + 2, name.length() - 1).trim();
				}
				String detect = "InteractionHandler: Verbose: [";
				sname = line.substring(line.indexOf(detect)+detect.length(), line.length());
				sname = sname.substring(0, sname.indexOf("-"));
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
		}	else if (line.indexOf(searchKillerName) >= 0) {
			try {
				String kind = slaveOrMaster(line);
				if (kind != "") {
					String name = line.substring(line.indexOf(searchKillerName), line.indexOf(kind));
					name = name.substring(name.indexOf("-") + 2, name.length() - 1).trim();
					if (playerExists(name) == false) {
						Player surv = new Player(name, true);
						players.add(surv);
						if (name.length() > 10) {
							Runtime.setText(Runtime.killerPlayerName, name.substring(0, 15) + "'s  Perks: ");
						} else {
							Runtime.setText(Runtime.killerPlayerName, name + "'s  Perks: ");
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
		for (int i=0; i < players.size(); i++) {
			if (players.get(i).isSurvivor) {
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
			//thrownPallets++;++
			Log.out("Pallet thrown by " + findPlayer(line).name);
		}
		if (line.indexOf(palletKillerStun) >= 0 && line.indexOf(interactionEnter) >= 0) {
			totalPalletStuns++;
			palletStunActive = true;
			Log.out("Killer stunned by Survivors: " + totalPalletStuns);
		}
		if (line.indexOf(searchWindowVaults) >= 0) {
			maxSpawnedWindows++;
			Runtime.setText(Runtime.totalVaults, maxSpawnedWindows + "");
			Log.out("Window Vault_" + maxSpawnedWindows + " spawn detected. ");
		}
		if (palletStunActive) {
			if (line.indexOf(palletPullDownShowUI) >= 0) {
				hudActive = true;
			}
		}
		if (palletStunActive && line.indexOf(palletKillerStun) >= 0 && line.indexOf(interactionExit) >= 0) {
			palletStunActive = false;
		}
		if (line.indexOf(palletDestroyed) >= 0 || line.indexOf(palletPowerDestroyed) >= 0) {
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
			Log.out("Pallet destroyed by Killer. Total destroyed: " + totalDestroyedPallets);
			Log.out("Destroyed Pallets without Spirit Fury: " + normalDestroyedPallets);
			Log.out("Current used pallets left: " + (thrownPallets-1));
		}
		if (line.indexOf(searchSpawnPallet) >= 0) {
			maxSpawnedPallets = Integer
					.parseInt(line.substring(line.indexOf(searchSpawnPallet) - 2, line.indexOf(searchSpawnPallet))
							.replace("_", "").trim());
			Runtime.setText(Runtime.destroyedPallets, totalDestroyedPallets + "/" + (maxSpawnedPallets + 1));
			Log.out("Pallet_" + maxSpawnedWindows + " spawned detected. ");
		}
	}

	public Player findPlayer(String line) {
		for (Player p : players) {
			if (line.indexOf(p.name) >= 0) {
				return p;
			}
		}
		return new Player("error");
	}
	
	public Player findSurvivor(String line) {
		for (Player p : players) {
			if (p.isSurvivor && line.indexOf(p.survivorName) >= 0) {
				return p;
			}
		}
		return new Player("error");
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
			Runtime.setImage(Runtime.killerOffering, "ebony_mori.png");
			offeringDetected = true;
		}
		if (line.indexOf(searchIvoryMori) >= 0) {
			Log.out("Killer Offering detected: Ivory Mori");
			Runtime.setImage(Runtime.killerOffering, "ivory_mori.png");
			offeringDetected = true;
		}
		if (line.indexOf(searchCypressMori) >= 0) {
			Log.out("Killer Offering detected: Cypress Mori");
			Runtime.setImage(Runtime.killerOffering, "cypress_mori.png");
			offeringDetected = true;
		}
		if (line.indexOf(searchShroudSeperation) >= 0) {
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
		//Main.skillCheckBar.setVisible(true);
		//Main.toggleText(Main.skillCheckText, true);
	}

	public void endSkillCheck() {
		skillAreaStart = 0;
		skillAreaLength = 0;
		skillBonusAreaLength = 0;
		skillCheckActive = false;
		//Main.skillCheckBar.setVisible(false);
		//Main.toggleText(Main.skillCheckText, false);
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
			//Main.skillCheckBar.setProgress(currentValue);
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
				int days = Integer.parseInt(line.substring(9, 11));
				int hours = Integer.parseInt(line.substring(12, 14));
				if (hours >= 24)
					hours = hours - 24;
				int minutes = Integer.parseInt(line.substring(15, 17));
				int seconds = Integer.parseInt(line.substring(18, 20));
				int duration = (days * 24 * 60 * 60) + (hours * 60 * 60) + (minutes * 60) + seconds + (60 * 60) * 2;
				matchStartTime = duration - 2;
			} catch (NumberFormatException e) {

			}
		}
	}

	public void checkSurvivorHookPhases(String line, int lineCount) {
		if (line.indexOf(dStrike) >= 0) {
			Player p = findPlayer(line);
			if (p != null) {
				Log.out(p.name + " has Decisive Strike!");
			}
		}
		if (line.indexOf("equested to Play: HookCamper") >= 0 && line.indexOf("AnimTag_Carry") >= 0 && line.indexOf("AnimLeader: BP_Camper") >= 0) {
			Player p = findSurvivor(line);
			if (p != null) {
				p.hooks++;
				Runtime.setText(Runtime.totalHooks[p.survivorId], "Hookstate: " + p.hooks);
				Log.out(lineCount + ": Survivor " + p.name + " hooks.");
				lockForTime(1);
				if (p.hooks >= 3) {
					p.hooks = 3;
					Platform.runLater(()-> {
						Runtime.setImage(Runtime.deadSymbols[p.survivorId], "dead_symbol.png");
						Runtime.deadSymbols[p.survivorId].setVisible(true);
						Runtime.deadSymbols[p.survivorId].setManaged(true);
						Log.out("Player " + p.name + "_" + p.survivorId + " has been sacrificed");
					});
				}
			}
		}
		if (line.indexOf("Interaction: Verbose: [SacrificeSurvivor]") >= 0 && line.indexOf(interactionExit) >= 0) {
			Player p = findSurvivor(line);
			if (p != null) {
				p.hooks = 3;
				Platform.runLater(()-> {
					Runtime.setImage(Runtime.deadSymbols[p.survivorId], "dead_symbol.png");
					Runtime.deadSymbols[p.survivorId].setVisible(true);
					Runtime.deadSymbols[p.survivorId].setManaged(true);
					Log.out("Player " + p.name + "_" + p.survivorId + " has been sacrificed");
				});
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
			Runtime.setText(Runtime.matchStatusTime, time);
			
			for (Player p : players) {
				if (p.borrowedTime > 0) {
					p.borrowedTime -= 0.5;
					Runtime.setProgress(Runtime.survivorBars[p.survivorId], p.borrowedTime/15.0);
					Runtime.toggleNode(Runtime.survivorBars[p.survivorId], true);
					if (p.borrowedTime == 0) {
						Runtime.toggleNode(Runtime.survivorBars[p.survivorId], false);
					}
				}
			}
		} else {
			Runtime.setText(Runtime.matchStatusTime, Runtime.matchStatusTime.getText()+".");
			if (Runtime.matchStatusTime.getText().length() > 10) {
				Runtime.setText(Runtime.matchStatusTime, "Loading");
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
				Runtime.setText(Runtime.mapName, mapName + " (" + mapTheme + ")");
				Runtime.toggleNode(Runtime.mapTitle, true);
				Runtime.toggleNode(Runtime.mapName, true);
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
			if (killerPerks[i] == "" || killerPerks[i] == null) {
				try {
					killerPerks[i] = perk;
					String image = "iconPerks_" + perk + ".png";
					Runtime.setImage(Runtime.killerPerks[i], image);
					Runtime.toggleImage(Runtime.killerPerks[i], true);
					Runtime.toggleImage(Runtime.killerPerkFrames[i], true);
					Runtime.playPerkAnimation(perk, i);
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

	public void lockForTime(int time) {
		skip = true;
		skipTimer = new Timer(time, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				skip = false;
			}
		});
		skipTimer.setInitialDelay(0);
		skipTimer.start();
	}
	
}