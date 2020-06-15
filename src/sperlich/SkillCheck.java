package sperlich;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Date;
import java.io.IOException;

public class SkillCheck extends Thread {

	public String progressInfo = "HudView::UpdateSkillCheckProgress";
	public Timer timer;
	public BufferedReader reader;
	public String keyHit;
	public double areaLength;
	public double areaStart;
	public double bonusLength;
	public double currentValue;
	public long timeDelay;
	public long totalTime;
	public double value1;
	public boolean isHit;
	public Date pcTime;
	public Date serverTime;
	public long serverDelay;
	public ArrayList<String> timeDifference;
	
	public SkillCheck(BufferedReader reader, String line) {
		this.reader = reader;
		startSkillCheck(line);
		try {
			processSkillCheck();
		} catch (IOException | InterruptedException e) {
			Log.out("An error occurred while processing the skillcheck!");
		}
	}
	
	public void startSkillCheck(String line) {
		timeDifference = new ArrayList<>();
		isHit = false;
		String[] strings = line.split(",");
		keyHit = strings[1].replace("keyText=", "").trim();
		areaStart = Math.round(Double.parseDouble(strings[2].replace("hitAreaStart=", "").trim()) * 10000.0) / 10000.0;
		areaLength = Math.round(Double.parseDouble(strings[3].replace("hitAreaLength=", "").trim()) * 10000.0) / 10000.0;
		bonusLength = Math.round(Double.parseDouble(strings[4].replace("bonusAreaLength=", "").trim()) * 10000.0) / 10000.0;
		Log.out("### SKILLCHECK START: " + keyHit + ", HitAreaStart: " + areaStart
				+ ", HitAreaLength: " + areaLength + ", HitAreaLength: " + areaLength + ", bonusAreaLength: "
				+ bonusLength + " ###");
		timeDifference.add(GameReader.getTimestamp(line));
		serverTime = GameReader.getDateFromTimestamp(GameReader.getTimestamp(line));
		pcTime = new Date();
		serverTime.setHours(pcTime.getHours());
		serverDelay = Math.abs(pcTime.getTime() - serverTime.getTime());
		Log.out("SERVER DELAY: " + serverDelay);
	}

	public void endSkillCheck() {
		areaStart = 0;
		areaLength = 0;
		bonusLength = 0;
		currentValue = 0;
		Log.out("### SKILLCHECK END ###");
	}

	public void processSkillCheck() throws InterruptedException, IOException {
		int lostTime = 0;
		while (timeDifference.size() < 2) {
			sleep(1);
			lostTime++;
			String newLine = reader.readLine();
			if (newLine != null && newLine.indexOf(progressInfo) >= 0) {
				timeDifference.add(GameReader.getTimestamp(newLine));
				value1 = getSkillCheckValue(newLine);
			}
		}
		timeDelay = GameReader.getDateFromTimestamp(timeDifference.get(1)).getTime() - GameReader.getDateFromTimestamp(timeDifference.get(0)).getTime();
		totalTime = Math.abs(Math.round((timeDelay/value1)*(areaStart+areaLength/2)-serverDelay-lostTime));
		Log.out("Time between values: " + timeDelay);
		Log.out("Calculated Hit-Time: " + totalTime);
		boolean blockSecondMethod = false;
		if (totalTime <= Math.round(areaStart)*1000.0) {
			Log.out("WARNING: Hit-Time calculation is lower than hitAreaStart! Setting to areaStart.");
			totalTime = Math.round(areaStart*1000.0);
			//Log.out("New calculated Hit-Time: " + totalTime);
			blockSecondMethod = true;
		}
		totalTime = (int)Math.floor(areaStart*1000)-100;
		Log.out("New calculated Hit-Time: " + totalTime);
		
		Runnable t = () -> hitSkillCheck();
		timer = new Timer((int)totalTime, t);
		
		/*while (!isHit) {
			try {
				String valueLine = reader.readLine();
				if (valueLine != null && valueLine.indexOf(progressInfo) >= 0) {
					if (getSkillCheckValue(valueLine) >= areaStart && !isHit) {
						Log.out("Skillcheck performed with backup method.");
						isHit = true;
						hitSkillCheck();
						break;
					}
				}
			} catch (IOException e) {}
		}*/
	}
	
	public double getSkillCheckValue(String line) {
		double value = 0;
		if (line != null && line.indexOf(progressInfo) >= 0) {
			line = (line.substring(line.indexOf("value="), line.length()));
			line = line.substring(6, line.length());
			try {
				value = Double.parseDouble(line.substring(0, line.indexOf(',')));
			} catch (NumberFormatException e) {
				throw new ExceptionHandler("Couldn't parse skillcheck value");
			}
		}
		value += 0.025;
		Log.out(value);
		return value;
	}
	
	public void hitSkillCheck() {
		Runtime.hardPress(Runtime.skillCheckLetter);
		timer.stop();
		isHit = true;
		endSkillCheck();
	}
}
