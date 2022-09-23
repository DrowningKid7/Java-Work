import java.util.ConcurrentModificationException;

public class UAV implements Runnable {
    private double UAVID;//UAVʶ��ID
    private UAVParameter UAVParameter;//UAV�����붯��ѧ����
    private UAVDrawParameter UAVDrawParameter;//UAVͼ�β���
    private CollisionDetection CollisionDetection;//UAV��ײ���
    private boolean IsForceExist;//�������Ƿ����
    private double ForceSpeedY;
    private boolean IsAlive;//UAV�Ƿ�����
    private Thread UAVThread;//UAV�߳�

    public UAV(int UAVID, double PositionX, double PositionY, double PositionZ,
               double SpeedX, double SpeedY, double SpeedZ,
               double Quality, double Radius) {
        this.UAVParameter = new UAVParameter(PositionX, PositionY, PositionZ, SpeedX, SpeedY, SpeedZ, Quality, Radius);
        this.UAVDrawParameter = new UAVDrawParameter((int) PositionX, (int) PositionY, (int) PositionZ, (int) Radius);
        this.UAVID = UAVID;
        this.IsForceExist = false;
        this.IsAlive = true;
        this.ForceSpeedY=400;
    }

    @Override
    public void run() {
        double UAVDynamicUpdateFrequency = GeneralData.UAVDynamicUpdateFrequency;
        double UAVDynamicUpdateTimeInterval = 1 / UAVDynamicUpdateFrequency;
        long UAVDynamicUpdateTimeIntervalMillisecond = (int) (UAVDynamicUpdateTimeInterval * 1000);
        double PreviousTime = Time.getCurrentTime();
        while (this.IsAlive) {

            double PositionX = UAVParameter.getPositionX();
            double PositionY = UAVParameter.getPositionY();
            double PositionZ = UAVParameter.getPositionZ();
            double SpeedX = UAVParameter.getSpeedX();
            double SpeedY = UAVParameter.getSpeedY();
            double SpeedZ = UAVParameter.getSpeedZ();
            double Quality = UAVParameter.getQuality();
            //���¼��ٶ�
            double AccelerationX = 0;
            double AccelerationY = -GeneralData.GravitationalAcceleration;
            double AccelerationZ = 0;

            //ʱ����
            double CurrentTime = Time.getCurrentTime();
            double TimeDelta = CurrentTime - PreviousTime;

            //��ǰ�ٶ�
            double CurrentSpeedX = SpeedX + AccelerationX * TimeDelta;
            double CurrentSpeedY;
            if (IsForceExist) {
                CurrentSpeedY = ForceSpeedY;
                IsForceExist = false;
            } else {
                CurrentSpeedY = SpeedY + AccelerationY * TimeDelta;
            }
            double CurrentSpeedZ = SpeedZ + AccelerationZ * TimeDelta;

            //��ǰλ��
            double PositionDeltaX = SpeedX * TimeDelta + 0.5 * AccelerationX * TimeDelta * TimeDelta;
            double CurrentPositionX = PositionX + PositionDeltaX;
            double PositionDeltaY = SpeedY * TimeDelta + 0.5 * AccelerationY * TimeDelta * TimeDelta;
            double CurrentPositionY = PositionY + PositionDeltaY;
            double PositionDeltaZ = SpeedZ * TimeDelta + 0.5 * AccelerationZ * TimeDelta * TimeDelta;
            double CurrentPositionZ = PositionZ + PositionDeltaZ;

            UAVParameter.setAccelerationX(AccelerationX);
            UAVParameter.setAccelerationY(AccelerationY);
            UAVParameter.setAccelerationZ(AccelerationZ);

            UAVParameter.setSpeedX(CurrentSpeedX);
            UAVParameter.setSpeedY(CurrentSpeedY);
            UAVParameter.setSpeedZ(CurrentSpeedZ);

            UAVParameter.setPositionX(CurrentPositionX);
            UAVParameter.setPositionY(CurrentPositionY);
            UAVParameter.setPositionZ(CurrentPositionZ);

            PreviousTime = CurrentTime;
            try {
                Thread.sleep(UAVDynamicUpdateTimeIntervalMillisecond);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("UAV Thread Dead!");
    }

    public double getUAVID() {
        return UAVID;
    }

    public void setUAVID(double UAVID) {
        this.UAVID = UAVID;
    }

    public UAVParameter getUAVParameter() {
        return UAVParameter;
    }

    public void setUAVParameter(UAVParameter UAVParameter) {
        this.UAVParameter = UAVParameter;
    }

    public UAVDrawParameter getUAVDrawParameter() {
        return UAVDrawParameter;
    }

    public void setUAVDrawParameter(UAVDrawParameter UAVDrawParameter) {
        this.UAVDrawParameter = UAVDrawParameter;
    }

    public CollisionDetection getCollisionDetection() {
        return CollisionDetection;
    }

    public void setCollisionDetection(CollisionDetection collisionDetection) {
        CollisionDetection = collisionDetection;
    }

    public boolean isForceExist() {
        return IsForceExist;
    }

    public void setForceExist(boolean forceExist) {
        IsForceExist = forceExist;
    }

    public double getForceSpeedY() {
        return ForceSpeedY;
    }

    public void setForceSpeedY(double forceSpeedY) {
        ForceSpeedY = forceSpeedY;
    }

    public boolean isAlive() {
        return IsAlive;
    }

    public void setAlive(boolean alive) {
        IsAlive = alive;
    }

    public Thread getUAVThread() {
        return UAVThread;
    }

    public void setUAVThread(Thread UAVThread) {
        this.UAVThread = UAVThread;
    }
}
