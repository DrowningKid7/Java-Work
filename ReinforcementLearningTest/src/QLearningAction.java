//Q-Learning�����ࣺ����Q-Learning��صķ���
public class QLearningAction implements Runnable {
    //����Q-Table
    public static final int Rows=80;
    public static final int Columns=160;
    public static final int ActionNumber=2;
    public static final int Rows_Benchmark=38;
    public static final int Columns_Benchmark=80;
    public static final double State_Block_Width =10;
    private static int TrainingTimes=0;//ѵ������
    private static double GreedyThreshold=0.95;//̰����ֵ

    public static double[][][] QTable = new double[Rows][Columns][ActionNumber];//Q-Table
    public static int[][] ActionTable = new int[Rows][Columns];//��Ϊ��0������Ϊ��1��������-1���޶���

    private final int FORCE_ACTION = 0;//����Ӧ����Ϊ
    private final int NO_FORCE_ACTION = 1;//��Ӧ����Ϊ
    private UAV UAV;
    private boolean IsQLearningWorking;

    private double LearningRate;//ѧϰЧ��
    private double DiscountFactor;//�ۿ�����

    private double StepReward;//������ʱ����
    private double UAVDeadReward;//UAV��������
    private double UAVPassingReward;//UAV��Խ�ϰ��ｱ��

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
        //���˻���ʼλ��
        double PositionX = UAV.getUAVParameter().getPositionX();
        double PositionY = UAV.getUAVParameter().getPositionY();
        double PositionZ = UAV.getUAVParameter().getPositionZ();
        //��ҪԽ�����ϰ����ʼλ�ã����Ͻǵ�ĺ�����λ�ã�
        int PreviousFacingObstacleFlag = GeneralData.getInformationBlock().getCurrentFacingObstacleFlag();
        Obstacle D_Obstacle = GeneralData.getInformationBlock().getD_ObstacleVector().get(PreviousFacingObstacleFlag);
        double ObstaclePositionX = D_Obstacle.getUL_PositionX();
        double ObstaclePositionY = D_Obstacle.getUL_PositionY();
        //ȷ����ʼ��ǰһ����Q-Table����λ��
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
        //ȷ����Ϊ
        int PreviousAction;
        //�жϵ�ǰ������Ϊ��Q-Table�е�ֵ�Ƿ����
        if (QTable[PreviousQTableFlagX][PreviousQTableFlagY][this.FORCE_ACTION]
                == QTable[PreviousQTableFlagX][PreviousQTableFlagY][this.NO_FORCE_ACTION]) {
            //��ȵĻ�ֱ�Ӳ���̽������
            double ExploreValue = Math.random();
            if (ExploreValue < 0.5) {
                UAV.setForceExist(true);
                PreviousAction = this.FORCE_ACTION;
            } else {
                PreviousAction = this.NO_FORCE_ACTION;
            }
        } else {
            //����ȵĻ�ȷ��̰����Ϊ
            if (QTable[PreviousQTableFlagX][PreviousQTableFlagY][this.FORCE_ACTION]
                    > QTable[PreviousQTableFlagX][PreviousQTableFlagY][this.NO_FORCE_ACTION]) {
                PreviousAction = this.FORCE_ACTION;
            } else {
                PreviousAction = this.NO_FORCE_ACTION;
            }
            // ����̰�Ļ�̽������
            double GreedyValue = Math.random();
            if (GreedyValue < this.GreedyThreshold) {
                //����
                if (PreviousAction == this.FORCE_ACTION) {
                    UAV.setForceExist(true);
                }
            } else {
                //̽��
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
            //���˻���ǰλ��
            PositionX = UAV.getUAVParameter().getPositionX();
            PositionY = UAV.getUAVParameter().getPositionY();
            PositionZ = UAV.getUAVParameter().getPositionZ();
            //��������ϰ���ǰλ�ã����Ͻǵ�ĺ�����λ�ã�
            int CurrentFacingObstacleFlag = GeneralData.getInformationBlock().getCurrentFacingObstacleFlag();
            D_Obstacle = GeneralData.getInformationBlock().getD_ObstacleVector().get(CurrentFacingObstacleFlag);
            ObstaclePositionX = D_Obstacle.getUL_PositionX();
            ObstaclePositionY = D_Obstacle.getUL_PositionY();
            //ȷ����ǰQ-Table����λ��
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
            //ȷ���Ƿ�Ϊͬ״̬
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
                //ȷ����ǰ״̬�µ�̰����Ϊ���ӦQ���е�ֵ
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
                //UAV�Ƿ�����
                if (UAV.isAlive()) {
                    //����Q��(�Ƿ񾭹��ϰ���,ֻ����ͬһ���ϰ����²Ÿ���Q��)
                    if (PreviousFacingObstacleFlag == CurrentFacingObstacleFlag) {
                        //����ǰһ��״̬��ǰһ����Ϊ�µļ�ʱ����
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
                        //����Q��
                        QTable[PreviousQTableFlagX][PreviousQTableFlagY][PreviousAction]
                                = (1 - this.LearningRate) * QTable[PreviousQTableFlagX][PreviousQTableFlagY][PreviousAction]
                                + this.LearningRate * (this.StepReward + this.DiscountFactor * CurrentMaxQValue);
                        //������Ϊ��
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
                    //�жϵ�ǰ������Ϊ��Q��ֵ�Ƿ����
                    if (QTable[CurrentQTableFlagX][CurrentQTableFlagY][this.FORCE_ACTION]
                            == QTable[CurrentQTableFlagX][CurrentQTableFlagY][this.NO_FORCE_ACTION]) {
                        //��ȵĻ�ֱ�Ӳ���̽������
                        double ExploreValue = Math.random();
                        if (ExploreValue <= 0.5) {
                            CurrentAction = this.FORCE_ACTION;
                            UAV.setForceExist(true);
                        } else {
                            CurrentAction = this.NO_FORCE_ACTION;
                        }
                    } else {
                        // ����ȵĻ�����̰�Ļ�̽������
                        double GreedyValue = Math.random();
                        if (GreedyValue <= this.GreedyThreshold) {
                            //����
                            if (CurrentAction == this.FORCE_ACTION) {
                                UAV.setForceExist(true);
                            }
                        } else {
                            //̽��
                            if (CurrentAction == this.FORCE_ACTION) {
                                CurrentAction = this.NO_FORCE_ACTION;
                            } else {
                                CurrentAction = this.FORCE_ACTION;
                                UAV.setForceExist(true);
                            }
                        }
                    }
                    //��������
                    PreviousDeltaBlockY = CurrentDeltaBlockY;
                    PreviousAction = CurrentAction;
                    PreviousFacingObstacleFlag = CurrentFacingObstacleFlag;
                    PreviousQTableFlagX = CurrentQTableFlagX;
                    PreviousQTableFlagY = CurrentQTableFlagY;
                } else {
                    //UAV����������Q-Table
                    QTable[PreviousQTableFlagX][PreviousQTableFlagY][PreviousAction]
                            = (1 - LearningRate) * QTable[PreviousQTableFlagX][PreviousQTableFlagY][PreviousAction]
                            + LearningRate * (this.UAVDeadReward + DiscountFactor * CurrentMaxQValue);
                    //������Ϊ��
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
