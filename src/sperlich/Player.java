package sperlich;

class Player {
	public String name;
	public boolean isSurvivor;
	public boolean isKiller;
	public int survivorId;
	public boolean hookPhase1;
	public boolean hookPhase2;
	public boolean isRepairing;
	public double borrowedTime;
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
