package sperlich;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

public class App {
	public boolean isRunning;
	public FlowLayout layer;
	public boolean displayOriginal = true;
	public boolean displayPreview = false;
	public boolean showBounds = false;
	public ScreenCapture capture;
	public BufferedImage buffImg;
	public Dimension imagePreviewDimension;
	public int count;
	public Record record;
	public int skillCheckFoundCount;
	public float oldAverageWhite;
	public float oldAverageBlack;
	public List<Integer> whiteX;
	public List<Integer> whiteY;
	public List<Integer> blackX;
	public List<Integer> blackY;
	List<Float> blacks = new ArrayList<>();
	List<Float> whites = new ArrayList<>();
	public float skillCheckImageScaler;
	int white = 16777215;
	int black = 0;
	int green = 65280;
	int blue = 255;
	int pink = 16711935;
	int aqua = 51350;
	int grey = new Color(25, 25, 25).getRGB();
	Color pointer = new Color(180, 25, 25);
	public boolean skillCheckFound;
	public float whiteAverageX;
	public float whiteAverageY;
	public int hitPoints;
	public float angleTarget;
	public Pos angleTargetPos;
	public WinUser.INPUT input;
	public boolean programStarted;
	public boolean autoStruggle;
	public char hitkey = ' ';
	public boolean whiteDetected;
	public Window win;

	public void CreateMainWindow() throws IOException, AWTException, InterruptedException {
		whiteDetected = false;
		screenshot();
	}
	public void hardPress(char letter) {
		// Pressing
		input.input.ki.dwFlags = new WinDef.DWORD(0);  // keydown
		User32.INSTANCE.SendInput( new WinDef.DWORD(1), ( WinUser.INPUT[] ) input.toArray( 1 ), input.size() );
		System.out.println("HARDKEY PRESSED " + letter);
	}
	public void start() throws AWTException, IOException, InterruptedException {
		programStarted = true;
		isRunning = true;
		autoStruggle = false;
		record = new Record();
		record.start();
		record.startRecord(this);
		//startButton.setText("Stop");
		//startButton.setBackground(new Color(75, 225, 75));
		System.out.println("STARTED");
	}
	public void stop() throws AWTException, InterruptedException, IOException {
		programStarted = false;
		isRunning = false;
		autoStruggle = false;
		Runtime.mouseButton1Hold = false;
		record.stopRecord();
		//startButton.setText("Start");
		//startButton.setBackground(new Color(225, 75, 75));
		//whiteDetectedPane.setBackground(new Color(0, 0, 0));
		System.out.println("STOPPED");
	}
	public void screenshot() throws IOException, AWTException, InterruptedException {
		if (programStarted) {
			buffImg = capture.screenshot();
			processImage(buffImg);
			//setScreenshotToPreview();
		}
	}
	public void setScreenshotToPreview() throws IOException, AWTException {
		// Capture Screenshot
		//screenImg = new ImageIcon(resize(buffImg, imagePreviewDimension.width, imagePreviewDimension.height));
		//imagePreviewLabel.setIcon(screenImg);
	}
	public static Image resize(BufferedImage buffer, int scaledWidth, int scaledHeight) throws IOException {
		// reads input image
		BufferedImage inputImage = buffer;

		// creates output image
		BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());

