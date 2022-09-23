//Q-Learning方法类：包含Q-Learning相关的方法
public class QLearningAction implements Runnable {
    //建立Q-Table
    public static final int Rows=80;
    public static final int Columns=160;
    public static final int ActionNumber=2;
    public static final int Rows_Benchmark=38;
    public static final int Columns_Benchmark=80;
    public static final double State_Block_Width =10;
    private static int TrainingTimes=0;//训练次数
    private static double GreedyThreshold=0.95;//贪心阈值

    public static double[][][] QTable = new double[Rows][Columns][ActionNumber];//Q-Table
    public static int[][] ActionTable = new int[Rows][Columns];//行为表：0：无行为；1：触发；-1：无动作

    private final int FORCE_ACTION = 0;//产生应力行为
    private final int NO_FORCE_ACTION = 1;//无应力行为
    private UAV UAV;
    private boolean IsQLearningWorking;

    private double LearningRate;//学习效率
    private double DiscountFactor;//折扣因子

    private double StepReward;//单步即时奖励
    private double UAVDeadReward;//UAV死亡奖励
    private double UAVPassingReward;//UAV跨越障碍物奖励

    private double InitialGreedyThreshold;


    public QLearningAction(UAV UAV) {
        this.IsQLearningWorking = true;
        this.UAV = UAV;
        this.LearningRate = 0.3;
        this.DiscountFactor = 0.95;
        this.InitialGreedyThreshold=0.95;
        this.StepReward = 1;
        this.UAVDeadReward = -1000;
        this.UAVPassingReward = 50;

    }

    public static void InitialQTable() {
        for (int i = 0; i < Rows; i++) {
            for (int j = 0; j < Columns; j++) {
                ActionTable[i][j]=0;
                for (int k = 0; k < ActionNumber; k++) {
                    QTable[i][j][k] = 0;
                }
            }
        }
    }

