import java.awt.event.*;

public class Action implements MouseListener, MouseMotionListener, KeyListener {

    //宏定义
    private final int KEYBOARD_LOWER_A = 65;
    private final int KEYBOARD_LOWER_B = 66;
    private final int KEYBOARD_LOWER_C = 67;
    private final int KEYBOARD_LOWER_D = 68;
    private final int KEYBOARD_LOWER_E = 69;
    private final int KEYBOARD_LOWER_F = 70;
    private final int KEYBOARD_LOWER_G = 71;
    private final int KEYBOARD_LOWER_H = 72;
    private final int KEYBOARD_LOWER_I = 73;
    private final int KEYBOARD_LOWER_J = 74;
    private final int KEYBOARD_LOWER_K = 75;
    private final int KEYBOARD_LOWER_L = 76;
    private final int KEYBOARD_LOWER_M = 77;
    private final int KEYBOARD_LOWER_N = 78;
    private final int KEYBOARD_LOWER_O = 79;
    private final int KEYBOARD_LOWER_P = 80;
    private final int KEYBOARD_LOWER_Q = 81;
    private final int KEYBOARD_LOWER_R = 82;
    private final int KEYBOARD_LOWER_S = 83;
    private final int KEYBOARD_LOWER_T = 84;
    private final int KEYBOARD_LOWER_U = 85;
    private final int KEYBOARD_LOWER_V = 86;
    private final int KEYBOARD_LOWER_W = 87;
    private final int KEYBOARD_LOWER_X = 88;
    private final int KEYBOARD_LOWER_Y = 89;
    private final int KEYBOARD_LOWER_Z = 90;
    private final int KEYBOARD_ESC = 27;

    private final int MOUSE_LEFT_BUTTON = 1;
    private final int MOUSE_RIGHT_BUTTON = 3;
    private final int MOUSE_WHEEL_BUTTON = 2;


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //触发

    }


    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MOUSE_LEFT_BUTTON) {
            if (GeneralData.isShowDelta() == false) {
                GeneralData.setShowDelta(true);
                System.out.println("Show Delta！");
            } else {
                GeneralData.setShowDelta(false);
                System.out.println("Hide Delta！");
            }

        } else if (e.getButton() == MOUSE_RIGHT_BUTTON) {
            if (GeneralData.isShowQTable() == false) {
                GeneralData.setShowQTable(true);
                System.out.println("Show Q-Table！");
            } else {
                GeneralData.setShowQTable(false);
                System.out.println("Hide Q-Table！");
            }

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MOUSE_LEFT_BUTTON) {

        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {


    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }


}
