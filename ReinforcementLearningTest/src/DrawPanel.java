import javax.swing.*;
import java.awt.*;

class DrawPanel extends JPanel implements Runnable {

    public DrawPanel() {
        Action Action = new Action();
        this.addMouseListener(Action);
        this.addMouseMotionListener(Action);
        GeneralData.setPanelCenterPointX(GeneralData.MyPanelWidth / 2);
        QLearningAction.InitialQTable();
        InformationBlock InformationBlock = new InformationBlock();
        GeneralData.setInformationBlock(InformationBlock);
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, GeneralData.MyWindowWidth, GeneralData.MyWindowHeight);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GeneralData.MyPanelWidth, GeneralData.MyPanelHeight);

        this.SetDrawParameter();
        this.DrawUAV(g);

        if(GeneralData.isShowQTable()){
            this.DrawBlock(g);
        }
        if (GeneralData.isShowDelta()) {
            this.DrawDeltaPosition(g);
        }
        this.DrawObstacle(g);

        g.setColor(Color.GRAY);
        g.fillRect(GeneralData.MyPanelWidth, 0, GeneralData.MyWindowWidth, GeneralData.MyWindowHeight);
        g.fillRect(0, GeneralData.MyPanelHeight, GeneralData.MyWindowWidth, GeneralData.MyWindowHeight);
        this.DrawInformation(g);


    }


    //绘制无人机
    private void DrawUAV(Graphics g) {
        g.setColor(Color.red);
        UAV UAV = GeneralData.getInformationBlock().getUAV();
        int PointX = UAV.getUAVDrawParameter().getPointX();
        int PointY = UAV.getUAVDrawParameter().getPointY();
        int Radius = UAV.getUAVDrawParameter().getRadius();
        if (PointX >= GeneralData.getPanelCenterPointX()) {
            GeneralData.setPanelCenterPointX(PointX);
            PointX = (int) GeneralData.MyPanelWidth / 2;
        }
        g.fillOval(PointX - Radius, PointY - Radius, Radius*2, Radius*2);
    }

    private void DrawObstacle(Graphics g) {
        try {
            int CurrentFacingObstacleFlag = GeneralData.getInformationBlock().getCurrentFacingObstacleFlag();
            if (CurrentFacingObstacleFlag >= 10) {
                for (int i = CurrentFacingObstacleFlag - 10; i < CurrentFacingObstacleFlag + 10; i++) {
                    Obstacle D_Obstacle = GeneralData.getInformationBlock().getD_ObstacleVector().get(i);
                    int PointX = (int) (D_Obstacle.getUL_PositionX() - GeneralData.getPanelCenterPointX() + GeneralData.MyPanelWidth / 2);
                    int PointY = (int) (GeneralData.MyPanelHeight - D_Obstacle.getUL_PositionY());
                    int Width = (int) D_Obstacle.getWidth();
                    int Height = (int) D_Obstacle.getHeight();
                    g.setColor(Color.green);
                    g.fillRect(PointX, PointY, Width, Height);
                    Obstacle U_Obstacle = GeneralData.getInformationBlock().getU_ObstacleVector().get(i);
                    PointX = (int) (U_Obstacle.getUL_PositionX() - GeneralData.getPanelCenterPointX() + GeneralData.MyPanelWidth / 2);
                    PointY = (int) (GeneralData.MyPanelHeight - U_Obstacle.getUL_PositionY());
                    Width = (int) U_Obstacle.getWidth();
                    Height = (int) U_Obstacle.getHeight();
                    g.setColor(Color.green);
                    g.fillRect(PointX, PointY, Width, Height);
                }
            } else {
                for (int i = 0; i < CurrentFacingObstacleFlag + 10; i++) {
                    Obstacle D_Obstacle = GeneralData.getInformationBlock().getD_ObstacleVector().get(i);
                    int PointX = (int) (D_Obstacle.getUL_PositionX() - GeneralData.getPanelCenterPointX() + GeneralData.MyPanelWidth / 2);
                    int PointY = (int) (GeneralData.MyPanelHeight - D_Obstacle.getUL_PositionY());
                    int Width = (int) D_Obstacle.getWidth();
                    int Height = (int) D_Obstacle.getHeight();
                    g.setColor(Color.green);
                    g.fillRect(PointX, PointY, Width, Height);
                    Obstacle U_Obstacle = GeneralData.getInformationBlock().getU_ObstacleVector().get(i);
                    PointX = (int) (U_Obstacle.getUL_PositionX() - GeneralData.getPanelCenterPointX() + GeneralData.MyPanelWidth / 2);
                    PointY = (int) (GeneralData.MyPanelHeight - U_Obstacle.getUL_PositionY());
                    Width = (int) U_Obstacle.getWidth();
                    Height = (int) U_Obstacle.getHeight();
                    g.setColor(Color.green);
                    g.fillRect(PointX, PointY, Width, Height);
                }
            }
        } catch (Exception e) {
            System.out.println("障碍物绘图错误！");
            System.out.println(e);

        }
    }

    private int MessagePositionX = 1410;
    private int MessagePositionY = 50;

    private void DrawInformation(Graphics g) {
        int CurrentPassedObstacleFlag = GeneralData.getInformationBlock().getCurrentPassedObstacleFlag();
        int CurrentFacingObstacleFlag = GeneralData.getInformationBlock().getCurrentFacingObstacleFlag();
        UAV UAV = GeneralData.getInformationBlock().getUAV();
        int PositionX = (int) UAV.getUAVParameter().getPositionX();
        int PositionY = (int) UAV.getUAVParameter().getPositionY();
        int SpeedX = (int) UAV.getUAVParameter().getSpeedX();
        int SpeedY = (int) UAV.getUAVParameter().getSpeedY();
        int AccelerationX = (int) UAV.getUAVParameter().getAccelerationX();
        int AccelerationY = (int) UAV.getUAVParameter().getAccelerationY();
        int TrainingTimes=QLearningAction.getTrainingTimes();
        double GreedyThreshold=QLearningAction.getGreedyThreshold();
        g.setColor(Color.red);
        g.setFont(new Font("New Time Rema", 1, 17));
        String CurrentPassedObstacleFlagMessage = "PassedObstacleFlag: " + CurrentPassedObstacleFlag;
        String CurrentFacingObstacleFlagMessage = "FacingObstacleFlag: " + CurrentFacingObstacleFlag;
        String PositionXMessage = "PositionX: " + PositionX;
        String PositionYMessage = "PositionY: " + PositionY;
        String SpeedXMessage = "SpeedX: " + SpeedX;
        String SpeedYMessage = "SpeedY: " + SpeedY;
        String AccelerationXMessage = "AccelerationX: " + AccelerationX;
        String AccelerationYMessage = "AccelerationY: " + AccelerationY;
        String TrainingTimesMessage = "Training Times: " + TrainingTimes;
        String GreedyThresholdMessage = "Greedy Threshold: " + GreedyThreshold;
        g.drawString(CurrentPassedObstacleFlagMessage, 100, 830);
        g.drawString(CurrentFacingObstacleFlagMessage, 100, 860);
        g.drawString(TrainingTimesMessage, 400, 830);
        g.drawString(GreedyThresholdMessage, 400, 860);
        g.drawString(PositionXMessage, MessagePositionX, MessagePositionY + 10);
        g.drawString(PositionYMessage, MessagePositionX, MessagePositionY + 40);
        g.drawString(SpeedXMessage, MessagePositionX, MessagePositionY + 70);
        g.drawString(SpeedYMessage, MessagePositionX, MessagePositionY + 100);
        g.drawString(AccelerationXMessage, MessagePositionX, MessagePositionY + 130);
        g.drawString(AccelerationYMessage, MessagePositionX, MessagePositionY + 160);
    }

    private void DrawDeltaPosition(Graphics g) {
        UAV UAV = GeneralData.getInformationBlock().getUAV();
        int PositionX = (int) (UAV.getUAVParameter().getPositionX() - GeneralData.getPanelCenterPointX() + GeneralData.MyPanelWidth / 2);
        int PositionY = (int) (GeneralData.MyPanelHeight - UAV.getUAVParameter().getPositionY());

        int CurrentFacingObstacleFlag = GeneralData.getInformationBlock().getCurrentFacingObstacleFlag();
        Obstacle D_Obstacle = GeneralData.getInformationBlock().getD_ObstacleVector().get(CurrentFacingObstacleFlag);
        int ObstaclePositionX = (int) (D_Obstacle.getUL_PositionX() - GeneralData.getPanelCenterPointX() + GeneralData.MyPanelWidth / 2);
        int ObstaclePositionY = (int) (GeneralData.MyPanelHeight - D_Obstacle.getUL_PositionY());
        int DeltaX = PositionX - ObstaclePositionX;
        int DeltaY = ObstaclePositionY - PositionY;
        g.setColor(Color.yellow);
        g.drawLine(PositionX, PositionY, ObstaclePositionX, ObstaclePositionY);
        //Y偏差
        g.drawLine(PositionX, PositionY, PositionX, ObstaclePositionY);
        //X偏差
        g.drawLine(PositionX, ObstaclePositionY, ObstaclePositionX, ObstaclePositionY);
        g.setFont(new Font("New Time Rema", 1, 13));
        int DeltaXPointX = (PositionX + ObstaclePositionX) / 2;
        int DeltaYPointY = (PositionY + ObstaclePositionY) / 2;
        g.drawString("Delta X: " + DeltaX, DeltaXPointX, ObstaclePositionY);
        g.drawString("Delta Y: " + DeltaY, PositionX, DeltaYPointY);

    }

    private void DrawBlock(Graphics g) {
        int CurrentFacingObstacleFlag = GeneralData.getInformationBlock().getCurrentFacingObstacleFlag();
        Obstacle D_Obstacle = GeneralData.getInformationBlock().getD_ObstacleVector().get(CurrentFacingObstacleFlag);
        int ObstaclePositionX = (int) (D_Obstacle.getUL_PositionX() - GeneralData.getPanelCenterPointX() + GeneralData.MyPanelWidth / 2);
        int ObstaclePositionY = (int) (GeneralData.MyPanelHeight - D_Obstacle.getUL_PositionY());
        int Delta = (int) QLearningAction.State_Block_Width;

        int Columns=QLearningAction.Columns;
        int Rows=QLearningAction.Rows;
        g.setFont(new Font("New Time Rema", 1, 8));
        for (int i = 5; i < 43; i++) {
            for (int j = Columns*1/3; j < Columns; j++) {
                int BlockPointNumberX = i - QLearningAction.Rows_Benchmark;
                int BlockPointNumberY = j - QLearningAction.Columns_Benchmark+1;
                int BlockPointX = ObstaclePositionX + BlockPointNumberX * Delta;
                int BlockPointY = ObstaclePositionY - BlockPointNumberY * Delta;
                g.setColor(Color.blue);
                g.drawRect(BlockPointX, BlockPointY, Delta, Delta);
                int Action = QLearningAction.ActionTable[i][j];
                String ActionMessage=String.valueOf(Action);
                if(Action==-1){
                    g.setColor(Color.PINK);
                } else if (Action==1) {
                    g.setColor(Color.CYAN);
                }else {
                    g.setColor(Color.blue);
                }
                g.drawString(ActionMessage, BlockPointX+Delta/3, BlockPointY+Delta*2/3);

            }
        }
    }

    private void SetDrawParameter() {
        UAV UAV = GeneralData.getInformationBlock().getUAV();
        double PositionX = UAV.getUAVParameter().getPositionX();
        double PositionY = UAV.getUAVParameter().getPositionY();
        int PointX = (int) PositionX;
        int PointY = GeneralData.MyPanelHeight - (int) PositionY;
        UAV.getUAVDrawParameter().setPointX(PointX);
        UAV.getUAVDrawParameter().setPointY(PointY);
    }

    @Override
    public void run() {
        double ScreenRefreshRate = GeneralData.ScreenRefreshRate;
        double ScreenRefreshTimeInterval = 1 / ScreenRefreshRate;
        long ScreenRefreshTimeIntervalMillisecond = (int) (ScreenRefreshTimeInterval * 1000);
        //每秒165Hz重绘画板
        while (GeneralData.isIsDrawContinue()) {
            try {
                Thread.sleep(ScreenRefreshTimeIntervalMillisecond);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!GeneralData.getInformationBlock().getQLearningAction().isQLearningWorking()) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("********* Restart! *********");
                GeneralData.setPanelCenterPointX(GeneralData.MyPanelWidth / 2);
                InformationBlock InformationBlock = new InformationBlock();
                GeneralData.setInformationBlock(InformationBlock);
            }
            this.repaint();
        }
    }

    public int getMessagePositionX() {
        return MessagePositionX;
    }

    public void setMessagePositionX(int messagePositionX) {
        MessagePositionX = messagePositionX;
    }

    public int getMessagePositionY() {
        return MessagePositionY;
    }

    public void setMessagePositionY(int messagePositionY) {
        MessagePositionY = messagePositionY;
    }
}
