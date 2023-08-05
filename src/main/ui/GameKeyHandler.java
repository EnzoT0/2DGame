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

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isFirePressed() {
        return firePressed;
    }

    public void setUpPressed(Boolean bool) {
        upPressed = bool;
    }

    public void setDownPressed(Boolean bool) {
        downPressed = bool;
    }

    public void setLeftPressed(Boolean bool) {
        leftPressed = bool;
    }

    public  void setRightPressed(Boolean bool) {
        rightPressed = bool;
    }

    public void setFirePressed(Boolean bool) {
        firePressed = bool;
    }

}