		// scales the input image to the output image
		Graphics2D g2d = outputImage.createGraphics();
		g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
		g2d.dispose();
		return outputImage;
	}
	public void paintPixel(int x, int y, BufferedImage target, int color) {
		if (x >= 0 && x < target.getWidth() && y >= 0 && y < target.getHeight()) {
			target.setRGB(x, y, color);
		}
	}
	public void processImage(BufferedImage img) throws IOException, AWTException, InterruptedException {
		if (!programStarted) {
			return;
		}
		BufferedImage copy = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		Graphics g = copy.getGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();
		if (displayOriginal) {
			displayPreview = false;
			copy = img;
		}
		// Recognition Color
		if (!skillCheckFound) {
			whiteX = new ArrayList<>();
			whiteY = new ArrayList<>();
		}
		blackX = new ArrayList<>();
		blackY = new ArrayList<>();
		whites = new ArrayList<>();
		boolean pointerExists = false;
		for (int x=0; x < img.getWidth();x++) {
			for (int y=0; y < img.getHeight();y++) {
				Color color = getColor(x, y, img);
				boolean whitePixelFound = false;
				boolean modified = false;
				if (true) {
					if (isWhiteColor(color)) {
						paintPixel(x, y, img, white);
						whites.add(x + y + 0f);
						if (!skillCheckFound) {
							whiteX.add(x);
							whiteY.add(y);
						}
						whitePixelFound = true;
						modified = true;
						if (displayPreview)
							paintPixel(x, y, copy, green);
					}
				}
				if (!whitePixelFound) {
					if (isPointerColor(pointer, color)) {
						paintPixel(x, y, img, black);
						blacks.add(x + y + 0f);
						blackX.add(x);
						blackY.add(y);
						pointerExists = true;
						modified = true;
						if (displayPreview)
							paintPixel(x, y, copy, pink);
					}
				}
				if (!modified) {
					paintPixel(x, y, img, grey);
				}
			}
		}
		if (whites.size() > 0) {
			whiteDetected = true;
		} else {
			whiteDetected = false;
		}
		if (!pointerExists) {
			angleTarget = 0;
			return;
		}
		// Search Skill Check
		if (checkSkillCheck(img)) {
			buffImg = copy;
			setScreenshotToPreview();
			hitSkillCheck();
			reset();
			record.pauseForSeconds();
		}
		buffImg = copy;
	}
	public boolean checkSkillCheck(BufferedImage img) {
		// Find Highest Y
		Pos top = new Pos(0,0);
		Pos bottom = new Pos(0,0);
		Pos right = new Pos(0,0);
		Pos left = new Pos(0,0);
		Pos center = new Pos(0,0);
		boolean skillCheckExists = false;
		firstLeft:
		for (int x=0;x<img.getWidth();x++) {
			for (int y=0;y<img.getHeight();y++) {
				if (isPureWhiteColor(getColor(x, y, img))) {
					left = new Pos(x,y);
					skillCheckExists = true;
					break firstLeft;
				} else {
					//paintPixel(x,y,img,black);
				}
			}
		}
		if (!skillCheckExists) {
			angleTarget = 0;
			return false;
		}
		firstTop:
		for (int y=0;y<img.getHeight();y++) {
			for (int x=0;x<img.getWidth();x++) {
				if (isPureWhiteColor(getColor(x, y, img))) {
					top = new Pos(x,y);
					break firstTop;
				} else {
					//paintPixel(x,y,img,black);
				}
			}
		}
		firstRight:
		for (int x=img.getWidth()-1;x>=0;x--) {
			for (int y=0;y<img.getHeight();y++) {
				if (isPureWhiteColor(getColor(x, y, img))) {
					right = new Pos(x,y);
					break firstRight;
				} else {
					//paintPixel(x,y,img,black);
				}
			}
		}
		firstBottom:
		for (int y=img.getHeight()-1;y>=0;y--) {
			for (int x=0;x<img.getWidth();x++) {
				if (isPureWhiteColor(getColor(x, y, img))) {
					bottom = new Pos(x,y);
					break firstBottom;
				} else {
					//paintPixel(x,y,img,black);
				}
			}
		}
		// Paint Box
		int width = 1;
		if (showBounds) {
			for (int i = left.x; i < right.x; i++) {
				for (int w = -width; w < width; w++) {
					paintPixel(i, top.y + w, img, pink);
				}
			}
			for (int i = left.x; i < right.x; i++) {
				for (int w = -width; w < width; w++) {
					paintPixel(i, bottom.y + w, img, pink);
				}
			}
			for (int i = top.y; i < bottom.y; i++) {
				for (int w = -width; w < width; w++) {
					paintPixel(left.x + w, i, img, pink);
				}
			}
			for (int i = top.y; i < bottom.y; i++) {
				for (int w = -width; w < width; w++) {
					paintPixel(right.x + w, i, img, pink);
				}
			}
		}
		// Find center
		center = new Pos((right.x + left.x)/2,(top.y + bottom.y)/2);
		//System.out.println(center.x + " : " + center.y);
		if (center.x < 90 && center.x > 200) return false;
		// Paint center
		if (showBounds) {
			paintCross(center, 2, 10, img, green);
		}
		// Go along Circle
		float originalRadius = (right.x - center.x);
		float radius = originalRadius*1.05f;
		if (originalRadius > 100) return false;
		float searchWidth = radius * 0.2f;
		hitPoints = 0;
		float anglePointer = 0f;
		for (float i=0;i<360;i+=1.2f) {
			double xDir = Math.cos(Math.toRadians(i));
			double yDir = Math.sin(Math.toRadians(i));
			int x = (int) Math.round((radius) * xDir + center.x);
			int y = (int) Math.round((radius) * yDir + center.y);

			if (showBounds) {
				paintPixel(x, y, img, green);
			}
			int whites = 0;
			for (int s=0;s<searchWidth;s++) {
				Pos pos = new Pos((int)(x - xDir*s), (int)(y - yDir*s));
				Color color = getColor(pos.x, pos.y, img);
				if (isPureWhiteColor(color)) {
					whites++;
				} else if (isBlackColor(color)) {
					anglePointer = (i + 90) % 360;
				} else if (whites > 0) break;
			}
			if (whites > 0.1f*radius && angleTarget == 0) {
				angleTarget = (i + 90) % 360;
			}
		}
		if (anglePointer <= 0) return false;
		if(angleTarget != 0) {
			double xDir = Math.cos(Math.toRadians(angleTarget - 88f));
			double yDir = Math.sin(Math.toRadians(angleTarget - 88f));
			int x = (int) Math.round((radius) * xDir + center.x);
			int y = (int) Math.round((radius) * yDir + center.y);
			paintCross(new Pos(x,y),2,5,img,blue);
		}
		int offset = 10;
		if (anglePointer >= angleTarget - offset && angleTarget >= 20) {
			return true;
		}
		return false;
	}
	public Color getColor(int x, int y, BufferedImage img) {
		if (x >= 0 && x < img.getWidth() && y >= 0 && y < img.getHeight()) {
			return new Color(img.getRGB(x, y));
		} else {
			return new Color(0, 0, 0);
		}
	}
	public void paintCross(Pos center, int width, int height, BufferedImage img, int color) {
		for (int y = -width; y < width; y++) {
			for (int x = -height; x < height; x++) {
				paintPixel(center.x+y,center.y+x,img,color);
			}
		}
		for (int y = -width; y < width; y++) {
			for (int x = -height; x < height; x++) {
				paintPixel(center.x+x, center.y+y,img,color);
			}
		}
	}
	public void searchSkillCheck() {
		float averageWhite = 0f;
		float averageBlack = 0f;
		if (skillCheckFound == false) {
			for (int i = 0; i < whites.size(); i++) {
				averageWhite += whites.get(i);
			}
			averageWhite = averageWhite / whites.size();

			for (int i = 0; i < blacks.size(); i++) {
				averageBlack += blacks.get(i);
			}
		}
		float totalX = 0;
		for (int x=0;x < whiteX.size();x++) {
			totalX += whiteX.get(x);
		}
		whiteAverageX = totalX/whiteX.size();
		float totalY = 0;
		for (int y=0;y < whiteY.size();y++) {
			totalY += whiteY.get(y);
		}
		whiteAverageY = totalY/whiteY.size();
		averageBlack = averageBlack/blacks.size();
		if (skillCheckFound == false) {
			oldAverageWhite = averageWhite;
			oldAverageBlack = averageBlack;
			//skillCheckLabel.setVisible(false);
		} else {
			//skillCheckLabel.setVisible(true);
		}
		// Check Pixel Clouds
		if (whiteX.size() > 50) {
			skillCheckFound = true;
		}
	}
	public void reset() {
		System.out.println("######### RESET #########");
		oldAverageWhite = 0;
		oldAverageBlack = 0;
		skillCheckFoundCount = 0;
		whiteX = new ArrayList<>();
		whiteY = new ArrayList<>();
		skillCheckFound = false;
	}
	public boolean isPointerColor(Color target, Color source) {
		float[] hsvS = new float[3];
		Color.RGBtoHSB(source.getRed(), source.getGreen(), source.getBlue(), hsvS);

		if (hsvS[0] < 0.02 || hsvS[0] > 0.96) {
			if (hsvS[2] > 0.8 && hsvS[2] < 1)
				return true;
		}
		return false;
	}
	public boolean isWhiteColor(Color source) {
		float[] hsvS = new float[3];
		Color.RGBtoHSB(source.getRed(), source.getGreen(), source.getBlue(), hsvS);
		if (hsvS[2] >= 0.3 && hsvS[1] < 0.2) {
			return true;
		}
		return false;
	}
	public boolean isBlackColor(Color source) {
		float[] hsvS = new float[3];
		Color.RGBtoHSB(source.getRed(), source.getGreen(), source.getBlue(), hsvS);
		if (hsvS[0] == 0 && hsvS[1] == 0 && hsvS[2] == 0) {
			return true;
		}
		return false;
	}
	public  boolean isCircleColor(Color source) {
		float[] hsvS = new float[3];
		Color.RGBtoHSB(source.getRed(), source.getGreen(), source.getBlue(), hsvS);
		if (hsvS[2] == 1) {
			return true;
		}
		return false;
	}
	public boolean isPureWhiteColor(Color source) {
		float[] hsvS = new float[3];
		Color.RGBtoHSB(source.getRed(), source.getGreen(), source.getBlue(), hsvS);
		if (hsvS[2] == 1 && hsvS[1] == 0) {
			return true;
		}
		return false;
	}
	public void hitSkillCheck() throws AWTException, InterruptedException {
		skillCheckImageScaler = 1.5f;
		//java.awt.Robot robot = new Robot();
		//robot.keyPress(KeyEvent.VK_SPACE);
		if (isRunning) {
			hardPress(hitkey);
		}
		record.pauseForSeconds();
	}
}
class Record extends Thread {
	public boolean isRunning;
	public App app;
	int count;
	public float gameTime;
	public Timer timer;
	int struggleCount;
	int struggleTimer;

