package sperlich;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.Timer;

public class GameReader extends Thread {

	/*public static boolean playerIsIngame;
	public static boolean skillCheckActive;
	public static boolean skillCheckActionPerformed;
	public static boolean noedActive;
	public static boolean ruinActive;
	public ArrayList<String> runningGameLog;
	String searchKiller = "BP_Menu_Slasher";
	String searchGameStarted = "UDBDGameInstance::StartOfferingSequence";
	String searchGameEnded = "LogAsyncLoad: Unloading library Game";
	String searchMapTheme = "ProceduralLevelGeneration: InitLevel:";
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
	String lastInteractionEnterLine = "";
	int camperPickedUpLine = 0;
	int camperDropLine = 0;
	int camperEnterHookLine = 0;
	String[] killerPerks = new String[4];
	public int foundGameStarts;
	public int foundGameEnds;
	public int startLineNumber;
	public int endLineNumber;
	public int totalLines;
	public int skillCheckStartLine;
	public int startSkillCheckCount;
	public int hideSkillCheckCount;
	public int cleanseTotemStartCount;
	public int cleanseTotemEndCount;
	public int totalCleansedTotems;
	public int camFadeOutLine;
	public int lastGenBlockLine;
	public int currentKillerId;
	public String valueLine;
	public double currentValue;
	public double skillAreaLength;
	public double skillAreaStart;
	public double skillBonusAreaLength;
	public String skillCheckKeyHit;
	public int updateDelta;
	public boolean stopSleep;
	public int matchStartTime;
	public int matchTime;
	public Timer timer;

	public void run() {
		System.out.println("GameReader successfully started.");
		String path = System.getProperty("user.home").replaceAll("\\\\", "/");
        path += "/AppData/Local/DeadByDaylight/Saved/Logs/DeadByDaylight.log";
        @SuppressWarnings("resource")
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(path));
			fullScan(reader);
			BufferedReader read = new BufferedReader(new FileReader(path));
			readLog(read);
		} catch (ExceptionHandler e) {
			throw new ExceptionHandler("ERROR: " + e);
		} catch (IOException e) {
			throw new ExceptionHandler("ERROR: " + e);
		} catch (InterruptedException e) {
			
		}
	}
	public void fullScan(BufferedReader reader) throws IOException, InterruptedException {
		System.out.println("Start Fullscan...");
		playerIsIngame = false;
        String line = "";
        int lineCount = 0;
        int matchStarts = 0;
        int matchEnds = 0;
        int lastLine = 0;
        Main.blockKillerSet = true;
        while (line != null) {
            line = reader.readLine();
            if (line != null) {
            	//checkIfIngame(line, lineCount);
            	if (line.indexOf(searchGameStarted) >= 0) {
            		matchStarts++;
            		//System.out.println(lineCount);
            		lastLine = lineCount;
            	}
            	if (line.indexOf(searchGameEnded) >= 0) {
            		matchEnds++;
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
        System.out.println("RESULT: " + matchStarts + " : " + matchEnds);
        Main.blockKillerSet = false;
        System.out.println("Fullscan Complete");
        reader.close();
	}
    public void readLog(BufferedReader reader) throws ExceptionHandler, IOException, InterruptedException {
    	System.out.println("Current Ingame Status: " + playerIsIngame);
        String line = "";
        int lineCount = 0;
        Main.setKillerPic(currentKillerId);
        Main.killerName.setText(GetKillerById(currentKillerId));
        Main.blockKillerSet = false;
        timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				updateTime();
			}
		});
		timer.setInitialDelay(0);
        while (true) {
            line = reader.readLine();
            if (line != null) {
            	if (lineCount >= startLineNumber) {
            		Thread.sleep(1);
            		scanLine(line, lineCount);
            	}
            	lineCount++;
            } else {
            	if (!stopSleep) {
            		Thread.sleep(1);
            	}
            }
        }
        
    }
    public void scanLine(String line, int lineCount) throws IOException {
    	checkIfIngame(line, lineCount);
    	matchTime(line, lineCount);
    	if (playerIsIngame) {
    		//System.out.println("Line: " + lineCount + " - " + line);
    		searchMap(line);
    		searchSurvivorStates(line, lineCount);
    		searchTotems(line);
    		searchKillerPerks(line, lineCount);
    		//processSkillCheck(line);
    		searchOfferings(line);
    		checkIfIngame(line, lineCount);
    	} else {
    		Main.setKillerPic(currentKillerId);
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
    		endGame();
    	}
    }
    public void startGame(int lineCount) {
    	playerIsIngame = true;
    	startLineNumber = lineCount;
		DropShadow shadow = new DropShadow();
		shadow.setColor(Color.rgb(30, 255, 10));
		Main.gameStatus.setText("INGAME");
		Main.killerPic.setEffect(shadow);
		System.out.println("Start Game");
		if (timer != null) {
			timer.start();
		}
    }
    public void endGame() {
    	playerIsIngame = false;
		Main.mapName.setVisible(false);
    	Main.mapTitle.setVisible(false);
		DropShadow shadow = new DropShadow();
		shadow.setColor(Color.rgb(0, 0, 0));
		Main.gameStatus.setText("LOBBY");
		Main.killerPic.setEffect(shadow);
		System.out.println("End Game");
		cleanseTotemStartCount = 0;
    	cleanseTotemEndCount = 0;
    	totalCleansedTotems = 0;
    	Main.gameStatus.setText("Lobby");
    	playerIsIngame = false;
    	killerPerks = new String[4];
    	matchTime = 0;
    	searchTotems("");
    	timer.stop();
    	for (int i=0; i < Main.killerPerks.length; i++) {
    		if (Main.killerPerks[i] != null && Main.killerPerkFrames[i] != null) {
	    		URL perk_icon = Main.class.getResource("perk_default.png");
	    		Main.killerPerks[i].setImage(new Image(perk_icon.toString()));
	    		Main.killerPerks[i].setVisible(false);
	    		Main.killerPerkFrames[i].setVisible(false);
    		}
    	}
    }
    public void searchKiller(String line) {
    	if (line.indexOf(searchKiller) >= 0) {
    		//System.out.println(line);
            int index = line.indexOf(searchKiller);
            line = line.substring(index, line.length());
            line = line.replace(searchKiller, "");
            currentKillerId = Integer.parseInt(line.substring(0,2));
            try {
            	if (!Main.blockKillerSet) {
            		Main.killerName.setText(GetKillerById(currentKillerId));
            	}
                Main.setKillerPic(currentKillerId);
                //System.out.println("Current Killer: " + Main.killerName.getText() + "_" + killerId);
            } catch (NullPointerException e) {
            	System.out.println("ERROR: Couldn't set Killer");
            }
        } else {
        	Main.setKillerPic(-1);
        }
    }
    public void searchOfferings(String line) {
    	if (line.indexOf(searchEbonyMori) >= 0) {
    		System.out.println("Offering detected: Ebony Mori");
    	}
    	if (line.indexOf(searchIvoryMori) >= 0) {
    		System.out.println("Offering detected: Ivory Mori");
    	}
    	if (line.indexOf(searchCypressMori) >= 0) {
    		System.out.println("Offering detected: Cypress Mori");
    	}
    	if (line.indexOf(searchShroudSeperation) >= 0) {
    		System.out.println("Offering detected: Shroud Of Seperation");
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
    	//System.out.println(line);
    	String[] strings = line.split(",");
    	skillCheckKeyHit = strings[1].replace("keyText=", "").trim();
    	skillAreaStart = Math.round(Double.parseDouble(strings[2].replace("hitAreaStart=", "").trim())*10000.0)/10000.0;
    	skillAreaLength = Math.round(Double.parseDouble(strings[3].replace("hitAreaLength=", "").trim())*10000.0)/10000.0;
    	skillBonusAreaLength = Math.round(Double.parseDouble(strings[4].replace("bonusAreaLength=", "").trim())*10000.0)/10000.0;
    	System.out.println("Skillcheck Infos: " + skillCheckKeyHit + ", HitAreaStart: " + skillAreaStart + ", HitAreaLength: " + skillAreaLength + ", HitAreaLength: " + skillAreaLength + ", bonusAreaLength: " + skillBonusAreaLength);
    	skillCheckActive = true;
    	skillCheckActionPerformed = false;
    	stopSleep = true;
    	Main.skillCheckBar.setVisible(true);
    	Main.skillCheckText.setVisible(true);
    }
    public void endSkillCheck() {
    	skillAreaStart = 0;
    	skillAreaLength = 0;
    	skillBonusAreaLength = 0;
    	skillCheckActive = false;
    	Main.skillCheckBar.setVisible(false);
    	Main.skillCheckText.setVisible(false);
    	stopSleep = false;
    	//System.out.println("End Of Skillcheck.");
    }
    public void processSkillCheck(String line) throws ExceptionHandler {
    	//System.out.print("Starts: " + startSkillCheckCount + " end: " + hideSkillCheckCount);
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
	    	//System.out.println(currentValue);
	    	if (currentValue >= skillAreaStart) {
	    		//Main.hardPress(' ');
	    		endSkillCheck();
	    	}
	    	Main.skillCheckBar.setProgress(currentValue);
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
    			//setPerk("franklinsLoss");
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
    		if (line.indexOf(fireUpVaultSpeed) >= 0 || line.indexOf(fireUpBreakPalletSpeed) >= 0 || line.indexOf(fireUpDamageGenSpeed) >= 0) {
    			//setPerk("fireUp"); NOT WORKING
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
    			} else if (lastGenBlockLine > camperPickedUpLine && (lastGenBlockLine < camperDropLine || lastGenBlockLine < camperEnterHookLine)) {
    				setPerk("thrillingTremors");
    			}
    		}
    		// Missing Perks
    		/*
    		Agitation
    		BloodHound
    		Bloodecho
    		DeerStalker
    		Discordance -- Multiplayer
    		Lightborn
    		Brutal Strength
    		Bamboozle
    		Curel Limits
    		Distressing
    		Franklings Demise --- Not confirmed
    		Furtive Chase
    		Gearhead
    		Infectious Fright
    		Insidious
    		IronGrasp
    		Madgrid
    		MonitorAbuse
    		MonstrousShrine
    		PopGoesTheWeasel
    		Nemesis
    		PlayWithYourFood
    		Predator
    		OverCharge
    		SaveTheBastForLast -- Multiplayer
    		ShadowBorn
    		SpiesFromTheShadows
    		SpiritFury
    		Surge
    		Surveillance
    		Unrelenting
    		Whispers
    		Zanshin Tactics
    		*/
    	/*}
    }
    public void matchTime(String line, int lineCount) {
    	if (line.indexOf(gameStartCamAnimationEnd) >= 0 && line.length() >= 24) {
    		try {
    		int hours = Integer.parseInt(line.substring(12, 14)) + 2;
    		int minutes = Integer.parseInt(line.substring(15, 17));
    		int seconds = Integer.parseInt(line.substring(18, 20));
    		int duration = hours * minutes + seconds;
    		
    		Calendar calendar = Calendar.getInstance();
    		
    		int chours = calendar.get(Calendar.HOUR_OF_DAY);
    		int cminutes = calendar.get(Calendar.MINUTE);
    		int cseconds = calendar.get(Calendar.SECOND);
    		int current = chours * cminutes + cseconds;
    		if (lineCount == startLineNumber) {
    			System.out.println(lineCount);
    			matchStartTime = duration;
    			//System.out.println("h:"+hours+" m:" + minutes + " s:" + seconds);
    			//System.out.println("Time: " + duration + " : " + current);
    			Main.matchTime.setText((current - matchStartTime)+"");
    		}
    		} catch (NumberFormatException e) {
    			
    		}
    	}
    }
    public void updateTime() {
    	Calendar calendar = Calendar.getInstance();
    	int chours = calendar.get(Calendar.HOUR_OF_DAY);
		int cminutes = calendar.get(Calendar.MINUTE);
		int cseconds = calendar.get(Calendar.SECOND);
		int current = chours * cminutes + cseconds;
    	Main.matchTime.setText((current - matchStartTime)+"s");
    }
    public void searchMap(String line) {
    	if (line != null) {
    		if (line.indexOf(searchMapTheme) >= 0) {
    			String[] strings = line.split(":");
    			mapTheme = strings[4].replace("Map", "").trim();
    			mapName = strings[5].replace("Generation Seed", "");
    			mapName = mapName.substring(mapName.indexOf("_")+1, mapName.length());
    			//System.out.println(mapName + " : " + mapTheme);
    			Main.mapName.setText(mapName);
    			Main.mapTitle.setVisible(true);
    			Main.mapName.setVisible(true);
    			System.out.println("Currently playing on theme=" + mapTheme + " Name=" + mapName);
    		}
    	}
    }
    public void searchTotems(String line) {
		if (line.indexOf(searchTotem) >= 0 && line.indexOf(interactionEnter) >= 0) {
			cleanseTotemStartCount++;
		} else if (line.indexOf(searchTotem) >= 0 && line.indexOf(interactionExit) >= 0) {
			cleanseTotemEndCount++;
		} else if (line.indexOf(interactionFinished) >= 0 && line.indexOf(searchTotem)>= 0) {
			totalCleansedTotems++;
		}
		DropShadow shadow = new DropShadow();
    	shadow.setWidth(0);
    	shadow.setHeight(0);
    	shadow.setBlurType(BlurType.ONE_PASS_BOX);
    	if (cleanseTotemStartCount > cleanseTotemEndCount) {
    		shadow.setColor(Color.rgb(255, 0, 255));
    		Main.totemPic.setEffect(shadow);
    	} else {
    		shadow.setColor(Color.rgb(100, 100, 100));
    		Main.totemPic.setEffect(shadow);
    	}
    	Main.totalTotems.setText(totalCleansedTotems+"");
    }
    public void setPerk(String perk) {
    	for (int i=0; i < killerPerks.length; i++) {
    		if (killerPerks[i] == perk) {
    			return;
    		}
    	}
    	for (int i=0; i < killerPerks.length; i++) {
    		if (killerPerks[i] == "" || killerPerks[i] == null) {
    			try {
    			killerPerks[i] = perk;
    			URL perk_icon = Main.class.getResource("iconPerks_" + perk + ".png");
        		Main.killerPerks[i].setImage(new Image(perk_icon.toString()));
        		Main.killerPerks[i].setVisible(true);
        		Main.killerPerkFrames[i].setVisible(true);
    			playPerkAnimation(perk, i);
    			System.out.println("Perk detected: " + perk);
    			break;
    			} catch (ArrayIndexOutOfBoundsException e) {
    				throw new ExceptionHandler("ERROR: Perk Imageview out of bounds");
    			} catch (NullPointerException e) {
    				throw new ExceptionHandler("ERROR: Imageview OutOfBoundsException");
    			}
    		}
    	}
    }
    public void playPerkAnimation(String perk, int slot) {
    	int startRot = 200;
    	int endRot = 360;
    	double startScale = 0;
    	double endScale = 1;
    	int speed = 200;
    	double delay = 500 * slot;
    	// Animate Perk Frames
    	try {
	    	rotateImage(Main.killerPerkFrames[slot], startRot, endRot, speed, delay);
	    	scaleXImage(Main.killerPerkFrames[slot], startScale, endScale, speed, delay);
	    	scaleYImage(Main.killerPerkFrames[slot], startScale, endScale, speed, delay);
	    	// Animate Perk Icons
	    	rotateImage(Main.killerPerks[slot], startRot, endRot, speed, delay);
	    	scaleXImage(Main.killerPerks[slot], startScale, endScale, speed, delay);
	    	scaleYImage(Main.killerPerks[slot], startScale, endScale, speed, delay);
    	} catch (NullPointerException e) {
    		throw new ExceptionHandler("ERROR: Perk Imageview not found");
    	}
    }
    public void rotateImage(ImageView i, int start, int end, int speed, double delay) {
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
    public void scaleXImage(ImageView i, double start, double end, int speed, double delay) {
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
    public void scaleYImage(ImageView i, double start, double end, int speed, double delay) {
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
    }*/
}