    @Override
    public void run() {
        double QLearningUpdateFrequency = GeneralData.QLearningUpdateFrequency;
        double QLearningUpdateTimeInterval = 1 / QLearningUpdateFrequency;
        long QLearningUpdateTimeIntervalMillisecond = (int) (QLearningUpdateTimeInterval * 1000);
        //无人机初始位置
        double PositionX = UAV.getUAVParameter().getPositionX();
        double PositionY = UAV.getUAVParameter().getPositionY();
        double PositionZ = UAV.getUAVParameter().getPositionZ();
        //需要越过的障碍物初始位置（左上角点的横坐标位置）
        int PreviousFacingObstacleFlag = GeneralData.getInformationBlock().getCurrentFacingObstacleFlag();
        Obstacle D_Obstacle = GeneralData.getInformationBlock().getD_ObstacleVector().get(PreviousFacingObstacleFlag);
        double ObstaclePositionX = D_Obstacle.getUL_PositionX();
        double ObstaclePositionY = D_Obstacle.getUL_PositionY();
        //确定初始（前一个）Q-Table索引位置
        double PreviousDeltaX =PositionX - ObstaclePositionX;
        double PreviousDeltaY = PositionY - ObstaclePositionY;
        int PreviousDeltaBlockX;
        int PreviousDeltaBlockY;
        if (PreviousDeltaX <0) {
            PreviousDeltaBlockX= (int) (PreviousDeltaX/ State_Block_Width)-1;
        }else {
            PreviousDeltaBlockX= (int) (PreviousDeltaX/ State_Block_Width);
        }
        if (PreviousDeltaY <0) {
            PreviousDeltaBlockY= (int) (PreviousDeltaY/ State_Block_Width)-1;
        }else {
            PreviousDeltaBlockY= (int) (PreviousDeltaY/ State_Block_Width);
        }
        int PreviousQTableFlagX = PreviousDeltaBlockX + Rows_Benchmark;
        int PreviousQTableFlagY = PreviousDeltaBlockY + Columns_Benchmark;
        //确定行为
        int PreviousAction;
        //判断当前两个行为在Q-Table中的值是否相等
        if (QTable[PreviousQTableFlagX][PreviousQTableFlagY][this.FORCE_ACTION]
                == QTable[PreviousQTableFlagX][PreviousQTableFlagY][this.NO_FORCE_ACTION]) {
            //相等的话直接采用探索策略
            double ExploreValue = Math.random();
            if (ExploreValue < 0.5) {
                UAV.setForceExist(true);
                PreviousAction = this.FORCE_ACTION;
            } else {
                PreviousAction = this.NO_FORCE_ACTION;
            }
        } else {
            //不相等的话确定贪心行为
            if (QTable[PreviousQTableFlagX][PreviousQTableFlagY][this.FORCE_ACTION]
                    > QTable[PreviousQTableFlagX][PreviousQTableFlagY][this.NO_FORCE_ACTION]) {
                PreviousAction = this.FORCE_ACTION;
            } else {
                PreviousAction = this.NO_FORCE_ACTION;
            }
            // 采用贪心或探索策略
            double GreedyValue = Math.random();
            if (GreedyValue < this.GreedyThreshold) {
                //利用
                if (PreviousAction == this.FORCE_ACTION) {
                    UAV.setForceExist(true);
                }
            } else {
                //探索
                if (PreviousAction == this.FORCE_ACTION) {
                    PreviousAction = this.NO_FORCE_ACTION;
                } else {
                    PreviousAction = this.FORCE_ACTION;
                    UAV.setForceExist(true);
                }
            }
        }
        while (this.IsQLearningWorking) {
            try {
                Thread.sleep(QLearningUpdateTimeIntervalMillisecond);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //无人机当前位置
            PositionX = UAV.getUAVParameter().getPositionX();
            PositionY = UAV.getUAVParameter().getPositionY();
            PositionZ = UAV.getUAVParameter().getPositionZ();
            //最近的下障碍当前位置（左上角点的横坐标位置）
            int CurrentFacingObstacleFlag = GeneralData.getInformationBlock().getCurrentFacingObstacleFlag();
            D_Obstacle = GeneralData.getInformationBlock().getD_ObstacleVector().get(CurrentFacingObstacleFlag);
            ObstaclePositionX = D_Obstacle.getUL_PositionX();
            ObstaclePositionY = D_Obstacle.getUL_PositionY();
            //确定当前Q-Table索引位置
            double CurrentDeltaX =PositionX - ObstaclePositionX;
            double CurrentDeltaY = PositionY - ObstaclePositionY;
            int CurrentDeltaBlockX;
            int CurrentDeltaBlockY;
            if (CurrentDeltaX <0) {
                CurrentDeltaBlockX= (int) (CurrentDeltaX/ State_Block_Width)-1;
            }else {
                CurrentDeltaBlockX= (int) (CurrentDeltaX/ State_Block_Width);
            }
            if (CurrentDeltaY <0) {
                CurrentDeltaBlockY= (int) (CurrentDeltaY/ State_Block_Width)-1;
            }else {
                CurrentDeltaBlockY= (int) (CurrentDeltaY/ State_Block_Width);
            }
            int CurrentQTableFlagX = CurrentDeltaBlockX + Rows_Benchmark;
            int CurrentQTableFlagY = CurrentDeltaBlockY + Columns_Benchmark;
            //确定是否为同状态
            if (PreviousQTableFlagX == CurrentQTableFlagX && PreviousQTableFlagY == CurrentQTableFlagY) {
                if (UAV.isAlive()) {
                    continue;
                } else {
                    QTable[PreviousQTableFlagX][PreviousQTableFlagY][PreviousAction]
                            = (1 - LearningRate) * QTable[PreviousQTableFlagX][PreviousQTableFlagY][PreviousAction]
                            + LearningRate * (this.UAVDeadReward);
                    if(QTable[PreviousQTableFlagX][PreviousQTableFlagY][this.FORCE_ACTION]
                            >QTable[PreviousQTableFlagX][PreviousQTableFlagY][this.NO_FORCE_ACTION]){
                        ActionTable[PreviousQTableFlagX][PreviousQTableFlagY]=1;
                    } else if (QTable[PreviousQTableFlagX][PreviousQTableFlagY][this.FORCE_ACTION]
                            <QTable[PreviousQTableFlagX][PreviousQTableFlagY][this.NO_FORCE_ACTION]) {
                        ActionTable[PreviousQTableFlagX][PreviousQTableFlagY]=-1;
                    }else {
                        ActionTable[PreviousQTableFlagX][PreviousQTableFlagY]=0;
                    }
                    this.IsQLearningWorking = false;
                    break;
                }
            } else {
                //确定当前状态下的贪心行为与对应Q表中的值
                int CurrentAction;
                double CurrentMaxQValue;
                if (QTable[CurrentQTableFlagX][CurrentQTableFlagY][this.FORCE_ACTION]
                        >= QTable[CurrentQTableFlagX][CurrentQTableFlagY][this.NO_FORCE_ACTION]) {
                    CurrentMaxQValue = QTable[CurrentQTableFlagX][CurrentQTableFlagY][this.FORCE_ACTION];
                    CurrentAction = this.FORCE_ACTION;
                } else {
                    CurrentMaxQValue = QTable[CurrentQTableFlagX][CurrentQTableFlagY][this.NO_FORCE_ACTION];
                    CurrentAction = this.NO_FORCE_ACTION;
                }
                //UAV是否生存
                if (UAV.isAlive()) {
                    //更新Q表(是否经过障碍物,只有在同一个障碍物下才更新Q表)
                    if (PreviousFacingObstacleFlag == CurrentFacingObstacleFlag) {
                        //计算前一个状态在前一个行为下的即时奖励
                        int DeltaBlock=5;
                        if (CurrentDeltaY>=0) {
                            if (PreviousAction == this.FORCE_ACTION) {
                                this.StepReward = -10*(CurrentDeltaY-DeltaBlock);
                            }else {
                                this.StepReward = CurrentDeltaY-DeltaBlock;
                            }
                        }else {
                            if (PreviousAction == this.FORCE_ACTION) {
                                this.StepReward = -(CurrentDeltaY-DeltaBlock);
                            }else {
                                this.StepReward = 10*(CurrentDeltaY-DeltaBlock);
                            }
                        }

//                        System.out.println(PreviousDeltaY + "  " + CurrentDeltaY + "  " + StepReward+"  "+PreviousAction);
                        //更新Q表
                        QTable[PreviousQTableFlagX][PreviousQTableFlagY][PreviousAction]
                                = (1 - this.LearningRate) * QTable[PreviousQTableFlagX][PreviousQTableFlagY][PreviousAction]
                                + this.LearningRate * (this.StepReward + this.DiscountFactor * CurrentMaxQValue);
                        //更新行为表
                        if(QTable[PreviousQTableFlagX][PreviousQTableFlagY][this.FORCE_ACTION]
                                >QTable[PreviousQTableFlagX][PreviousQTableFlagY][this.NO_FORCE_ACTION]){
                            ActionTable[PreviousQTableFlagX][PreviousQTableFlagY]=1;
                        } else if (QTable[PreviousQTableFlagX][PreviousQTableFlagY][this.FORCE_ACTION]
                                <QTable[PreviousQTableFlagX][PreviousQTableFlagY][this.NO_FORCE_ACTION]) {
                            ActionTable[PreviousQTableFlagX][PreviousQTableFlagY]=-1;
                        }else {
                            ActionTable[PreviousQTableFlagX][PreviousQTableFlagY]=0;
                        }
                    }
                    //判断当前两个行为的Q表值是否相等
                    if (QTable[CurrentQTableFlagX][CurrentQTableFlagY][this.FORCE_ACTION]
                            == QTable[CurrentQTableFlagX][CurrentQTableFlagY][this.NO_FORCE_ACTION]) {
                        //相等的话直接采用探索策略
                        double ExploreValue = Math.random();
                        if (ExploreValue <= 0.5) {
                            CurrentAction = this.FORCE_ACTION;
                            UAV.setForceExist(true);
                        } else {
                            CurrentAction = this.NO_FORCE_ACTION;
                        }
                    } else {
                        // 不相等的话采用贪心或探索策略
                        double GreedyValue = Math.random();
                        if (GreedyValue <= this.GreedyThreshold) {
                            //利用
                            if (CurrentAction == this.FORCE_ACTION) {
                                UAV.setForceExist(true);
                            }
                        } else {
                            //探索
                            if (CurrentAction == this.FORCE_ACTION) {
                                CurrentAction = this.NO_FORCE_ACTION;
                            } else {
                                CurrentAction = this.FORCE_ACTION;
                                UAV.setForceExist(true);
                            }
                        }
                    }
                    //参数迭代
                    PreviousDeltaBlockY = CurrentDeltaBlockY;
                    PreviousAction = CurrentAction;
                    PreviousFacingObstacleFlag = CurrentFacingObstacleFlag;
                    PreviousQTableFlagX = CurrentQTableFlagX;
                    PreviousQTableFlagY = CurrentQTableFlagY;
                } else {
                    //UAV死亡，更行Q-Table
                    QTable[PreviousQTableFlagX][PreviousQTableFlagY][PreviousAction]
                            = (1 - LearningRate) * QTable[PreviousQTableFlagX][PreviousQTableFlagY][PreviousAction]
                            + LearningRate * (this.UAVDeadReward + DiscountFactor * CurrentMaxQValue);
                    //更新行为表
                    if(QTable[PreviousQTableFlagX][PreviousQTableFlagY][this.FORCE_ACTION]
                            >QTable[PreviousQTableFlagX][PreviousQTableFlagY][this.NO_FORCE_ACTION]){
                        ActionTable[PreviousQTableFlagX][PreviousQTableFlagY]=1;
                    } else if (QTable[PreviousQTableFlagX][PreviousQTableFlagY][this.FORCE_ACTION]
                            <QTable[PreviousQTableFlagX][PreviousQTableFlagY][this.NO_FORCE_ACTION]) {
                        ActionTable[PreviousQTableFlagX][PreviousQTableFlagY]=-1;
                    }else {
                        ActionTable[PreviousQTableFlagX][PreviousQTableFlagY]=0;
                    }
                    this.IsQLearningWorking = false;
                    break;
                }
            }
        }
        TrainingTimes+=1;
        if(GreedyThreshold<1){
            GreedyThreshold=this.InitialGreedyThreshold+(double) TrainingTimes/5000;
        }
        System.out.println("Q-Learning Thread Dead!");
    }

    public boolean isQLearningWorking() {
        return IsQLearningWorking;
    }

    public void setQLearningWorking(boolean QLearningWorking) {
        IsQLearningWorking = QLearningWorking;
    }

    public static int getTrainingTimes() {
        return TrainingTimes;
    }

    public static void setTrainingTimes(int trainingTimes) {
        TrainingTimes = trainingTimes;
    }

    public static double getGreedyThreshold() {
        return GreedyThreshold;
    }

    public static void setGreedyThreshold(double greedyThreshold) {
        GreedyThreshold = greedyThreshold;
    }
}
