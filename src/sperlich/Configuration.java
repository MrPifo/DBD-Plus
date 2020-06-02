package sperlich;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@SuppressWarnings("serial")
public class Configuration implements Serializable {
	public String selectedTheme;
	public boolean performSkillchecks;
	public char skillCheckLetter;

	public void save() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("/DBDPlus/dbdplus.config"));
			oos.writeObject(Runtime.config);
			oos.close();
		} catch (IOException e) {
			throw new ExceptionHandler("ERROR: Couldn't save configuration.");
		}
	}
	
	public void load() {
		try {
			ObjectInputStream objectInput = new ObjectInputStream(new FileInputStream("/DBDPlus/dbdplus.config"));
			Runtime.config = (Configuration) objectInput.readObject();
			objectInput.close();
		} catch (Exception e) {
			Log.out("Failed to load save file! Creating new...");
			Runtime.config = new Configuration();
			Runtime.config.performSkillchecks = false;
			Runtime.config.skillCheckLetter = ' ';
		}
	}
}
