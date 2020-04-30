package sperlich;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ScreenCapture {

	public BufferedImage screenshot() throws AWTException, IOException {
		Robot robot = new Robot();
		
		//System.out.println("Processing Screen Capture...");
		//robot.keyPress(KeyEvent.VK_SPACE);

		String path = System.getProperty("user.home") + "/Desktop";
		String fileName = "shot.jpg";

		Rectangle rec = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		int width = 300;
		rec.x = rec.width/2 - width/2;
		rec.y = rec.height/2 - width/2;
		rec.width = width;
		rec.height = width;


		BufferedImage bufferedImage = robot.createScreenCapture(rec);

		//File image = new File(path+"/"+fileName);
		//ImageIO.write(bufferedImage, "jpg", image);
		//System.out.println("Screenshot saved");
		return bufferedImage;
	}
	
}