	public void startRecord(App app) {
		isRunning = true;
		this.app = app;
		timer = new Timer(20, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				try {
					update();
				} catch (AWTException e) {

				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		timer.setInitialDelay(20);
		timer.start();
	}
	public void update() throws AWTException, InterruptedException, IOException {
		if (!app.programStarted) return;
		if (app.skillCheckImageScaler >= 1) {
			app.skillCheckImageScaler -= 0.05f;
		} else {
			app.skillCheckImageScaler = 1;
		}
		/*if (!app.holdMouseButton.isSelected()) {
			Main.mouseButton1Hold = true;
		}*/
		/*if (app.hotkey1Field.getText().length() > 1) {
			app.hitkey = app.hotkey1Field.getText().charAt(0);
		} else {
			app.hitkey = ' ';
		}*/
		if (isRunning && Runtime.mouseButton1Hold) {
			app.screenshot();
		}
		/*if (app.whiteDetected && app.programStarted) {
			app.whiteDetectedPane.setBackground(new Color(255, 100, 100));
		} else if (app.programStarted) {
			app.whiteDetectedPane.setBackground(new Color(100, 255, 100));
		}*/
		/*if (app.autoStruggle) {
			struggleCount++;
			if (struggleCount < 8) {
				app.struggleText.setText("");
			} else if(struggleCount > 20) {
				struggleCount = 0;
			} else {
				app.struggleText.setText("<html><a color='white'>STRUGGLE</a></html>");
			}
		} else {
			struggleCount = 0;
			app.struggleText.setText("");
		}*/
		if (app.autoStruggle) {
			struggleTimer++;
			if (struggleTimer >= 10) {
				app.hardPress(' ');
				struggleTimer = 0;
			}
		}
	}
	public void stopRecord() {
		app.input.input.ki.dwFlags = new WinDef.DWORD(2);  // keyup
		User32.INSTANCE.SendInput( new WinDef.DWORD(1), ( WinUser.INPUT[] ) app.input.toArray( 1 ), app.input.size() );
		isRunning = false;
		timer.stop();
		//app.struggleText.setText("");
		struggleCount = 0;
		app.autoStruggle = false;
	}
	public void pauseForSeconds() throws InterruptedException, AWTException {
		stopRecord();
		sleep(50);
		isRunning = true;
		timer.start();
	}
}
class Pos {
	int x = 0;
	int y = 0;
	Pos(int x, int y) {
		this.x = x;
		this.y = y;
	}
}