package GameManager.Main;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class KeyHandler implements KeyListener {

    private static ArrayList<KeyEvent> lastTickPressedKeys = new ArrayList<>();
    private static ArrayList<KeyEvent> currentTickPressedKeys = new ArrayList<>();
    private static ArrayList<KeyEvent> realPressedKeys = new ArrayList<>();

    public static void update() {
        lastTickPressedKeys = currentTickPressedKeys;
        currentTickPressedKeys = realPressedKeys;
    }

    public void start(Component c) {
        c.addKeyListener(this);
        c.setFocusable(true);
        c.requestFocusInWindow();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        realPressedKeys.add(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        realPressedKeys.removeIf(pressedKey -> pressedKey.getKeyCode() == e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static boolean isPressed(int keyCode) {
        boolean pressed = false;
        for (KeyEvent keyEvent : currentTickPressedKeys)
            if (keyEvent.getKeyCode() == keyCode) {
                pressed = true;
                break;
            }
        return pressed;
    }

    public static boolean wasPressedDown(int keyCode) {
        boolean pressed = false;
        for (KeyEvent keyEvent : currentTickPressedKeys)
            if (keyEvent.getKeyCode() == keyCode) {
                pressed = true;
                break;
            }
        for (KeyEvent keyEvent : lastTickPressedKeys)
            if (keyEvent.getKeyCode() == keyCode) {
                pressed = false;
                break;
            }
        return pressed;
    }

    public static boolean wasReleased(int keyCode) {
        boolean pressed = false;
        for (KeyEvent keyEvent : lastTickPressedKeys)
            if (keyEvent.getKeyCode() == keyCode) {
                pressed = true;
                break;
            }
        for (KeyEvent keyEvent : currentTickPressedKeys)
            if (keyEvent.getKeyCode() == keyCode) {
                pressed = false;
                break;
            }
        return pressed;
    }
}