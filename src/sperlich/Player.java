package sperlich;

class Player {
	public String name;
	public boolean isSurvivor;
	public boolean isKiller;
	public int survivorId;
	public int hooks;
	public double borrowedTime;
	public boolean hasEscaped;
	public boolean isInteracting;
	public String survivorName;
	public boolean isDead;
	public int obessionState;
	public boolean isHooked;
	public double dstrikeTime;
	
	Player(String name, boolean isKiller) {
		this.name = name;
		if (isKiller) {
			isKiller = true;
		} else {
			isSurvivor = true;
		}
	}
	Player(String name) {
		this.name = name;
	}
}
