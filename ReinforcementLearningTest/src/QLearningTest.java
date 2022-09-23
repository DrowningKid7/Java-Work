import javax.swing.*;
import java.net.SocketException;

//QLearning测试代码
public class QLearningTest extends JFrame {

    public static void main(String[] args) throws SocketException {
        new QLearningTest();
    }

    public QLearningTest() throws SocketException {

        Action Action = new Action();
        this.addKeyListener(Action);

        DrawPanel DrawPanel = new DrawPanel();
        Thread DrawPanelThread = new Thread(DrawPanel);
        DrawPanelThread.start();

        this.add(DrawPanel);
        this.setSize(GeneralData.MyWindowWidth, GeneralData.MyWindowHeight);
        this.setTitle("QLearningTest");
        this.setVisible(true);
        this.setDefaultCloseOperation(3);
    }
}
