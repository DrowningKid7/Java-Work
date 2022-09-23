import java.util.Vector;

public class InformationBlock {
    private UAV UAV;
    private CollisionDetection CollisionDetection;
    private Vector<Obstacle> U_ObstacleVector;
    private Vector<Obstacle> D_ObstacleVector;
    private int CurrentPassedObstacleFlag;//��ǰ�������ϰ�����
    private int CurrentFacingObstacleFlag;//��ǰ��Ե��ϰ�����
    private Time Time;
    private ObstacleAction ObstacleAction;
    private QLearningAction QLearningAction;

    public InformationBlock(){
        this.U_ObstacleVector=new Vector<>();
        this.D_ObstacleVector=new Vector<>();
        this.CurrentPassedObstacleFlag =-1;
        this.CurrentFacingObstacleFlag =0;

        this.UAV = new UAV(1, 30, 600, 0, 100, 0, 0, 10,8);
        this.CollisionDetection=new CollisionDetection(UAV,10);

        this.Time = new Time();
        this.ObstacleAction=new ObstacleAction(this.UAV,this.U_ObstacleVector,this.D_ObstacleVector);

        this.QLearningAction=new QLearningAction(this.UAV);
        //����ʱ��
        Thread TimeThread = new Thread(this.Time);
        Time.setTimeThread(TimeThread);
        TimeThread.start();

        //����UAV
        Thread UAVThread = new Thread(UAV);
        UAV.setUAVThread(UAVThread);
        UAVThread.start();

        //�����ϰ���
        Thread ObstacleActionThread = new Thread(this.ObstacleAction);
        ObstacleActionThread.start();

        //������ײ���
        Thread CollisionDetectionThread=new Thread(CollisionDetection);
        CollisionDetectionThread.start();

        Thread QLearningActionThread=new Thread(QLearningAction);
        QLearningActionThread.start();
    }

    public UAV getUAV() {
        return UAV;
    }

    public void setUAV(UAV UAV) {
        this.UAV = UAV;
    }

    public CollisionDetection getCollisionDetection() {
        return CollisionDetection;
    }

    public void setCollisionDetection(CollisionDetection collisionDetection) {
        CollisionDetection = collisionDetection;
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

    public int getCurrentPassedObstacleFlag() {
        return CurrentPassedObstacleFlag;
    }

    public void setCurrentPassedObstacleFlag(int currentPassedObstacleFlag) {
        CurrentPassedObstacleFlag = currentPassedObstacleFlag;
    }

    public int getCurrentFacingObstacleFlag() {
        return CurrentFacingObstacleFlag;
    }

    public void setCurrentFacingObstacleFlag(int currentFacingObstacleFlag) {
        CurrentFacingObstacleFlag = currentFacingObstacleFlag;
    }

    public Time getTime() {
        return Time;
    }

    public void setTime(Time time) {
        Time = time;
    }

    public ObstacleAction getObstacleAction() {
        return ObstacleAction;
    }

    public void setObstacleAction(ObstacleAction obstacleAction) {
        ObstacleAction = obstacleAction;
    }

    public QLearningAction getQLearningAction() {
        return QLearningAction;
    }

    public void setQLearningAction(QLearningAction QLearningAction) {
        this.QLearningAction = QLearningAction;
    }
}
