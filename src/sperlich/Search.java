package sperlich;

public class Search {
	static String killer = "GameFlow: (BP_Menu_Slasher";
	static String gameStarted = "UDBDGameInstance::StartOfferingSequence";
	static String gameLeft = "LogAIModule: Creating AISystem for world OfflineLobby";
	static String mapTheme = "ProceduralLevelGeneration: InitLevel:";
	static String palletGeneration = ".PalletPullUpChargeable Enabled?: Enabled";
	static String vaultGeneration = "ProceduralLevel:PersistentLevel.WindowStandard_C_";
	static String startSkillCheck = "Investigation: HudView::ShowSkillCheck";
	static String endSkillCheck = "Investigation: HudView::HideSkillCheck";
	static String totem = "[CleanseTotem][BP_TotemBase_C_";
	static String interactEnter = "[==> Interaction Enter]";
	static String interactExit = "[<== Interaction Exit]";
	static String interactFinished = "[<!> Charge Complete Received]";
	static String interactionGenerator = "";
	static String cleansedHexTotem = "Originating Effect: Hex";
	static String ebonyMori = "Ebony Memento Mori";
	static String ivoryMori = "Ivory Memento Mori";
	static String cypressMori = "Cypress Memento Mori";
	static String shroudSeperation = "Shroud of Separation";
	static String seperationSurvivor = "Shroud of Binding";
	static String gameClosed = "LogExit: Exiting.";
	static String bloodPoints = "GameFlow: WriteGameEndStats: Skulls:";
	// PERKS
	static String noed = "Originating Effect: No_One_Escapes_Death";
	static String ruin = "Originating Effect: Hex_Ruin";
	static String overwhelmingPresence = "Originating Effect: OverwhelmingPresence";
	static String lullaby = "Originating Effect: Hex_HuntressLullaby";
	static String retribution = "Originating Effect: HexRetribution";
	static String hauntedGrounds = "Originating Effect: Hex_HauntedGround";
	static String devourHope = "Originating Effect: Hex_Devour_Hope";
	static String thirdSeal = "Originating Effect: Hex_The_Third_Seal";
	static String nurseCalling = "Originating Effect: NurseCalling";
	static String bitterMurmur = "Originating Effect: Bitter_Murmur";
	static String thrillOfTheHunt = "Originating Effect: Hex_Thrill_Of_The_Hunt";
	static String bloodWarden = "Originating Effect: BloodWarden";
	static String coulrophobia = "Originating Effect: Coulrophobia";
	static String darkDevotion = "Originating Effect: DarkDevotion";
	static String dyingLight = "Originating Effect: Dying_Light";
	static String deadManSwitch = "Originating Effect: DeadMansSwitch";
	static String franklinsDemise = "GameFlow: OnDropped"; // Unsicher
	static String ironMaiden = "Originating Effect: Ironmaiden";
	static String bbqAndChilli = "Originating Effect: BBQAndChilli";
	static String mindBreaker = "Originating Effect: MindBreaker";
	static String rancor = "Originating Effect: Rancor";
	static String rememberMe = "Id: RememberMe_Effect";
	static String thanatophobia = "Id: Thanatophobia_Effect_";
	static String territorialImperative = "Id: TerritorialImperativeEffect";
	static String tinkerer = "Originating Effect: Tinkerer";
	static String beastOfPrey = "Originating Effect: BeastOfPrey";
	static String hangMan = "Originating Effect: HangmansTrick";
	static String allEars = "Originating Effect: ImAllEars";
	static String knockOut = "Originating Effect: InTheDark";
	static String stridor = "Stridor_Effect_";
	static String sloppyButcher = "Originating Effect: Sloppy_Butcher";
	static String unnvervingPresence = "Originating Effect: Unnerving_Presence";
	static String makeYourChoice = "Originating Effect: MakeYourChoice";
	static String gameStartCamAnimationEnd = "HudView::OnShowHUDComplete called";
	static String genBlocked = "Requested to Play: EntityGeneratorBlock None.";
	static String camperPickUp = "Requested to Play: SurvivorPickup_";
	static String camperDrop = "Requested to Play: SurvivorDrop";
	static String camperStunDrop = "Requested to Play: SurvivorStunDrop";
	static String camperEnterHook = "<Entering state Hooked>";
	static String camperExitHook = "InitializeStatusEffect - Id: Camper Was Recently Unhooked";
	static String defaultVaultSpeed = "Interaction time: 1.70";
	static String bamboozleVaultSpeed = "Interaction time: 1.478";
	static String myersVaultSpeed = "Interaction time: 1.275";
	static String fireUpVaultSpeed = "Interaction time: 1.63";
	static String defaultBreakPalletSpeed = "Interaction time: 2.60";
	static String fireUpBreakPalletSpeed = "Interaction time: 2.50";
	static String fireUpDamageGenSpeed = "Interaction time: 1.92";
	static String brutalStrengthPalletSpeed = "Interaction time: 2.16";
	static String defaultDamageGenSpeed = "Interaction time: 2.00";
	static String brutalStrengthDamageGenSpeed = "Interaction time: 1.66";
	static String enduringPalletStunSpeed = "Interaction time: 1.00";
	static String palletStun = "[PalletStun]";
	static String palletDestroyed = "Requested to Play: PlankDestroy";
	static String palletPowerDestroyed = "Requested to Play: DestroyWithPowerAttack";
	static String palletPullDownShowUI = "HudScreen: HudView::AddInputPrompts";
	static String palletPullDownL = "[PulldownDefinitionLeftStationary]";
	static String palletPullDownR = "[PulldownDefinitionRightStationary]";
	static String palletPullDownRunningR = "[PulldownDefinitionRight]";
	static String palletPullDownRunningL = "[PulldownDefinitionLeft]";
	static String palletPullDownExit = "AuthoritativeMovement set to False";
	static String palletKillerStun = "[PalletStun][SlasherInteractable_C_";
	static String survivorSecondHook = "Verbose: [Struggle][BP_SmallMeatLocker";
	static String survivorFirstHook = "Id: Camper Was Recently Unhooked";
	static String survivorDead = "[CleanInteractionArray AFTER]";
	static String closetEnter = "Interaction: Verbose: [ClosetHideEnterSneak]";
	static String closetExit  = "Interaction: Verbose: [ClosetHideExitSneak]";
	static String closetEnterRushed = "Interaction: Verbose: [ClosetHideEnterRushed]";
	static String closetExitRushed = "Interaction: Verbose: [ClosetHideExitRushed]";
	static String searchKillerName = "InteractionHandler: Verbose: [BP_Slasher_Character_";
	static String searchSurvivorNamePart1 = "Verbose: [BP_Camper";
	static String searchSurvivorNamePart2 = "_Character_C_";
	static String searchSurvivorRepair = "Interaction: Verbose: [GeneratorRepair";
	static String searchSurvivorRecover = "Interaction: Verbose: [Recover]";
	static String searchSurvivorWiggle = "Interaction: Verbose: [Wiggle]";
	static String searchSuvivorSelfhealMedkit = "Interaction: Verbose: [SelfHealWithMedkit]";
	static String searchSurvivorLooting = "Interaction: Verbose: [OpenSearchable]";
	static String searchSurvivorHealOther = "Interaction: Verbose: [HealOther";
	static String searchSurvivorSelfcare = "Interaction: Verbose: [SelfHealNoMedkit]";
	static String searchSurvivorExitEscape = "Verbose: [OpenEscape]";
	static String borrowedTime = " Originating Effect: BorrowedTime";
	static String dStrike = "Originating Effect: DecisiveStrike";
	static String borrowedTimeInit = "StatusEffect::Multicast_InitializeStatusEffect";
	static String searchPlayerName = " playerName:";
	static String playerEscaped = "[EscapeMap]";
	static String searchHatchSpawned = "Sequential Loading of SoundBank Hatch";
	static String searchPlayerPosition = "StopSnapping Final Location (";
	static String searchActionComplete = "[<!> Charge Complete Received]";
	static String searchGenComplete = "Investigation: HudMatchEventsContextComponent::OnRemainingGeneratorCountChanged:";
	static String searchSteamId = "[FOnlineSessionMirrors::AddSessionPlayer] Session:GameSession PlayerId:";
	static String obsessionState = "playerObsessionState:";
	
	public static boolean contains(String src, String search) {
		src = src.toLowerCase();
		search = search.toLowerCase();
		if (src.indexOf(search) >= 0) {
			return true;
		}
		return false;
	}
	
	public static boolean containsCase(String src, String search) {
		if (src.indexOf(search) >= 0) {
			return true;
		}
		return false;
	}
	
	public static int getIndex(String src, String search) {
		return src.indexOf(search);
	}
	
	public static int getLastIndex(String src, String search) {
		return src.lastIndexOf(search);
	}
}
