package sperlich;

class Player {
	public String name;
	public boolean isSurvivor;
	public boolean isKiller;
	public int survivorId;
	public int hooks;
	public boolean isRepairing;
	public double borrowedTime;
	public boolean isEscaped;
	public String survivorName;
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
