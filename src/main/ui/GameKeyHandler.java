package ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;

public class GameKeyHandler implements KeyListener {

    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean firePressed;


    @Override
    public void keyTyped(KeyEvent e) {
        ///
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_F) {
            firePressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_F) {
            firePressed = false;
        }
    }

    // EFFECTS: Returns whether downPressed is true or false.
    public boolean isDownPressed() {
        return downPressed;
    }

    // EFFECTS: Returns whether isUpPressed is true or false.
    public boolean isUpPressed() {
        return upPressed;
    }

    // EFFECTS: Returns whether isLeftPressed is true or false.
    public boolean isLeftPressed() {
        return leftPressed;
    }

    // EFFECTS: Returns whether isRightPressed is true or false.
    public boolean isRightPressed() {
        return rightPressed;
    }

    // EFFECTS: Returns whether isFirePressed is true or false.
    public boolean isFirePressed() {
        return firePressed;
    }

    // MODIFIES: this
    // EFFECTS: Sets the setUpPressed to the specified boolean.
    public void setUpPressed(Boolean bool) {
        upPressed = bool;
    }

    // MODIFIES: this
    // EFFECTS: Sets the setDownPressed to the specified boolean.
    public void setDownPressed(Boolean bool) {
        downPressed = bool;
    }

    // MODIFIES: this
    // EFFECTS: Sets the setLeftPressed to the specified boolean.
    public void setLeftPressed(Boolean bool) {
        leftPressed = bool;
    }

    // MODIFIES: this
    // EFFECTS: Sets the setRightPressed to the specified boolean.
    public  void setRightPressed(Boolean bool) {
        rightPressed = bool;
    }

    // MODIFIES: this
    // EFFECTS: Sets the setFirePressed to the specified boolean.
    public void setFirePressed(Boolean bool) {
        firePressed = bool;
    }

}

