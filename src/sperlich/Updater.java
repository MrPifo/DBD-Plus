package sperlich;

import java.awt.AWTException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class Updater extends Thread {
	public void downloadNewVersion() {
		URL link;
		BufferedInputStream in;
		Log.out("Start download...");
		try {
			in = new BufferedInputStream(new URL(Main.download).openStream());
			FileOutputStream fileOutputStream = new FileOutputStream(Main.downloadPath);
	        double downloaded = 0;
	        byte dataBuffer[] = new byte[1024];
	        int bytesRead;
	        Main.toggleNode(Main.downloadProgress, true);
	        URL url = new URL(Main.download);
	        URLConnection conn = url.openConnection();
	        int fileSize = conn.getContentLength()/1024/1024;
	        while ((bytesRead = in.read(dataBuffer, 0, 256)) != -1) {
	        	fileOutputStream.write(dataBuffer, 0, bytesRead);
	        	downloaded += 256;
	        	Main.downloadProgress.setProgress(downloaded/1024/1024/fileSize);
	        	//Log.out(Math.round(downloaded/1024/1024*1000.0)/1000.0);
	        }
	        File f = new File(Main.downloadPath); 
	        if (f.exists()) {
	        	Main.displayTray();
	        	Main.restart();
	        }
		} catch (IOException e1) {
			Main.downloadProgress.setVisible(false);
		} catch (AWTException e) {
        	Log.out("Couldn't display tray notification");
        	Main.downloadProgress.setVisible(false);
        }
	}
}
