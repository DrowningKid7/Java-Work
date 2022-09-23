public class GeneralData {

    public final static int MyWindowWidth = 1600;//窗口宽度
    public final static int MyWindowHeight = 1000;//窗口高度
    public final static int MyPanelWidth = 1400;//场景宽度
    public final static int MyPanelHeight = 800;//场景高度
    private static double PanelCenterPointX;//场景中点横坐标

    public final static double ObstacleUpdateFrequency = 10;//障碍物动态更新频率
    public final static double UAVDynamicUpdateFrequency = 200;//无人机动态更新频率
    public final static double GlobalTimeUpdateFrequency = 400;//全局时间更新频率
    public final static double ScreenRefreshRate = 165;//屏幕刷新率
    public final static double CollisionDetectionUpdateFrequency = 400;//障碍物检测更新频率
    public final static double QLearningUpdateFrequency = 200;//Q-Learning学习频率
    private static boolean ShowQTable = false;//现实Q表
    private static boolean ShowDelta=false;//显示偏差

    private static InformationBlock InformationBlock;

    private static boolean IsDrawContinue = true;//画图是否继续

    public final static double ObstacleStartPositionX = 200;//初始障碍物位置横坐标
    public final static double ObstacleInterval = 100;//障碍物之间间隔
    public final static double ObstacleWidth = 30;//障碍物宽度
    public final static double GravitationalAcceleration = 1000;//重力加速度

    public static double getPanelCenterPointX() {
        return PanelCenterPointX;
    }

    public static void setPanelCenterPointX(double panelCenterPointX) {
        PanelCenterPointX = panelCenterPointX;
    }

    public static boolean isShowQTable() {
        return ShowQTable;
    }

    public static void setShowQTable(boolean showQTable) {
        ShowQTable = showQTable;
    }

    public static boolean isShowDelta() {
        return ShowDelta;
    }

    public static void setShowDelta(boolean showDelta) {
        ShowDelta = showDelta;
    }

    public static InformationBlock getInformationBlock() {
        return InformationBlock;
    }

    public static void setInformationBlock(InformationBlock informationBlock) {
        InformationBlock = informationBlock;
    }

    public static boolean isIsDrawContinue() {
        return IsDrawContinue;
    }

    public static void setIsDrawContinue(boolean isDrawContinue) {
        IsDrawContinue = isDrawContinue;
    }
}
