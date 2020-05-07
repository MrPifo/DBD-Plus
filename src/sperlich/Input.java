package sperlich;
import java.io.IOException;
import java.util.Map.Entry;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import lc.kra.system.mouse.GlobalMouseHook;
import lc.kra.system.mouse.event.GlobalMouseAdapter;
import lc.kra.system.mouse.event.GlobalMouseEvent;

class Input extends Thread {

    private static boolean run = true;

    public void run() {
        // Might throw a UnsatisfiedLinkError if the native library fails to load or a RuntimeException if hooking fails
        GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(true); // Use false here to switch to hook instead of raw input
        System.out.println("Global keyboard hook successfully started.");

        for (Entry<Long, String> keyboard : GlobalKeyboardHook.listKeyboards().entrySet()) {
            //System.out.format("%d: %s\n", keyboard.getKey(), keyboard.getValue());
        }

        keyboardHook.addKeyListener(new GlobalKeyAdapter() {

            @Override
            public void keyPressed(GlobalKeyEvent event) {
                try {
                    Runtime.keyPressed(event);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void keyReleased(GlobalKeyEvent event) {
                Runtime.keyReleased(event);
            }
        });

        try {
            while(run) {
                Thread.sleep(50);
            }
        } catch(InterruptedException e) {
            //Do nothing
        } finally {
            keyboardHook.shutdownHook();
        }
    }
}
class MouseInput extends Thread {

    private static boolean run = true;

    public void run() {
        // Might throw a UnsatisfiedLinkError if the native library fails to load or a RuntimeException if hooking fails
        GlobalMouseHook mouseHook = new GlobalMouseHook(); // Add true to the constructor, to switch to raw input mode
        System.out.println("Global mouse hook successfully started.");
        for (Entry<Long,String> mouse:GlobalMouseHook.listMice().entrySet()) {
            //System.out.format("%d: %s\n", mouse.getKey(), mouse.getValue());
        }
        mouseHook.addMouseListener(new GlobalMouseAdapter() {

            @Override
            public void mousePressed(GlobalMouseEvent event)  {
                if (event.getButton() == GlobalMouseEvent.BUTTON_LEFT) {
                    Runtime.leftMouseButtonPressed();
                }
            }

            @Override
            public void mouseReleased(GlobalMouseEvent event)  {
                if (event.getButton() == GlobalMouseEvent.BUTTON_LEFT) {
                    Runtime.leftMouseButtonReleased();
                }
            }

            @Override
            public void mouseMoved(GlobalMouseEvent event) {

            }

            @Override
            public void mouseWheel(GlobalMouseEvent event) {

            }
        });

        try {
            while(run) {
                Thread.sleep(128);
            }
        } catch(InterruptedException e) {
            //Do nothing
        } finally {
            mouseHook.shutdownHook();
        }
    }
}