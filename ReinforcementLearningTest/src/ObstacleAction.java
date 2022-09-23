import java.util.Vector;

//障碍物移动
public class ObstacleAction implements Runnable {

    private double CurrentLocationX;//最新实例化的障碍物的位置
    private double D_ObstacleHeight;//下障碍物高度基准
    private double HeightFloatingRange;//下障碍物浮动区间：根据前一个障碍物高度确定，保证不会出现较大跨度导致无法通过
    private double PassingInterval;//上下障碍物之间可以被穿越的区间高度
    private int TopBottomFlag;//顶部底部标记：-1：底部；0：中间；1：顶部。
    private double MinD_ObstacleHeight;//最小下障碍物高度
    private double MinU_ObstacleHeight;//最小上障碍物高度
    private boolean IsObstacleGeneratorWorking;//障碍物是否工作标记
    private final int ObstacleUpdateNumber; //每次更新的障碍物数量
    private int CurrentObstacleNumberThreshold;//当前障碍物数量阈值，超过阈值更新障碍物数量，同时更新阈值
    private UAV UAV;
    private Vector<Obstacle> U_ObstacleVector;
    private Vector<Obstacle> D_ObstacleVector;

    public ObstacleAction(UAV UAV, Vector<Obstacle> U_ObstacleVector, Vector<Obstacle> D_ObstacleVector) {
        this.CurrentLocationX = GeneralData.ObstacleStartPositionX;
        this.D_ObstacleHeight = GeneralData.MyPanelHeight / 2;//初始化初始下障碍物高度
        this.HeightFloatingRange = 250;//
        this.PassingInterval = 110;
        this.TopBottomFlag = 0;
        this.MinD_ObstacleHeight = 20;
        this.MinU_ObstacleHeight = 5;
        this.IsObstacleGeneratorWorking = true;
        this.ObstacleUpdateNumber = 50;
        this.CurrentObstacleNumberThreshold = 35;
        this.UAV = UAV;
        this.U_ObstacleVector = U_ObstacleVector;
        this.D_ObstacleVector = D_ObstacleVector;
        this.ObstacleGenerator();
    }

