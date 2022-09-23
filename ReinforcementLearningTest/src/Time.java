public class Time implements Runnable {

    private static double CurrentTime;
    private boolean IsTimeContinue;

    private Thread TimeThread;//时间线程

    public Time() {
        CurrentTime = 0;
        this.IsTimeContinue=true;
    }

    @Override
    public void run() {
        double GlobalTimeUpdateFrequency = GeneralData.GlobalTimeUpdateFrequency;
        double GlobalTimeUpdateTimeInterval = 1 / GlobalTimeUpdateFrequency;
        long GlobalTimeIntervalMillisecond = (int) (GlobalTimeUpdateTimeInterval * 1000);
        while (this.IsTimeContinue) {
            CurrentTime = CurrentTime + GlobalTimeUpdateTimeInterval;
            try {
                Thread.sleep(GlobalTimeIntervalMillisecond);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Time Thread Dead!");
    }

    public static double getCurrentTime() {
        return CurrentTime;
    }

    public static void setCurrentTime(double currentTime) {
        CurrentTime = currentTime;
    }

    public boolean isTimeContinue() {
        return IsTimeContinue;
    }

    public void setTimeContinue(boolean timeContinue) {
        IsTimeContinue = timeContinue;
    }

    public Thread getTimeThread() {
        return TimeThread;
    }

    public void setTimeThread(Thread timeThread) {
        TimeThread = timeThread;
    }
}
