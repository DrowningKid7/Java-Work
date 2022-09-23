//针对给定UAV的碰撞检测
public class CollisionDetection implements Runnable {

    private UAV UAV;//给定UAV
    private int CurrentFacingObstacleFlag;//当前需要进行碰撞检测的障碍物标记
    private double CurrentObstaclePositionX;//当前障碍物的X坐标
    private int DetectionPointNumber;//UAV用于碰撞检测的标记点数量
    private double DeltaAngle;//UAV用于碰撞检测的标记点相邻夹角间隔
    private boolean IsCollision;//是否撞击

    public CollisionDetection(UAV UAV, int DetectionPointResolutionRatio) {
        this.UAV = UAV;
        this.CurrentFacingObstacleFlag = 0;
        this.DetectionPointNumber = (int) UAV.getUAVParameter().getRadius() * DetectionPointResolutionRatio;
        this.DeltaAngle = 2 * Math.PI / this.DetectionPointNumber;
        this.IsCollision = false;
        this.CurrentObstaclePositionX = GeneralData.ObstacleStartPositionX;
        UAV.setCollisionDetection(this);
    }

    @Override
    public void run() {
        double CollisionDetectionUpdateFrequency = GeneralData.CollisionDetectionUpdateFrequency;
        double CollisionDetectionUpdateTimeInterval = 1 / CollisionDetectionUpdateFrequency;
        long CollisionDetectionUpdateTimeIntervalMillisecond = (int) (CollisionDetectionUpdateTimeInterval * 1000);
        while (!this.IsCollision) {
            double PositionX = UAV.getUAVParameter().getPositionX();
            double PositionY = UAV.getUAVParameter().getPositionY();
            double PositionZ = UAV.getUAVParameter().getPositionZ();
            double Radius = UAV.getUAVParameter().getRadius();
            if (PositionY - Radius > 0 && PositionY + Radius < GeneralData.MyPanelHeight) {
                try {
                    //更新需要判断的障碍物标记
                    if (PositionX - Radius > CurrentObstaclePositionX + GeneralData.ObstacleWidth) {
                        GeneralData.getInformationBlock().setCurrentPassedObstacleFlag(CurrentFacingObstacleFlag);
                        CurrentFacingObstacleFlag = CurrentFacingObstacleFlag + 1;
                        GeneralData.getInformationBlock().setCurrentFacingObstacleFlag(CurrentFacingObstacleFlag);
                    }
                    Obstacle D_Obstacle = GeneralData.getInformationBlock().getD_ObstacleVector().get(CurrentFacingObstacleFlag);
                    //当前要判断的障碍物位置（下障碍物左上点位置）
                    CurrentObstaclePositionX=D_Obstacle.getUL_PositionX();
                    Obstacle U_Obstacle = GeneralData.getInformationBlock().getU_ObstacleVector().get(CurrentFacingObstacleFlag);
                    //下障碍物位置
                    double D_UL_PositionX = D_Obstacle.getUL_PositionX();
                    double D_UL_PositionY = D_Obstacle.getUL_PositionY();
                    double D_UR_PositionX = D_Obstacle.getUR_PositionX();
                    double D_UR_PositionY = D_Obstacle.getUR_PositionY();
                    double D_DL_PositionX = D_Obstacle.getDL_PositionX();
                    double D_DL_PositionY = D_Obstacle.getDL_PositionY();
                    double D_DR_PositionX = D_Obstacle.getDR_PositionX();
                    double D_DR_PositionY = D_Obstacle.getDR_PositionY();
                    double[] D_VertexPositionX = {D_UL_PositionX, D_UR_PositionX, D_DL_PositionX, D_DR_PositionX};
                    double[] D_VertexPositionY = {D_UL_PositionY, D_UR_PositionY, D_DL_PositionY, D_DR_PositionY};
                    //上障碍物位置
                    double U_UL_PositionX = U_Obstacle.getUL_PositionX();
                    double U_UL_PositionY = U_Obstacle.getUL_PositionY();
                    double U_UR_PositionX = U_Obstacle.getUR_PositionX();
                    double U_UR_PositionY = U_Obstacle.getUR_PositionY();
                    double U_DL_PositionX = U_Obstacle.getDL_PositionX();
                    double U_DL_PositionY = U_Obstacle.getDL_PositionY();
                    double U_DR_PositionX = U_Obstacle.getDR_PositionX();
                    double U_DR_PositionY = U_Obstacle.getDR_PositionY();
                    double[] U_VertexPositionX = {U_UL_PositionX, U_UR_PositionX, U_DL_PositionX, U_DR_PositionX};
                    double[] U_VertexPositionY = {U_UL_PositionY, U_UR_PositionY, U_DL_PositionY, U_DR_PositionY};
                    double CurrentAngle = 0;
                    for (int i = 0; i < DetectionPointNumber; i++) {
                        //获得一个碰撞监测点
                        double DetectionPointPositionX = PositionX + Radius * Math.cos(CurrentAngle);
                        double DetectionPointPositionY = PositionY + Radius * Math.sin(CurrentAngle);
                        //判断该点与上障碍物是否接触
                        this.IsCollision = this.ContainsPolygon(DetectionPointPositionX, DetectionPointPositionY,
                                U_VertexPositionX, U_VertexPositionY, 4);
                        //如果接触就跳出，不接触继续判断与下障碍物是否接触
                        if (this.IsCollision) {
                            break;
                        } else {
                            this.IsCollision = this.ContainsPolygon(DetectionPointPositionX, DetectionPointPositionY,
                                    D_VertexPositionX, D_VertexPositionY, 4);
                            if (this.IsCollision) {
                                break;
                            }
                        }
                        //更新当前角度
                        CurrentAngle = CurrentAngle + DeltaAngle;
                    }

                } catch (Exception e) {

                }
            } else {
                this.IsCollision = true;
            }
            if (this.IsCollision) {
                UAV.setAlive(false);
                break;
            }
            try {
                Thread.sleep(CollisionDetectionUpdateTimeIntervalMillisecond);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        GeneralData.getInformationBlock().getTime().setTimeContinue(false);
        GeneralData.getInformationBlock().getObstacleAction().setObstacleGeneratorWorking(false);
        System.out.println("Collision Detection Thread Dead!");
    }

    //判断点是否在多边形内部
    private boolean ContainsPolygon(double PositionX, double PositionY, double[] VertexPositionX, double[] VertexPositionY, int VertexNumber) {
        boolean result = false;
        int i = 0;
        for (int j = VertexNumber - 1; i < VertexNumber; j = i++) {
            if (VertexPositionY[i] > PositionY != VertexPositionY[j] > PositionY
                    && PositionX < (VertexPositionX[j] - VertexPositionX[i]) * (PositionY - VertexPositionY[i])
                    / (VertexPositionY[j] - VertexPositionY[i]) + VertexPositionX[i]) {
                result = !result;
            }
        }
        return result;
    }

    public UAV getUAV() {
        return UAV;
    }

    public void setUAV(UAV UAV) {
        this.UAV = UAV;
    }

    public int getCurrentFacingObstacleFlag() {
        return CurrentFacingObstacleFlag;
    }

    public void setCurrentFacingObstacleFlag(int currentFacingObstacleFlag) {
        CurrentFacingObstacleFlag = currentFacingObstacleFlag;
    }

    public double getCurrentObstaclePositionX() {
        return CurrentObstaclePositionX;
    }

    public void setCurrentObstaclePositionX(double currentObstaclePositionX) {
        CurrentObstaclePositionX = currentObstaclePositionX;
    }

    public int getDetectionPointNumber() {
        return DetectionPointNumber;
    }

    public void setDetectionPointNumber(int detectionPointNumber) {
        DetectionPointNumber = detectionPointNumber;
    }

    public double getDeltaAngle() {
        return DeltaAngle;
    }

    public void setDeltaAngle(double deltaAngle) {
        DeltaAngle = deltaAngle;
    }

    public boolean isCollision() {
        return IsCollision;
    }

    public void setCollision(boolean collision) {
        IsCollision = collision;
    }
}