    @Override
    public void run() {
        double ObstacleUpdateFrequency = GeneralData.ObstacleUpdateFrequency;
        double ObstacleUpdateTimeInterval = 1 / ObstacleUpdateFrequency;
        long ObstacleTimeIntervalMillisecond = (int) (ObstacleUpdateTimeInterval * 1000);
        while (this.IsObstacleGeneratorWorking) {
            //当前UAV经过的障碍物数量
            int CurrentPassingObstacleNumber = UAV.getCollisionDetection().getCurrentFacingObstacleFlag();
            if (CurrentPassingObstacleNumber > CurrentObstacleNumberThreshold) {
                this.ObstacleGenerator();
                CurrentObstacleNumberThreshold += ObstacleUpdateNumber;
            }
            try {
                Thread.sleep(ObstacleTimeIntervalMillisecond);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Obstacle Action Thread Dead!");
    }

    private void ObstacleGenerator() {
        for (int i = 0; i < ObstacleUpdateNumber; i++) {
            if (TopBottomFlag == 1) {
                D_ObstacleHeight = D_ObstacleHeight - Math.random() * HeightFloatingRange / 2;
                TopBottomFlag = 0;
            } else if (TopBottomFlag == -1) {
                D_ObstacleHeight = D_ObstacleHeight + Math.random() * HeightFloatingRange / 2;
                TopBottomFlag = 0;
            } else {
                //计算当前的下障碍物高度
                D_ObstacleHeight = D_ObstacleHeight + (Math.random() - 0.5) * HeightFloatingRange;
                //如果下障碍物高度小于最小下障碍物高度
                if (D_ObstacleHeight < MinD_ObstacleHeight) {
                    D_ObstacleHeight = MinD_ObstacleHeight;
                    TopBottomFlag = -1;
                    //如果下障碍物高度挤占最小上障碍物高度
                } else if (D_ObstacleHeight > GeneralData.MyPanelHeight - PassingInterval - MinU_ObstacleHeight) {
                    D_ObstacleHeight = GeneralData.MyPanelHeight - PassingInterval - MinU_ObstacleHeight;
                    TopBottomFlag = 1;
                }
            }
            //构建上障碍物
            double U_UL_PositionX = CurrentLocationX;
            double U_UL_PositionY = GeneralData.MyPanelHeight;
            double U_UR_PositionX = CurrentLocationX + GeneralData.ObstacleWidth;
            double U_UR_PositionY = U_UL_PositionY;
            double U_DL_PositionX = U_UL_PositionX;
            double U_DL_PositionY = D_ObstacleHeight + PassingInterval;
            double U_DR_PositionX = U_UR_PositionX;
            double U_DR_PositionY = U_DL_PositionY;
            double U_Width = GeneralData.ObstacleWidth;
            double U_Height = U_UL_PositionY - U_DL_PositionY;
            Obstacle U_Obstacle = new Obstacle(U_UL_PositionX, U_UL_PositionY, U_UR_PositionX, U_UR_PositionY,
                    U_DL_PositionX, U_DL_PositionY, U_DR_PositionX, U_DR_PositionY, U_Width, U_Height);
            this.U_ObstacleVector.add(U_Obstacle);
            //构建下障碍物
            double D_UL_PositionX = U_UL_PositionX;
            double D_UL_PositionY = D_ObstacleHeight;
            double D_UR_PositionX = U_UR_PositionX;
            double D_UR_PositionY = D_UL_PositionY;
            double D_DL_PositionX = D_UL_PositionX;
            double D_DL_PositionY = 0;
            double D_DR_PositionX = D_UR_PositionX;
            double D_DR_PositionY = D_DL_PositionY;
            double D_Width = GeneralData.ObstacleWidth;
            double D_Height = D_ObstacleHeight;
            Obstacle D_Obstacle = new Obstacle(D_UL_PositionX, D_UL_PositionY, D_UR_PositionX, D_UR_PositionY,
                    D_DL_PositionX, D_DL_PositionY, D_DR_PositionX, D_DR_PositionY, D_Width, D_Height);
            this.D_ObstacleVector.add(D_Obstacle);
            //更新当前位置
            CurrentLocationX = CurrentLocationX + GeneralData.ObstacleWidth + GeneralData.ObstacleInterval;
        }
    }

    public double getCurrentLocationX() {
        return CurrentLocationX;
    }

    public void setCurrentLocationX(double currentLocationX) {
        CurrentLocationX = currentLocationX;
    }

    public double getD_ObstacleHeight() {
        return D_ObstacleHeight;
    }

    public void setD_ObstacleHeight(double d_ObstacleHeight) {
        D_ObstacleHeight = d_ObstacleHeight;
    }

    public double getHeightFloatingRange() {
        return HeightFloatingRange;
    }

    public void setHeightFloatingRange(double heightFloatingRange) {
        HeightFloatingRange = heightFloatingRange;
    }

    public double getPassingInterval() {
        return PassingInterval;
    }

    public void setPassingInterval(double passingInterval) {
        PassingInterval = passingInterval;
    }

    public int getTopBottomFlag() {
        return TopBottomFlag;
    }

    public void setTopBottomFlag(int topBottomFlag) {
        TopBottomFlag = topBottomFlag;
    }

    public double getMinD_ObstacleHeight() {
        return MinD_ObstacleHeight;
    }

    public void setMinD_ObstacleHeight(double minD_ObstacleHeight) {
        MinD_ObstacleHeight = minD_ObstacleHeight;
    }

    public double getMinU_ObstacleHeight() {
        return MinU_ObstacleHeight;
    }

    public void setMinU_ObstacleHeight(double minU_ObstacleHeight) {
        MinU_ObstacleHeight = minU_ObstacleHeight;
    }

    public boolean isObstacleGeneratorWorking() {
        return IsObstacleGeneratorWorking;
    }

    public void setObstacleGeneratorWorking(boolean obstacleGeneratorWorking) {
        IsObstacleGeneratorWorking = obstacleGeneratorWorking;
    }

    public int getObstacleUpdateNumber() {
        return ObstacleUpdateNumber;
    }

    public int getCurrentObstacleNumberThreshold() {
        return CurrentObstacleNumberThreshold;
    }

    public void setCurrentObstacleNumberThreshold(int currentObstacleNumberThreshold) {
        CurrentObstacleNumberThreshold = currentObstacleNumberThreshold;
    }

    public UAV getUAV() {
        return UAV;
    }

    public void setUAV(UAV UAV) {
        this.UAV = UAV;
    }

    public Vector<Obstacle> getU_ObstacleVector() {
        return U_ObstacleVector;
    }

    public void setU_ObstacleVector(Vector<Obstacle> u_ObstacleVector) {
        U_ObstacleVector = u_ObstacleVector;
    }

    public Vector<Obstacle> getD_ObstacleVector() {
        return D_ObstacleVector;
    }

    public void setD_ObstacleVector(Vector<Obstacle> d_ObstacleVector) {
        D_ObstacleVector = d_ObstacleVector;
    }
}
